package com.oauth2.faceboookLogin.Oauth2Facebook;





import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

//@SpringBootApplication
//@EnableOAuth2Sso
//@RestController
public class SoccialApplicationVersion1 /*extends WebSecurityConfigurerAdapter*/{
	  @RequestMapping("/user")
	  public Principal user(Principal principal) {
	    return principal;
	  }
	  /*
	  public static void main(String[] args) {
	    SpringApplication.run(SoccialApplicationVersion1.class, args);
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
	        .and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
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
	  
	  private CsrfTokenRepository csrfTokenRepository() {
		  HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		  repository.setHeaderName("X-XSRF-TOKEN");
		  return repository;
		}
	   */
}