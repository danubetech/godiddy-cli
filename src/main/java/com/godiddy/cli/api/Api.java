package com.godiddy.cli.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godiddy.api.client.ApiClient;
import com.godiddy.api.client.ApiResponse;
import com.godiddy.api.client.swagger.api.*;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Flow;
import java.util.function.Consumer;

public class Api {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ApiClient apiClient() {
        ApiClient apiClient = new ApiClient();
        String endpoint = Endpoint.getEndpoint();
        if (endpoint.endsWith("/")) endpoint = endpoint.substring(0, endpoint.length()-1);
        apiClient.updateBaseUri(endpoint);
        apiClient.setRequestInterceptor(requestInterceptor);
        apiClient.setResponseInterceptor(responseInterceptor);
        return apiClient;
    }

    public static UniversalResolverApi universalResolverApi() {
        return new UniversalResolverApi(apiClient());
    }

    public static UniversalRegistrarApi universalRegistrarApi() {
        return new UniversalRegistrarApi(apiClient());
    }

    public static WalletServiceApi walletServiceApi() {
        return new WalletServiceApi(apiClient());
    }

    public static VersionServiceApi versionServiceApi() {
        return new VersionServiceApi(apiClient());
    }

    public static UniversalIssuerApi universalIssuerApi() {
        return new UniversalIssuerApi(apiClient());
    }

    public static UniversalVerifierApi universalVerifierApi() {
        return new UniversalVerifierApi(apiClient());
    }

    private static final Flow.Subscriber<ByteBuffer> loggingSubscriber = new Flow.Subscriber<>() {

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(ByteBuffer byteBuffer) {
            String body = new String(byteBuffer.array(), StandardCharsets.UTF_8);
            Api.print(body);
        }

        @Override
        public void onError(Throwable throwable) { }

        @Override
        public void onComplete() { }
    };

    private static final Consumer<HttpRequest.Builder> requestInterceptor = builder -> {
        String apiKey = ApiKey.getApiKey();
        builder.header("Authorization", "Bearer " + apiKey);
        HttpRequest httpRequest = builder.build();
        System.out.println(">>> " + httpRequest.method() + " " + httpRequest.uri());
        Api.print(httpRequest.headers(), ">");
        if (httpRequest.bodyPublisher().isPresent()) {
            httpRequest.bodyPublisher().get().subscribe(loggingSubscriber);
        }
    };

    private static final Consumer<HttpResponse<InputStream>> responseInterceptor = inputStreamHttpResponse -> {
        System.out.println("<<< " + inputStreamHttpResponse.statusCode() + " " + inputStreamHttpResponse.request().uri());
        Api.print(inputStreamHttpResponse.headers(), "<");
    };

    public static <T> T execute(Callable<ApiResponse<T>> supplier) throws Exception {
        ApiResponse<T> apiResponse = supplier.call();
        T data = apiResponse.getData();
        Api.print(data);
        return data;
    }

    public static Map<String, Object> fromJson(String json) {
        return fromJson(json, Map.class);
    }

    public static <T> T fromJson(String json, Class<T> cl) {
        try {
            return objectMapper.readValue(json, cl);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static void print(Object object) {
        String string;
        Formatting.Value formatting = Formatting.getFormatting();
        try {
            if (formatting == Formatting.Value.pretty) {
                if (object instanceof String) object = objectMapper.readValue((String) object, Object.class);
                string = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            } else if (formatting == Formatting.Value.flat) {
                if (object instanceof String) object = objectMapper.readValue((String) object, Object.class);
                string = objectMapper.writeValueAsString(object);
            } else if (formatting == Formatting.Value.raw) {
                string = Objects.toString(object);
            } else if (formatting == Formatting.Value.off) {
                string = null;
            } else {
                throw new IllegalStateException("Unexpected formatting value: " + formatting);
            }
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException(ex);
        }
        if (string != null) System.out.println(string);
    }

    private static void print(HttpHeaders httpHeaders, String prefix) {
        String string;
        Headers.Value headers = Headers.getHeaders();
        if (headers == Headers.Value.on) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            for (Map.Entry<String, List<String>> headersEntry : httpHeaders.map().entrySet()) {
                for (String headersValue : headersEntry.getValue()) {
                    printWriter.println(prefix + headersEntry.getKey() + ": " + headersValue);
                }
            }
            string = stringWriter.toString();
        } else if (headers == Headers.Value.off) {
            string = null;
        } else {
            throw new IllegalStateException("Unexpected headers value: " + headers);
        }
        if (string != null) System.out.println(string);
    }
}
