package com.mkyong.helloworld.beans;

public class RequestProcessor
{
  private RequestValidator validator;

  public void handleRequest(String requestId)
  {
    validator.validate(requestId);
    // Process the request and update
  }

  public RequestValidator getValidator()
  {
    return validator;
  }

  public void setValidator(RequestValidator validator)
  {
    this.validator = validator;
  }
}
