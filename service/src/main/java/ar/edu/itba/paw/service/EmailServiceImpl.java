package ar.edu.itba.paw.service;
import java.util.List;
import java.util.Locale;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
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
	private TemplateEngine templateEngine;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserService us;

	@Autowired
	private GameService gs;
	
	private static final String WEBSITE_PATH = "http://pawserver.it.itba.edu.ar/paw-2020a-4/";
	//private static final String WEBSITE_PATH = "http://localhost:8080/webapp/";

	@Transactional
	@Async
	@Override
	public void sendWelcomeEmail(User u)
	{
		if(u == null)
			return;
		Locale locale = u.getLocale();
		final Context ctx = new Context(locale);
		ctx.setVariable("username", u.getUsername());

	    final MimeMessage mimeMessage = emailSender.createMimeMessage();
	    MimeMessageHelper message;
		try
		{
			message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			message.setSubject(messageSource.getMessage("email.register.title", null, locale));
			message.setFrom("no.reply.paw.questlog@gmail.com");
			message.setTo(u.getEmail());
			String htmlContent = templateEngine.process("html/welcome.html", ctx);
			message.setText(htmlContent, true);
			emailSender.send(mimeMessage);
		}
		catch (MessagingException e)
		{
			;
		}
	}

	@Async
	@Scheduled(cron = "00 00 09 * * *")
	@Override
	public void sendDailyEmails()
	{
		String url = WEBSITE_PATH + "games/";
		List<User> userList = us.getAllUsers();
		for(User u : userList)
		{
			List<Game> backlog = gs.getGamesInBacklogReleasingTomorrow(u);
			if(!backlog.isEmpty())
			{
				final Context ctx = new Context(u.getLocale());
				ctx.setVariable("games", backlog);
				ctx.setVariable("path", url);
				final MimeMessage mimeMessage = emailSender.createMimeMessage();
				MimeMessageHelper message;
				try
				{
					message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
					Locale locale = LocaleContextHolder.getLocale();
					message.setSubject(messageSource.getMessage("email.upcoming.subject", null, locale));
					message.setFrom("no.reply.paw.questlog@gmail.com");
					message.setTo(u.getEmail());
					String htmlContent = templateEngine.process("html/upcomingNotification.html", ctx);
					message.setText(htmlContent, true);
					emailSender.send(mimeMessage);
				}
				catch (MessagingException e)
				{
					;
				}
			}
		}
	}

	@Async
	@Override
	public void sendAccountRecoveryEmail(User u, String token)
	{
		String url = WEBSITE_PATH + "changePassword/";
		if(u == null)
			return;
		Locale locale = u.getLocale();
		final Context ctx = new Context(locale);
		ctx.setVariable("username", u.getUsername());
		ctx.setVariable("path", url);
		ctx.setVariable("token", token);
		
	    final MimeMessage mimeMessage = emailSender.createMimeMessage();
	    MimeMessageHelper message;
		try
		{
			message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			message.setSubject(messageSource.getMessage("email.forgotPassword.subject", null, locale));
			
			message.setFrom("no.reply.paw.questlog@gmail.com");
			message.setTo(u.getEmail());
			
			String htmlContent = templateEngine.process("html/changePassword.html", ctx);
			message.setText(htmlContent, true);
			emailSender.send(mimeMessage);
		}
		catch (MessagingException e)
		{
			;
		}
	}
}