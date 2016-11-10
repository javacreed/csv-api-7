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
 * Test the {@link BlankHeaders#indexOf(String)} method
 *
 * @author Albert Attard
 * @see BlankHeaders#indexOf(String)
 */
public class BlankHeaders_IndexOf_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /**
   * Tests the {@link BlankHeaders#indexOf(String)} method to make sure that it fails as expected
   *
   * @throws UnsupportedOperationException
   *           method not supported
   */
  @Test
  public void testWithName() throws UnsupportedOperationException {
    thrown.expect(UnsupportedOperationException.class);
    thrown.expectMessage("Column names were not provided");
    new BlankHeaders(0).indexOf("test");
  }

  /**
   * Tests the {@link BlankHeaders#indexOf(String)} method to make sure that it fails as expected
   *
   * @throws UnsupportedOperationException
   *           method not supported
   */
  @Test
  public void testWithNull() throws UnsupportedOperationException {
    thrown.expect(UnsupportedOperationException.class);
    thrown.expectMessage("Column names were not provided");
    new BlankHeaders(0).indexOf(null);
  }
}
