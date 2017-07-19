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
package eu.geekplace.javapinning;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import eu.geekplace.javapinning.pin.Pin;

public class JavaPinning {

	public static final String TLS = "TLS";

	public static final JavaPinning INSTANCE = new JavaPinning();

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
	 * Do not use.
	 *
	 * @deprecated Please use the correctly named: {@link #trustManagerForPins(Collection)}
	 */
	@Deprecated
	public static X509TrustManager trustManagerforPins(Collection<Pin> pins) {
		return trustManagerforPins(pins);
	}

	public static SSLContext forPins(String... pinStrings) throws KeyManagementException,
			NoSuchAlgorithmException {
		return INSTANCE.ctxForPins(pinStrings);
	}

	public static SSLContext forPin(String pinString) throws KeyManagementException,
			NoSuchAlgorithmException {
		return INSTANCE.ctxForPin(pinString);
	}

	public static SSLContext forPins(Collection<Pin> pins) throws KeyManagementException,
			NoSuchAlgorithmException {
		return INSTANCE.ctxForPins(pins);
	}

	protected JavaPinning() {
	}

	protected final X509TrustManager tmForPin(String pinString) {
		return tmForPins(pinString);
	}

	protected final X509TrustManager tmForPins(String... pinStrings) {
		List<Pin> pins = new ArrayList<>(pinStrings.length);
		for (String pin : pinStrings) {
			pins.add(Pin.fromString(pin));
		}
		return tmForPins(pins);
	}

	protected final X509TrustManager tmForPins(Collection<Pin> pins) {
		PinningTrustManager pinningTrustManager = new PinningTrustManager(pins);
		X509TrustManager trustManager = eventuallySpecialize(pinningTrustManager);
		return trustManager;
	}

	protected final SSLContext ctxForPins(String... pinStrings) throws KeyManagementException,
			NoSuchAlgorithmException {
		TrustManager trustManager = tmForPins(pinStrings);
		return fromTrustManager(trustManager);
	}

	protected final SSLContext ctxForPin(String pinString) throws KeyManagementException,
			NoSuchAlgorithmException {
		return ctxForPins(pinString);
	}

	protected final SSLContext ctxForPins(Collection<Pin> pins) throws KeyManagementException,
			NoSuchAlgorithmException {
		TrustManager trustManager = tmForPins(pins);
		return fromTrustManager(trustManager);
	}

	private static final SSLContext fromTrustManager(TrustManager trustManager)
			throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustManagers = new TrustManager[] { trustManager };
		SSLContext sslContext = SSLContext.getInstance(TLS);
		sslContext.init(null, trustManagers, new SecureRandom());
		return sslContext;
	}

	protected X509TrustManager eventuallySpecialize(PinningTrustManager pinningTrustManager) {
		return pinningTrustManager;
	}
}
