/**
 *
 * Copyright 2015 Florian Schmaus
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
package eu.geekplace.javapinning.pin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class PinTest {

	public static final String PIN_STRING_W_COLON = "83:F9:17:1E:06:A3:13:11:88:89:F7:D7:93:02:BD:1B:7A:20:42:EE:0C:FD:02:9A:BF:8D:D0:6F:FA:6C:D9:D3";

	@Test
	public void pinWithFullwidthColon() {
		Pin pin = Pin.fromString("CERTSHA256:" + PIN_STRING_W_COLON);
		byte[] pinBytes = pin.getPinBytes();
		StringBuilder sb = new StringBuilder(PIN_STRING_W_COLON.length());
		for (byte b : pinBytes) {
			sb.append(String.format("%02X:", b));
		}
		// String.format() appends a ':' at the very end, so we have to do that too
		assertEquals(PIN_STRING_W_COLON + ":", sb.toString());
	}

	@Test
	public void pinWithWhitespaces() {
		// Let's shoot some holes into the pin string
		final int[] subsequences = new int[] { 4, 17, 21, 32 };
		StringBuilder sb = new StringBuilder();
		int start = 0;
		for (int i : subsequences) {
			sb.append(PIN_STRING_W_COLON.subSequence(start, i));
			sb.append(" ");
			start = i;
		}
		sb.append(PIN_STRING_W_COLON.subSequence(start, PIN_STRING_W_COLON.length()));
		Pin pin = Pin.fromString("CERTSHA256:" + sb.toString());
		sb = new StringBuilder(PIN_STRING_W_COLON.length());
		byte[] pinBytes = pin.getPinBytes();
		for (byte b : pinBytes) {
			sb.append(String.format("%02X:", b));
		}
		// String.format() appends a ':' at the very end, so we have to do that too
		assertEquals(PIN_STRING_W_COLON + ":", sb.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void pinWithControlChars() {
		final String pinString = "\tAB:cd\u1234";
		Pin.fromString("SHA256:" + pinString);
	}

	@Test(expected = IllegalArgumentException.class)
	public void pinWithnonAsciiLetters() {
		final String pinString = "αΒΕGλ";
		Pin.fromString("SHA256:" + pinString);
	}
}
