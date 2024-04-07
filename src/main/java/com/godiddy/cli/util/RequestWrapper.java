package com.godiddy.cli.util;

import com.godiddy.api.client.openapi.model.CreateRequest;
import com.godiddy.api.client.openapi.model.DeactivateRequest;
import com.godiddy.api.client.openapi.model.RequestSecret;
import com.godiddy.api.client.openapi.model.UpdateRequest;

public class RequestWrapper {

    private final Object request;

    public RequestWrapper(Object request) {
        this.request = request;
    }

    public Object getWrappedRequest() {
        return this.request;
    }

    public String getJobId() {
        switch (this.request) {
            case CreateRequest createRequest -> {
                return createRequest.getJobId();
            }
            case UpdateRequest updateRequest -> {
                return updateRequest.getJobId();
            }
            case DeactivateRequest deactivateRequest -> {
                return deactivateRequest.getJobId();
            }
            default -> throw new IllegalStateException("Unexpected request class: " + request.getClass().getName());
        }
    }

    public void setJobId(String jobId) {
        switch (this.request) {
            case CreateRequest createRequest -> {
                createRequest.setJobId(jobId);
            }
            case UpdateRequest updateRequest -> {
                updateRequest.setJobId(jobId);
            }
            case DeactivateRequest deactivateRequest -> {
                deactivateRequest.setJobId(jobId);
            }
            default -> throw new IllegalStateException("Unexpected request class: " + request.getClass().getName());
        }
    }

    public Object getOptions() {
        switch (this.request) {
            case CreateRequest createRequest -> {
                return createRequest.getOptions();
            }
            case UpdateRequest updateRequest -> {
                return updateRequest.getOptions();
            }
            case DeactivateRequest deactivateRequest -> {
                return deactivateRequest.getOptions();
            }
            default -> throw new IllegalStateException("Unexpected request class: " + request.getClass().getName());
        }
    }

    public void setOptions(Object options) {
        switch (this.request) {
            case CreateRequest createRequest -> {
                createRequest.setOptions(options);
            }
            case UpdateRequest updateRequest -> {
                updateRequest.setOptions(options);
            }
            case DeactivateRequest deactivateRequest -> {
                deactivateRequest.setOptions(options);
            }
            default -> throw new IllegalStateException("Unexpected request class: " + request.getClass().getName());
        }
    }

    public RequestSecret getSecret() {
        switch (this.request) {
            case CreateRequest createRequest -> {
                return createRequest.getSecret();
            }
            case UpdateRequest updateRequest -> {
                return updateRequest.getSecret();
            }
            case DeactivateRequest deactivateRequest -> {
                return deactivateRequest.getSecret();
            }
            default -> throw new IllegalStateException("Unexpected request class: " + request.getClass().getName());
        }
    }

    public void setSecret(RequestSecret secret) {
        switch (this.request) {
            case CreateRequest createRequest -> {
                createRequest.setSecret(secret);
            }
            case UpdateRequest updateRequest -> {
                updateRequest.setSecret(secret);
            }
            case DeactivateRequest deactivateRequest -> {
                deactivateRequest.setSecret(secret);
            }
            default -> throw new IllegalStateException("Unexpected request class: " + request.getClass().getName());
        }
    }
}
