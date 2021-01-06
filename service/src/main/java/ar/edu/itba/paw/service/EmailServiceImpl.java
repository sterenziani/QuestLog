package ar.edu.itba.paw.service;
import java.util.List;
import java.util.Locale;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
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
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.User;

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
	
	@Autowired
	@Qualifier("QuestLog-baseUrl")
	private String baseUrl;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Transactional
	@Async
	@Override
	public void sendWelcomeEmail(User u)
	{
		if(u == null)
			return;
		Locale locale = u.getLocale();
		final Context ctx = new Context(locale);
		ctx.setVariable("path", baseUrl);
		ctx.setVariable("username", u.getUsername());

	    final MimeMessage mimeMessage = emailSender.createMimeMessage();
	    MimeMessageHelper message;
	    LOGGER.debug("Sending welcome email to user {} at {}. Using language {}", u.getUsername(), u.getEmail(), u.getLocale().toLanguageTag());
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
			LOGGER.error("An exception was thrown while attempting to send welcome email to user {} at {} in {}", u.getUsername(), u.getEmail(), u.getLocale().toLanguageTag(), e);
		}
	}

	@Async
	@Scheduled(cron = "00 00 09 * * *")
	@Override
	@Transactional
	public void sendDailyEmails()
	{
		LOGGER.debug("Sending daily emails to all users");
		List<User> userList = us.getAllUsers();
		for(User u : userList)
		{
			List<Game> backlog = gs.getGamesInBacklogReleasingTomorrow(u);
			if(!backlog.isEmpty())
			{
				final Context ctx = new Context(u.getLocale());
				ctx.setVariable("games", backlog);
				ctx.setVariable("path", baseUrl);
				final MimeMessage mimeMessage = emailSender.createMimeMessage();
				MimeMessageHelper message;
				LOGGER.debug("Sending daily email to {}, who has {} games releasing tomorrow they should know about.", u.getUsername(), backlog.size());
				try
				{
					message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
					Locale locale = u.getLocale();
					message.setSubject(messageSource.getMessage("email.upcoming.subject", null, locale));
					message.setFrom("no.reply.paw.questlog@gmail.com");
					message.setTo(u.getEmail());
					String htmlContent = templateEngine.process("html/upcomingNotification.html", ctx);
					message.setText(htmlContent, true);
					emailSender.send(mimeMessage);
				}
				catch (MessagingException e)
				{
					LOGGER.error("An exception was thrown while attempting to send daily email to user {} at {} in {}", u.getUsername(), u.getEmail(), u.getLocale().toLanguageTag(), e);
				}
			}
		}
	}

	@Async
	@Override
	@Transactional
	public void sendAccountRecoveryEmail(User u, String token)
	{
		if(u == null)
			return;
		Locale locale = u.getLocale();
		final Context ctx = new Context(locale);
		ctx.setVariable("username", u.getUsername());
		ctx.setVariable("path", baseUrl);
		ctx.setVariable("token", token);
		
	    final MimeMessage mimeMessage = emailSender.createMimeMessage();
	    MimeMessageHelper message;
	    LOGGER.debug("Sending password reset email to {} at {} in {}", u.getUsername(), u.getEmail(), u.getLocale().toLanguageTag());
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
			LOGGER.error("An exception was thrown while attempting to send password reset email to user {} at {} in {}", u.getUsername(), u.getEmail(), u.getLocale().toLanguageTag(), e);
		}
	}
}
