package com.godiddy.cli.util;

import org.modelmapper.ModelMapper;

public class MappingUtil {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static uniregistrar.openapi.model.RegistrarRequest map(com.godiddy.api.client.openapi.model.RegistrarRequest registrarRequest) {
        return modelMapper.map(registrarRequest, uniregistrar.openapi.model.RegistrarRequest.class);
    }

    public static com.godiddy.api.client.openapi.model.RegistrarRequest map(uniregistrar.openapi.model.RegistrarRequest registrarRequest) {
        return modelMapper.map(registrarRequest, com.godiddy.api.client.openapi.model.RegistrarRequest.class);
    }

    public static uniregistrar.openapi.model.RegistrarState map(com.godiddy.api.client.openapi.model.RegistrarState registrarState) {
        return modelMapper.map(registrarState, uniregistrar.openapi.model.RegistrarState.class);
    }

    public static com.godiddy.api.client.openapi.model.RegistrarState map(uniregistrar.openapi.model.RegistrarState registrarRequest) {
        return modelMapper.map(registrarRequest, com.godiddy.api.client.openapi.model.RegistrarState.class);
    }
}
