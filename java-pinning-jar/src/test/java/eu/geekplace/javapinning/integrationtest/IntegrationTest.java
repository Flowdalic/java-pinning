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
package eu.geekplace.javapinning.integrationtest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

import eu.geekplace.javapinning.JavaPinning;


public class IntegrationTest {

	public static void main(String[] args) throws UnknownHostException, IOException, KeyManagementException, NoSuchAlgorithmException {
		SSLContext sc = JavaPinning.forPin("CERTSHA256:83F9171E06A313118889F7D79302BD1B7A2042EE0CFD029ABF8DD06FFA6CD9D3");
		Socket socket = new Socket("geekplace.eu", 443);
		SSLSocket sslSocket = (SSLSocket) sc.getSocketFactory().createSocket(socket, "geekplace.eu", 443, true);
		sslSocket.startHandshake();
		String name = sslSocket.getSession().getPeerPrincipal().getName();
		System.out.println(name);
		OutputStream os = sslSocket.getOutputStream();
		os.write("GET /".getBytes());
		os.flush();
	}
}
