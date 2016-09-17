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

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test the {@link NumbersUtils#failIfNotInRange(int, int, int, String)} method
 *
 * @author Albert Attard
 * @see NumbersUtils#failIfNotInRange(int, int, int, String)
 */
public class NumbersUtils_FailIfInRange_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /**
   * Tests the {@link NumbersUtils#failIfNotPositive(int, String)} method with a negative value (lower than lower bound)
   * to make sure that it fails as expected
   *
   * @throws IllegalArgumentException
   *           the test input is invalid
   */
  @Test
  public void testOutOfRangeValue1() throws IllegalArgumentException {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("The test value -1 is out of range: [0,10]");
    NumbersUtils.failIfNotInRange(-1, 0, 10, "test");
  }

  /**
   * Tests the {@link NumbersUtils#failIfNotPositive(int, String)} method with a negative value (upper than lower bound)
   * to make sure that it fails as expected
   *
   * @throws IllegalArgumentException
   *           the test input is invalid
   */
  @Test
  public void testOutOfRangeValue2() throws IllegalArgumentException {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("The test value 11 is out of range: [0,10]");
    NumbersUtils.failIfNotInRange(11, 0, 10, "test");
  }

  /**
   * Tests the {@link NumbersUtils#failIfNotInRange(int, int, int, String)} method with valid values
   */
  @Test
  public void testValidValues() {
    Assert.assertEquals(0, NumbersUtils.failIfNotInRange(0, 0, 10, "test"));
    Assert.assertEquals(10, NumbersUtils.failIfNotInRange(10, 0, 10, "test"));
    Assert.assertEquals(5, NumbersUtils.failIfNotInRange(5, 5, 5, "test"));
    Assert.assertEquals(Integer.MAX_VALUE,
        NumbersUtils.failIfNotInRange(Integer.MAX_VALUE, 0, Integer.MAX_VALUE, "test"));
  }
}
