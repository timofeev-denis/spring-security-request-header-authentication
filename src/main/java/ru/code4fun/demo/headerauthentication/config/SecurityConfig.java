package ru.code4fun.demo.headerauthentication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String PRINCIPAL_REQUEST_HEADER = "principal";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .addFilterAfter(requestHeaderAuthenticationFilter(), LogoutFilter.class)
                .authenticationProvider(preAuthenticatedAuthenticationProvider());
    }

    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(userDetailsByNameServiceWrapper());
        return provider;
    }

    public UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsByNameServiceWrapper() {
        UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> serviceWrapper = new UserDetailsByNameServiceWrapper<>();
        serviceWrapper.setUserDetailsService(customUserDetailsService());
        return serviceWrapper;
    }

    public UserDetailsService customUserDetailsService() {
        return username -> User.withUsername(username)
                .password("")
                .roles("USER")
                .build();
    }

    public RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() throws Exception {
        RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
        filter.setPrincipalRequestHeader(PRINCIPAL_REQUEST_HEADER);
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setExceptionIfHeaderMissing(false);
        filter.setCheckForPrincipalChanges(true);
        return filter;
    }
}
