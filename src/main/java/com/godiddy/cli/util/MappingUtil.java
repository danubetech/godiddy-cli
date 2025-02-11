package com.godiddy.cli.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godiddy.api.client.openapi.model.*;

public class MappingUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static uniregistrar.openapi.model.RegistrarRequest map(RegistrarRequest registrarRequest) {
        return switch (registrarRequest) {
            case CreateRequest ignored -> objectMapper.convertValue(registrarRequest, uniregistrar.openapi.model.CreateRequest.class);
            case UpdateRequest ignored -> objectMapper.convertValue(registrarRequest, uniregistrar.openapi.model.UpdateRequest.class);
            case DeactivateRequest ignored -> objectMapper.convertValue(registrarRequest, uniregistrar.openapi.model.DeactivateRequest.class);
            case ExecuteRequest ignored -> objectMapper.convertValue(registrarRequest, uniregistrar.openapi.model.ExecuteRequest.class);
            case CreateResourceRequest ignored -> objectMapper.convertValue(registrarRequest, uniregistrar.openapi.model.CreateResourceRequest.class);
            case UpdateResourceRequest ignored -> objectMapper.convertValue(registrarRequest, uniregistrar.openapi.model.UpdateResourceRequest.class);
            case DeactivateResourceRequest ignored -> objectMapper.convertValue(registrarRequest, uniregistrar.openapi.model.DeactivateResourceRequest.class);
            case null -> null;
            default -> objectMapper.convertValue(registrarRequest, uniregistrar.openapi.model.RegistrarRequest.class);
        };
    }

    public static RegistrarRequest map(uniregistrar.openapi.model.RegistrarRequest registrarRequest) {
        return switch (registrarRequest) {
            case uniregistrar.openapi.model.CreateRequest ignored -> objectMapper.convertValue(registrarRequest, CreateRequest.class);
            case uniregistrar.openapi.model.UpdateRequest ignored -> objectMapper.convertValue(registrarRequest, UpdateRequest.class);
            case uniregistrar.openapi.model.DeactivateRequest ignored -> objectMapper.convertValue(registrarRequest, DeactivateRequest.class);
            case uniregistrar.openapi.model.ExecuteRequest ignored -> objectMapper.convertValue(registrarRequest, ExecuteRequest.class);
            case uniregistrar.openapi.model.CreateResourceRequest ignored -> objectMapper.convertValue(registrarRequest, CreateResourceRequest.class);
            case uniregistrar.openapi.model.UpdateResourceRequest ignored -> objectMapper.convertValue(registrarRequest, UpdateResourceRequest.class);
            case uniregistrar.openapi.model.DeactivateResourceRequest ignored -> objectMapper.convertValue(registrarRequest, DeactivateResourceRequest.class);
            case null -> null;
            default -> objectMapper.convertValue(registrarRequest, RegistrarRequest.class);
        };
    }

    public static uniregistrar.openapi.model.RegistrarState map(RegistrarState registrarState) {
        return switch (registrarState) {
            case CreateState ignored -> objectMapper.convertValue(registrarState, uniregistrar.openapi.model.CreateState.class);
            case UpdateState ignored -> objectMapper.convertValue(registrarState, uniregistrar.openapi.model.UpdateState.class);
            case DeactivateState ignored -> objectMapper.convertValue(registrarState, uniregistrar.openapi.model.DeactivateState.class);
            case ExecuteState ignored -> objectMapper.convertValue(registrarState, uniregistrar.openapi.model.ExecuteState.class);
            case null -> null;
            default -> objectMapper.convertValue(registrarState, uniregistrar.openapi.model.RegistrarState.class);
        };
    }

    public static RegistrarState map(uniregistrar.openapi.model.RegistrarState registrarState) {
        return switch (registrarState) {
            case uniregistrar.openapi.model.CreateState ignored -> objectMapper.convertValue(registrarState, CreateState.class);
            case uniregistrar.openapi.model.UpdateState ignored -> objectMapper.convertValue(registrarState, UpdateState.class);
            case uniregistrar.openapi.model.DeactivateState ignored -> objectMapper.convertValue(registrarState, DeactivateState.class);
            case uniregistrar.openapi.model.ExecuteState ignored -> objectMapper.convertValue(registrarState, ExecuteState.class);
            case null -> null;
            default -> objectMapper.convertValue(registrarState, RegistrarState.class);
        };
    }

    public static uniregistrar.openapi.model.RegistrarResourceState map(RegistrarResourceState registrarResourceState) {
        return switch (registrarResourceState) {
            case CreateResourceState ignored -> objectMapper.convertValue(registrarResourceState, uniregistrar.openapi.model.CreateResourceState.class);
            case UpdateResourceState ignored -> objectMapper.convertValue(registrarResourceState, uniregistrar.openapi.model.UpdateResourceState.class);
            case DeactivateResourceState ignored -> objectMapper.convertValue(registrarResourceState, uniregistrar.openapi.model.DeactivateResourceState.class);
            case null -> null;
            default -> objectMapper.convertValue(registrarResourceState, uniregistrar.openapi.model.RegistrarResourceState.class);
        };
    }

    public static RegistrarResourceState map(uniregistrar.openapi.model.RegistrarResourceState registrarResourceState) {
        return switch (registrarResourceState) {
            case uniregistrar.openapi.model.CreateResourceState ignored -> objectMapper.convertValue(registrarResourceState, CreateResourceState.class);
            case uniregistrar.openapi.model.UpdateResourceState ignored -> objectMapper.convertValue(registrarResourceState, UpdateResourceState.class);
            case uniregistrar.openapi.model.DeactivateResourceState ignored -> objectMapper.convertValue(registrarResourceState, DeactivateResourceState.class);
            case null -> null;
            default -> objectMapper.convertValue(registrarResourceState, RegistrarResourceState.class);
        };
    }
}
