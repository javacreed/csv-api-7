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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test the {@link DefaultHeaders#DefaultHeaders(int)} constructor
 *
 * @author Albert Attard
 * @see DefaultHeaders#DefaultHeaders(int)
 */
public class DefaultHeaders_Constructor_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /**
   * Tests the {@link DefaultHeaders#DefaultHeaders(int)} constructor with a negative value to make sure that it fails
   * as expected
   *
   * @throws IllegalArgumentException
   *           the test input is invalid
   */
  @Test
  public void testNegativeValue1() throws IllegalArgumentException {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("The header size cannot be negative");
    new DefaultHeaders(-1);
  }

  /**
   * Tests the {@link DefaultHeaders#DefaultHeaders(int)} constructor with a negative value to make sure that it fails
   * as expected
   *
   * @throws IllegalArgumentException
   *           the test input is invalid
   */
  @Test
  public void testNegativeValue2() throws IllegalArgumentException {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("The header size cannot be negative");
    new DefaultHeaders(Integer.MIN_VALUE);
  }

  /**
   * Tests the {@link DefaultHeaders#DefaultHeaders(int)} constructor with valid values
   */
  @Test
  public void testValidValues() {
    new DefaultHeaders(0);
    new DefaultHeaders(1024);
  }
}
