package ar.edu.itba.paw.webapp.config;
import ar.edu.itba.paw.webapp.auth.RefererRedirectionAuthenticationSuccessHandler;
import ar.edu.itba.paw.webapp.auth.RefererRedirectionLogoutSuccessHandler;
import org.apache.commons.io.IOUtils;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;

@Configuration
@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private PawUserDetailsService userDetails;

	@Autowired
	private RefererRedirectionAuthenticationSuccessHandler authSuccessHandler;

	@Autowired
	private RefererRedirectionLogoutSuccessHandler logoutSuccessHandler;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(userDetails).passwordEncoder(passwordEncoder());
	}
	
	@Override
	public void configure(final WebSecurity web) throws Exception
	{
		web.ignoring().antMatchers("/images/**", "/css/**", "/js/**", "/img/**", "/favicon.ico", "/403");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
	      return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception
	{
		http.sessionManagement().invalidSessionUrl("/")
			.and().authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/profile", "/games/scores/**", "/createRun/**", "/reviews/**").authenticated()
				.antMatchers("/**").permitAll()
			.and().formLogin()
				.successHandler(new RefererRedirectionAuthenticationSuccessHandler())
				.usernameParameter("username")
				.passwordParameter("password")
				.failureUrl("/login_error")
				.loginPage("/login")
			.and().rememberMe()
				.rememberMeParameter("rememberme")
				.userDetailsService(userDetails)
				.key(getEncryptionKey())
				.tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(60))
			.and().logout()
				.logoutUrl("/logout")
				.deleteCookies("JSESSIONID")
				.logoutSuccessHandler(logoutSuccessHandler)
			.and().exceptionHandling()
				.accessDeniedPage("/error403")
			.and().csrf().disable();
	}
	
    private String getEncryptionKey()
    {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("rememberme.key");
        try
        {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    }
}
