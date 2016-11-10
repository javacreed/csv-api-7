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

import java.util.Arrays;
import java.util.Objects;

import com.javacreed.api.csv.common.Headers;

public abstract class AbstractCsvLine implements CsvLine {

  private final Headers headers;
  private final Object[] values;

  public AbstractCsvLine(final Headers headers) throws NullPointerException {
    this(headers, new Object[headers.size()]);
  }

  public AbstractCsvLine(final Headers headers, final Object[] values) throws NullPointerException {
    this.headers = Objects.requireNonNull(headers);
    this.values = Objects.requireNonNull(values);
    if (headers.size() != values.length) {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Set all values to {@code null}
   *
   * @return this (for method chaining)
   */
  public AbstractCsvLine clear() {
    Arrays.fill(values, null);
    return this;
  }

  public Object[] getValues() {
    return values;
  }

  @Override
  public AbstractCsvLine setValue(final int columnIndex, final Object value) {
    if (columnIndex < 0 || columnIndex >= values.length) {
      throw new CsvHeadersIndexOutOfBoundsException(columnIndex, values.length);
    }

    values[columnIndex] = value;
    return this;
  }

  @Override
  public AbstractCsvLine setValue(final String columnName, final Object value) {
    setValue(headers.indexOf(columnName), value);
    return this;
  }
}
