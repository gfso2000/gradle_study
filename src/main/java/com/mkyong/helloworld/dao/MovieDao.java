package com.mkyong.helloworld.dao;

import com.mkyong.helloworld.model.Movie;

public interface MovieDao
{
  Movie findByDirector(String name);
}
