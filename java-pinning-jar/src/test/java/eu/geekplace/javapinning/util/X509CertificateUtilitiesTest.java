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
package eu.geekplace.javapinning.util;

import static org.junit.Assert.assertNotNull;

import eu.geekplace.javapinning.TestUtilities;

import org.junit.Test;

public class X509CertificateUtilitiesTest {

    @Test
    public void decodeX509Certificate_validEncodedCertificate_returnsX509Certificate() {
        assertNotNull(X509CertificateUtilities.decodeX509Certificate(TestUtilities.PLAIN_CERTIFICATE_1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decodeX509Certificate_invalidEncodedCertificate_throwsIllegalArgumentException() {
        assertNotNull(X509CertificateUtilities.decodeX509Certificate("03a391615ce416307380e27601a0767e83c3264dba6b31074b1ac1480643d4d2"));
    }

    @Test
    public void decodeX509PublicKey_validEncodedPublicKey_returnsX509Certificate() {
        assertNotNull(X509CertificateUtilities.decodeX509PublicKey(TestUtilities.PLAIN_PUBLIC_KEY_1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decodeX509PublicKey_invalidEncodedPublicKey_throwsIllegalArgumentException() {
        assertNotNull(X509CertificateUtilities.decodeX509PublicKey("03a391615ce416307380e27601a0767e83c3264dba6b31074b1ac1480643d4d2"));
    }
}
