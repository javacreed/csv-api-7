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

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class CsvWriter implements AutoCloseable {

  private final Appendable appendable;

  private Headers headers;

  private Object[] lineValues;

  private DefaultCsvLine line;

  private CsvFormatter formatter = DefaultCsvFormatter.DEFAULT;

  private ErrorHandler errorHandler = DefaultErrorHandler.INSTANCE;

  private boolean closeAppendable;

  public CsvWriter(final Appendable appendable) throws NullPointerException {
    this.appendable = Objects.requireNonNull(appendable);
  }

  @Override
  public void close() {
    try {
      writeLine();
    } finally {
      if (closeAppendable && appendable instanceof AutoCloseable) {
        try {
          ((AutoCloseable) appendable).close();
        } catch (final Exception e) {
          errorHandler.exception(e);
        }
      }
    }
  }

  public CsvWriter closeAppendableWhenDone() {
    this.closeAppendable = true;
    return this;
  }

  public CsvWriter columns(final int numberOfColumns) {
    if (this.headers != null) {
      throw new CsvHeadersAlreadySetException();
    }

    this.headers = new BlankHeaders(numberOfColumns);
    lineValues = new Object[headers.size()];
    return this;
  }

  public CsvWriter errorHandler(final ErrorHandler errorHandler) throws NullPointerException {
    this.errorHandler = Objects.requireNonNull(errorHandler, "The error handler cannot be null");
    return this;
  }

  public CsvWriter formatter(final CsvFormatter formatter) throws NullPointerException {
    this.formatter = Objects.requireNonNull(formatter, "The formatter cannot be null");
    return this;
  }

  public CsvWriter headers(final DefaultHeaders headers) throws NullPointerException, CsvHeadersAlreadySetException {
    if (this.headers != null) {
      throw new CsvHeadersAlreadySetException();
    }

    this.headers = Objects.requireNonNull(headers, "The headers cannot be null");
    lineValues = new Object[headers.size()];
    writeHeader(headers.getHeaders());
    return this;
  }

  public CsvWriter headers(final String... headers) throws DuplicateColumnNameException, CsvHeadersAlreadySetException {
    return headers(new DefaultHeaders(headers));
  }

  public CsvLine line() {
    writeLine();
    return line = new DefaultCsvLine(headers, lineValues);
  }

  private void writeHeader(final String... cells) {
    try {
      appendable.append(formatter.formatHeaderValue(0, cells[0]));
      for (int columnIndex = 1; columnIndex < cells.length; columnIndex++) {
        appendable.append(formatter.getValueSeparator());
        appendable.append(formatter.formatHeaderValue(columnIndex, cells[columnIndex]));
      }
      appendable.append(formatter.getLineSeparator());
    } catch (final IOException e) {
      errorHandler.io(e);
    }
  }

  private void writeLine() {
    if (line != null) {
      writeLine(lineValues);
      line = null;
    }
  }

  private void writeLine(final Object... cells) {
    try {
      appendable.append(formatter.formatCellValue(0, cells[0]));
      for (int columnIndex = 1; columnIndex < cells.length; columnIndex++) {
        appendable.append(formatter.getValueSeparator());
        appendable.append(formatter.formatCellValue(columnIndex, cells[columnIndex]));
      }
      appendable.append(formatter.getLineSeparator());
    } catch (final IOException e) {
      errorHandler.io(e);
    }
  }
}
