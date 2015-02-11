/**
 *
 * Copyright 2014-2015 Florian Schmaus
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
		// Convert pinHexString to lower case. Note that we don't need to use
		// the locale argument version of toLowerCase() as we will throw an
		// exception later anyway when a character ^[a-f0-9] is found
		pinHexString = pinHexString.toLowerCase();
		// Replace all ':' and whitespace characters with the empty string, i.e. remove them from pinHexString
		pinHexString = pinHexString.replaceAll("[:\\s]", "");

		final char[] pinHexChars = pinHexString.toCharArray();
		// Check that pinHexChars only contains chars [a-f0-9]
		for (char c : pinHexChars) {
			// Throw exception if char is ^[a-f0-9]
			if (! ((c >= 'a' && c <= 'f') || (c >= '0' && c <= '9'))) {
				throw new IllegalArgumentException(
						"Pin String must only contain whitespaces, semicolons (':'), and ASCII letters [a-fA-F] and numbers [0-9], found offending char: '"
								+ c + "'");
			}
		}

		// Convert the pinHexString to bytes
		final int len = pinHexChars.length;
		pinBytes = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			pinBytes[i / 2] = (byte) ((Character.digit(pinHexString.charAt(i), 16) << 4) + Character
					.digit(pinHexString.charAt(i + 1), 16));
		}
	}

	public abstract boolean pinsCertificate(X509Certificate x509certificate) throws CertificateEncodingException;

	protected abstract boolean pinsCertificate(byte[] pubkey);

	/**
	 * Create a new Pin from the given String.
	 * <p>
	 * The Pin String must be in the format <tt>[type]:[hex-string]</tt>, where
	 * <tt>type</tt> denotes the type of the Pin and <tt>hex-string</tt> is the
	 * binary value of the Pin encoded in hex. Currently supported types are
	 * <ul>
	 * <li>PLAIN</li>
	 * <li>SHA256</li>
	 * <li>CERTPLAIN</li>
	 * <li>CERTSHA256</li>
	 * </ul>
	 * The hex-string must contain only of whitespace characters, colons (':'),
	 * numbers [0-9] and ASCII letters [a-fA-F]. It must be a valid hex-encoded
	 * binary representation. First the string is lower-cased, then all
	 * whitespace characters and colons are removed before the string is decoded
	 * to bytes.
	 * </p>
	 *
	 * @param string
	 *            the Pin String.
	 * @return the Pin for the given Pin String.
	 * @throws IllegalArgumentException
	 *             if the given String is not a valid Pin String
	 */
	public static Pin fromString(String string) {
		// The Pin's string may have multiple colons (':'), assume that
		// everything before the first colon is the Pin type and everything
		// after the colon is the Pin's byte encoded in hex.
		String[] pin = string.split(":", 2);
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

	/**
	 * Returns a clone of the bytes that represent this Pin.
	 * <p>
	 * This method is meant for unit testing only and therefore not public.
	 * </p>
	 *
	 * @return a clone of the bytes that represent this Pin.
	 */
	byte[] getPinBytes() {
		return pinBytes.clone();
	}
}
