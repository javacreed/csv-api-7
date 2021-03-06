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

import com.javacreed.api.csv.common.CsvRuntimeException;

/**
 * Thrown when the headers are already set. Once the headers are set, these cannot be changed
 *
 * @author Albert Attard
 */
public class CsvHeadersAlreadySetException extends CsvRuntimeException {

  private static final long serialVersionUID = 361423641749283863L;

  /**
   * Creates an instance of this class
   */
  public CsvHeadersAlreadySetException() {
    super("Csv headers already set");
  }
}
