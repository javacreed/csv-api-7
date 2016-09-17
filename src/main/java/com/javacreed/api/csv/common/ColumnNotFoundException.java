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
 * Thrown when the column with a given name is not found
 *
 * @author Albert Attard
 */
public class ColumnNotFoundException extends ColumnException {

  private static final long serialVersionUID = 4093488658878165320L;

  /**
   * Formats the exception message
   *
   * @param columnName
   *          the column name which
   * @return the formatted message
   */
  private static String formatMessage(final String columnName) {
    if (columnName == null) {
      return "Column with NULL name was not found";
    }

    return "Column with name '" + columnName + "' was not found";
  }

  /**
   * Creates an instance of this class
   *
   * @param columnName
   *          the column name (which can be blank)
   */
  public ColumnNotFoundException(final String columnName) {
    super(columnName, ColumnNotFoundException.formatMessage(columnName));
  }
}
