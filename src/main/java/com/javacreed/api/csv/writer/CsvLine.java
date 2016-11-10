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

import com.javacreed.api.csv.common.CsvColumnNotFoundException;
import com.javacreed.api.csv.common.DefaultHeaders;
import com.javacreed.api.csv.common.Headers;

/**
 * Represents the current line that is being written
 *
 * @author Albert Attard
 */
public interface CsvLine {

  /**
   * Sets the value at the given column index. The value can be {@code null} and if another value already exists in the
   * given column index, the new one will always replace the existing one. If the column index is out of range, then a
   * {@link CsvHeadersIndexOutOfBoundsException} is thrown.
   *
   * @param columnIndex
   *          the column index (which needs to be between 0 (inclusive) and the headers' size (exclusive))
   * @param value
   *          the value (which can be {@code null})
   * @return this (for method chaining)
   * @throws CsvHeadersIndexOutOfBoundsException
   *           if the given {@code columnIndex} is out of range, that is less than 0 or greater-than/equal-to the
   *           headers' size
   */
  CsvLine setValue(int columnIndex, Object value) throws CsvHeadersIndexOutOfBoundsException;

  /**
   * Sets the value of the given column name. he value can be {@code null} and if a value exists for the same column
   * name, the new value will always replace the existing one. The column name is generally case-insensitive but this is
   * dependent on the type of {@link Headers} used. If no column is found matching the given name, a
   * {@link CsvColumnNotFoundException} is thrown.
   * <p>
   * This is optional functionality and is only available if {@link Headers} used support it. An
   * {@link UnsupportedOperationException} is thrown if the {@link Headers} used do not support this.
   *
   * @param columnName
   *          the column name (which cannot be {@code null})
   * @param value
   *          the value (which can be {@code null})
   * @return this (for method chaining)
   * @throws CsvColumnNotFoundException
   *           if no column is found with the given name
   * @throws UnsupportedOperationException
   *           if the CVS does not provide column names
   * @throws NullPointerException
   *           if the given {@code columnName} is {@code null}
   *
   * @see Headers
   * @see DefaultHeaders
   */
  CsvLine setValue(String columnName, Object value)
      throws CsvColumnNotFoundException, UnsupportedOperationException, NullPointerException;

  void write() throws CsvWriteException;
}
