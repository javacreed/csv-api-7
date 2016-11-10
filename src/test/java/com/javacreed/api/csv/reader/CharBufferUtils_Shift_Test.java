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

import java.io.IOException;
import java.io.StringReader;
import java.nio.CharBuffer;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test the {@link CharBufferUtils#shift(CharBuffer)} and {@link CharBufferUtils#shift(CharBuffer, int)} methods
 *
 * @author Albert Attard
 * @see CharBufferUtils#shift(CharBuffer)
 * @see CharBufferUtils#shift(CharBuffer, int)
 */
public class CharBufferUtils_Shift_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /**
   * Test the {@link CharBufferUtils#shift(CharBuffer, int)} method with large offset value (larger than the limit)
   */
  @Test
  public void testLargeValue() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("The offset value must be between 1 and the buffer's limit of 5 both inclusive");
    CharBufferUtils.shift(CharBuffer.allocate(5), 6);
  }

  /**
   * Test the {@link CharBufferUtils#shift(CharBuffer, int)} method with negative offset
   */
  @Test
  public void testNegativeValue() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("The offset value must be between 1 and the buffer's limit of 5 both inclusive");
    CharBufferUtils.shift(CharBuffer.allocate(5), -1);
  }

  /**
   * Test the {@link CharBufferUtils#shift(CharBuffer)} method
   *
   * @throws IOException
   *           if an error occurs while testing
   */
  @Test
  public void testShift() throws IOException {
    final CharBuffer charBuffer = CharBuffer.allocate(11);
    charBuffer.limit(0);
    final String testString = "test string";
    CharBufferUtils.fill(charBuffer, new StringReader(testString));

    /* Read the characters from the buffer and make sure that the right character is read with every shift */
    for (int i = 0; i < testString.length(); i++) {
      Assert.assertEquals(testString.substring(i, i + 1), CharBufferUtils.shift(charBuffer));
    }

    /* Once all characters in the buffer are shifted, the shift method returns 0 */
    Assert.assertEquals(new String(new char[1]), CharBufferUtils.shift(charBuffer));
  }

  /**
   * Test the {@link CharBufferUtils#shift(CharBuffer, int)} method, by reading three characters at each go.
   *
   * @throws IOException
   *           if an error occurs while testing
   */
  @Test
  public void testShiftThreeCharacters() throws IOException {
    final CharBuffer charBuffer = CharBuffer.allocate(11);
    charBuffer.limit(0);
    final String testString = "test string";
    CharBufferUtils.fill(charBuffer, new StringReader(testString));

    /* We will use this string to compare with as it is padded as expected */
    final String expectedString = new String(new char[] { 't', 'e', 's', 't', ' ', 's', 't', 'r', 'i', 'n', 'g', 0 });

    /*
     * Read three characters from the buffer and make sure that the right character is read with every shift. Note that
     * the length of the string is not a multiple of three on purpose and the last read should return two characters and
     * not three.
     */
    for (int i = 0, offset = 3, length = testString.length(); i < length; i += offset) {
      Assert.assertEquals(expectedString.substring(i, i + offset), CharBufferUtils.shift(charBuffer, offset));
    }

    /* Once all characters in the buffer are shifted, the shift method returns 0 */
    Assert.assertEquals(new String(new char[3]), CharBufferUtils.shift(charBuffer, 3));
  }
}
