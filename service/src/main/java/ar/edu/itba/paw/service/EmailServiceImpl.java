package ar.edu.itba.paw.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.User;

@Service
public class EmailServiceImpl implements EmailService
{
	@Autowired
    private JavaMailSender emailSender;
	
	@Autowired
	private UserService us;
	
	@Autowired
	private GameService gs;
	
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
	@Scheduled(cron = "00 00 09 * * *")
	@Override
	public void sendDailyEmails()
	{
		List<User> userList = us.getAllUsers();
		List<Game> upcoming = gs.getGamesReleasingTomorrow();
		for(User u : userList)
		{
			List<Game> backlog = gs.getGamesInBacklog(u);
			backlog.retainAll(upcoming);
			if(!backlog.isEmpty())
			{
				String msg = "Don't forget the following games are coming out tomorrow!";
				for(Game g : upcoming)
					msg += "\n # " +g.getTitle();
				sendEmail(u.getEmail(), "A Friendly Reminder from QuestLog!", msg);
			}
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
