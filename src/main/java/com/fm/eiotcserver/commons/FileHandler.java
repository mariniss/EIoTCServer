package com.fm.eiotcserver.commons;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

public class FileHandler implements Handler<HttpServerRequest> {
        private final String file;
 
        public FileHandler(String file) {
            this.file = file;
        }
 
        @Override
        public void handle(HttpServerRequest httpServerRequest) {
            httpServerRequest.response().sendFile(this.getClass().getResource(file).getFile().replaceAll("^/[A-Z]:", ""));
        }
    }