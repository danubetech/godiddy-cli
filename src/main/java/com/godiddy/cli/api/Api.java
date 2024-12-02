package com.godiddy.cli.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.godiddy.api.client.ApiClient;
import com.godiddy.api.client.ApiResponse;
import com.godiddy.api.client.openapi.api.*;
import com.godiddy.api.client.openapi.model.CreateRequest;
import com.godiddy.api.client.openapi.model.DidStateAction;
import com.godiddy.api.client.openapi.model.RegistrarRequest;
import com.godiddy.api.client.openapi.model.RegistrarState;
import uniregistrar.openapi.RFC3339DateFormat;

import java.io.*;
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

    private static final ObjectMapper objectMapper = JsonMapper.builder()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
            .defaultDateFormat(new RFC3339DateFormat())
            .addModule(new JavaTimeModule())
            .build();

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

    public static void writeJson(File file, Object object, boolean pretty) throws IOException {
        if (pretty) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
        } else {
            objectMapper.writeValue(file, object);
        }
    }

    public static Object readJson(File file, Class cl) throws IOException {
        return objectMapper.readValue(file, cl);
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

    public static <T> T convert(Object object, Class<T> cl) {
        return objectMapper.convertValue(object, cl);
    }

    public static void print(Object object) {
        if (object instanceof RegistrarRequest registrarRequest) {
            print(registrarRequest, constructInterpretedString(registrarRequest));
        } else if (object instanceof RegistrarState registrarState) {
            print(registrarState, constructInterpretedString(registrarState));
        } else {
            print(object, object.getClass().getSimpleName());
        }
    }

    private static String constructInterpretedString(RegistrarRequest registrarRequest) {
        int didDocumentVerificationMethods = ! (registrarRequest instanceof CreateRequest createRequest) ? 0 : createRequest.getDidDocument() == null ? 0 : createRequest.getDidDocument().getVerificationMethod() == null ? 0 : createRequest.getDidDocument().getVerificationMethod().size();
        int secretVerificationMethods = registrarRequest.getSecret() == null ? 0 : registrarRequest.getSecret().getVerificationMethod() == null ? 0 : registrarRequest.getSecret().getVerificationMethod().size();
        int signingResponses = registrarRequest.getSecret() == null ? 0 : registrarRequest.getSecret().getSigningResponse() == null ? 0 : registrarRequest.getSecret().getSigningResponse().size();
        int decryptionResponses = registrarRequest.getSecret() == null ? 0 : registrarRequest.getSecret().getDecryptionResponse() == null ? 0 : registrarRequest.getSecret().getDecryptionResponse().size();
        return registrarRequest.getClass().getSimpleName() + ": jobId=" + registrarRequest.getJobId() + " / " + didDocumentVerificationMethods + " DID document verification methods / " + secretVerificationMethods + " secret verification methods / " + signingResponses + " signing responses / " + decryptionResponses + " decryption responses";
    }

    private static String constructInterpretedString(RegistrarState registrarState) {
        int verificationMethodTemplates = registrarState.getDidState() == null ? 0 : ! (registrarState.getDidState() instanceof DidStateAction didStateAction) ? 0 : didStateAction.getVerificationMethodTemplate() == null ? 0 : didStateAction.getVerificationMethodTemplate().size();
        int signingRequests = registrarState.getDidState() == null ? 0 : ! (registrarState.getDidState() instanceof DidStateAction didStateAction) ? 0 : didStateAction.getSigningRequest() == null ? 0 : didStateAction.getSigningRequest().size();
        int decryptionRequests = registrarState.getDidState() == null ? 0 : ! (registrarState.getDidState() instanceof DidStateAction didStateAction) ? 0 : didStateAction.getDecryptionRequest() == null ? 0 : didStateAction.getDecryptionRequest().size();
        return registrarState.getClass().getSimpleName() + ": jobId=" + registrarState.getJobId() + " / state=" + registrarState.getDidState() + " / " + verificationMethodTemplates + " verification method templates / " + signingRequests + " signing requests / " + decryptionRequests + " decryption requests";
    }

    private static void print(Object object, String interpretedString) {
        if (object == null) {
            System.out.println("(null)  ");
            return;
        }
        String string;
        Formatting.Value formatting = Formatting.getFormatting();
        try {
            if (formatting == Formatting.Value.interpreted) {
                string = interpretedString;
            } else if (formatting == Formatting.Value.pretty) {
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
        if (string != null) {
            System.out.println("(" + object.getClass().getName() + ")");
            System.out.println(string);
        }
    }

    public static void print(HttpHeaders httpHeaders, String prefix) {
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
