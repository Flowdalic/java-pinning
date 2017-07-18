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
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.geekplace.javapinning.util.HexUtilities;

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

	protected Pin(byte[] pinBytes){
		this.pinBytes = pinBytes;
	}

	protected Pin(String pinHexString) {
		pinBytes = HexUtilities.decodeFromHex(pinHexString);
	}

	public abstract boolean pinsCertificate(X509Certificate x509certificate) throws CertificateEncodingException;

	protected abstract boolean pinsCertificate(byte[] pubkey);

	/**
	 * Create a new {@link Pin} from the given String.
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
			throw new IllegalArgumentException("Invalid pin string, expected: 'format-specifier:hex-string'.");
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
