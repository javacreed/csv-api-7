/*
 * #%L
 * JavaCreed CSV API
 * %%
 * Copyright (C) 2012 - 2016 Java Creed
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
package com.javacreed.api.csv.writer;

import java.io.IOException;
import java.util.Objects;

import com.javacreed.api.csv.common.BlankHeaders;
import com.javacreed.api.csv.common.DefaultHeaders;
import com.javacreed.api.csv.common.DuplicateCsvColumnNameException;
import com.javacreed.api.csv.common.Headers;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class CsvWriter implements AutoCloseable {

  private final Appendable appendable;

  private Headers headers;

  private AbstractCsvLine line;

  private CsvFormatter formatter = DefaultCsvFormatter.DEFAULT;

  // private ErrorHandler errorHandler = DefaultErrorHandler.INSTANCE;

  private boolean closeAppendable;

  public CsvWriter(final Appendable appendable) throws NullPointerException {
    this.appendable = Objects.requireNonNull(appendable);
  }

  @Override
  public void close() {
    if (closeAppendable && appendable instanceof AutoCloseable) {
      try {
        ((AutoCloseable) appendable).close();
      } catch (final Exception e) {}
    }
  }

  /**
   * A convenient method that instructs the CSV writer to also close the {@link Appendable} given to the constructor
   * when done. This only works if the given {@link Appendable} is an instance of {@link AutoCloseable}. If the
   * {@link Appendable} is not an instance of {@link AutoCloseable}, then this method has no effect and you need to
   * close it manually.
   *
   * @return this (for method chaining)
   */
  public CsvWriter closeAppendableWhenDone() {
    this.closeAppendable = true;
    return this;
  }

  public CsvWriter columns(final int numberOfColumns) throws IllegalArgumentException, CsvHeadersAlreadySetException {
    if (this.headers != null) {
      throw new CsvHeadersAlreadySetException();
    }

    this.headers = new BlankHeaders(numberOfColumns);
    createLine();
    return this;
  }

  private void createLine() {
    line = new AbstractCsvLine(headers) {
      @Override
      public void write() throws CsvWriteException {
        try {
          writeLine(line.getValues());
        } finally {
          clear();
        }
      }
    };
  }

  /**
   * Sets the formatter to be used
   *
   * @param formatter
   *          the formatter to be used
   * @return this (for method chaining)
   * @throws NullPointerException
   *           if the given formatter is {@code null}
   */
  public CsvWriter formatter(final CsvFormatter formatter) throws NullPointerException {
    this.formatter = Objects.requireNonNull(formatter, "The formatter cannot be null");
    return this;
  }

  /**
   * Sets the headers for this CSV object. Headers can only be set once as any CSV objects can have only one headers. If
   * the headers are already set, then {@link CsvHeadersAlreadySetException} is thrown.
   *
   * @param headers
   *          the headers (which cannot be {@code null})
   * @return this (for method chaining)
   * @throws NullPointerException
   *           if the given headers are {@code null}
   * @throws CsvHeadersAlreadySetException
   *           if the headers are already set
   */
  public CsvWriter headers(final DefaultHeaders headers) throws NullPointerException, CsvHeadersAlreadySetException {
    if (this.headers != null) {
      throw new CsvHeadersAlreadySetException();
    }

    this.headers = Objects.requireNonNull(headers, "The headers cannot be null");
    if (headers.hasValues()) {
      writeHeader(headers.getHeaders());
    }
    createLine();
    return this;
  }

  /**
   * Sets the headers for this CSV object by creating an instance of {@link DefaultHeaders}. Headers can only be set
   * once as any CSV objects can have only one headers. If the headers are already set, then
   * {@link CsvHeadersAlreadySetException} is thrown. If the same column name is found more than once, a
   * {@link DuplicateCsvColumnNameException} is thrown.
   * <p>
   * This is equivalent to:
   *
   * <pre>
   * CsvWriter csv = ...
   * csv.headers(new DefaultHeaders(headers));
   * </pre>
   *
   * @param headers
   *          the headers (which cannot be {@code null})
   * @return this (for method chaining)
   * @throws NullPointerException
   *           if the given headers are {@code null}
   * @throws DuplicateCsvColumnNameException
   *           if the same column name is encountered more than once
   * @throws CsvHeadersAlreadySetException
   *           if the headers are already set
   *
   * @see #headers(DefaultHeaders)
   * @see DefaultHeaders
   */
  public CsvWriter headers(final String... headers)
      throws NullPointerException, DuplicateCsvColumnNameException, CsvHeadersAlreadySetException {
    return headers(new DefaultHeaders(headers));
  }

  /**
   * Returns the {@link CsvLine} where the values are written. Once ready, write the changes by calling
   * {@link CsvLine#write()} to persist the values.
   * <p>
   * This can be used as follows
   *
   * <pre>
   * CsvWriter csv = ...
   * csv.headers("Name","Surname","Age");
   *
   * CsvLine line = csv.line();
   * line.setValue("age", 22);
   * line.setValue("name", "Albert");
   * line.setValue("surname", "Attard");
   * line.write();
   * </pre>
   *
   * The order in which the {@link CsvLine#setValue(String, Object)} is invoked. These values are always written in the
   * headers' order. Furthermore, the column names are usually case-insensitive, but this is determined by the
   * {@link Headers}.
   *
   * @return the {@link CsvLine} where the values are written
   */
  public CsvLine line() {
    return line;
  }

  private void writeHeader(final String... cells) throws CsvWriteException {
    try {
      appendable.append(formatter.formatHeaderValue(0, cells[0]));
      for (int columnIndex = 1; columnIndex < cells.length; columnIndex++) {
        appendable.append(formatter.getValueSeparator());
        appendable.append(formatter.formatHeaderValue(columnIndex, cells[columnIndex]));
      }
      appendable.append(formatter.getLineSeparator());
    } catch (final IOException e) {
      throw new CsvWriteException(e);
    }
  }

  private void writeLine(final Object... cells) throws CsvWriteException {
    try {
      appendable.append(formatter.formatCellValue(0, cells[0]));
      for (int columnIndex = 1; columnIndex < cells.length; columnIndex++) {
        appendable.append(formatter.getValueSeparator());
        appendable.append(formatter.formatCellValue(columnIndex, cells[columnIndex]));
      }
      appendable.append(formatter.getLineSeparator());
    } catch (final IOException e) {
      throw new CsvWriteException(e);
    }
  }
}
