package ar.edu.itba.paw.interfaces.service;
import ar.edu.itba.paw.model.User;

public interface EmailService
{
	void sendWelcomeEmail(User u);
	void sendAccountRecoveryEmail(User u);
	void sendDailyEmails();
}
