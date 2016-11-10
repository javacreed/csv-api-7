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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

/**
 * The default implementation of the {@link Headers} interface.
 * <p>
 * Note that this class is immutable but the parameters passed to the constructors are not copied (are not deep clone).
 * Instead the same instance is used. If these objects are modified outside this class, it will also affect the object.
 *
 * @author Albert Attard
 */
@ThreadSafe
@Immutable
public class DefaultHeaders implements Headers {

  /** The number of columns (also referred to as the header size) */
  private final int size;

  /** The headers may not be provided and this the instance will not have values */
  private final boolean hasValues;

  /** The header columns names (may be null) */
  private final String[] headers;

  /** The column indices indexed by the column name */
  private final Map<String, Integer> columnsIndices;

  /**
   * Creates an instance of this class
   *
   * @param caseSensitive
   *          {@code true} if the columns lookup will be case-sensitive, otherwise {@code false}
   * @param headers
   *          the headers array (which cannot be {@code null})
   * @throws NullPointerException
   *           if the given {@code headers} are {@code null}
   * @throws DuplicateCsvColumnNameException
   *           if the given {@code headers} contain duplicate names
   */
  public DefaultHeaders(final boolean caseSensitive, final String... headers)
      throws NullPointerException, DuplicateCsvColumnNameException {
    this.size = headers.length;
    hasValues = true;
    this.headers = headers;

    columnsIndices = caseSensitive ? new LinkedHashMap<String, Integer>()
        : new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
    for (int i = 0; i < headers.length; i++) {
      final String header = headers[i];
      if (columnsIndices.containsKey(header)) {
        throw new DuplicateCsvColumnNameException(header);
      }

      columnsIndices.put(header, i);
    }
  }

  /**
   * Creates an instance without values of this class
   *
   * @param size
   *          the headers size (which must be positive, not less than 0)
   * @throws IllegalArgumentException
   *           if the given {@code size} is less than 0
   */
  public DefaultHeaders(final int size) throws IllegalArgumentException {
    if (size < 0) {
      throw new IllegalArgumentException("The header size cannot be negative");
    }

    this.size = size;
    hasValues = false;
    headers = null;
    columnsIndices = Collections.emptyMap();
  }

  /**
   * Creates an instance of this class with custom headers mapping. This constructor allows mapping multiple column
   * names to the same columns indices as it takes the mapping map as a parameter. For example, we can map the column
   * names: {@code "Column 1"} and {@code "Column One"} to the same index.
   * <p>
   * No validations are made between the map and headers. The values of the headers may be completely different from
   * those found in the map.
   * <p>
   * Note that modifying these parameters after passing them to this class may also affect the created object.
   *
   * @param columnsIndices
   *          the map of column names and indices (which cannot be {@code null})
   * @param headers
   *          the header names (which cannot be {@code null})
   * @throws NullPointerException
   *           if any of the parameters are {@code null}
   */
  public DefaultHeaders(final Map<String, Integer> columnsIndices, final String... headers)
      throws NullPointerException {

    this.size = headers.length;
    hasValues = true;
    this.headers = headers;
    this.columnsIndices = Objects.requireNonNull(columnsIndices);
  }

  /**
   * Creates an instance of this class using the arrays' values as the header columns mapping. It is important that the
   * header values are unique (case-insensitive) as otherwise a {@link DuplicateCsvColumnNameException} is thrown.
   * Furthermore, the headers cannot have {@code null} values as otherwise a {@link NullPointerException} is thrown
   * <p>
   * This is equivalent to calling {@code new DefaultHeaders(false, headers)}
   *
   * @param headers
   *          the header (which cannot be {@code null}, cannot have {@code null} values and cannot have duplicate
   *          (case-insensitive) values)
   * @throws NullPointerException
   *           if the given {@code headers} is {@code null} or has {@code null} values
   * @throws DuplicateCsvColumnNameException
   *           if the given {@code headers} contains duplicate (case-insensitive) values
   * @see DefaultHeaders#DefaultHeaders(boolean, String...)
   */
  public DefaultHeaders(final String... headers) throws NullPointerException, DuplicateCsvColumnNameException {
    this(false, headers);
  }

  /**
   * Returns a copy of the headers. If this method is invoked when no headers values are set, a
   * {@link CsvHeadersNotSetException} is thrown
   *
   * @return a copy of the headers
   * @throws CsvHeadersNotSetException
   *           if the headers are not set
   * @see #hasValues()
   */
  public String[] getHeaders() throws CsvHeadersNotSetException {
    if (hasValues) {
      return Arrays.copyOf(headers, headers.length);
    }

    throw new CsvHeadersNotSetException();
  }

  /**
   * Returns {@code true} if headers values are provided, {@code false} otherwise
   *
   * @return {@code true} if headers values are provided, {@code false} otherwise
   */
  public boolean hasValues() {
    return hasValues;
  }

  @Override
  public int indexOf(final String columnName) {
    if (columnsIndices.containsKey(columnName)) {
      return columnsIndices.get(columnName);
    }

    throw new CsvColumnNotFoundException(columnName);
  }

  @Override
  public int size() {
    return size;
  }
}
