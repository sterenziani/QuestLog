package ar.edu.itba.paw.webapp.auth.handlers;

import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class RefererRedirectionLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    public RefererRedirectionLogoutSuccessHandler() {
        super();
        setUseReferer(true);
    }
}
