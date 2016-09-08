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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.javacreed.api.csv.common.NumbersUtils;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class CsvReadableParameters {

  private static final int DEFAULT_BUFFER_SIZE = 1024;

  private static final String DEFAULT_VALUE_SEPARATOR = ",";

  private static final String DEFAULT_VALUE_BOUNDERIES = "\"";

  private static final String DEFAULT_ESCAPE_CHARACTER = "\\";

  private static final Set<String> DEFAULT_LINE_SEPARATORS = Collections
      .unmodifiableSet(new LinkedHashSet<>(Arrays.asList("\r\n", "\n", "\r")));

  private int bufferSize = CsvReadableParameters.DEFAULT_BUFFER_SIZE;
  private String valueSeparator = CsvReadableParameters.DEFAULT_VALUE_SEPARATOR;
  private String valueBounderies = CsvReadableParameters.DEFAULT_VALUE_BOUNDERIES;
  private String escapeCharacter = CsvReadableParameters.DEFAULT_ESCAPE_CHARACTER;
  private Set<String> lineSeparators = CsvReadableParameters.DEFAULT_LINE_SEPARATORS;

  public void addLineSeparator(final String lineSeparator) throws NullPointerException {
    if (lineSeparators == null) {
      lineSeparators = new LinkedHashSet<>();
    }
    lineSeparators.add(lineSeparator);
  }

  public int getBufferSize() {
    return bufferSize;
  }

  public String getEscapeCharacter() {
    return escapeCharacter;
  }

  public Set<String> getLineSeparators() {
    return lineSeparators;
  }

  public String getValueBounderies() {
    return valueBounderies;
  }

  public String getValueSeparator() {
    return valueSeparator;
  }

  public void setBufferSize(final int bufferSize) throws IllegalArgumentException {
    this.bufferSize = NumbersUtils.failIfNotPositive(bufferSize, "buffer size");
  }

  public void setEscapeCharacter(final String escapeCharacter) throws NullPointerException {
    this.escapeCharacter = Objects.requireNonNull(escapeCharacter);
  }

  public void setLineSeparators(final Set<String> lineSeparators) throws NullPointerException {
    this.lineSeparators = Objects.requireNonNull(lineSeparators);
  }

  public void setValueBounderies(final String valueBounderies) throws NullPointerException {
    this.valueBounderies = Objects.requireNonNull(valueBounderies);
  }

  public void setValueSeparator(final String valueSeparator) throws NullPointerException {
    this.valueSeparator = Objects.requireNonNull(valueSeparator);
  }
}
