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
package eu.geekplace.javapinning.pin;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import eu.geekplace.javapinning.util.X509CertificateUtilities;

public class CertPlainPin extends CertPin {

	private final X509Certificate certificate;

	protected CertPlainPin(X509Certificate certificate) throws CertificateEncodingException {
		super(certificate.getEncoded());
		this.certificate = certificate;
	}

	protected CertPlainPin(String pinHexString) {
		super(pinHexString);
		if (sha256md == null) {
			throw new IllegalStateException("Can not create sha256 pins");
		}
		this.certificate = X509CertificateUtilities.decodeX509Certificate(pinBytes);
	}

	/**
	 * Create a new "plain certificate" {@link Pin} from the given {@link X509Certificate}.
	 * @param certificate the certificate to create a {@link Pin} for.
	 * @return the {@link Pin} for the given certificate.
	 * @throws CertificateEncodingException if the given certificate could not be encoded.
	 */
	public static CertPlainPin fromCertificate(X509Certificate certificate) throws CertificateEncodingException {
		return new CertPlainPin(certificate);
	}

	@Override
	protected boolean pinsCertificate(byte[] certificate) {
		return Arrays.equals(pinBytes, certificate);
	}

	public X509Certificate getX509Certificate(){
		return certificate;
	}
}
