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
package eu.geekplace.javapinning;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import eu.geekplace.javapinning.util.HexUtilities;
import eu.geekplace.javapinning.util.X509CertificateUtilities;

import org.junit.Test;

public class PinningTrustManagerTest {

	private static final X509Certificate[] DUMMY_CHAIN = new X509Certificate[] {X509CertificateUtilities.decodeX509Certificate(HexUtilities.decodeFromHex(TestUtilities.PLAIN_CERTIFICATE_1))};

	@Test(expected = CertificateNotPinnedException.class)
	public void shouldThrowIfNotPinned() throws CertificateException {
		X509TrustManager tm = JavaPinning.trustManagerForPin("CERTPLAIN:" + TestUtilities.PLAIN_CERTIFICATE_2);
		tm.checkServerTrusted(DUMMY_CHAIN, "");
	}

	@Test
	public void shouldReportPinIfNotPinned() {
		X509TrustManager tm = JavaPinning.trustManagerForPin("CERTPLAIN:" + TestUtilities.PLAIN_CERTIFICATE_2);
		try {
			tm.checkServerTrusted(DUMMY_CHAIN, "");
			fail();
		} catch (CertificateException e) {
			// Assert that the message of the exception contains a hint how to pin the certificate
			final String dummyCertPin = "CERTPLAIN:" + TestUtilities.PLAIN_CERTIFICATE_1;
			assertTrue(e.getMessage().contains(dummyCertPin));
		}
	}
}
