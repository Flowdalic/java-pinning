package eu.geekplace.javapinning.java7;

import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;

import eu.geekplace.javapinning.PinningTrustManager;

/**
 * An wrapper for {@link PinningTrustManager} to become a {@link X509ExtendedTrustManager}.
 *
 * Be aware that that the <code>checkClientTrusted()</code> method will throw an Exception.
 *
 */
public class X509ExtendedTrustManagerWrapper extends X509ExtendedTrustManager {

	private final PinningTrustManager pinningTrustManager;

	X509ExtendedTrustManagerWrapper(PinningTrustManager pinningTrustManager) {
		this.pinningTrustManager = pinningTrustManager;
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		pinningTrustManager.checkClientTrusted(chain, authType);
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		pinningTrustManager.checkServerTrusted(chain, authType);
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return pinningTrustManager.getAcceptedIssuers();
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType,
			Socket socket) throws CertificateException {
		checkClientTrusted(chain, authType);
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType,
			SSLEngine sslEngine) throws CertificateException {
		checkClientTrusted(chain, authType);
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType,
			Socket socket) throws CertificateException {
		checkServerTrusted(chain, authType);
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType,
			SSLEngine sslEngine) throws CertificateException {
		checkServerTrusted(chain, authType);
	}

}
