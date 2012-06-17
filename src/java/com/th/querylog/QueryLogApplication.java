package com.th.querylog;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class QueryLogApplication extends Application {

  @Override
  public synchronized Restlet createInboundRoot() {
    Router router = new Router(getContext());

    // Defines only one route
    router.attach("/suggest/{message}", SuggestResource.class);
    router.attach("/related/{message}", RelatedResource.class);

    return router;
  }

}