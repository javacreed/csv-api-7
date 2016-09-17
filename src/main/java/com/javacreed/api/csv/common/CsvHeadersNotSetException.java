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
 * Thrown when a request is made for the headers and these are not set
 *
 * @author Albert Attard
 */
public class CsvHeadersNotSetException extends CsvRuntimeException {

  private static final long serialVersionUID = -2547472977606508690L;

  /**
   * Creates an instance of this exception
   */
  public CsvHeadersNotSetException() {
    super("The CSV headers are not set");
  }
}