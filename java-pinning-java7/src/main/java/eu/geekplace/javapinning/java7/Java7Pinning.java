/**
 *
 * Copyright 2016-2017 Florian Schmaus
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
package eu.geekplace.javapinning.java7;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import eu.geekplace.javapinning.JavaPinning;
import eu.geekplace.javapinning.PinningTrustManager;
import eu.geekplace.javapinning.pin.Pin;

/**
 * A specialized version of {@link JavaPinning} for runtime environments
 * providing {@code X509ExtendedTrustManager}. Use this instead of JavaPinning
 * when possible, e.g. if you target a recent Java SE version and <b>not</b>
 * Android.
 */
public final class Java7Pinning extends JavaPinning {

	static {
		try {
			Class.forName("javax.net.ssl.X509ExtendedTrustManager");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(
					Java7Pinning.class.getSimpleName()
							+ " requires X509ExtendedTrustManager, but it was not found in classpath (Are you on Android?)",
					e);
		}
	}

	private static final Java7Pinning INSTANCE = new Java7Pinning();

	public static X509TrustManager trustManagerForPins(String... pinStrings) {
		return INSTANCE.tmForPins(pinStrings);
	}

	public static X509TrustManager trustManagerForPin(String pinString) {
		return INSTANCE.tmForPin(pinString);
	}

	public static X509TrustManager trustManagerForPins(Collection<Pin> pins) {
		return INSTANCE.tmForPins(pins);
	}

	/**
	 * Deprecated.
	 *
	 * @deprecated Please use the correctly named: {@link #trustManagerForPins(Collection)}
	 */
	@Deprecated
	public static X509TrustManager trustManagerforPins(Collection<Pin> pins) {
		return INSTANCE.tmForPins(pins);
	}

	public static SSLContext forPins(String... pinString) throws KeyManagementException,
			NoSuchAlgorithmException {
		return INSTANCE.ctxForPins(pinString);
	}

	public static SSLContext forPin(String pinString) throws KeyManagementException,
			NoSuchAlgorithmException {
		return INSTANCE.ctxForPin(pinString);
	}

	public static SSLContext forPins(Collection<Pin> pins) throws KeyManagementException,
			NoSuchAlgorithmException {
		return INSTANCE.ctxForPins(pins);
	}

	private Java7Pinning() {
	}

	/**
	 * This method specializes the PinningTrustManager from a
	 * {@code X509TrustManager} to a {@code X509ExtendedTrustManager}. Newer
	 * JREs, since a certain u-release of JRE/JDK 8 to be precisce, will perform
	 * further verification steps if the TrustManager <b>is not</b> of type
	 * X509ExtendedTrustManager. This verification steps include ensuring
	 * algorithmic constraints, which doesn't make much sense when Java Pinning
	 * is used.
	 */
	@Override
	protected X509TrustManager eventuallySpecialize(PinningTrustManager pinningTrustManager) {
		return new X509ExtendedTrustManagerWrapper(pinningTrustManager);
	}
}
