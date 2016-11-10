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

import java.nio.CharBuffer;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test the {@link CharBufferUtils#startsWith(CharBuffer, String)} method
 *
 * @author Albert Attard
 * @see CharBufferUtils#startsWith(CharBuffer, String)
 */
public class CharBufferUtils_StartsWith_Test {

  /**
   * Test the {@link CharBufferUtils#startsWith(CharBuffer, String)} method with several inputs
   */
  @Test
  public void test() {
    final CharBuffer charBuffer = CharBuffer.wrap("test string");

    Assert.assertEquals(0, charBuffer.position());
    Assert.assertFalse(CharBufferUtils.startsWith(charBuffer, "string"));
    Assert.assertTrue(CharBufferUtils.startsWith(charBuffer, "test"));
    Assert.assertEquals(0, charBuffer.position());

    charBuffer.position(5);
    Assert.assertTrue(CharBufferUtils.startsWith(charBuffer, "string"));
    Assert.assertEquals(5, charBuffer.position());

    charBuffer.position(10);
    Assert.assertTrue(CharBufferUtils.startsWith(charBuffer, "g"));
    Assert.assertEquals(10, charBuffer.position());

    /* Make sure that if we go over the buffer no exceptions are thrown */
    Assert.assertFalse(CharBufferUtils.startsWith(charBuffer, "gentle"));
  }
}
