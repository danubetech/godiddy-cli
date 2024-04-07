package com.godiddy.cli.util;

import com.godiddy.api.client.openapi.model.CreateState;
import com.godiddy.api.client.openapi.model.DeactivateState;
import com.godiddy.api.client.openapi.model.DidState;
import com.godiddy.api.client.openapi.model.UpdateState;

public class StateWrapper {

    private final Object state;

    public StateWrapper(Object state) {
        this.state = state;
    }

    public Object getWrappedState() {
        return this.state;
    }

    public String getJobId() {
        switch (this.state) {
            case CreateState createState -> {
                return createState.getJobId();
            }
            case UpdateState updateState -> {
                return updateState.getJobId();
            }
            case DeactivateState deactivateState -> {
                return deactivateState.getJobId();
            }
            default -> throw new IllegalStateException("Unexpected state class: " + state.getClass().getName());
        }
    }

    public DidState getDidState() {
        switch (this.state) {
            case CreateState createState -> {
                return createState.getDidState();
            }
            case UpdateState updateState -> {
                return updateState.getDidState();
            }
            case DeactivateState deactivateState -> {
                return deactivateState.getDidState();
            }
            default -> throw new IllegalStateException("Unexpected state class: " + state.getClass().getName());
        }
    }

    public Object getDidRegistrationMetadata() {
        switch (this.state) {
            case CreateState createState -> {
                return createState.getDidRegistrationMetadata();
            }
            case UpdateState updateState -> {
                return updateState.getDidRegistrationMetadata();
            }
            case DeactivateState deactivateState -> {
                return deactivateState.getDidRegistrationMetadata();
            }
            default -> throw new IllegalStateException("Unexpected state class: " + state.getClass().getName());
        }
    }

    public Object getDidDocumentMetadata() {
        switch (this.state) {
            case CreateState createState -> {
                return createState.getDidDocumentMetadata();
            }
            case UpdateState updateState -> {
                return updateState.getDidDocumentMetadata();
            }
            case DeactivateState deactivateState -> {
                return deactivateState.getDidDocumentMetadata();
            }
            default -> throw new IllegalStateException("Unexpected state class: " + state.getClass().getName());
        }
    }
}
