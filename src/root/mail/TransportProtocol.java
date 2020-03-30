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

import java.net.InetAddress;
import java.util.Properties;

import javax.mail.SendFailedException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

import com.sun.mail.smtp.SMTPAddressSucceededException;
import com.sun.mail.smtp.SMTPMessage;
import com.sun.mail.smtp.SMTPTransport;

import root.lang.Prefix;
import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public enum TransportProtocol {

	// <><><><><><><><><><><><><><>< Enum Values ><><><><><><><><><><><><><><>

	SMTP("smtp"),
	SMTPS("smtps");

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final String protocolName;
	private final Prefix propertyPrefix;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	private TransportProtocol(final String protocolName) {
		this.protocolName = protocolName;
		this.propertyPrefix = new Prefix(new StringExtractor("mail.").append(protocolName).append('.'));
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * If set to {@code true}, and the server supports the {@code 8BITMIME} extension, text parts of messages that use the "quoted-printable" or
	 * "base64" encodings are converted to use "8bit" encoding if they follow the <a href="https://en.wikipedia.org/wiki/Base64#MIME">RFC 2045</a>
	 * rules for 8bit text.
	 *
	 * @return the mail.<i>protocol</i>.allow8bitmime property name
	 */
	public final String getAllow8BitMime() {
		return this.propertyPrefix.toString("allow8bitmime");
	}

	/**
	 * If {@code true}, attempt to authenticate the user using the {@code AUTH} command. Defaults to {@code false}.
	 *
	 * @return the mail.<i>protocol</i>.auth property name
	 */
	public final String getAuth() {
		return this.propertyPrefix.toString("auth");
	}

	/**
	 * If {@code true}, prevents use of the {@code AUTH DIGEST-MD5} command. Default is {@code false}.
	 *
	 * @return the mail.<i>protocol</i>.auth.digest-md5.disable property name
	 */
	public final String getAuthDigestMD5Disable() {
		return this.propertyPrefix.toString("auth.digest-md5.disable");
	}

	/**
	 * If {@code true}, prevents use of the {@code AUTH LOGIN} command. Default is {@code false}.
	 *
	 * @return the mail.<i>protocol</i>.auth.login.disable property name
	 */
	public final String getAuthLoginDisable() {
		return this.propertyPrefix.toString("auth.login.disable");
	}

	/**
	 * If set, lists the authentication mechanisms to consider, and the order in which to consider them. Only mechanisms supported by the server and
	 * supported by the current implementation will be used. The default is {@code "LOGIN PLAIN DIGEST-MD5 NTLM"}, which includes all the
	 * authentication mechanisms supported by the current implementation except {@code XOAUTH2}.
	 *
	 * @return the mail.<i>protocol</i>.auth.mechanisms property name
	 */
	public final String getAuthMechanisms() {
		return this.propertyPrefix.toString("auth.mechanisms");
	}

	/**
	 * If {@code true}, prevents use of the {@code AUTH NTLM} command. Default is {@code false}.
	 *
	 * @return the mail.<i>protocol</i>.auth.ntlm.disable property name
	 */
	public final String getAuthNtlmDisable() {
		return this.propertyPrefix.toString("auth.ntlm.disable");
	}

	/**
	 * The NTLM authentication domain.
	 *
	 * @return the mail.<i>protocol</i>.auth.ntlm.domain property name
	 */
	public final String getAuthNtlmDomain() {
		return this.propertyPrefix.toString("auth.ntlm.domain");
	}

	/**
	 * NTLM protocol-specific flags. See <a href="http://curl.haxx.se/rfc/ntlm.html#theNtlmFlags">The NTLM Flags</a> for details.
	 *
	 * @return the mail.<i>protocol</i>.auth.ntlm.flags property name
	 */
	public final String getAuthNtlmFlags() {
		return this.propertyPrefix.toString("auth.ntlm.flags");
	}

	/**
	 * If {@code true}, prevents use of the {@code AUTH PLAIN} command. Default is {@code false}.
	 *
	 * @return the mail.<i>protocol</i>.auth.plain.disable property name
	 */
	public final String getAuthPlainDisable() {
		return this.propertyPrefix.toString("auth.plain.disable");
	}

	/**
	 * If {@code true}, prevents use of the {@code AUTHENTICATE XOAUTH2} command. Because the OAuth 2.0 protocol requires a special access token
	 * instead of a password, this mechanism is disabled by default. Enable it by explicitly setting this property to "false" or by setting the
	 * "mail.smtp.auth.mechanisms" property to "XOAUTH2".
	 *
	 * @return the mail.<i>protocol</i>.auth.xoauth2.disable property name
	 */
	public final String getAuthXOAuth2Disable() {
		return this.propertyPrefix.toString("auth.xoauth2.disable");
	}

	/**
	 * Socket connection timeout value in milliseconds. This timeout is implemented by {@link java.net.Socket}. Default is infinite timeout.
	 *
	 * @return the mail.<i>protocol</i>.connectiontimeout property name
	 */
	public final String getConnectionTimeout() {
		return this.propertyPrefix.toString("connectiontimeout");
	}

	/**
	 * The {@code NOTIFY} option to the {@code RCPT} command. Either {@code NEVER}, or some combination of {@code SUCCESS}, {@code FAILURE}, and
	 * {@code DELAY} (separated by commas).
	 *
	 * @return the mail.<i>protocol</i>.dsn.notify property name
	 */
	public final String getDsnNotify() {
		return this.propertyPrefix.toString("dsn.notify");
	}

	/**
	 * The {@code RET} option to the {@code MAIL} command. Either {@code FULL} or {@code HDRS}.
	 *
	 * @return the mail.<i>protocol</i>.dsn.ret property name
	 */
	public final String getDsnRet() {
		return this.propertyPrefix.toString("dsn.ret");
	}

	/**
	 * If {@code false}, do not attempt to sign on with the {@code EHLO} command. Defaults to {@code true}. Normally failure of the {@code EHLO}
	 * command will fallback to the {@code HELO} command; this property exists only for servers that don't fail {@code EHLO} properly or don't
	 * implement {@code EHLO} properly.
	 *
	 * @return the mail.<i>protocol</i>.ehlo property name
	 */
	public final String getEhlo() {
		return this.propertyPrefix.toString("ehlo");
	}

	/**
	 * Email address to use for {@code SMTP MAIL} command. This sets the envelope return address. Defaults to {@code msg.getFrom()} or
	 * {@link InternetAddress#getLocalAddress(javax.mail.Session)}. NOTE: {@code mail.smtp.user} was previously used for this.
	 *
	 * @return the mail.<i>protocol</i>.from property name
	 */
	public final String getFrom() {
		return this.propertyPrefix.toString("from");
	}

	/**
	 * The SMTP server to connect to.
	 *
	 * @return the mail.<i>protocol</i>.host property name
	 */
	public final String getHost() {
		return this.propertyPrefix.toString("host");
	}

	/**
	 * Local address (host name) to bind to when creating the SMTP socket. Defaults to the address picked by the {@link java.net.Socket} class. Should
	 * not normally need to be set, but useful with multi-homed hosts where it's important to pick a particular local address to bind to.
	 *
	 * @return the mail.<i>protocol</i>.localaddress property name
	 */
	public final String getLocalAddress() {
		return this.propertyPrefix.toString("localaddress");
	}

	/**
	 * Local host name used in the {@code SMTP HELO} or {@code EHLO} command. Defaults to {@code InetAddress.getLocalHost().getHostName()}. Should not
	 * normally need to be set if your JDK and your name service are configured properly.
	 *
	 * @return the mail.<i>protocol</i>.localhost property name
	 */
	public final String getLocalhost() {
		return this.propertyPrefix.toString("localhost");
	}

	/**
	 * Local port number to bind to when creating the SMTP socket. Defaults to the port number picked by the {@link java.net.Socket} class.
	 *
	 * @return the mail.<i>protocol</i>.localport property name
	 */
	public final String getLocalPort() {
		return this.propertyPrefix.toString("localport");
	}

	/**
	 * Extension string to append to the {@code MAIL} command. The extension string can be used to specify standard SMTP service extensions as well as
	 * vendor-specific extensions. Typically the application should use the {@link SMTPTransport#supportsExtension(String)} to verify that the server
	 * supports the desired service extension. See <a href="https://tools.ietf.org/html/rfc1869">RFC 1869</a> and other RFCs that define specific
	 * extensions.
	 *
	 * @return the mail.<i>protocol</i>.mailextension property name
	 */
	public final String getMailExtension() {
		return this.propertyPrefix.toString("mailextension");
	}

	/**
	 * If set to {@code true} (the default), insist on a 250 response code from the {@code NOOP} command to indicate success. The {@code NOOP} command
	 * is used by the {@link Transport#isConnected()} method to determine if the connection is still alive. Some older servers return the wrong
	 * response code on success, some servers don't implement the {@code NOOP} command at all and so always return a failure code. Set this property
	 * to {@code false} to handle servers that are broken in this way. Normally, when a server times out a connection, it will send a 421 response
	 * code, which the client will see as the response to the next command it issues. Some servers send the wrong failure response code when timing
	 * out a connection. Do not set this property to {@code false} when dealing with servers that are broken in this way.
	 *
	 * @return the mail.<i>protocol</i>.noop.strict property name
	 */
	public final String getNoopStrict() {
		return this.propertyPrefix.toString("noop.strict");
	}

	/**
	 * The SMTP server port to connect to, if the {@link Transport#connect()} method doesn't explicitly specify one. Defaults to 25.
	 *
	 * @return the mail.<i>protocol</i>.port property name
	 */
	public final String getPort() {
		return this.propertyPrefix.toString("port");
	}

	/**
	 * Returns the name of the transport protocol.
	 *
	 * @return the name of the transport protocol
	 */
	public final String getProtocolName() {
		return this.protocolName;
	}

	/**
	 * If set to {@code false}, the {@code QUIT} command is sent and the connection is immediately closed. If set to {@code true} (the default),
	 * causes the transport to wait for the response to the {@code QUIT} command.
	 *
	 * @return the mail.<i>protocol</i>.quitwait property name
	 */
	public final String getQuitWait() {
		return this.propertyPrefix.toString("quitwait");
	}

	/**
	 * If set to {@code true}, causes the transport to include an {@link SMTPAddressSucceededException} for each address that is successful. Note also
	 * that this will cause a {@link SendFailedException} to be thrown from
	 * {@link SMTPTransport#sendMessage(javax.mail.Message, javax.mail.Address[])} even if all addresses were correct and the message was sent
	 * successfully.
	 *
	 * @return the mail.<i>protocol</i>.reportsuccess property name
	 */
	public final String getReportSuccess() {
		return this.propertyPrefix.toString("reportsuccess");
	}

	/**
	 * The authorization ID to use in the SASL authentication. If not set, the authentication ID (user name) is used.
	 *
	 * @return the mail.<i>protocol</i>.sasl.authorizationid property name
	 */
	public final String getSaslAuthorizationId() {
		return this.propertyPrefix.toString("sasl.authorizationid");
	}

	/**
	 * If set to {@code true}, attempt to use the {@code javax.security.sasl} package to choose an authentication mechanism for login. Defaults to
	 * {@code false}.
	 *
	 * @return the mail.<i>protocol</i>.sasl.enable property name
	 */
	public final String getSaslEnable() {
		return this.propertyPrefix.toString("sasl.enable");
	}

	/**
	 * A space or comma separated list of SASL mechanism names to try to use.
	 *
	 * @return the mail.<i>protocol</i>.sasl.mechanisms property name
	 */
	public final String getSaslMechanisms() {
		return this.propertyPrefix.toString("sasl.mechanisms");
	}

	/**
	 * The realm to use with {@code DIGEST-MD5} authentication.
	 *
	 * @return the mail.<i>protocol</i>.sasl.realm property name
	 */
	public final String getSaslRealm() {
		return this.propertyPrefix.toString("sasl.realm");
	}

	/**
	 * If set to {@code true}, the canonical host name returned by {@link InetAddress#getCanonicalHostName()} is passed to the SASL mechanism, instead
	 * of the host name used to connect. Defaults to {@code false}.
	 *
	 * @return the mail.<i>protocol</i>.sasl.usecanonicalhostname property name
	 */
	public final String getSaslUseCanonicalHostname() {
		return this.propertyPrefix.toString("sasl.usecanonicalhostname");
	}

	/**
	 * If set to {@code true}, and a message has some valid and some invalid addresses, send the message anyway, reporting the partial failure with a
	 * {@link SendFailedException}. If set to {@code false} (the default), the message is not sent to any of the recipients if there is an invalid
	 * recipient address.
	 *
	 * @return the mail.<i>protocol</i>.sendpartial property name
	 */
	public final String getSendPartial() {
		return this.propertyPrefix.toString("sendpartial");
	}

	/**
	 * If set to a class that implements the {@link javax.net.SocketFactory} interface, this class will be used to create SMTP sockets. Note that this
	 * is an instance of a class, not a name, and must be set using the {@link Properties#put(Object, Object)} method, not the
	 * {@link Properties#setProperty(String, String)} method.
	 *
	 * @return the mail.<i>protocol</i>.socketFactory property name
	 */
	public final String getSocketFactory() {
		return this.propertyPrefix.toString("socketFactory");
	}

	/**
	 * If set, specifies the name of a class that implements the {@link javax.net.SocketFactory} interface. This class will be used to create SMTP
	 * sockets.
	 *
	 * @return the mail.<i>protocol</i>.socketFactory.class property name
	 */
	public final String getSocketFactoryClass() {
		return this.propertyPrefix.toString("socketFactory.class");
	}

	/**
	 * If set to {@code true}, failure to create a socket using the specified socket factory class will cause the socket to be created using the
	 * {@link java.net.Socket} class. Defaults to {@code true}.
	 *
	 * @return the mail.<i>protocol</i>.socketFactory.fallback property name
	 */
	public final String getSocketFactoryFallback() {
		return this.propertyPrefix.toString("socketFactory.fallback");
	}

	/**
	 * Specifies the port to connect to when using the specified socket factory. If not set, the default port will be used.
	 *
	 * @return the mail.<i>protocol</i>.socketFactory.port property name
	 */
	public final String getSocketFactoryPort() {
		return this.propertyPrefix.toString("socketFactory.port");
	}

	/**
	 * Specifies the host name of a SOCKS5 proxy server that will be used for connections to the mail server. (Note that this only works on JDK 1.5 or
	 * newer)
	 *
	 * @return the mail.<i>protocol</i>.socks.host property name
	 */
	public final String getSocksHost() {
		return this.propertyPrefix.toString("socks.host");
	}

	/**
	 * Specifies the port number for the SOCKS5 proxy server. This should only need to be used if the proxy server is not using the standard port
	 * number of 1080.
	 *
	 * @return the mail.<i>protocol</i>.socks.port property name
	 */
	public final String getSocksPort() {
		return this.propertyPrefix.toString("socks.port");
	}

	/**
	 * If set to {@code true}, check the server identity as specified by <a href="https://www.ietf.org/rfc/rfc2595.txt">RFC 2595</a>. These additional
	 * checks based on the content of the server's certificate are intended to prevent man-in-the-middle attacks. Defaults to {@code false}.
	 *
	 * @return the mail.<i>protocol</i>.ssl.checkserveridentity property name
	 */
	public final String getSslCheckServerIdentity() {
		return this.propertyPrefix.toString("ssl.checkserveridentity");
	}

	/**
	 * Specifies the SSL cipher suites that will be enabled for SSL connections. The property value is a whitespace separated list of tokens
	 * acceptable to the {@link javax.net.ssl.SSLSocket#setEnabledCipherSuites(String[])} method.
	 *
	 * @return the mail.<i>protocol</i>.ssl.ciphersuites property name
	 */
	public final String getSslCipherSuites() {
		return this.propertyPrefix.toString("ssl.ciphersuites");
	}

	/**
	 * If set to {@code true}, use SSL to connect and use the SSL port by default. Defaults to {@code false} for the SMTP protocol and {@code true}
	 * for the SMTPS protocol.
	 *
	 * @return the mail.<i>protocol</i>.ssl.enable property name
	 */
	public final String getSslEnable() {
		return this.propertyPrefix.toString("ssl.enable");
	}

	/**
	 * Specifies the SSL protocols that will be enabled for SSL connections. The property value is a whitespace separated list of tokens acceptable to
	 * the {@link javax.net.ssl.SSLSocket#setEnabledProtocols(String[])} method.
	 *
	 * @return the mail.<i>protocol</i>.ssl.protocols property name
	 */
	public final String getSslProtocols() {
		return this.propertyPrefix.toString("ssl.protocols");
	}

	/**
	 * If set to a class that extends the {@link javax.net.ssl.SSLSocketFactory} class, this class will be used to create SMTP SSL sockets. Note that
	 * this is an instance of a class, not a name, and must be set using the {@link Properties#put(Object, Object)} method, not the
	 * {@link Properties#setProperty(String, String)} method.
	 *
	 * @return the mail.<i>protocol</i>.ssl.socketFactory property name
	 */
	public final String getSslSocketFactory() {
		return this.propertyPrefix.toString("ssl.socketFactory");
	}

	/**
	 * If set, specifies the name of a class that extends the {@link javax.net.ssl.SSLSocketFactory} class. This class will be used to create SMTP SSL
	 * sockets.
	 *
	 * @return the mail.<i>protocol</i>.ssl.socketFactory.class property name
	 */
	public final String getSslSocketFactoryClass() {
		return this.propertyPrefix.toString("ssl.socketFactory.class");
	}

	/**
	 * Specifies the port to connect to when using the specified socket factory. If not set, the default port will be used.
	 *
	 * @return the mail.<i>protocol</i>.ssl.socketFactory.port property name
	 */
	public final String getSslSocketFactoryPort() {
		return this.propertyPrefix.toString("ssl.socketFactory.port");
	}

	/**
	 * If set, and a socket factory hasn't been specified, enables use of a {@code MailSSLSocketFactory}. If set to "*", all hosts are trusted. If set
	 * to a whitespace separated list of hosts, those hosts are trusted. Otherwise, trust depends on the certificate the server presents.
	 *
	 * @return the mail.<i>protocol</i>.ssl.trust property name
	 */
	public final String getSslTrust() {
		return this.propertyPrefix.toString("ssl.trust");
	}

	/**
	 * If {@code true}, enables the use of the {@code STARTTLS} command (if supported by the server) to switch the connection to a TLS-protected
	 * connection before issuing any login commands. Note that an appropriate trust store must be configured so that the client will trust the
	 * server's certificate. Defaults to {@code false}.
	 *
	 * @return the mail.<i>protocol</i>.starttls.enable property name
	 */
	public final String getStartTlsEnable() {
		return this.propertyPrefix.toString("starttls.enable");
	}

	/**
	 * If {@code true}, requires the use of the {@code STARTTLS} command. If the server doesn't support the {@code STARTTLS} command, or the command
	 * fails, the connect method will fail. Defaults to {@code false}.
	 *
	 * @return the mail.<i>protocol</i>.starttls.required property name
	 */
	public final String getStartTlsRequired() {
		return this.propertyPrefix.toString("starttls.required");
	}

	/**
	 * The submitter to use in the {@code AUTH} tag in the {@code MAIL FROM} command. Typically used by a mail relay to pass along information about
	 * the original submitter of the message. See also {@link SMTPMessage#setSubmitter(String)}. Mail clients typically do not use this.
	 *
	 * @return the mail.<i>protocol</i>.submitter property name
	 */
	public final String getSubmitter() {
		return this.propertyPrefix.toString("submitter");
	}

	/**
	 * Socket read timeout value in milliseconds. This timeout is implemented by {@link java.net.Socket}. Default is infinite timeout.
	 *
	 * @return the mail.<i>protocol</i>.timeout property name
	 */
	public final String getTimeout() {
		return this.propertyPrefix.toString("timeout");
	}

	/**
	 * Default user name for SMTP.
	 *
	 * @return the mail.<i>protocol</i>.user property name
	 */
	public final String getUser() {
		return this.propertyPrefix.toString("user");
	}

	/**
	 * If set to {@code true}, use the {@code RSET} command instead of the {@code NOOP} command in the {@link Transport#isConnected()} method. In some
	 * cases sendmail will respond slowly after many {@code NOOP} commands; use of {@code RSET} avoids this sendmail issue. Defaults to {@code false}.
	 *
	 * @return the mail.<i>protocol</i>.userset property name
	 */
	public final String getUseRset() {
		return this.propertyPrefix.toString("userset");
	}

	/**
	 * Socket write timeout value in milliseconds. This timeout is implemented by using a {@link java.util.concurrent.ScheduledExecutorService} per
	 * connection that schedules a thread to close the socket if the timeout expires. Thus, the overhead of using this timeout is one thread per
	 * connection. Default is infinite timeout.
	 *
	 * @return the mail.<i>protocol</i>.writetimeout property name
	 */
	public final String getWriteTimeout() {
		return this.propertyPrefix.toString("writetimeout");
	}

} // End TransportProtocol
