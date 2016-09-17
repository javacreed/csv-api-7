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
package com.javacreed.api.csv.reader;

import java.util.Objects;

import com.javacreed.api.csv.common.Headers;

public class DefaultCsvLine implements CsvLine {

  private final Headers headers;
  private final String[] values;

  public DefaultCsvLine(final Headers headers, final String[] values) {
    this.headers = Objects.requireNonNull(headers);
    this.values = Objects.requireNonNull(values);
  }

  @Override
  public <T> T getParsedValue(final CsvParser<T> parser, final int columnIndex) throws CsvParserException {
    final String value = getValue(columnIndex);
    try {
      return parser.parse(value);
    } catch (final Exception e) {
      throw new CsvParserException(
          "Failed to parse the value of column with index " + columnIndex + ": '" + value + "'", e);
    }
  }

  @Override
  public <T> T getParsedValue(final CsvParser<T> parser, final String columnName) throws CsvParserException {
    final int columnIndex = headers.indexOf(columnName);
    final String value = getValue(columnIndex);
    try {
      return parser.parse(value);
    } catch (final Exception e) {
      throw new CsvParserException(
          "Failed to parse the value of column with name '" + columnName + "': '" + value + "'", e);
    }
  }

  @Override
  public String getValue(final int columnIndex) {
    return values[columnIndex];
  }

  @Override
  public String getValue(final String columnName) {
    return getValue(headers.indexOf(columnName));
  }
}
