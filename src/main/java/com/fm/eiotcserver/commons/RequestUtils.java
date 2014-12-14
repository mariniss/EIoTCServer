package com.fm.eiotcserver.commons;

import org.vertx.java.core.buffer.Buffer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fabiomarini on 10/12/14.
 */
public class RequestUtils {

   public static Map<String, Object> getParamsFromBody(Buffer body) {

      Map<String, Object> params = new HashMap<String, Object>();

      try {
         String queryString = URLDecoder.decode(body.toString(), "UTF-8");

         String[] queryParts = queryString.split("&");

         for (String param : queryParts) {
            String[] nameAndValue = param.split("=");
            params.put(nameAndValue[0], nameAndValue[1]);
         }

      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      }

      return params;
   }
}
