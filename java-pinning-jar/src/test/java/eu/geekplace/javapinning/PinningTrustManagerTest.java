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
package eu.geekplace.javapinning;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.junit.Test;

import eu.geekplace.javapinning.util.JavaPinningUtil;

public class PinningTrustManagerTest {

	private static final DummyX509Certificate DUMMY_LEAF_CERTIFICATE = new DummyX509Certificate();
	private static final X509Certificate[] DUMMY_CHAIN = new X509Certificate[] { DUMMY_LEAF_CERTIFICATE };

	@Test(expected = CertificateException.class)
	public void shouldThrowIfNotPinned() throws CertificateException {
		X509TrustManager tm = JavaPinning.trustManagerForPin("CERTPLAIN:abba");
		tm.checkServerTrusted(DUMMY_CHAIN, "");
	}

	@Test
	public void shouldReportPinIfNotPinned() {
		X509TrustManager tm = JavaPinning.trustManagerForPin("CERTPLAIN:abba");
		try {
			tm.checkServerTrusted(DUMMY_CHAIN, "");
			fail();
		} catch (CertificateException e) {
			// Assert that the message of the exception contains a hint how to pin the certificate
			final String dummyCertPin = "CERTPLAIN:" + JavaPinningUtil.toHex(DUMMY_LEAF_CERTIFICATE.getEncoded(), false, false);
			assertTrue(e.getMessage().contains(dummyCertPin));
		}
	}
}
