package com.myorg.document.config;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import com.myorg.document.rest.DocumentService;

public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(RequestContextFilter.class);
        register(DocumentService.class);
        packages("com.myorg.document");
        register(LoggingFilter.class);
        register(MultiPartFeature.class);
    }
}
