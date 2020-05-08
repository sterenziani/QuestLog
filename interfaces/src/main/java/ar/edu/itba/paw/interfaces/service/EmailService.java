package ar.edu.itba.paw.interfaces.service;
import ar.edu.itba.paw.model.User;

public interface EmailService
{
	void sendWelcomeEmail(User u);
	void sendDailyEmails();
	void sendAccountRecoveryEmail(User u, String token);
}
