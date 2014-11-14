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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Pin {

	private static final Logger LOGGER = Logger.getLogger(Sha256Pin.class.getName());

	protected static final MessageDigest sha256md;

	static {
		MessageDigest sha256mdtemp = null;
		try {
			sha256mdtemp = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.log(Level.WARNING, "SHA-256 MessageDigest not available", e);
		}
		sha256md = sha256mdtemp;
	}

	protected final byte[] pinBytes;

	protected Pin(String pinHexString) {
		// Convert the pinHexString to bytes
		int len = pinHexString.length();
		pinBytes = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			pinBytes[i / 2] = (byte) ((Character.digit(pinHexString.charAt(i), 16) << 4) + Character
					.digit(pinHexString.charAt(i + 1), 16));
		}
	}

	public abstract boolean pinsCertificate(X509Certificate x509certificate) throws CertificateEncodingException;

	protected abstract boolean pinsCertificate(byte[] pubkey);

	public static Pin fromString(String string) {
		String[] pin = string.split(":");
		if (pin.length != 2) {
			throw new IllegalArgumentException();
		}
		String type = pin[0];
		String pinHex = pin[1];
		switch (type) {
		case "SHA256":
			return new Sha256Pin(pinHex);
		case "PLAIN":
			return new PlainPin(pinHex);
		case "CERTSHA256":
			return new CertSha256Pin(pinHex);
		case "CERTPLAIN":
			return new CertPlainPin(pinHex);
		default:
			throw new IllegalArgumentException();
		}
	}
}
