package ar.edu.itba.paw.service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.User;

@Service
public class EmailServiceImpl implements EmailService
{
	@Autowired
    private JavaMailSender emailSender;
	
	@Autowired
	private UserService us;
	
	private void sendEmail(String to, String subject, String text)
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}
	
	@Async
	@Override
	public void sendCustomEmail(User u, String subject, String msg)
	{
		if(u == null)
			return;
		sendEmail(u.getEmail(), subject, msg);
	}

	@Async
	@Scheduled(cron = "0 0 9 * * *")
	@Override
	public void sendDailyEmails()
	{
		// TO DO: Get a list of all users
		List<User> userList = new ArrayList<User>();
		for(User u : userList)
		{
			sendEmail(u.getEmail(), "Good morning!", "It's 9 AM. Have a nice day!");
		}
	}

	@Async
	@Override
	public void sendWelcomeEmail(User u)
	{
		if(u == null)
			return;
		sendEmail(u.getEmail(), "Welcome to QuestLog!", "Thank you so much for joining QuestLog!");
	}

	@Override
	public void sendAccountRecoveryEmail(User u)
	{
		// TODO Auto-generated method stub
	}
}
