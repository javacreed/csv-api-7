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

public class NumbersUtils {

  public static int failIfNotInRange(final int value, final int minValue, final int maxValue, final String fieldName) {
    if (value < minValue || value > maxValue) {
      throw new IllegalArgumentException(
          "The " + fieldName + " value " + value + " is out of range: [" + minValue + "," + maxValue + "]");
    }

    return value;
  }

  public static int failIfNotPositive(final int value, final String fieldName) {
    if (value < 0) {
      throw new IllegalArgumentException("The " + fieldName + " value is negative (less than zero)");
    }

    return value;
  }

  private NumbersUtils() {}
}
