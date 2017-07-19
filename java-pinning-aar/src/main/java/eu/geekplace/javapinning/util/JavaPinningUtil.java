/**
 *
 * Copyright 2015-2017 Florian Schmaus
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

/**
 * Deprecated.
 *
 * @deprecated Please use {@link HexUtilities}.
 */
@Deprecated
public class JavaPinningUtil {

    /**
     * Deprecated.
     *
     * @deprecated This method appends an unnecessary colon to the end of the HEX String, please
     * use {@link HexUtilities#encodeToHex(byte[])} instead, which does not do this.
     */
    @Deprecated
    public static StringBuilder toHex(byte[] bytes, boolean uppercase) {
        return new StringBuilder(HexUtilities.encodeToHex(bytes, uppercase, true) + ":");
    }

    /**
     * Deprecated.
     *
     * @deprecated This method appends an unnecessary colon to the end of the HEX String if
     * colonSeparator is set to true, please use
     * {@link HexUtilities#encodeToHex(byte[], boolean, boolean)} instead, which does not do this.
     */
    @Deprecated
    public static StringBuilder toHex(byte[] bytes, boolean uppercase, boolean colonSeparator) {
        return new StringBuilder(HexUtilities.encodeToHex(bytes, uppercase, colonSeparator) +
                (colonSeparator ? ":" : ""));
    }
}
