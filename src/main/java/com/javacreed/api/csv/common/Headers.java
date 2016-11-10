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
package com.javacreed.api.csv.common;

/**
 * The CSV header defines the number of columns the CSV file has and optionally provides a mapping between column names
 * and column indices. The latter is optional as we may have CSV without headers and thus we do not have means to map
 * between column names and indices.
 *
 * @author Albert Attard
 */
public interface Headers {

  /**
   * Returns the index of the given column name if one is found, otherwise a {@link CsvColumnNotFoundException} is
   * thrown. Note that the default implementation is case-insensitive but other implementation may be not.
   *
   * @param columnName
   *          the column name (which cannot be {@code null})
   * @return the index of this column
   * @throws CsvColumnNotFoundException
   *           if no column is found with the given name
   * @throws UnsupportedOperationException
   *           if the CVS does not provide column names
   * @throws NullPointerException
   *           if the given {@code columnName} is {@code null}
   */
  int indexOf(String columnName) throws CsvColumnNotFoundException, UnsupportedOperationException, NullPointerException;

  /**
   * Returns the number of columns (a positive number)
   *
   * @return the number of columns (a positive number)
   */
  int size();
}
