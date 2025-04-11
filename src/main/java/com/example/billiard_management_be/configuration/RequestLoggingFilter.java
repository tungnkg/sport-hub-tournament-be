package com.example.billiard_management_be.configuration;

import com.example.billiard_management_be.shared.utils.SecurityUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        try {
            filterChain.doFilter(requestWrapper, response);
        } finally {
            var gson = new Gson();
            JsonObject object = new JsonObject();
            object.addProperty("action", requestWrapper.getRequestURI());
            object.addProperty("method", requestWrapper.getMethod());
            JsonObject formData = gson.fromJson(gson.toJson(requestWrapper.getParameterMap()), JsonObject.class);
            object.add("formData", formData);
            String requestBody = requestWrapper.getContentAsString();
            try {
                JsonObject body = gson.fromJson(requestBody, JsonObject.class);
                object.add("body", body);
            } catch (Exception e) {
                object.addProperty("body", requestBody);
            }
            var userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails != null) {
                object.addProperty("userName", userDetails.getEmail());
                object.addProperty("userId", userDetails.getId());
            }
            object.addProperty("timestamp", System.currentTimeMillis());
            object.addProperty("ip", requestWrapper.getRemoteAddr());
            object.addProperty("responseStatus", responseWrapper.getStatus());
            object.addProperty("referer", requestWrapper.getHeader("referer"));
            logger.warn("RequestLoggingFilter: {}", object);
        }
    }
}