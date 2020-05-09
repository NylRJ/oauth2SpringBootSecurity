package com.i9developed.oauth2.ws.services.email;

import javax.mail.internet.MimeMessage;

import com.i9developed.oauth2.ws.domain.User;
import com.i9developed.oauth2.ws.domain.VerificationToken;

public interface EmailService {
	
	void sendHtmlEmail(MimeMessage msg);
	void sendConfirmationHtmlEmail(User user, VerificationToken vToken);
	
	

}
 