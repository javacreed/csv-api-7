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

/**
 * Utilities methods related to numbers
 *
 * @author Albert Attard
 */
public class NumbersUtils {

  /**
   * Fails with an {@link IllegalArgumentException} if the given {@code value} is less than {@code minValue} or greater
   * than {@code maxValue}, otherwise it returns the given {@code value}.
   *
   * @param value
   *          the value to be checked
   * @param minValue
   *          the minimum allowed value (inclusive)
   * @param maxValue
   *          the maximum allowed value (inclusive)
   * @param fieldName
   *          the field name (used in the exception message)
   * @return the given value
   * @throws IllegalArgumentException
   *           if the given {@code value} is less than or greater than the given boundaries (both inclusive)
   */
  public static int failIfNotInRange(final int value, final int minValue, final int maxValue, final String fieldName)
      throws IllegalArgumentException {
    if (value < minValue || value > maxValue) {
      throw new IllegalArgumentException(
          "The " + fieldName + " value " + value + " is out of range: [" + minValue + "," + maxValue + "]");
    }

    return value;
  }

  /**
   * Fails if the given {@code value} is negative (that is less than {@code 0}).
   *
   * @param value
   *          the value to be checked
   * @param fieldName
   *          the field name (used in the exception message)
   * @return the given value
   * @throws IllegalArgumentException
   *           if the given {@code value} negative (that is less than 0)
   */
  public static int failIfNotPositive(final int value, final String fieldName) throws IllegalArgumentException {
    if (value < 0) {
      throw new IllegalArgumentException("The " + fieldName + " value is negative (less than zero)");
    }

    return value;
  }

  /** Cannot be initialised */
  private NumbersUtils() {}
}
