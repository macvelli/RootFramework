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

import java.io.UnsupportedEncodingException;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import root.lang.StringExtractor;

/**
 * The <a href="http://www.oracle.com/technetwork/java/javamail/index.html">JavaMail API</a> is a pain in the rear to work with. So this class
 * provides a much easier to use API that wraps <tt>JavaMail</tt> functionality for programmatically creating and sending an email.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Email {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final MimeMessage message;

	private final StringExtractor text;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Email(final Session session, final String subject) throws MessagingException {
		this.message = new MimeMessage(session);
		this.message.setSubject(subject);
		this.text = new StringExtractor(8192);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final StringExtractor append(final String text) {
		return this.text.append(text);
	}

	public final void from(final String name, final String address) throws UnsupportedEncodingException, MessagingException {
		this.message.setFrom(new InternetAddress(address, name));
	}

	public final void send() throws MessagingException {
		this.message.setText(this.text.toString());
		Transport.send(this.message);
	}

	public final void to(final Address... addresses) throws MessagingException {
		this.message.addRecipients(RecipientType.TO, addresses);
	}

	public final void to(final String name, final String address) throws UnsupportedEncodingException, MessagingException {
		this.message.addRecipient(RecipientType.TO, new InternetAddress(address, name));
	}

} // End Email
