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

  /**
   * A builder class for the {@link DefaultCsvFormatter}. The builder starts with the defaults and each part can be
   * modified to meet the requirements.
   *
   * @author Albert Attard
   */
  @NotThreadSafe
  public static class Builder {

    /** The value separator */
    private String valueSeparator = DefaultCsvFormatter.DEFAULT_VALUE_SEPERATOR;

    /** The line separator */
    private String lineSeparator = DefaultCsvFormatter.DEFAULT_LINE_SEPERATOR;

    /** The escape character (which can be more than a character long) */
    private String escapeCharacter = DefaultCsvFormatter.DEFAULT_ESCAPE_CHARACTER;

    /** The header column formatter */
    private HeaderColumnFormatter headerColumnFormatter = DefaultHeaderColumnFormatter.INSTANCE;

    /** The builder used to populate the column formatters */
    private final ChainedDataColumnFormatterProvider.Builder columnFormatterProviderBuilder = new ChainedDataColumnFormatterProvider.Builder();

    /**
     * Creates a new instance of the {@link DefaultCsvFormatter} using the given parameters
     *
     * @return a new instance of {@link DefaultCsvFormatter}
     */
    public DefaultCsvFormatter build() {
      final DataColumnFormatterProvider columnFormatterProvider = columnFormatterProviderBuilder.build();
      return new DefaultCsvFormatter(valueSeparator, lineSeparator, escapeCharacter, headerColumnFormatter,
          columnFormatterProvider);
    }

    /**
     * Set the escape characters (which can be more than a character long) to be used
     *
     * @param escapeCharacter
     *          the escape characters (which cannot be {@code null})
     * @return this (for method chaining)
     * @throws NullPointerException
     *           if the given escape character is {@code null}
     */
    public Builder escapeCharacter(final String escapeCharacter) throws NullPointerException {
      this.escapeCharacter = Objects.requireNonNull(escapeCharacter);
      return this;
    }

    /**
     * Sets the header column formatter
     *
     * @param headerColumnFormatter
     *          the header column formatter (which cannot be {@code null})
     * @return this (for method chaining)
     * @throws NullPointerException
     *           if the given header column formatter is {@code null}
     */
    public Builder headerColumnFormatter(final HeaderColumnFormatter headerColumnFormatter)
        throws NullPointerException {
      this.headerColumnFormatter = Objects.requireNonNull(headerColumnFormatter);
      return this;
    }

    /**
     * Sets the line separator
     *
     * @param lineSeparator
     *          the line separator (which cannot be {@code null})
     * @return this (for method chaining)
     * @throws NullPointerException
     *           if the given line separator is {@code null}
     */
    public Builder lineSeparator(final String lineSeparator) throws NullPointerException {
      this.lineSeparator = Objects.requireNonNull(lineSeparator);
      return this;
    }

    /**
     * Uses the platform line separator. Please note that this varies between one operating system to another.
     *
     * @return this (for method chaining)
     */
    public Builder nativeLineSeparator() {
      return lineSeparator(System.lineSeparator());
    }

    /**
     * Registers a column formatter for the given type. This will affect not only this type but also any objects that
     * inherit from this type. When selecting the right formatter, the closest implementation is preferred over a less
     * generic one. For example, The {@link String} class implements the {@link CharSequence} interface. Therefore when
     * formatting strings, the formatter register for {@link String} type is preferred over the one for
     * {@link CharSequence}.
     *
     * @param type
     *          the object type (which cannot be {@code null})
     * @param columnFormatter
     *          the column formatter (which cannot be {@code null})
     * @return this (for method chaining)
     * @throws NullPointerException
     *           if any of the given parameters are {@code null}
     */
    public Builder register(final Class<?> type, final DataColumnFormatter columnFormatter)
        throws NullPointerException {
      columnFormatterProviderBuilder.register(type, columnFormatter);
      return this;
    }

    /**
     * Registers the given formatter at the given column indices, replacing any existing formatter at the same column.
     *
     * @param columns
     *          the column indices (and each index needs to be greater than or equal to 0)
     * @param columnFormatter
     *          the column formatter (which cannot be {@code null})
     * @return this (for method chaining)
     * @throws NullPointerException
     *           if the given column formatter is {@code null}
     * @throws IllegalArgumentException
     *           if any of the given varagrs column index are negative, that is, less than 0
     */
    public Builder register(final DataColumnFormatter columnFormatter, final int... columns) {
      columnFormatterProviderBuilder.register(columnFormatter, columns);
      return this;
    }

    /**
     * Adds a column formatter provider to the list of providers
     *
     * @param columnFormatterProvider
     *          the column formatter provider (which cannot be {@code null})
     * @return this (for method chaining)
     * @throws NullPointerException
     *           if the given provider is {@code null}
     */
    public Builder register(final OrderedDataColumnFormatterProvider columnFormatterProvider)
        throws NullPointerException {
      columnFormatterProviderBuilder.register(columnFormatterProvider);
      return this;
    }

    /**
     * Registers a new instance of the null column formatter provider using the default order.
     * <p>
     * This is equivalent to
     *
     * <pre>
     * String nullEquivalent = ...
     * DefaultCsvFormatter.Builder builder = ...
     * builder.register(new NullColumnFormatterProvider("THIS-IS-NULL"));
     * </pre>
     *
     * @param nullEquivalent
     *          the string with which {@code null}s will be replaced (which cannot be {@code null})
     * @return this (for method chaining)
     * @throws NullPointerException
     *           if the given null equivalent string is {@code null}
     *
     * @see #register(OrderedDataColumnFormatterProvider)
     * @see NullColumnFormatterProvider
     */
    public Builder replaceNullsWith(final String nullEquivalent) throws NullPointerException {
      return register(new NullColumnFormatterProvider(nullEquivalent));
    }

    /**
     * Change the value separator
     *
     * @param valueSeparator
     *          the value separator (which cannot be {@code null})
     * @return this (for method chaining)
     * @throws NullPointerException
     *           if the given value separator is {@code null}
     */
    public Builder valueSeparator(final String valueSeparator) throws NullPointerException {
      this.valueSeparator = Objects.requireNonNull(valueSeparator);
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
  public CharSequence formatCellValue(final int columnIndex, final Object value) throws CsvConfigurationException {
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
  public CharSequence formatHeaderValue(final int columnIndex, final String value) throws CsvConfigurationException {
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
