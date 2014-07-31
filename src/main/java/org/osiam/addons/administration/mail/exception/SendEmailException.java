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

package org.osiam.addons.administration.mail.exception;

import org.osiam.addons.administration.exception.OsiamException;
import org.springframework.http.HttpStatus;

public class SendEmailException extends OsiamException {

    private static final long serialVersionUID = -292158452140136468L;

    public SendEmailException() {
        super();
    }

    public SendEmailException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SendEmailException(String message, String key, Throwable cause) {
        super(message, key, HttpStatus.INTERNAL_SERVER_ERROR.value(), cause);
    }

    public SendEmailException(String message, String key) {
        super(message, key, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
