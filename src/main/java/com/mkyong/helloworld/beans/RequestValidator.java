package com.mkyong.helloworld.beans;

import java.util.ArrayList;
import java.util.List;

public class RequestValidator
{
  private List<String> errorMessages = new ArrayList<String>();

  public RequestValidator()
  {
    System.out.println("Validator instance created!");
  }

  // Validates the request and populates error messages
  public void validate(String requestId)
  {

  }

  public List<String> getErrorMessages()
  {
    return errorMessages;
  }
}
