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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
@Immutable
public class DefaultCsvFormatter implements CsvFormatter {

  @NotThreadSafe
  public static class Builder {

    private String valueSeparator = DefaultCsvFormatter.DEFAULT_VALUE_SEPERATOR;

    private String lineSeparator = DefaultCsvFormatter.DEFAULT_LINE_SEPERATOR;

    private String escapeCharacter = DefaultCsvFormatter.DEFAULT_ESCAPE_CHARACTER;

    private HeaderColumnFormatter headerColumnFormatter = DefaultHeaderColumnFormatter.INSTANCE;

    private DataColumnFormatterProvider columnFormatterProvider;

    private final DefaultDataColumnFormatterProvider.Builder columnFormatterProviderBuilder = new DefaultDataColumnFormatterProvider.Builder();

    public DefaultCsvFormatter build() {
      final DataColumnFormatterProvider columnFormatterProvider;
      if (this.columnFormatterProvider == null) {
        columnFormatterProvider = columnFormatterProviderBuilder.build();
      } else {
        columnFormatterProvider = this.columnFormatterProvider;
      }

      return new DefaultCsvFormatter(valueSeparator, lineSeparator, escapeCharacter, headerColumnFormatter,
          columnFormatterProvider);
    }

    public Builder columnFormatterProvider(final DataColumnFormatterProvider columnFormatterProvider) {
      this.columnFormatterProvider = Objects.requireNonNull(columnFormatterProvider);
      return this;
    }

    public Builder escapeCharacter(final String escapeCharacter) {
      this.escapeCharacter = escapeCharacter;
      return this;
    }

    private void failIfColumnFormatterProviderIsSet() throws IllegalStateException {
      if (columnFormatterProvider != null) {
        throw new IllegalStateException("The column formatter provider is already set");
      }
    }

    public Builder headerColumnFormatter(final HeaderColumnFormatter headerColumnFormatter)
        throws NullPointerException {
      this.headerColumnFormatter = Objects.requireNonNull(headerColumnFormatter);
      return this;
    }

    public Builder lineSeparator(final String lineSeparator) {
      this.lineSeparator = lineSeparator;
      return this;
    }

    public Builder nativeLineSeparator() {
      return lineSeparator(System.lineSeparator());
    }

    public Builder register(final Class<?> type, final DataColumnFormatter columnFormatter) {
      failIfColumnFormatterProviderIsSet();
      columnFormatterProviderBuilder.register(type, columnFormatter);
      return this;
    }

    public void register(final DataColumnFormatter columnFormatter, final int... columns) {
      columnFormatterProviderBuilder.register(columnFormatter, columns);
    }

    public Builder register(final OrderedDataColumnFormatterProvider columnFormatterProvider) {
      failIfColumnFormatterProviderIsSet();
      columnFormatterProviderBuilder.register(columnFormatterProvider);
      return this;
    }

    public Builder valueSeparator(final String valueSeparator) {
      this.valueSeparator = valueSeparator;
      return this;
    }
  }

  public static final DefaultCsvFormatter DEFAULT = new DefaultCsvFormatter.Builder().build();

  public static final String DEFAULT_ESCAPE_CHARACTER = "\\";

  public static final String DEFAULT_VALUE_SEPERATOR = ",";

  public static final String DEFAULT_LINE_SEPERATOR = "\n";

  private final String valueSeparator;

  private final String lineSeparator;

  private final DataColumnFormatterProvider columnFormatterProvider;

  private final HeaderColumnFormatter headerColumnFormatter;

  private final Map<String, String> toEscape;

  public DefaultCsvFormatter(final String valueSeparator, final String lineSeparator, final String escapeCharacter,
      final HeaderColumnFormatter headerColumnFormatter, final DataColumnFormatterProvider columnFormatterProvider) {
    this.valueSeparator = Objects.requireNonNull(valueSeparator);
    this.lineSeparator = Objects.requireNonNull(lineSeparator);
    this.headerColumnFormatter = Objects.requireNonNull(headerColumnFormatter);
    this.columnFormatterProvider = Objects.requireNonNull(columnFormatterProvider);

    final Map<String, String> map = new HashMap<>();
    for (final String key : new String[] { escapeCharacter, lineSeparator, valueSeparator }) {
      map.put(key, escapeCharacter + key);
    }
    map.put("\t", escapeCharacter + "t"); // Tab
    map.put("\b", escapeCharacter + "b"); // Backspace
    map.put("\n", escapeCharacter + "n"); // Newline
    map.put("\r", escapeCharacter + "r"); // Carriage Return
    map.put("\r\n", escapeCharacter + "r" + escapeCharacter + "n"); // Carriage Return + Newline
    map.put("\f", escapeCharacter + "f"); // Formfeed
    map.put("\'", escapeCharacter + "'"); // Single Quote
    map.put("\"", escapeCharacter + "\""); // Double Quote
    map.put("\\", escapeCharacter + "\\"); // Backslash
    toEscape = Collections.unmodifiableMap(map);
  }

  private String escape(final String text) {
    final StringBuilder builder = new StringBuilder(text.length());
    outerLoop: for (int i = 0, size = text.length(); i < size;) {
      for (final Entry<String, String> entry : toEscape.entrySet()) {
        final String escape = entry.getKey();
        if (text.startsWith(escape, i)) {
          builder.append(entry.getValue());
          i += escape.length();
          continue outerLoop;
        }
      }

      builder.append(text.charAt(i));
      i++;
    }

    return builder.toString();
  }

  @Override
  public CharSequence formatCellValue(final int columnIndex, final Object value) {
    final DataColumnFormatter columnFormatter = columnFormatterProvider.find(columnIndex, value);
    if (columnFormatter == null) {
      throw new CsvConfigurationException("Column formatter not found");
    }

    final String formatted = columnFormatter.format(columnIndex, value);
    if (formatted == null) {
      throw new CsvConfigurationException("Column formatter returned an illegal value");
    }

    return escape(formatted);
  }

  @Override
  public CharSequence formatHeaderValue(final int columnIndex, final String value) {
    final String formatted = headerColumnFormatter.format(columnIndex, value);
    if (formatted == null) {
      throw new CsvConfigurationException("Header formatter returned an illegal value");
    }

    return escape(formatted);
  }

  @Override
  public String getLineSeparator() {
    return lineSeparator;
  }

  @Override
  public String getValueSeparator() {
    return valueSeparator;
  }
}
