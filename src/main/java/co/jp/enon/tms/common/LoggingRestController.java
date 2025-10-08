package co.jp.enon.tms.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

@Component
public class LoggingRestController extends OncePerRequestFilter {
    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.valueOf("text/*"),
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.valueOf("application/*+json"),
            MediaType.valueOf("application/*+xml"),
            MediaType.MULTIPART_FORM_DATA
    );
    private static final List<MediaType> FORM_TYPES = Arrays.asList(
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA
    );
    private final ThreadLocal<Map<String, Object>> localParams = ThreadLocal.withInitial(LinkedHashMap::new);

    @Autowired
    private ObjectMapper mapper;

    @PostConstruct
    protected void postConstruct() {
        //logger.info("start postConstruct()");
    }

    @PreDestroy
    public void preDestroy() {
        //logger.info("start preDestroy()");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
        }
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        try {
            beforeRequest(request, response);
            outputParams();
            localParams.remove();

            filterChain.doFilter(request, response);

        } finally {
            afterRequest(request, response);
            outputParams();
            localParams.remove();
            response.copyBodyToResponse();
        }
    }

    protected void beforeRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (logger.isDebugEnabled()) {
            throwable("logRequestHeader", () -> logRequestHeader(request));
            throwable("logRequestBody", () -> logRequestBody(request));
        }
    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (logger.isDebugEnabled()) {
            throwable("logResponse", () -> logResponse(response));
        }
    }

    private void logRequestHeader(ContentCachingRequestWrapper request) {
        localParams.get().put("client", request.getRemoteAddr());
        String queryString = request.getQueryString();
        if (queryString == null) {
            localParams.get().put("request", request.getMethod() + " " + request.getRequestURI());
        } else {
            localParams.get().put("request", request.getMethod() + " " + request.getRequestURI() + "?" + queryString);
        }
        Map<String, List<String>> headers = new LinkedHashMap<>();
        Collections.list(request.getHeaderNames()).forEach(headerName ->
            Collections.list(request.getHeaders(headerName)).forEach(headerValue -> {
                headers.computeIfAbsent(headerName, k -> new ArrayList<>()).add(headerValue);
            }));
        localParams.get().put("requestHeaders", headers);
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (null != ctx && ctx.getAuthentication() != null) {
            localParams.get().put("authentication", convertTo(ctx.getAuthentication()));
        }
    }

    private void logRequestBody(ContentCachingRequestWrapper request) throws ServletException, IOException {
        Collection<Part> parts = getRequestParts(request);
        if (parts.isEmpty()) {
            byte[] content = request.getContentAsByteArray();
            if (content.length > 0) {
                logContent(content, request.getContentType(), request.getCharacterEncoding(), "requestBody");
            } else {
                localParams.get().put("requestBody", "[empty body]");
            }
        } else {
            Map<String, Object> contents = new LinkedHashMap<>();
            for (Part p : parts) {
                String name = p.getName();
                try {
                    byte[] content = FileCopyUtils.copyToByteArray(p.getInputStream());
                    contents.put(name, logContent(content, p.getContentType(), "UTF-8"));
                } catch (IOException e) {
                    String fileName = p.getSubmittedFileName();
                    if (StringUtils.hasText(fileName)) {
                        contents.put(name, String.format("fileName[%s]", fileName));
                    } else {
                        contents.put(name, String.format("IOException[%s]", e.getMessage()));
                    }
                }
            }
            localParams.get().put("requestBody", contents);
        }
    }

    private Collection<Part> getRequestParts(ContentCachingRequestWrapper request) throws ServletException, IOException {
        MediaType mediaType = getMediaType(request.getContentType());
        boolean isPost = FORM_TYPES.stream().anyMatch(formType -> formType.includes(mediaType));
        return isPost ? request.getParts() : Collections.emptyList();
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        int status = response.getStatus();
        localParams.get().put("response", status + " " + HttpStatus.valueOf(status).getReasonPhrase());
        Map<String, List<String>> headers = new LinkedHashMap<>();
        response.getHeaderNames().forEach(headerName ->
            response.getHeaders(headerName).forEach(headerValue -> {
                headers.computeIfAbsent(headerName, k -> new ArrayList<>()).add(headerValue);
            }));
        localParams.get().put("responseHeaders", headers);
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, response.getContentType(), "UTF-8", "responseBody");
        }
    }

    private void logContent(byte[] content, String contentType, String contentEncoding, String key) {
        localParams.get().put(key, logContent(content, contentType, contentEncoding));
    }

    private Object logContent(byte[] content, String contentType, String contentEncoding) {
        MediaType mediaType = getMediaType(contentType);
        if (mediaType == null) {
            return String.format("[%d bytes content]", content.length);
        }

        boolean visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
        if (visible) {
            try {
                String contentString = new String(content, contentEncoding);
                if (mediaType.getSubtype().contains("json")) {
                    return mapper.readTree(contentString);
                } else {
                    return contentString;
                }
            } catch (IOException e) {
                return String.format("[%d bytes content]", content.length);
            }
        } else {
            return String.format("[%d bytes content]", content.length);
        }
    }

    private MediaType getMediaType(String contentType) {
        try {
            return MediaType.valueOf(contentType);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void outputParams() {
        try {
            logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(localParams.get()));
        } catch (JsonProcessingException e) {
            logger.debug("Failed to log parameters: " + e.getMessage());
        }
    }

    private JsonNode convertTo(Object obj) {
        if (obj == null) {
            return NullNode.getInstance();
        }
        try {
            return mapper.valueToTree(obj);
        } catch (IllegalArgumentException e) {
            return NullNode.getInstance();
        }
    }

    private void throwable(String key, ThrowableExecutor method) {
        try {
            method.apply();
        } catch (Exception e) {
            localParams.get().put(key, String.format("%s[%s]", e.getClass().getSimpleName(), e.getMessage()));
        }
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }

    @FunctionalInterface
    public interface ThrowableExecutor {
        void apply() throws Exception;
    }
}