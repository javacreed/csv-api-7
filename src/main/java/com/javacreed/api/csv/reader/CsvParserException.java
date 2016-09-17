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

public class CsvParserException extends CsvReadException {

  /** */
  private static final long serialVersionUID = -6766221199427575731L;

  public CsvParserException() {
    super();
  }

  public CsvParserException(final String message) {
    super(message);
  }

  public CsvParserException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public CsvParserException(final Throwable cause) {
    super(cause);
  }
}
