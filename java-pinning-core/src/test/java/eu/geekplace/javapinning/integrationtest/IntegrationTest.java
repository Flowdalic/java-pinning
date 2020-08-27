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
package eu.geekplace.javapinning.integrationtest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

import eu.geekplace.javapinning.JavaPinning;

import org.junit.Test;

public class IntegrationTest {

	private static final String PIN_GITHUB_DIGICERT = "SHA256:451335746aa70c5b022570531e4cb5eaf8b5a1b3a50a01459ffc8e848ff2fa1a";
	private static final String PIN_GITHUB_LEAF = "SHA256:a39a1ae45e0b6d911f799d245c3826694d8adec20f62d6d0a53dc440b259aae3";

	@Test
	public void testGithubDigiCert()  throws NoSuchAlgorithmException, KeyManagementException, IOException {
		connect(JavaPinning.forPin(PIN_GITHUB_DIGICERT));
	}

	@Test
	public void testGithubLeaf()  throws NoSuchAlgorithmException, KeyManagementException, IOException {
		connect(JavaPinning.forPin(PIN_GITHUB_LEAF));
	}


	private void connect(SSLContext sc) throws NoSuchAlgorithmException, KeyManagementException, IOException {
		Socket socket = new Socket("github.com", 443);
		SSLSocket sslSocket = (SSLSocket) sc.getSocketFactory().createSocket(socket, "github.com", 443, true);
		sslSocket.startHandshake();
		String name = sslSocket.getSession().getPeerPrincipal().getName();
		// CHECKSTYLE:OFF
		System.out.println(name);
		// CHECKSTYLE:ON
		OutputStream os = sslSocket.getOutputStream();
		os.write("GET /".getBytes());
		os.flush();
	}
}
