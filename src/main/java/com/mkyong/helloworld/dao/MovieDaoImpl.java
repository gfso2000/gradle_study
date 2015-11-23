package com.mkyong.helloworld.dao;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.mkyong.helloworld.model.Movie;

@Repository("movieDao")
public class MovieDaoImpl implements MovieDao
{

  @Override
  @Cacheable(value="cache1", key="#name")
  public Movie findByDirector(String name)
  {
    //slowQuery(2000L);
    System.out.println("findByDirector is running...");
    return new Movie(1, "Forrest Gump", "Robert Zemeckis");
  }

  private void slowQuery(long seconds)
  {
    try
    {
      Thread.sleep(seconds);
    }
    catch (InterruptedException e)
    {
      throw new IllegalStateException(e);
    }
  }
}
