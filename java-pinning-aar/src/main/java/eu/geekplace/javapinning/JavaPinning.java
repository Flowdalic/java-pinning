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

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import eu.geekplace.javapinning.pin.Pin;

public class JavaPinning {

	public static final String TLS = "TLS";

	public static TrustManager trustManagerForPin(String pinString) {
		Pin pin = Pin.fromString(pinString);
		List<Pin> pins = new ArrayList<Pin>(1);
		pins.add(pin);
		return trustManagerforPins(pins);
	}

	public static TrustManager trustManagerforPins(Collection<Pin> pins) {
		return new PinningTrustManager(pins);
	}

	public static SSLContext forPin(String pinString) throws KeyManagementException,
			NoSuchAlgorithmException {
		TrustManager trustManager = trustManagerForPin(pinString);
		return fromTrustManager(trustManager);
	}

	public static SSLContext forPins(Collection<Pin> pins) throws KeyManagementException,
			NoSuchAlgorithmException {
		TrustManager trustManager = new PinningTrustManager(pins);
		return fromTrustManager(trustManager);
	}

	private static SSLContext fromTrustManager(TrustManager trustManager)
			throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustManagers = new TrustManager[] { trustManager };
		SSLContext sslContext = SSLContext.getInstance(TLS);
		sslContext.init(null, trustManagers, new SecureRandom());
		return sslContext;
	}
}
