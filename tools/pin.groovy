import javax.net.ssl.HttpsURLConnection
import java.security.MessageDigest
import java.security.cert.Certificate
import java.security.cert.X509Certificate

def domain = args.size() ? args[0] : null
if (!domain) {
    println 'Usage: pin.groovy <domain:[port]>'
    System.exit(0)
}

def con = "https://$domain".toURL().openConnection() as HttpsURLConnection
con.connect()

con.getServerCertificates().eachWithIndex { Certificate cert, int i ->

    println "Certificate Nr. ${i + 1}"
    if (!cert instanceof X509Certificate) return

    def xc = cert as X509Certificate

    println "Subject: ${xc.subjectDN.name}"
    println "Valid from ${xc.notBefore} to ${xc.notAfter}"

    if (xc.subjectAlternativeNames?.size())
        println "Alternate DNS: ${xc.subjectAlternativeNames*.last().join(', ')}"

    def sha = new BigInteger(1, MessageDigest.getInstance('SHA-256').digest(cert.publicKey.encoded))
            .toString(16).padLeft(64, '0').toLowerCase()

    println "Pin string: SHA256:$sha\n"
}
con.disconnect()