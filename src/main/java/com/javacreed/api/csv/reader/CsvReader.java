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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.javacreed.api.csv.common.BlankHeaders;
import com.javacreed.api.csv.common.DefaultHeaders;
import com.javacreed.api.csv.common.Headers;

public class CsvReader {

  private Headers headers;

  private final CsvReadable reader;

  public CsvReader(final CsvReadable reader) throws NullPointerException {
    this.reader = Objects.requireNonNull(reader);
  }

  public boolean hasMoreLines() throws CsvReadException {
    return reader.hasMoreLines();
  }

  public CsvReader headers(final Headers headers) throws NullPointerException {
    this.headers = Objects.requireNonNull(headers);
    return this;
  }

  public CsvReader readHeaders() throws CsvReadException {
    headers = new DefaultHeaders(readValues());
    return this;
  }

  public CsvLine readLine() throws CsvReadException {
    if (false == reader.hasMoreLines()) {
      return null;
    }

    final String[] values = readValues();
    if (headers == null) {
      headers = new BlankHeaders(values.length);
    } else if (values.length != headers.size()) {
      throw new CsvReadException("Row size " + values.length + " is different from header size " + headers.size());
    }

    return new DefaultCsvLine(headers, values);
  }

  public String[] readValues() throws CsvReadException {
    final List<String> values = new ArrayList<>();
    reader.beginLine();
    while (reader.hasMoreValues()) {
      values.add(reader.readValue());
    }
    reader.endLine();
    return values.toArray(new String[values.size()]);
  }
}
