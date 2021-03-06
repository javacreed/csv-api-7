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

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

/**
 * Represents headers without names. The method {@link #indexOf(String)} throws an {@link UnsupportedOperationException}
 * when invoked.
 *
 * @author Albert Attard
 */
@ThreadSafe
@Immutable
public class BlankHeaders implements Headers {

  /** The number of columns (also referred to as the header size) */
  private final int size;

  /**
   * Creates an instance of this class
   *
   * @param size
   *          the number of columns (which must be greater than or equal to 0)
   * @throws IllegalArgumentException
   *           if the given {@code size} is negative (less than 0)
   */
  public BlankHeaders(final int size) throws IllegalArgumentException {
    if (size < 0) {
      throw new IllegalArgumentException("The header size cannot be negative");
    }

    this.size = size;
  }

  @Override
  public int indexOf(final String columnName) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Column names were not provided");
  }

  @Override
  public int size() {
    return size;
  }
}
