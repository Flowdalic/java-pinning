#!/usr/bin/env python

from M2Crypto import X509
import binascii
import hashlib
import ssl
import sys

def main(argv):
    if len(argv) != 1 and len(argv) != 2:
        print "Usage: pin.py [<certificate_path> | <host> <port>]"
        return

    if (len(argv) == 1):
        cert        = X509.load_cert(argv[0])
    else:
        peerCert = ssl.get_server_certificate((argv[0], int(argv[1])))
        cert = X509.load_cert_string(peerCert)
    pubkey = cert.get_pubkey().as_der()

    digest = hashlib.sha256()
    digest.update(pubkey)
    sha256 = digest.digest()

    print "Calculating PIN for certificate: " + cert.get_subject().as_text()
    print "\n"
    print "Public Key Pins:"
    print "----------------"
    print "SHA256:" + binascii.hexlify(sha256)
    print "PLAIN:" + binascii.hexlify(pubkey)
    print "\n"
    print "Certificate Pins:"
    print "-----------------"
    print "CERTSHA256:" + cert.get_fingerprint('sha256')
    print "CERTPLAIN:" + binascii.hexlify(cert.as_der())

if __name__ == '__main__':
    main(sys.argv[1:])
