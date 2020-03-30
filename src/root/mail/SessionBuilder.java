/*
 * Copyright 2006-2016 Edward Smith
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
package root.mail;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import com.sun.mail.smtp.SMTPAddressSucceededException;

import root.annotation.Builder;

/**
 * This class makes it easy to configure a {@link Session} object with all of the possible transport protocol configuration properties as documented
 * in the <a href="https://javamail.java.net/nonav/docs/api/com/sun/mail/smtp/package-summary.html">com.sun.mail.smtp</a> package.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Builder
public final class SessionBuilder implements root.lang.Builder {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final Properties props;
	private final TransportProtocol protocol;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public SessionBuilder(final TransportProtocol protocol) {
		this.props = new Properties();
		this.props.setProperty("mail.transport.protocol", protocol.getProtocolName());

		this.protocol = protocol;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final Session build() {
		return Session.getInstance(this.props);
	}

	/**
	 * Sets the mail.<i>protocol</i>.allow8bitmime property.
	 *
	 * @param allow8BitMime
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getAllow8BitMime()
	 */
	public final SessionBuilder setAllow8BitMime(final boolean allow8BitMime) {
		this.props.setProperty(this.protocol.getAllow8BitMime(), Boolean.toString(allow8BitMime));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.auth property.
	 *
	 * @param auth
	 *            authenticate the user using the AUTH command
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getAuth()
	 */
	public final SessionBuilder setAuth(final boolean auth) {
		this.props.setProperty(this.protocol.getAuth(), Boolean.toString(auth));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.auth.digest-md5.disable property.
	 *
	 * @param authDigestMD5Disable
	 *            prevents use of the {@code AUTH DIGEST-MD5} command if {@code true}
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getAuthDigestMD5Disable()
	 */
	public final SessionBuilder setAuthDigestMD5Disable(final boolean authDigestMD5Disable) {
		this.props.setProperty(this.protocol.getAuthDigestMD5Disable(), Boolean.toString(authDigestMD5Disable));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.auth.login.disable property.
	 *
	 * @param authLoginDisable
	 *            prevents use of the {@code AUTH LOGIN} command if {@code true}
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getAuthLoginDisable()
	 */
	public final SessionBuilder setAuthLoginDisable(final boolean authLoginDisable) {
		this.props.setProperty(this.protocol.getAuthLoginDisable(), Boolean.toString(authLoginDisable));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.auth.mechanisms property.
	 *
	 * @param authMechanisms
	 *            the authentication mechanisms to consider
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getAuthMechanisms()
	 */
	public final SessionBuilder setAuthMechanisms(final String authMechanisms) {
		this.props.setProperty(this.protocol.getAuthMechanisms(), authMechanisms);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.auth.ntlm.disable property.
	 *
	 * @param authNtlmDisable
	 *            prevents use of the {@code AUTH NTLM} command if {@code true}
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getAuthNtlmDisable()
	 */
	public final SessionBuilder setAuthNtlmDisable(final boolean authNtlmDisable) {
		this.props.setProperty(this.protocol.getAuthNtlmDisable(), Boolean.toString(authNtlmDisable));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.auth.ntlm.domain property.
	 *
	 * @param authNtlmDomain
	 *            the NTLM authentication domain
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getAuthNtlmDomain()
	 */
	public final SessionBuilder setAuthNtlmDomain(final String authNtlmDomain) {
		this.props.setProperty(this.protocol.getAuthNtlmDomain(), authNtlmDomain);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.auth.ntlm.flags property.
	 *
	 * @param authNtlmFlags
	 *            NTLM protocol-specific flags
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getAuthNtlmFlags()
	 */
	public final SessionBuilder setAuthNtlmFlags(final int authNtlmFlags) {
		this.props.setProperty(this.protocol.getAuthNtlmFlags(), Integer.toString(authNtlmFlags));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.auth.plain.disable property.
	 *
	 * @param authPlainDisable
	 *            prevents use of the {@code AUTH PLAIN} command if {@code true}
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getAuthPlainDisable()
	 */
	public final SessionBuilder setAuthPlainDisable(final boolean authPlainDisable) {
		this.props.setProperty(this.protocol.getAuthPlainDisable(), Boolean.toString(authPlainDisable));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.auth.xoauth2.disable property.
	 *
	 * @param authXOAuth2Disable
	 *            prevents use of the {@code AUTHENTICATE XOAUTH2} command if {@code true}
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getAuthXOAuth2Disable()
	 */
	public final SessionBuilder setAuthXOAuth2Disable(final boolean authXOAuth2Disable) {
		this.props.setProperty(this.protocol.getAuthXOAuth2Disable(), Boolean.toString(authXOAuth2Disable));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.connectiontimeout property.
	 *
	 * @param connectionTimeout
	 *            the socket connection timeout value in milliseconds
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getConnectionTimeout()
	 */
	public final SessionBuilder setConnectionTimeout(final int connectionTimeout) {
		this.props.setProperty(this.protocol.getConnectionTimeout(), Integer.toString(connectionTimeout));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.dsn.notify property.
	 *
	 * @param dsnNotify
	 *            the {@code NOTIFY} option to the {@code RCPT} command
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getDsnNotify()
	 */
	public final SessionBuilder setDsnNotify(final String dsnNotify) {
		this.props.setProperty(this.protocol.getDsnNotify(), dsnNotify);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.dsn.ret property.
	 *
	 * @param dsnRet
	 *            the {@code RET} option to the {@code MAIL} command
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getDsnRet()
	 */
	public final SessionBuilder setDsnRet(final String dsnRet) {
		this.props.setProperty(this.protocol.getDsnRet(), dsnRet);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.ehlo property.
	 *
	 * @param ehlo
	 *            attempt to sign on with the EHLO command
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getEhlo()
	 */
	public final SessionBuilder setEhlo(final boolean ehlo) {
		this.props.setProperty(this.protocol.getEhlo(), Boolean.toString(ehlo));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.from property.
	 *
	 * @param from
	 *            the envelope return address
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getFrom()
	 */
	public final SessionBuilder setFrom(final String from) {
		this.props.setProperty(this.protocol.getFrom(), from);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.host property.
	 *
	 * @param hostName
	 *            the server to connect to
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getHost()
	 */
	public final SessionBuilder setHost(final String hostName) {
		this.props.setProperty(this.protocol.getHost(), hostName);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.localaddress property.
	 *
	 * @param localAddress
	 *            the local address (host name) to bind to when creating the SMTP socket
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getLocalAddress()
	 */
	public final SessionBuilder setLocalAddress(final String localAddress) {
		this.props.setProperty(this.protocol.getLocalAddress(), localAddress);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.localhost property.
	 *
	 * @param localhostName
	 *            the local host name
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getLocalhost()
	 */
	public final SessionBuilder setLocalhost(final String localhostName) {
		this.props.setProperty(this.protocol.getLocalhost(), localhostName);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.localport property.
	 *
	 * @param localPort
	 *            the local port number to bind to when creating the SMTP socket
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getLocalPort()
	 */
	public final SessionBuilder setLocalPort(final int localPort) {
		this.props.setProperty(this.protocol.getLocalPort(), Integer.toString(localPort));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.mailextension property.
	 *
	 * @param mailExtension
	 *            extension string to append to the {@code MAIL} command
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getMailExtension()
	 */
	public final SessionBuilder setMailExtension(final String mailExtension) {
		this.props.setProperty(this.protocol.getMailExtension(), mailExtension);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.noop.strict property.
	 *
	 * @param noopStrict
	 *            see {@link TransportProtocol#getNoopStrict()}
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getNoopStrict()
	 */
	public final SessionBuilder setNoopStrict(final boolean noopStrict) {
		this.props.setProperty(this.protocol.getNoopStrict(), Boolean.toString(noopStrict));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.port property.
	 *
	 * @param port
	 *            the server port
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getPort()
	 */
	public final SessionBuilder setPort(final int port) {
		this.props.setProperty(this.protocol.getPort(), Integer.toString(port));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.quitwait property.
	 *
	 * @param quitWait
	 *            if set to {@code false}, the {@code QUIT} command is sent and the connection is immediately closed
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getQuitWait()
	 */
	public final SessionBuilder setQuitWait(final boolean quitWait) {
		this.props.setProperty(this.protocol.getQuitWait(), Boolean.toString(quitWait));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.reportsuccess property.
	 *
	 * @param reportSuccess
	 *            if set to {@code true}, causes the transport to include an {@link SMTPAddressSucceededException} for each address that is successful
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getReportSuccess()
	 */
	public final SessionBuilder setReportSuccess(final boolean reportSuccess) {
		this.props.setProperty(this.protocol.getReportSuccess(), Boolean.toString(reportSuccess));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.sasl.authorizationid property.
	 *
	 * @param saslAuthorizationId
	 *            the authorization ID to use in the SASL authentication
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSaslAuthorizationId()
	 */
	public final SessionBuilder setSaslAuthorizationId(final String saslAuthorizationId) {
		this.props.setProperty(this.protocol.getSaslAuthorizationId(), saslAuthorizationId);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.sasl.enable property.
	 *
	 * @param saslEnable
	 *            if set to {@code true}, attempt to use the {@code javax.security.sasl} package to choose an authentication mechanism for login
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSaslEnable()
	 */
	public final SessionBuilder setSaslEnable(final boolean saslEnable) {
		this.props.setProperty(this.protocol.getSaslEnable(), Boolean.toString(saslEnable));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.sasl.mechanisms property.
	 *
	 * @param saslMechanisms
	 *            a space or comma separated list of SASL mechanism names to try to use
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSaslMechanisms()
	 */
	public final SessionBuilder setSaslMechanisms(final String saslMechanisms) {
		this.props.setProperty(this.protocol.getSaslMechanisms(), saslMechanisms);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.sasl.realm property.
	 *
	 * @param saslRealm
	 *            the realm to use with {@code DIGEST-MD5} authentication
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSaslRealm()
	 */
	public final SessionBuilder setSaslRealm(final String saslRealm) {
		this.props.setProperty(this.protocol.getSaslRealm(), saslRealm);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.sasl.usecanonicalhostname property.
	 *
	 * @param saslUseCanonicalHostname
	 *            see {@link TransportProtocol#getSaslUseCanonicalHostname()}
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSaslUseCanonicalHostname()
	 */
	public final SessionBuilder setSaslUseCanonicalHostname(final boolean saslUseCanonicalHostname) {
		this.props.setProperty(this.protocol.getSaslUseCanonicalHostname(), Boolean.toString(saslUseCanonicalHostname));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.sendpartial property.
	 *
	 * @param sendPartial
	 *            if set to {@code true}, and a message has some valid and some invalid addresses, send the message anyway
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSendPartial()
	 */
	public final SessionBuilder setSendPartial(final boolean sendPartial) {
		this.props.setProperty(this.protocol.getSendPartial(), Boolean.toString(sendPartial));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.socketFactory property.
	 *
	 * @param socketFactory
	 *            this class will be used to create SMTP sockets
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSocketFactory()
	 */
	public final SessionBuilder setSocketFactory(final SocketFactory socketFactory) {
		this.props.put(this.protocol.getSocketFactory(), socketFactory);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.socketFactory.class property.
	 *
	 * @param socketFactoryClass
	 *            this class will be used to create SMTP sockets
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSocketFactoryClass()
	 */
	public final SessionBuilder setSocketFactoryClass(final String socketFactoryClass) {
		this.props.setProperty(this.protocol.getSocketFactoryClass(), socketFactoryClass);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.socketFactory.fallback property.
	 *
	 * @param socketFactoryFallback
	 *            see {@link TransportProtocol#getSocketFactoryFallback()}
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSocketFactoryFallback()
	 */
	public final SessionBuilder setSocketFactoryFallback(final boolean socketFactoryFallback) {
		this.props.setProperty(this.protocol.getSocketFactoryFallback(), Boolean.toString(socketFactoryFallback));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.socketFactory.port property.
	 *
	 * @param socketFactoryPort
	 *            specifies the port to connect to when using the specified socket factory
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSocketFactoryPort()
	 */
	public final SessionBuilder setSocketFactoryPort(final int socketFactoryPort) {
		this.props.setProperty(this.protocol.getSocketFactoryPort(), Integer.toString(socketFactoryPort));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.socks.host property.
	 *
	 * @param socksHost
	 *            specifies the host name of a SOCKS5 proxy server
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSocksHost()
	 */
	public final SessionBuilder setSocksHost(final String socksHost) {
		this.props.setProperty(this.protocol.getSocksHost(), socksHost);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.socks.port property.
	 *
	 * @param socksPort
	 *            specifies the port number for the SOCKS5 proxy server
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSocksPort()
	 */
	public final SessionBuilder setSocksPort(final int socksPort) {
		this.props.setProperty(this.protocol.getSocksPort(), Integer.toString(socksPort));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.ssl.checkserveridentity property.
	 *
	 * @param sslCheckServerIdentity
	 *            check the server identity as specified by RFC 2595
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSslCheckServerIdentity()
	 */
	public final SessionBuilder setSslCheckServerIdentity(final boolean sslCheckServerIdentity) {
		this.props.setProperty(this.protocol.getSslCheckServerIdentity(), Boolean.toString(sslCheckServerIdentity));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.ssl.ciphersuites property.
	 *
	 * @param sslCipherSuites
	 *            specifies the SSL cipher suites that will be enabled for SSL connections
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSslCipherSuites()
	 */
	public final SessionBuilder setSslCipherSuites(final String sslCipherSuites) {
		this.props.setProperty(this.protocol.getSslCipherSuites(), sslCipherSuites);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.ssl.enable property.
	 *
	 * @param sslEnable
	 *            use SSL to connect and use the SSL port by default
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSslEnable()
	 */
	public final SessionBuilder setSslEnable(final boolean sslEnable) {
		this.props.setProperty(this.protocol.getSslEnable(), Boolean.toString(sslEnable));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.ssl.protocols property.
	 *
	 * @param sslProtocols
	 *            specifies the SSL protocols that will be enabled for SSL connections
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSslProtocols()
	 */
	public final SessionBuilder setSslProtocols(final String sslProtocols) {
		this.props.setProperty(this.protocol.getSslProtocols(), sslProtocols);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.ssl.socketFactory property.
	 *
	 * @param sslSocketFactory
	 *            this class will be used to create SMTP SSL sockets
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSslSocketFactory()
	 */
	public final SessionBuilder setSslSocketFactory(final SSLSocketFactory sslSocketFactory) {
		this.props.put(this.protocol.getSslSocketFactory(), sslSocketFactory);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.ssl.socketFactory.class property.
	 *
	 * @param sslSocketFactoryClass
	 *            this class will be used to create SMTP SSL sockets
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSslSocketFactoryClass()
	 */
	public final SessionBuilder setSslSocketFactoryClass(final String sslSocketFactoryClass) {
		this.props.setProperty(this.protocol.getSslSocketFactoryClass(), sslSocketFactoryClass);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.ssl.socketFactory.port property.
	 *
	 * @param sslSocketFactoryPort
	 *            specifies the port to connect to when using the specified socket factory
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSslSocketFactoryPort()
	 */
	public final SessionBuilder setSslSocketFactoryPort(final int sslSocketFactoryPort) {
		this.props.setProperty(this.protocol.getSslSocketFactoryPort(), Integer.toString(sslSocketFactoryPort));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.ssl.trust property.
	 *
	 * @param sslTrust
	 *            see {@link TransportProtocol#getSslTrust()}
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSslTrust()
	 */
	public final SessionBuilder setSslTrust(final String sslTrust) {
		this.props.setProperty(this.protocol.getSslTrust(), sslTrust);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.starttls.enable property.
	 *
	 * @param startTlsEnable
	 *            enables the use of the {@code STARTTLS} command (if supported by the server)
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getStartTlsEnable()
	 */
	public final SessionBuilder setStartTlsEnable(final boolean startTlsEnable) {
		this.props.setProperty(this.protocol.getStartTlsEnable(), Boolean.toString(startTlsEnable));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.starttls.required property.
	 *
	 * @param startTlsRequired
	 *            if {@code true}, requires the use of the {@code STARTTLS} command
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getStartTlsRequired()
	 */
	public final SessionBuilder setStartTlsRequired(final boolean startTlsRequired) {
		this.props.setProperty(this.protocol.getStartTlsRequired(), Boolean.toString(startTlsRequired));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.submitter property.
	 *
	 * @param submitter
	 *            the submitter to use in the {@code AUTH} tag in the {@code MAIL FROM} command
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getSubmitter()
	 */
	public final SessionBuilder setSubmitter(final String submitter) {
		this.props.setProperty(this.protocol.getSubmitter(), submitter);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.timeout property.
	 *
	 * @param timeout
	 *            the socket read timeout value in milliseconds
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getTimeout()
	 */
	public final SessionBuilder setTimeout(final int timeout) {
		this.props.setProperty(this.protocol.getTimeout(), Integer.toString(timeout));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.user property.
	 *
	 * @param userName
	 *            the default user name
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getUser()
	 */
	public final SessionBuilder setUser(final String userName) {
		this.props.setProperty(this.protocol.getUser(), userName);

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.userset property.
	 *
	 * @param useRset
	 *            if set to {@code true}, use the {@code RSET} command instead of the {@code NOOP} command in the {@link Transport#isConnected()}
	 *            method
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getUseRset()
	 */
	public final SessionBuilder setUseRset(final boolean useRset) {
		this.props.setProperty(this.protocol.getUseRset(), Boolean.toString(useRset));

		return this;
	}

	/**
	 * Sets the mail.<i>protocol</i>.writetimeout property.
	 *
	 * @param writeTimeout
	 *            the socket write timeout value in milliseconds
	 * @return this {@link SessionBuilder}
	 * @see TransportProtocol#getWriteTimeout()
	 */
	public final SessionBuilder setWriteTimeout(final int writeTimeout) {
		this.props.setProperty(this.protocol.getWriteTimeout(), Integer.toString(writeTimeout));

		return this;
	}

} // End SessionBuilder
