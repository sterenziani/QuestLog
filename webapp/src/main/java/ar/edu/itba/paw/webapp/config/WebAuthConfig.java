package ar.edu.itba.paw.webapp.config;
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
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(userDetails).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception
	{
		http.sessionManagement().invalidSessionUrl("/login")
			.and().authorizeRequests()
				.antMatchers("/login", "/login_error", "/create").anonymous()
				.antMatchers("/admin").hasRole("ADMIN")
				//.antMatchers("/posts/vote").hasRole("VOTER")
				//.antMatchers("/posts/edit").hasRole("EDITOR")
				//.antMatchers("/posts/create").hasRole("USER")
				.antMatchers("/profile", "/games/scores/**", "/createRun/**").authenticated()
				.antMatchers("/**").permitAll()// Para cualquier otra URL
			.and().formLogin()
				.usernameParameter("username")
				.passwordParameter("password")
				.defaultSuccessUrl("/", false)
				.failureUrl("/login_error")
				.loginPage("/login")
			.and().rememberMe()
				.rememberMeParameter("rememberme")
				//.userDetailsService(userDetails)
				//.key("mysupersecretketthatnobodyknowsabout") // no hacer esto, crear una aleatoria segura suficientemente grande y colocarla bajo src/main/resources
				.tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
			.and().logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login")
			.and().exceptionHandling()
				.accessDeniedPage("/403")
			.and().csrf().disable();
	}
	
	@Override
	public void configure(final WebSecurity web) throws Exception
	{
		web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/403");
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
	      return super.authenticationManagerBean();
	}
}
