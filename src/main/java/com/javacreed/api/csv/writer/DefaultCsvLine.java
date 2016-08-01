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

import java.util.Objects;

public class DefaultCsvLine implements CsvLine {

  private final Headers headers;
  private final Object[] values;

  public DefaultCsvLine(final Headers headers) throws NullPointerException {
    this(headers, new Object[headers.size()]);
  }

  public DefaultCsvLine(final Headers headers, final Object[] values) throws NullPointerException {
    this.headers = Objects.requireNonNull(headers);
    this.values = Objects.requireNonNull(values);
    if (headers.size() != values.length) {
      throw new IllegalArgumentException();
    }
  }

  public Object[] getValues() {
    return values;
  }

  @Override
  public void setValue(final int columnIndex, final Object value) {
    values[columnIndex] = value;
  }

  @Override
  public void setValue(final String columnName, final Object value) {
    setValue(headers.indexOf(columnName), value);
  }
}