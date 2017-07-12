/**
 *
 * Copyright 2014-2015 Florian Schmaus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.geekplace.javapinning.util;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HexUtilitiesTest {

    @Test
    public void decodeFromHex_lowercaseHexString_returnsCorrectByteArray() {
        assertArrayEquals(new byte[]{75, 110, 97, 98}, HexUtilities.decodeFromHex("4b6e6162"));
    }

    @Test
    public void decodeFromHex_allCaseHexStringWithWhitespacesAndSemicolons_returnsCorrectByteArray() {
        assertArrayEquals(new byte[]{75, 110, 97, 98}, HexUtilities.decodeFromHex("4B   6e 61:62"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decodeFromHex_notDivisibleByTwoHexString_throwsException() {
        HexUtilities.decodeFromHex("4b6e616");
    }

    @Test(expected = IllegalArgumentException.class)
    public void decodeFromHex_divisibleByTwoWithInvalidCharactersString_throwsException() {
        HexUtilities.decodeFromHex("This is not a valid hex String");
    }

    @Test
    public void encodeToHex_anyByteArray_returnsCorrectHexString() {
        assertEquals("4b6e6162", HexUtilities.encodeToHex(new byte[]{75, 110, 97, 98}));
    }

    @Test
    public void encodeToHexUppercase_anyByteArray_returnsCorrectHexString() {
        assertEquals("4B6E6162", HexUtilities.encodeToHex(new byte[]{75, 110, 97, 98}, true, false));
    }

    @Test
    public void encodeToHexSemicolons_anyByteArray_returnsCorrectHexString() {
        assertEquals("4b:6e:61:62", HexUtilities.encodeToHex(new byte[]{75, 110, 97, 98}, false, true));
    }

    @Test
    public void encodeToHex_emptyArray_returnsEmptyString() {
        assertEquals("", HexUtilities.encodeToHex(new byte[]{}));
    }
}
