/**
 *
 * Copyright 2015 Florian Schmaus
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

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Set;

/**
 * A dummy X509Certificate for unit testing purposes.
 * <p>
 * This certificate does only implement {@link #getEncoded()} and throws
 * {@link UnsupportedOperationException} when all other methods are invoked.
 * </p>
 */
public class DummyX509Certificate extends X509Certificate {

	@Override
	public boolean hasUnsupportedCriticalExtension() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getCriticalExtensionOIDs() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getNonCriticalExtensionOIDs() {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] getExtensionValue(String oid) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public BigInteger getSerialNumber() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Principal getIssuerDN() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Principal getSubjectDN() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Date getNotBefore() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Date getNotAfter() {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] getTBSCertificate() throws CertificateEncodingException {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] getSignature() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSigAlgName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSigAlgOID() {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] getSigAlgParams() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean[] getIssuerUniqueID() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean[] getSubjectUniqueID() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean[] getKeyUsage() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getBasicConstraints() {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] getEncoded() {
		return new byte[] { 1, 2, 3, 4, 5, 6, 7, 31, 58, 3, 49, 10, 30, 94, 120, };
	}

	@Override
	public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException,
			NoSuchProviderException, SignatureException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void verify(PublicKey key, String sigProvider) throws CertificateException, NoSuchAlgorithmException,
			InvalidKeyException, NoSuchProviderException, SignatureException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return getClass().getName();
	}

	@Override
	public PublicKey getPublicKey() {
		throw new UnsupportedOperationException();
	}

}
