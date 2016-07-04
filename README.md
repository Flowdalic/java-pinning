Java Pinning
============

Java Pinning provides support for pinning TLS (and SSL) certificates without any CA validation required.
The library runs on Java and Android and is available from Maven Central. It is licensed under the Apache License, Version 2.0.
If Java Pinning **pins the public key** of a certificate, or the hash of such, and produces a SSLContext which will only accept connections to a host in possession of the corresponding private key.
There is no additional validation using the system's trust store, i.e. Java Pinning disables PKI (CA validation).
This means that as soon as the private key is compromised, an attacker will be able to impersonate the host.

You should only use Java Pinning if you are in control of both ends of the TLS connection, so that you can replace the Pin in case the private key has been compromised.
To be absolutely clear: **If you need to change the server certificate, you need to change also the PIN in the client**.
Usually you want to add the new PIN first, before changing the server certificate.
If you don't add the new PIN, your clients wont be able to connect to your server!
Please inform yourself about the available alternatives to Java Pinning to determine if your use case is a match for Java Pinning.
See also the "Alternatives" section in this README.

Changelog
---------

### 1.0.1

The Pin format requirements have been relaxed. The Pin string may now contain

- Colons (':')
- Uppercase characters
- Spaces

How to use
----------

### XMPP Users

If your XMPP service does not provide you the server certificates SHA256 hash or the full certificate over a secure channel, then you could obtain the information for example via `xmpp.net`:

1. Go to [xmpp.net](https://xmpp.net)
2. Select "Test a server", enter your service name and make sure "c2s" is selected
3. Press "Check!"
4. Wait a bit until the information in the "Certificates" section appears
5. The first certificate ("#0") is the one of your service. In the "Details" section select SHA-256
6. Copy the SHA-256 hash, e.g. `83:F9:17:1E:06:A3:13:11:88:89:F7:D7:93:02:BD:1B:7A:20:42:EE:0C:FD:02:9A:BF:8D:D0:6F:FA:6C:D9:D3`
7. Java Pinning versions < 1.0.1 require the colons to be removed and the letters to be lowercase. This transformation could be done with the following bash script

```bash
$ pin=83:F9:17:1E:06:A3:13:11:88:89:F7:D7:93:02:BD:1B:7A:20:42:EE:0C:FD:02:9A:BF:8D:D0:6F:FA:6C:D9:D3
$ echo $pin | tr '[:upper:]' '[:lower:]' | tr -d :
```

8. Create the Java Pinning Pin String by prefixing `CERTSHA256` to the Pin. Thus our example SHA256 value becomes `CERTSHA256:83:F9:17:1E:06:A3:13:11:88:89:F7:D7:93:02:BD:1B:7A:20:42:EE:0C:FD:02:9A:BF:8D:D0:6F:FA:6C:D9:D3`

### HTTPS (and other services using TLS right from the start)

```bash
$ ./tools/pin.py example.org 443
Calculating PIN for certificate: CN=example.org
Certificate Fingerprint (SHA256): 4CBF7A80EC338E34DC4DA50136AFF0B6C1D91F1DD87BBF56FB0EBA3FC7451835
Pin Value: SHA256:e3b1812d945da1a2a2c5fa28029d2fe34c7c4142fb098f5cfedff1ff20e98781
Pin Value: PLAIN:30820222300d06092a864886f70d01010105000382020f003082020a0282020100b2617fdff43d5c469f067e879b6a0f5a721f6b17f2231942e3de417f16ab5ec4a50bd3cfbeb38c1dc942f333fd8056c28f07373178faa48700770be6c1ece59c3286e7c1992b23c7e1a328adddb58d06fde0ef87d95357d916fdce995921a49a54dd992ff8a87d0534d9b5988914a1ecf348815f125e4c156c7dcbe966717d3a7dd5f33999686aecc8ddbfc4f24ee7155cf09fe99229f47e7939b560e1bec8815f751fc7e864322d0bc56bfe0876cbfd642a554237f9ccca076f8b8a97d0cccb49a05894bed6bcd405e3b0325c4a5950ef19b4399ad504d3a6e5c1c77d9cb43c74016bebecbc62d90a6cf5c5388ad7e38f18cd19b26a222d8909e3d8a306c9ae81f3582ae775b8cd5766e001713209d807f033ab3e2041e030c85d08982bb2fa50205caad78984f682873dd4f6f8e2c044cfe5a8c804c3c7300ba75b692f5f4b8c0ebb10cf375424fe97e19962bf4fddf6a9c704ba50cd6870733f78f33a39b00098ce10ebafb54b2946e4739ad20d17c6e6c6985b07c55bde518b328bce9e9745b7c8c659aca5057d705f9ce4cfcfc8141e9ad3688516914462e278458a729455d79cb5f9e4f1d6bf11d51cda4b5a08799c7b9826692f00f3c07891fe8565d654c0a49e9be3d91285a11aa01fc608b4cd606dac7ad427a4641c03c64bc8c9fffe252b6293d606e7c2224e47da98c357f44a720c42ccb2cb7663bd2fe86bfc870203010001
```


```java
SSLContext sc = JavaPinning
  .forPin("SHA256:e3b1812d945da1a2a2c5fa28029d2fe34c7c4142fb098f5cfedff1ff20e98781");
```

Now you can setup this context.

Examples
--------

# [Smack](http://www.igniterealtime.org/projects/smack/) 4.0

```java
ConnectionConfiguration conf = …
conf.setCustonSSLContext(sc);
```

# [Smack](http://www.igniterealtime.org/projects/smack/) 4.1


```java
XMPPTCPConnectionConfiguration conf = XMPPTCPConnectionConfiguration.builder()
  .setUsernameAndPassword("user", "pass").setService("example.org")
  .setCustomSSLContext(sc).build();
```

How to add this as dependency
-----------------------------

### Gradle

```groovy
compile('eu.geekplace.java-pinning:java-pinning-jar:1.0.1')
```

### Maven

```xml
<dependency>
	<groupId>eu.geekplace.javapinning</groupId>
	<artifactId>java-pinning-jar</artifactId>
	<version>1.0.1</version>
</dependency>
```

Difference between Public Key Pins and Certificate Pins
-------------------------------------------------------

Both provide the same security guarantee.
If the private key is compromised, but fail to provide a security guarantee any more.
Using Public Key pinning provides (theoretically) the advantage that the certificate could be replaced with a new one using the same private/public key pair, thus requiring no changes to the used Pins.

Alternatives
============

Java (and Android)
------------------

### Pure Certificate Pinning

This technique creates a custom SSLContext that will only accept certificates found or signed by certificates in a key store file.

```java
File keyStoreFile = new File("./myKeyStore");
KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
ks.load(new FileInputStream(keyStoreFile), "keyStorePassword".toCharArray());
TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
tmf.init(ks);
SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());
```

You can read more about it at https://op-co.de/blog/posts/java_sslsocket_mitm/#index10h3

Android
-------

### [Memorizing Trust Manager](https://github.com/ge0rg/MemorizingTrustManager)

Memorizing Trust Manager (MTM) excels in user scenarios because of its GUI confirmation dialog.
MTM does pin the certificate and does also not perform additional validate by the system trust store.
It is only available on Android (at the moment).

### [Android Pinning](https://github.com/moxie0/AndroidPinning)

Android Pinning (AP) does additional validate the pinned certificate by using the system's trust store.
It provides probably the best level of security, as it additionally strengthens PKI with pinning.
As the name suggests, Android Pinning is only available for Android.
The fact that the last commit date is June 2013 makes it questionable if the project is still actively maintained.


Build Instructions
==================

Build with

```bash
gradle build
```

Run tests with

```bash
gradle check
```
