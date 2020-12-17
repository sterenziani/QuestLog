package ar.edu.itba.paw.webapp.config;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import ar.edu.itba.paw.webapp.auth.PawAuthenticationFilter;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.auth.RestAuthenticationEntryPoint;
import ar.edu.itba.paw.webapp.auth.handlers.PawAuthenticationFailureHandler;
import ar.edu.itba.paw.webapp.auth.handlers.PawAuthenticationSuccessHandler;
import ar.edu.itba.paw.webapp.auth.handlers.RefererRedirectionLogoutSuccessHandler;
import ar.edu.itba.paw.webapp.auth.schemes.basic.BasicProvider;
import ar.edu.itba.paw.webapp.auth.schemes.jwt.JWTProvider;

@Configuration
@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private PawUserDetailsService userDetails;
	@Autowired
	private RefererRedirectionLogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private BasicProvider basicAuthenticationProvider;
    @Autowired
    private JWTProvider jwtAuthenticationProvider;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    private PawAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private PawAuthenticationFailureHandler authenticationFailureHandler;

	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.authenticationProvider(basicAuthenticationProvider).authenticationProvider(jwtAuthenticationProvider);
	}
	
    @Bean
    public PawAuthenticationFilter ourAuthenticationFilter() throws Exception {
    	PawAuthenticationFilter authenticationFilter = new PawAuthenticationFilter();
    	authenticationFilter.setAuthenticationManager(authenticationManager());
    	authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
    	authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return authenticationFilter;
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
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
	      return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception
	{
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		http.addFilterBefore(encodingFilter, CsrfFilter.class);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().authorizeRequests()
				.antMatchers(HttpMethod.POST,"/api/users/register").permitAll()
				.antMatchers(HttpMethod.POST,"/api/users/login").permitAll()
	            .antMatchers(HttpMethod.POST,"/api/users/forgot_password").permitAll()
	            .antMatchers(HttpMethod.POST, "/api/**").authenticated()
	            .antMatchers(HttpMethod.PUT, "/api/**").authenticated()
	            .antMatchers(HttpMethod.DELETE, "/api/**").authenticated()
	            .antMatchers(HttpMethod.GET, "/api/**").permitAll()
	            .antMatchers("/admin/**").hasRole("ADMIN")
	            .antMatchers("/api/**").permitAll()
	            .antMatchers("/**").permitAll()
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
				.authenticationEntryPoint(restAuthenticationEntryPoint)
			.and().addFilterBefore(ourAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.csrf().disable();
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
