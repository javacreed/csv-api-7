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

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
@Immutable
public class Headers {

  private final String[] headers;
  private final Map<String, Integer> columnsIndices = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  public Headers(final String... headers) throws DuplicateColumnNameException {
    this.headers = headers;
    for (int i = 0; i < headers.length; i++) {
      final String header = headers[i];
      if (columnsIndices.containsKey(header)) {
        throw new DuplicateColumnNameException(header);
      }

      columnsIndices.put(header, i);
    }
  }

  public String[] getHeaders() {
    return Arrays.copyOf(headers, headers.length);
  }

  public int indexOf(final String columnName) {
    if (columnsIndices.containsKey(columnName)) {
      return columnsIndices.get(columnName);
    }

    throw new ColumnNotFoundException(columnName);
  }

  public int size() {
    return columnsIndices.size();
  }
}
