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
 * A base exception for column related exceptions
 *
 * @author Albert Attard
 */
public class ColumnException extends CsvRuntimeException {

  private static final long serialVersionUID = -6390804865219241991L;

  /** The column name (related with the exception) */
  private final String columnName;

  /**
   * Creates an instance of this class
   *
   * @param columnName
   *          the column name (which can be blank)
   * @param message
   *          the exception message (which can be blank)
   */
  public ColumnException(final String columnName, final String message) {
    super(message);
    this.columnName = columnName;
  }

  /**
   * Returns the column name (which can be blank)
   *
   * @return the column name (which can be blank)
   */
  public String getColumnName() {
    return columnName;
  }
}
