package com.fm.eiotcserver.commons;

import org.vertx.java.core.http.HttpServerRequest;

/**
 * Created by fabiomarini on 12/12/14.
 */
public interface IBaseAction {

   /**
    *
    * @param request
    */
   void processRequest(HttpServerRequest request);
}
