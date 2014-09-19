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

import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.osiam.addons.administration.mail.exception.TemplateException;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.fragment.DOMSelectorFragmentSpec;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.google.common.base.Strings;

/**
 * Email template renderer service for thymeleaf template engine
 *
 */
@Service
public class EmailTemplateRenderer {

	@Inject
	private SpringTemplateEngine templateEngine;

	public String renderEmailSubject(String templateName, Locale locale, Map<String, Object> variables) {
		String emailSubject = renderTemplate(templateName, "#mail-subject", locale, variables);
		if (Strings.isNullOrEmpty(emailSubject)) {
			throw new TemplateException(
					"Could not find the mail subject in your template file '" + templateName
							+ "'. Please provide an HTML element with the ID 'mail-subject'.", "template.email.exception.malformed");
		}
		return emailSubject;
	}

	public String renderEmailBody(String templateName, Locale locale, Map<String, Object> variables) {
		String emailBody = renderTemplate(templateName, "#mail-body", locale, variables);
		if (Strings.isNullOrEmpty(emailBody)) {
			throw new TemplateException(
					"Could not find the mail body in your template file '" + templateName
							+ "'. Please provide an HTML element with the ID 'mail-body'.", "template.email.exception.malformed");
		}
		return emailBody;
	}

	private String renderTemplate(String templateName, String selectorExpression, Locale locale, Map<String, Object> variables) {
		Context context = new Context(locale);
		context.setVariables(variables);

		return templateEngine.process(templateName + "-email", context, new DOMSelectorFragmentSpec(selectorExpression));
	}
}
