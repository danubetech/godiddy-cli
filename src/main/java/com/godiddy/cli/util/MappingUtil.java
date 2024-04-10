package com.godiddy.cli.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;

public class MappingUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ModelMapper modelMapper = new ModelMapper();

    public static uniregistrar.openapi.model.RegistrarRequest map(com.godiddy.api.client.openapi.model.RegistrarRequest registrarRequest) {
        return objectMapper.convertValue(registrarRequest, uniregistrar.openapi.model.RegistrarRequest.class);
    }

    public static com.godiddy.api.client.openapi.model.RegistrarRequest map(uniregistrar.openapi.model.RegistrarRequest registrarRequest) {
        return objectMapper.convertValue(registrarRequest, com.godiddy.api.client.openapi.model.RegistrarRequest.class);
    }

    public static uniregistrar.openapi.model.RegistrarState map(com.godiddy.api.client.openapi.model.RegistrarState registrarState) {
        return objectMapper.convertValue(registrarState, uniregistrar.openapi.model.RegistrarState.class);
    }

    public static com.godiddy.api.client.openapi.model.RegistrarState map(uniregistrar.openapi.model.RegistrarState registrarRequest) {
        return objectMapper.convertValue(registrarRequest, com.godiddy.api.client.openapi.model.RegistrarState.class);
    }
}
