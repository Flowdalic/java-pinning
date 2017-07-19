/**
 *
 * Copyright 2014-2017 Florian Schmaus
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

import java.security.PublicKey;
import java.util.Arrays;

import eu.geekplace.javapinning.util.X509CertificateUtilities;

public class PlainPin extends PublicKeyPin {

	private final PublicKey publicKey;

	protected PlainPin(PublicKey publicKey) {
		super(publicKey.getEncoded());
		this.publicKey = publicKey;
	}

	protected PlainPin(String pinHexString) {
		super(pinHexString);
		this.publicKey = X509CertificateUtilities.decodeX509PublicKey(pinBytes);
	}

	/**
	 * Create a new {@link Pin} from the given {@link PublicKey}.
	 * @param publicKey the public-key to create a {@link Pin} for.
	 * @return the {@link Pin} for the given certificate.
	 */
	public static PlainPin fromPublicKey(PublicKey publicKey) {
		return new PlainPin(publicKey);
	}

	@Override
	protected boolean pinsCertificate(byte[] pubkey) {
		return Arrays.equals(pinBytes, pubkey);
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}
}
