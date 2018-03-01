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

	@Test
	public  void main() throws NoSuchAlgorithmException, KeyManagementException, IOException {
		SSLContext sc = JavaPinning.forPin("SHA256:a4bd7ea9bf474cc459266b82fdb07f648f5ddf4d8162baea895b91c96f831ab5");

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
