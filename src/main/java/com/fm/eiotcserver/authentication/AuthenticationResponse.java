package com.fm.eiotcserver.authentication;

import org.vertx.java.core.json.JsonObject;

/**
 * Created by fabiomarini on 21/12/14.
 */
public class AuthenticationResponse {

   protected static String RESPONSE_KEY = "response";

   protected static String RESPONSE_VALUE_OK = "OK";
   protected static String RESPONSE_VALUE_NOOK = "NOOK";

   protected static JsonObject baseRespose() {
      JsonObject response = new JsonObject();
      response.putString(RESPONSE_KEY, RESPONSE_VALUE_NOOK);

      return response;
   }

   public static String positiveRepose() {
      return baseRespose().putString(RESPONSE_KEY, RESPONSE_VALUE_OK).encodePrettily();
   }

   public static String negativeRepose() {
      return baseRespose().putString(RESPONSE_KEY, RESPONSE_VALUE_NOOK).encodePrettily();
   }
}
