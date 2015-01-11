package com.fm.eiotcserver.authentication;

import com.fm.eiotcserver.commons.RequestUtils;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by fabiomarini on 09/12/14.
 */
public class AuthenticationVerticle extends Verticle {

   public void start() {
      container.logger().info("Starting EIoTC Authentication server ... ");

      configureDepenciesModules();

      RouteMatcher matcher = new RouteMatcher();

      matcher.post("/security/user", req -> {
         final String contentType = req.headers().get("Content-Type");

         req.bodyHandler(event -> {
               Map<String, Object> params = null;
               if ("application/x-www-form-urlencoded".equals(contentType)) {
                  params = RequestUtils.getParamsFromBody(event);
               } else if ("application/json".equals(contentType) ||
                          "application/vnd.org.jfrog.artifactory.security.group+json".equals(contentType)) {
                  params = new JsonObject(new String(event.toString())).toMap();
               }

               if (params != null) {
                  JsonObject document = new JsonObject();

                  Set entries = params.entrySet();
                  Iterator iterator = entries.iterator();
                  while(iterator.hasNext()) {
                     Map.Entry entry = (Map.Entry) iterator.next();

                     document.putString((String)entry.getKey(), (String)entry.getValue());
                  }

                  JsonObject json = new JsonObject().putString("collection", "eiotcs-auth")
                        .putString("action", "save")
                        .putObject("document", document);

                  JsonObject data = new JsonObject();
                  data.putArray("results", new JsonArray());

                  vertx.eventBus().send("mongodb-persistor", json,
                     (Message<JsonObject> jsonObjectMessage) ->
                        req.response().end(jsonObjectMessage.body().encodePrettily()));
               }
               else {
                  req.response().end(AuthenticationResponse.negativeRepose());
               }
          });
      });

      matcher.post("/security/check/connection", req -> {
         final String contentType = req.headers().get("Content-Type");

         req.bodyHandler(event -> {
            Map<String, Object> params = null;
            if ("application/x-www-form-urlencoded".equals(contentType)) {
               params = RequestUtils.getParamsFromBody(event);
            } else if ("application/json".equals(contentType)) {
               params = new JsonObject(new String(event.toString())).toMap();
            }

            if (params != null) {
               JsonObject document = new JsonObject();

               Set entries = params.entrySet();
               Iterator iterator = entries.iterator();
               while(iterator.hasNext()) {
                  Map.Entry entry = (Map.Entry) iterator.next();

                  document.putString((String)entry.getKey(), (String)entry.getValue());
               }

               JsonObject json = new JsonObject().putString("collection", "eiotcs-auth")
                     .putString("action", "count")
                     .putObject("matcher", document);

               JsonObject data = new JsonObject();
               data.putArray("results", new JsonArray());

               vertx.eventBus().send("mongodb-persistor", json,
                     (Message<JsonObject> jsonObjectMessage) -> {
                        JsonObject mresp = jsonObjectMessage.body();

                        if(mresp.getInteger("count") >= 1) {
                           req.response().end(AuthenticationResponse.positiveRepose());
                        }
                        else {
                           req.response().end(AuthenticationResponse.negativeRepose());
                        }
                   });
            }
            else {
               req.response().end(AuthenticationResponse.negativeRepose());
            }
         });
      });

      matcher.post("/security/check/session", req -> {
         final String contentType = req.headers().get("Content-Type");

         req.bodyHandler(event -> {
            Map<String, Object> params = null;
            if ("application/x-www-form-urlencoded".equals(contentType)) {
               params = RequestUtils.getParamsFromBody(event);
            } else if ("application/json".equals(contentType)) {
               params = new JsonObject(new String(event.toString())).toMap();
            }

            if (params != null) {
               JsonObject document = new JsonObject();

               Set entries = params.entrySet();
               Iterator iterator = entries.iterator();
               while(iterator.hasNext()) {
                  Map.Entry entry = (Map.Entry) iterator.next();

                  document.putString((String)entry.getKey(), (String)entry.getValue());
               }

               JsonObject json = new JsonObject().putString("collection", "eiotcs-auth")
                     .putString("action", "count")
                     .putObject("matcher", document);

               JsonObject data = new JsonObject();
               data.putArray("results", new JsonArray());

               vertx.eventBus().send("mongodb-persistor", json,
                     (Message<JsonObject> jsonObjectMessage) -> {
                        JsonObject mresp = jsonObjectMessage.body();

                        if(mresp.getInteger("count") == 1) {
                           req.response().end(AuthenticationResponse.positiveRepose());
                        }
                        else {
                           req.response().end(AuthenticationResponse.negativeRepose());
                        }
                     });
            }
            else {
               req.response().end(AuthenticationResponse.negativeRepose());
            }
         });
      });

      matcher.noMatch(req ->
         req.response().sendFile("./site/index.html")
      );

      vertx.createHttpServer().requestHandler(matcher).listen(8888);

      container.logger().info(" ... EIoTC Authentication server started, listening on port: 8888");
   }

   private void configureDepenciesModules() {
      JsonObject appConfig = container.config();

      container.deployModule("io.vertx~mod-mongo-persistor~2.1.1", appConfig.getObject("mongo-persistor"));
   }
}
