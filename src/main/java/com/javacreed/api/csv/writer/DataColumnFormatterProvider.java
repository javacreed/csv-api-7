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

/**
 * A provider that provides the correct {@link DataColumnFormatter} if one is available for the given parameters,
 * otherwise {@code null}.
 *
 * @author Albert Attard
 * @see DataColumnFormatter
 */
public interface DataColumnFormatterProvider {

  /**
   * Returns a column formatter for the given column index and value, if one is found, otherwise {@code null}
   *
   * @param columnIndex
   *          the column index
   * @param value
   *          the value (which may be {@code null})
   * @return a column formatter for the given column index and value, if one is found, otherwise {@code null}
   */
  DataColumnFormatter find(int columnIndex, Object value);

}
