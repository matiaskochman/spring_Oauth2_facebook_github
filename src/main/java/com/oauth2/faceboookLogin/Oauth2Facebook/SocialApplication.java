package com.oauth2.faceboookLogin.Oauth2Facebook;



import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CompositeFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@SpringBootApplication
@RestController
@EnableOAuth2Client
public class SocialApplication extends WebSecurityConfigurerAdapter{

	  @Autowired
	  OAuth2ClientContext oauth2ClientContext;	
	
	  @RequestMapping("/user")
	  public Principal user(Principal principal) {
	    return principal;
	  }

	  public static void main(String[] args) {
	    SpringApplication.run(SocialApplication.class, args);
	  }
	  
	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
	    http
	      .antMatcher("/**")
	      .authorizeRequests()
	        .antMatchers("/", "/login**", "/webjars/**")
	        .permitAll()
	      .anyRequest()
	        .authenticated().and().logout().logoutSuccessUrl("/").permitAll()
	        .and().csrf().csrfTokenRepository(csrfTokenRepository())
	        .and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
	        .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);;
	  }
	  
	  private Filter ssoFilter() {
		  CompositeFilter filter = new CompositeFilter();
		  List<Filter> filters = new ArrayList<>();

		  OAuth2ClientAuthenticationProcessingFilter facebookFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/facebook");
		  OAuth2RestTemplate facebookTemplate = new OAuth2RestTemplate(facebook(), oauth2ClientContext);
		  facebookFilter.setRestTemplate(facebookTemplate);
		  facebookFilter.setTokenServices(new UserInfoTokenServices(facebookResource().getUserInfoUri(), facebook().getClientId()));
		  filters.add(facebookFilter);

		  OAuth2ClientAuthenticationProcessingFilter githubFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/github");
		  OAuth2RestTemplate githubTemplate = new OAuth2RestTemplate(github(), oauth2ClientContext);
		  githubFilter.setRestTemplate(githubTemplate);
		  githubFilter.setTokenServices(new UserInfoTokenServices(githubResource().getUserInfoUri(), github().getClientId()));
		  filters.add(githubFilter);

		  filter.setFilters(filters);
		  return filter;		
	 }
	  
	  
	  
	  private Filter csrfHeaderFilter() {
		  return new OncePerRequestFilter() {
		    @Override
		    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		        FilterChain filterChain) throws ServletException, IOException {
		      CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		      if (csrf != null) {
		        Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
		        String token = csrf.getToken();
		        if (cookie == null || token != null && !token.equals(cookie.getValue())) {
		          cookie = new Cookie("XSRF-TOKEN", token);
		          cookie.setPath("/");
		          response.addCookie(cookie);
		        }
		      }
		      filterChain.doFilter(request, response);
		    }
		  };
		}
	  
	  @Bean
	  @ConfigurationProperties("facebook.client")
	  OAuth2ProtectedResourceDetails facebook() {
	    return new AuthorizationCodeResourceDetails();
	  }
	  
	  @Bean
	  @ConfigurationProperties("facebook.resource")
	  ResourceServerProperties facebookResource() {
	    return new ResourceServerProperties();
	  }
	  
	  @Bean
	  @ConfigurationProperties("github.client")
	  OAuth2ProtectedResourceDetails github() {
	  	return new AuthorizationCodeResourceDetails();
	  }

	  @Bean
	  @ConfigurationProperties("github.resource")
	  ResourceServerProperties githubResource() {
	  	return new ResourceServerProperties();
	  }
	  
	  private CsrfTokenRepository csrfTokenRepository() {
		  HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		  repository.setHeaderName("X-XSRF-TOKEN");
		  return repository;
	  }	  
}

