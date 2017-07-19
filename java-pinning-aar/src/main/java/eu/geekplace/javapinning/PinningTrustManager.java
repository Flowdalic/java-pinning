/**
 *
 * Copyright 2014 Florian Schmaus
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

import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;

import javax.net.ssl.X509TrustManager;

import eu.geekplace.javapinning.pin.Pin;
import eu.geekplace.javapinning.util.HexUtilities;
import eu.geekplace.javapinning.util.JavaPinningUtil;

public class PinningTrustManager implements X509TrustManager {

	private final Collection<Pin> pins;

	PinningTrustManager(Collection<Pin> pins) {
		this.pins = pins;
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		final X509Certificate leafCertificate = chain[0];
		if (isPinned(leafCertificate)) {
			return;
		}
		// Throw a CertificateException with a meaningful message. Note that we
		// use CERTPLAIN, which tends to be long, so colons as separator are of
		// no use and most other software UIs show the "public key" without
		// colons (and using lowercase letters).
		final String pinHexString = HexUtilities.encodeToHex(leafCertificate.getEncoded());
		throw new CertificateNotPinnedException("Certificate not pinned. Use 'CERTPLAIN:" + pinHexString
				+ "' to pin this certificate. But only pin the certificate if you are sure this is the correct certificate!");
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}

	private boolean isPinned(X509Certificate x509certificate) throws CertificateEncodingException {
		for (Pin pin : pins) {
			if (pin.pinsCertificate(x509certificate)) {
				return true;
			}
		}
		return false;
	}
}
