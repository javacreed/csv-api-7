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

public class DateColumnFormatter extends StringColumnFormatter {

  public static final String DEFAULT_DATE_PATTERN = "%tF";

  public static final String DEFAULT_TIME_PATTERN = "%tT";

  public static final String DEFAULT_DATE_TIME_PATTERN = "%tF %<tT";

  public static final DataColumnFormatter DEFAULT_DATE_FORMATER = new DateColumnFormatter(
      DateColumnFormatter.DEFAULT_DATE_PATTERN);

  public static final DataColumnFormatter DEFAULT_DATE_TIME_FORMATER = new DateColumnFormatter(
      DateColumnFormatter.DEFAULT_DATE_TIME_PATTERN);

  public static final DataColumnFormatter DEFAULT_TIME_FORMATER = new DateColumnFormatter(
      DateColumnFormatter.DEFAULT_TIME_PATTERN);

  public static DataColumnFormatter date() {
    return DateColumnFormatter.DEFAULT_DATE_FORMATER;
  }

  public static DataColumnFormatter dateTime() {
    return DateColumnFormatter.DEFAULT_DATE_TIME_FORMATER;
  }

  public static DataColumnFormatter time() {
    return DateColumnFormatter.DEFAULT_TIME_FORMATER;
  }

  private static String validate(final String pattern) throws IllegalArgumentException {
    if (pattern == null) {
      throw new IllegalArgumentException("The pattern cannot be null");
    }

    try {
      String.format(pattern, System.currentTimeMillis());
    } catch (final RuntimeException e) {
      throw new IllegalArgumentException("Invalid date/time formatting pattern");
    }

    return pattern;
  }

  public DateColumnFormatter() {
    this(DateColumnFormatter.DEFAULT_DATE_TIME_PATTERN);
  }

  public DateColumnFormatter(final String pattern) {
    super(DateColumnFormatter.validate(pattern));
  }
}
