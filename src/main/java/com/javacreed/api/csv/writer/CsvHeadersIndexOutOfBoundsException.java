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
 * Thrown when the given header index is out of the expected range
 *
 * @author Albert Attard
 */
public class CsvHeadersIndexOutOfBoundsException extends CsvRuntimeException {

  private static final long serialVersionUID = -859176464265481414L;

  private static final String formatMessage(final int index, final int headerLength) {
    return "The index " + index + " is out of range [0," + headerLength + ")";
  }

  private final int index;

  private final int headerLength;

  public CsvHeadersIndexOutOfBoundsException(final int index, final int headerLength) {
    super(CsvHeadersIndexOutOfBoundsException.formatMessage(index, headerLength));
    this.index = index;
    this.headerLength = headerLength;
  }

  public int getHeaderLength() {
    return headerLength;
  }

  public int getIndex() {
    return index;
  }
}
