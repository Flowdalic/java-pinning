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

public abstract class CertPin extends Pin {

	protected CertPin(byte[] pinBytes) {
		super(pinBytes);
	}

	protected CertPin(String pinHexString) {
		super(pinHexString);
	}

	@Override
	public boolean pinsCertificate(X509Certificate x509certificate) throws CertificateEncodingException {
		byte[] pubkey = x509certificate.getEncoded();
		return pinsCertificate(pubkey);
	}

	protected abstract boolean pinsCertificate(byte[] certificate);

}
