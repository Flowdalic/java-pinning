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
package eu.geekplace.javapinning.util;

public class JavaPinningUtil {

	public static StringBuilder toHex(byte[] bytes, boolean uppercase) {
		return toHex(bytes, uppercase, true);
	}

	public static StringBuilder toHex(byte[] bytes, boolean uppercase, boolean colonSeparator) {
		final StringBuilder formatStringBuilder = new StringBuilder(6);
		formatStringBuilder.append("%02");

		if (uppercase) {
			formatStringBuilder.append('X');
		} else {
			formatStringBuilder.append('x');
		}

		if (colonSeparator) {
			formatStringBuilder.append(':');
		}

		final String formatString = formatStringBuilder.toString();
		final StringBuilder sb = new StringBuilder(bytes.length);
		for (byte b : bytes) {
			sb.append(String.format(formatString, b));
		}
		return sb;
	}
}
