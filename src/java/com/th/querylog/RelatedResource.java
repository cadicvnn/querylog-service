package com.th.querylog;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Resource which has only one representation.
 * 
 */
public class RelatedResource extends ServerResource {
  @Get
  public String represent() {
    String message = (String) getRequest().getAttributes().get("message");
    try {
      message = URLDecoder.decode(message, "UTF-8");
    } catch (UnsupportedEncodingException e) {
    }
    String text = Related.getInstance().related(message);
    return text;
  }

}