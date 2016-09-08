/*
 * #%L
 * JavaCreed CSV API
 * %%
 * Copyright (C) 2012 - 2015 Java Creed
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.javacreed.api.csv.reader;

import java.nio.CharBuffer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvReadable {

  /**
   *
   * @author Albert Attard
   *
   * @param <T>
   */
  public static interface Command<T> {

    /**
     *
     * @return
     * @throws Exception
     */
    T execute() throws Exception;

  }

  /**
   *
   * @author Albert Attard
   */
  private static enum State {
    INVALID, BEGIN_LINE, IN_LINE, END_LINE
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(CsvReadable.class);

  private static final CsvReadableParameters DEFAULT_PARAMETERS = new CsvReadableParameters();
  private final Readable in;

  private final CharBuffer charBuffer;
  private final String valueSeparator;
  private final String valueBounderies;
  private final String escapeCharacter;
  private final Set<String> lineSeparators;

  private final Set<String> valueSeparators;

  private State state = State.END_LINE;

  private Throwable lastCause;

  private final Map<String, String> toRevert;

  public CsvReadable(final Readable in) throws NullPointerException, IllegalArgumentException {
    this(in, CsvReadable.DEFAULT_PARAMETERS);
  }

  public CsvReadable(final Readable in,  final CsvReadableParameters parameters)
      throws NullPointerException, IllegalArgumentException {
    this.in = Objects.requireNonNull(in);
    charBuffer = CharBuffer.allocate(parameters.getBufferSize());
    charBuffer.limit(0);

    this.valueSeparator = parameters.getValueSeparator();
    this.valueBounderies = parameters.getValueBounderies();
    this.escapeCharacter = parameters.getEscapeCharacter();
    lineSeparators = new LinkedHashSet<String>(parameters.getLineSeparators());

    valueSeparators = new LinkedHashSet<String>();
    valueSeparators.add(valueSeparator);
    valueSeparators.addAll(lineSeparators);

    final Map<String, String> map = new LinkedHashMap<String, String>();
    map.put(escapeCharacter + "t", "\t");
    map.put(escapeCharacter + "b", "\b");
    map.put(escapeCharacter + "n", "\n");
    map.put(escapeCharacter + "r", "\r");
    map.put(escapeCharacter + "f", "\f");
    map.put(escapeCharacter + "'", "\'");
    map.put(escapeCharacter + "\"", "\"");
    map.put(escapeCharacter + "\\", "\\");
    toRevert = Collections.unmodifiableMap(map);
  }

  public void beginLine() throws CsvReadException {
    execute(new Command<Void>() {
      @Override
      public Void execute() throws Exception {
        switch (state) {
        case END_LINE:
          // Fill the buffer
          CharBufferUtils.fill(charBuffer, in);
          CsvReadable.LOGGER.trace("Begin line: '{}' (Position: {} and Limit: {})",
              new Object[] { charBuffer, charBuffer.position(), charBuffer.limit() });
          state = State.BEGIN_LINE;
          break;
        default:
          throw new CsvReadException("Cannot begin line before ending the previous one");
        }
        return null;
      }
    });
  }

  public void endLine() throws CsvReadException {
    execute(new Command<Void>() {
      @Override
      public Void execute() throws Exception {
        switch (state) {
        case BEGIN_LINE:
        case IN_LINE:
          checkBlock: {
            for (final String endOfLineToken : lineSeparators) {
              if (CharBufferUtils.startsWith(charBuffer, endOfLineToken)) {
                CharBufferUtils.shiftReadRewind(charBuffer, endOfLineToken.length(), in);
                break checkBlock;
              }
            }
            throw new CsvReadException("Invalid end of line");
          }
          state = State.END_LINE;
          return null;
        default:
          throw new CsvReadException("Cannot end line before begining it");
        }
      }
    });
  }

  private <T> T execute(final Command<T> command) throws CsvReadException {
    if (state == State.INVALID) {
      throw new CsvReadException("CSV Reader is in an invalid state", lastCause);
    }

    try {
      return command.execute();
    } catch (final Exception e) {
      state = State.INVALID;
      lastCause = e;
      if (e instanceof CsvReadException) {
        throw (CsvReadException) e;
      }
      throw new CsvReadException(e);
    }
  }

  public boolean hasMoreLines() throws CsvReadException {
    return execute(new Command<Boolean>() {
      @Override
      public Boolean execute() throws Exception {
        return charBuffer.hasRemaining() || CharBufferUtils.fill(charBuffer, in) != -1;
      }
    });
  }

  public boolean hasMoreValues() throws CsvReadException {
    return execute(new Command<Boolean>() {
      @Override
      public Boolean execute() throws Exception {
        switch (state) {
        case BEGIN_LINE:
        case IN_LINE:
          if (charBuffer.hasRemaining() || CharBufferUtils.fill(charBuffer, in) != -1) {
            for (final String endOfLineToken : lineSeparators) {
              if (CharBufferUtils.startsWith(charBuffer, endOfLineToken)) {
                return false;
              }
            }
            return true;
          }

          return false;
        default:
          throw new CsvReadException("Cannot verify whether more values are available before starting a line");
        }
      }
    });
  }

  public String readValue() throws CsvReadException {
    return execute(new Command<String>() {
      @Override
      public String execute() throws Exception {
        switch (state) {
        case IN_LINE:
          if (CharBufferUtils.startsWith(charBuffer, valueSeparator)) {
            CharBufferUtils.shiftReadRewind(charBuffer, valueSeparator.length(), in);
          } else {
            throw new CsvReadException("Value seperator missing");
          }
        case BEGIN_LINE:

          final boolean hasBounderies = CharBufferUtils.startsWith(charBuffer, valueBounderies);
          if (hasBounderies) {
            CsvReadable.LOGGER.trace("Value has bounderies");
            CharBufferUtils.shiftReadRewind(charBuffer, valueBounderies.length(), in);
          }

          boolean escapeNext = false;
          final StringBuilder valueBuffer = new StringBuilder();
          outerLoop: while (true) {
            if (escapeNext) {
              escapeNext = false;

              final String shifted = CharBufferUtils.shiftReadRewind(charBuffer, 2, in);
              CsvReadable.LOGGER.trace("Read char: '{}'. Buffer: '{}' (Position: {} and Limit: {})", shifted,
                  charBuffer, charBuffer.position(), charBuffer.limit());

              String value = toRevert.get(shifted);
              if (value == null) {
                value = shifted.substring(escapeCharacter.length());
              }
              valueBuffer.append(value);
            } else {
              if (CharBufferUtils.startsWith(charBuffer, escapeCharacter)) {
                escapeNext = true;
                continue;
              }

              if (hasBounderies) {
                // Fail if the end is reached. TODO may be we need to test for when the stream is not ready to return
                // more, but it has not yet been closed.
                if (false == charBuffer.hasRemaining()) {
                  throw new CsvReadException("Value boundary missing");
                }

                if (CharBufferUtils.startsWith(charBuffer, valueBounderies)) {
                  CharBufferUtils.shiftReadRewind(charBuffer, valueBounderies.length(), in);
                  break;
                }
              } else {
                // Fail if the end is reached. TODO may be we need to test for when the stream is not ready to return
                // more, but it has not yet been closed.
                if (false == charBuffer.hasRemaining()) {
                  throw new CsvReadException("Value seperator missing");
                }

                for (final String endOfValueToken : valueSeparators) {
                  if (CharBufferUtils.startsWith(charBuffer, endOfValueToken)) {
                    break outerLoop;
                  }
                }
              }
              final String shifted = CharBufferUtils.shiftReadRewind(charBuffer, in);
              CsvReadable.LOGGER.trace("Read char: '{}'. Buffer: '{}' (Position: {} and Limit: {})", shifted,
                  charBuffer, charBuffer.position(), charBuffer.limit());
              valueBuffer.append(shifted);
            }
          }

          state = State.IN_LINE;
          return valueBuffer.toString();
        default:
          throw new CsvReadException("Cannot write value before begining a line");
        }
      }
    });
  }
}
