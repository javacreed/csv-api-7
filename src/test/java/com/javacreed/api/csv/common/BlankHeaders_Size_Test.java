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
import org.junit.Test;

/**
 * Test the {@link BlankHeaders#size()} method
 *
 * @author Albert Attard
 * @see BlankHeaders#size()
 */
public class BlankHeaders_Size_Test {

  /**
   * Tests the {@link BlankHeaders#size()} method with valid values
   */
  @Test
  public void test() {
    Assert.assertEquals(0, new BlankHeaders(0).size());
    Assert.assertEquals(10, new BlankHeaders(10).size());
    Assert.assertEquals(Integer.MAX_VALUE, new BlankHeaders(Integer.MAX_VALUE).size());
  }
}
