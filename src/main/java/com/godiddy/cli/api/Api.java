package com.godiddy.cli.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godiddy.api.client.ApiClient;
import com.godiddy.api.client.swagger.api.*;

import java.util.Map;

public class Api {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ApiClient apiClient() {
        ApiClient apiClient = new ApiClient();
        String apiKey = ApiKey.getApiKey();
        String endpoint = Endpoint.getEndpoint();
        if (endpoint.endsWith("/")) endpoint = endpoint.substring(0, endpoint.length()-1);
        apiClient.updateBaseUri(endpoint);
        apiClient.setRequestInterceptor(x -> x.header("Authorization", "Bearer " + apiKey));
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

    public static String toJson(Object object, boolean pretty) {
        try {
            if (pretty) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            } else {
                return objectMapper.writeValueAsString(object);
            }
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
