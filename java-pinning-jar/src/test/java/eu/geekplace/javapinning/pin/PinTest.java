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
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.security.cert.CertificateEncodingException;

import eu.geekplace.javapinning.TestUtilities;
import eu.geekplace.javapinning.util.HexUtilities;
import eu.geekplace.javapinning.util.X509CertificateUtilities;

public class PinTest {

	private static final String PIN_STRING_W_COLON = "83:F9:17:1E:06:A3:13:11:88:89:F7:D7:93:02:BD:1B:7A:20:42:EE:0C:FD:02:9A:BF:8D:D0:6F:FA:6C:D9:D3";

	@Test
	public void pinWithFullwidthColon() {
		Pin pin = Pin.fromString("CERTSHA256:" + PIN_STRING_W_COLON);
		byte[] pinBytes = pin.getPinBytes();
		final String pinString = HexUtilities.encodeToHex(pinBytes, true, true);
		// String.format() appends a ':' at the very end, so we have to do that too
		assertEquals(PIN_STRING_W_COLON, pinString);
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
		byte[] pinBytes = pin.getPinBytes();
		final String pinString = HexUtilities.encodeToHex(pinBytes, true, true);
		// String.format() appends a ':' at the very end, so we have to do that too
		assertEquals(PIN_STRING_W_COLON, pinString);
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

	@Test
	public void fromString_plainCertHexString_returnsCertPlainPin(){
		CertPlainPin plainPin = (CertPlainPin) Pin.fromString("CERTPLAIN:" + TestUtilities.PLAIN_CERTIFICATE_1);
		assertNotNull(plainPin.getX509Certificate());
	}

	@Test
	public void fromString_plainPinHexString_returnsPlainPin(){
		PlainPin plainPin = (PlainPin) Pin.fromString("PLAIN:" + TestUtilities.PLAIN_PUBLIC_KEY_1);
		assertNotNull(plainPin.getPublicKey());
	}

	@Test
	public void fromCertificate_notNull_returnsCertPLainPin() throws CertificateEncodingException {
		CertPlainPin certPlainPin = Pin.fromCertificate(X509CertificateUtilities.decodeX509Certificate(TestUtilities.PLAIN_CERTIFICATE_1));
		assertNotNull(certPlainPin);
	}

	@Test
	public void fromPublicKey_notNull_returnsPLainPin() throws CertificateEncodingException {
		PlainPin plainPin = Pin.fromPublicKey(X509CertificateUtilities.decodeX509PublicKey(TestUtilities.PLAIN_PUBLIC_KEY_1));
		assertNotNull(plainPin);
	}
}
