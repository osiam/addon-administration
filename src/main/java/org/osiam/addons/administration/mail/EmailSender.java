/*
 * Copyright (C) 2014 tarent AG
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.osiam.addons.administration.mail;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.osiam.addons.administration.mail.exception.SendEmailException;
import org.osiam.resources.helper.SCIMHelper;
import org.osiam.resources.scim.Email;
import org.osiam.resources.scim.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;

/**
 * Send email service for sending an email
 * 
 */
@Component
public class EmailSender {

    @Inject
    private JavaMailSender mailSender;

    @Inject
    private EmailTemplateRenderer renderer;

    @Value("${org.osiam.mail.from}")
    private String fromAddress;

    public void sendDeactivateMail(User user) {
    	sendDeactivateMail(user, new Locale(user.getLocale()));
    }
    
    public void sendDeactivateMail(User user, Locale locale) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("user", user);
        
        String mailConent = renderer.renderEmailBody("deactivate", locale, variables);
        String mailSubject = renderer.renderEmailSubject("deactivate", locale, variables);
        Optional<Email> email = SCIMHelper.getPrimaryOrFirstEmail(user);
        if(!email.isPresent()){
            throw new SendEmailException("The user has no email!", "user.no.email");
        }
        
        sendHTMLMail(fromAddress, email.get().getValue(), mailSubject, mailConent);
    }
    
    public void sendActivateMail(User user) {
        sendActivateMail(user, new Locale(user.getLocale()));
    }
    
    public void sendActivateMail(User user, Locale locale) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("user", user);
        
        String mailConent = renderer.renderEmailBody("activate", locale, variables);
        String mailSubject = renderer.renderEmailSubject("activate", locale, variables);
        Optional<Email> email = SCIMHelper.getPrimaryOrFirstEmail(user);
        if (!email.isPresent()) {
            throw new SendEmailException("The user has no email!", "user.no.email");
        }

        sendHTMLMail(fromAddress, email.get().getValue(), mailSubject, mailConent);
    }

    public void sendPlainTextMail(String fromAddress, String toAddress, String subject, String content) {
        mailSender.send(getMessage(fromAddress, toAddress, subject, content));
    }

    public void sendHTMLMail(String fromAddress, String toAddress, String subject, String htmlContent) {
        mailSender.send(getMimeMessage(fromAddress, toAddress, subject, htmlContent));
    }

    private SimpleMailMessage getMessage(String fromAddress, String toAddress, String subject, String mailContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(mailContent);
        message.setSentDate(new Date());
        return message;
    }

    private MimeMessage getMimeMessage(String fromAddress, String toAddress, String subject, String mailContent) {
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        try {
            message.setFrom(fromAddress);
            message.setTo(toAddress);
            message.setSubject(subject);
            message.setText(mailContent, true);
            message.setSentDate(new Date());
        } catch (MessagingException e) {
            throw new SendEmailException("Could not create metadata for email", e);
        }
        return mimeMessage;
    }
}
