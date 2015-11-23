package com.mkyong.helloworld.model;

import java.io.Serializable;

public class Movie implements Serializable
{
  int id;
  String name;
  String directory;
  public Movie(int id, String name, String directory)
  {
    this.id = id;
    this.name = name;
    this.directory = directory;
  }
  public int getId()
  {
    return id;
  }
  public void setId(int id)
  {
    this.id = id;
  }
  public String getName()
  {
    return name;
  }
  public void setName(String name)
  {
    this.name = name;
  }
  public String getDirectory()
  {
    return directory;
  }
  public void setDirectory(String directory)
  {
    this.directory = directory;
  }
  
}
