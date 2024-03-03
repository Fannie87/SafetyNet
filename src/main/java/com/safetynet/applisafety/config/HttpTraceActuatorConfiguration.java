package com.safetynet.applisafety.config;

import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Configuration to make endpoint httptrace work again
@Configuration
public class HttpTraceActuatorConfiguration {
 @Bean
 public HttpTraceRepository httpTraceRepository() {
     return new InMemoryHttpTraceRepository();
 }
}