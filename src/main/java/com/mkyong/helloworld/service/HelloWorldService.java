package com.mkyong.helloworld.service;

import java.io.IOException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mkyong.helloworld.beans.RequestProcessor;
import com.mkyong.helloworld.dao.MovieDao;

@Service
public class HelloWorldService {

	private static final Logger logger = LoggerFactory.getLogger(HelloWorldService.class);
	@Autowired
	private ApplicationContext appContext;
	CacheManager cm = CacheManager.newInstance();
	
	public String getDesc() {
		logger.debug("getDesc() is executed!");
		RequestProcessor processor = (RequestProcessor)appContext.getBean("requestProcessor");
		processor.handleRequest("aaa");
		processor = (RequestProcessor)appContext.getBean("requestProcessor");
    processor.handleRequest("bbb");
    
    ///////////////////////////////////EHCache///////////////////////////////
    Cache cache = cm.getCache("cache1");
    
    //3. Put few elements in cache
    cache.put(new Element("1","Jan"));
    cache.put(new Element("2","Feb"));
    cache.put(new Element("3","Mar"));
    
    //4. Get element from cache
    Element ele = cache.get("2");
    
    //5. Print out the element
    String output = (ele == null ? null : ele.getObjectValue().toString());
    System.out.println(output);
    
    //6. Is key in cache?
    System.out.println(cache.isKeyInCache("3"));
    System.out.println(cache.isKeyInCache("10"));
    ///////////////////////////////////EHCache///////////////////////////////
    
    ///////////////////////////////////EHCache for dao///////////////////////////////
    MovieDao obj = (MovieDao) appContext.getBean("movieDao");
    
    logger.debug("Result : {}", obj.findByDirector("dummy"));
    logger.debug("Result : {}", obj.findByDirector("dummy"));
    logger.debug("Result : {}", obj.findByDirector("dummy"));
    ///////////////////////////////////EHCache for dao///////////////////////////////
    
    System.out.println(this.getFile("file/readme.txt"));
		return "Gradle + Spring MVC Hello World Example";
	}

  private String getFile(String fileName)
  {
    String result = "";
    
    ClassLoader classLoader = getClass().getClassLoader();
    try {
        result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
    } catch (IOException e) {
      e.printStackTrace();
    }
      
    return result;
  }
  
	public String getTitle(String name) {

		logger.debug("getTitle() is executed! $name : {}", name);

		if(StringUtils.isEmpty(name)){
			return "Hello World";
		}else{
			return "Hello " + name;
		}
		
	}

}