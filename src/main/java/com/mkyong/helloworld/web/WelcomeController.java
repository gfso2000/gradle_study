package com.mkyong.helloworld.web;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.mkyong.helloworld.exception.CustomGenericException;
import com.mkyong.helloworld.service.HelloWorldService;

@Controller
public class WelcomeController implements ServletContextAware {

	private final Logger logger = LoggerFactory.getLogger(WelcomeController.class);
	private final HelloWorldService helloWorldService;
	private ServletContext servletContext;
	
	@Autowired
	public WelcomeController(HelloWorldService helloWorldService) {
		this.helloWorldService = helloWorldService;
	}

	@RequestMapping(value = "/hello/{name:.+}", method = RequestMethod.GET)
	public ModelAndView hello(@PathVariable("name") String name) throws Exception {
		logger.debug("hello() is executed - $name {}", name);

		ModelAndView model = new ModelAndView();
		model.setViewName("index");
		
		model.addObject("title", helloWorldService.getTitle(name));
		model.addObject("msg", helloWorldService.getDesc());
		model.addObject("jsdebug", servletContext.getInitParameter("jsdebug"));
		if ("error".equals(name)) {
	    // go handleCustomException
	    throw new CustomGenericException("E888", "This is custom message");
	    } else if ("io-error".equals(name)) {
	    // go handleAllException
	    throw new IOException();
	    }
		return model;
	}

	@ExceptionHandler(CustomGenericException.class)
  public ModelAndView handleCustomException(CustomGenericException ex) {
    ModelAndView model = new ModelAndView("error");
    model.addObject("errCode", ex.getErrCode());
    model.addObject("errMsg", ex.getErrMsg());

    return model;
  }

  @ExceptionHandler(Exception.class)
  public ModelAndView handleAllException(Exception ex) {
    ModelAndView model = new ModelAndView("error");
    model.addObject("errMsg", "this is Exception.class");

    return model;
  }
  
  @RequestMapping(value = { "/", "/welcome**" }, method = RequestMethod.GET)
  public ModelAndView welcomePage()
  {
    ModelAndView model = new ModelAndView();
    model.addObject("title", "Spring Security Hello World");
    model.addObject("message", "This is welcome page!");
    model.setViewName("hello");
    return model;
  }

  @RequestMapping(value = "/admin**", method = RequestMethod.GET)
  public ModelAndView adminPage()
  {
    ModelAndView model = new ModelAndView();
    model.addObject("title", "Spring Security Hello World");
    model.addObject("message", "This is protected page!");
    model.setViewName("admin");
    return model;
  }

  //Spring Security see this :
  /**
   * both "normal login" and "login for update" shared this form.
   * 
   */
  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public ModelAndView login(@RequestParam(value = "error", required = false) String error,
    @RequestParam(value = "logout", required = false) String logout, 
          HttpServletRequest request) {

    ModelAndView model = new ModelAndView();
    if (error != null) {
      model.addObject("error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
      //login form for update page
      //if login error, get the targetUrl from session again.
      String targetUrl = getRememberMeTargetUrlFromSession(request);
      System.out.println(targetUrl);
      if(StringUtils.hasText(targetUrl)){
        model.addObject("targetUrl", targetUrl);
        model.addObject("loginUpdate", true);
      }
    }

    if (logout != null) {
      model.addObject("msg", "You've been logged out successfully.");
    }
    model.setViewName("login");

    return model;
  }
  /**
   * This update page is for user login with password only.
   * If user is login via remember me cookie, send login to ask for password again.
   * To avoid stolen remember me cookie to update info
   */
  @RequestMapping(value = "/admin/update**", method = RequestMethod.GET)
  public ModelAndView updatePage(HttpServletRequest request) {

    ModelAndView model = new ModelAndView();

    if (isRememberMeAuthenticated()) {
      //send login for update
      setRememberMeTargetUrlToSession(request);
      model.addObject("loginUpdate", true);
      model.setViewName("/login");
    } else {
      model.setViewName("update");
    }

    return model;
  }
  /**
   * Check if user is login by remember me cookie, refer
   * org.springframework.security.authentication.AuthenticationTrustResolverImpl
   */
  private boolean isRememberMeAuthenticated() {

    Authentication authentication = 
                    SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return false;
    }

    return RememberMeAuthenticationToken.class.isAssignableFrom(authentication.getClass());
  }
  
  /**
   * save targetURL in session
   */
  private void setRememberMeTargetUrlToSession(HttpServletRequest request){
    HttpSession session = request.getSession(false);
    if(session!=null){
      session.setAttribute("targetUrl", "/admin/update");
    }
  }

  /**
   * get targetURL from session
   */
  private String getRememberMeTargetUrlFromSession(HttpServletRequest request){
    String targetUrl = "";
    HttpSession session = request.getSession(false);
    if(session!=null){
      targetUrl = session.getAttribute("targetUrl")==null?""
                             :session.getAttribute("targetUrl").toString();
    }
    return targetUrl;
  }
  
  // customize the error message
  private String getErrorMessage(HttpServletRequest request, String key)
  {

    Exception exception = (Exception) request.getSession().getAttribute(key);

    String error = "";
    if (exception instanceof BadCredentialsException)
    {
      error = "Invalid username and password!";
    }
    else if (exception instanceof LockedException)
    {
      error = exception.getMessage();
    }
    else
    {
      error = "Invalid username and password!";
    }

    return error;
  }
  // for 403 access denied page
  @RequestMapping(value = "/403", method = RequestMethod.GET)
  public ModelAndView accesssDenied()
  {
    ModelAndView model = new ModelAndView();
    // check if user is login
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (!(auth instanceof AnonymousAuthenticationToken))
    {
      UserDetails userDetail = (UserDetails) auth.getPrincipal();
      model.addObject("username", userDetail.getUsername());
    }
    model.setViewName("403");
    return model;
  }

  @Override
  public void setServletContext(ServletContext servletContext)
  {
    this.servletContext = servletContext;
  }
}