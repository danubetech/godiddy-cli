package com.godiddy.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godiddy.api.client.openapi.model.CreateRequest;
import picocli.CommandLine;
import uniregistrar.openapi.model.RegistrarRequest;

import java.util.Map;
import java.util.concurrent.Callable;

public abstract class Main {

    private static final String INPUT =
    """
{
  "didDocument" : {
    "verificationMethod" : [ {
      "type" : "JsonWebKey2020",
      "id" : "#signingKey",
      "publicKeyJwk" : {
        "kid" : "#signingKey",
        "kty" : "OKP",
        "crv" : "Ed25519",
        "x" : "SHcjHnPWZWYgqt_Y8djkuArv55zERFssvCoC8UthCFc"
      }
    } ],
    "assertionMethod" : [ "#signingKey" ],
    "@context" : [ "https://www.w3.org/ns/did/v1", "https://w3id.org/security/suites/jws-2020/v1" ],
    "authentication" : [ "#signingKey" ]
  },
  "options" : {
    "clientSecretMode" : true,
    "requestVerificationMethod" : [ {
      "id" : "#signingKey",
      "type" : "Ed25519VerificationKey2018",
      "purpose" : [ "authentication", "assertionMethod" ]
    } ],
    "network" : "testnet"
  },
  "secret" : {
    "verificationMethod" : [ {
      "publicKeyJwk" : {
        "kty" : "EC",
        "crv" : "secp256k1",
        "x" : "CMJrvDjT2SFZx7FT5MpXvZiZOKVtsEhtHu1xFhwovWw",
        "y" : "CV6yY3gwzQPf0idO4s_EOcDq3g4KPKXSkTG0MivkU0o"
      },
      "type" : "JsonWebKey2020",
      "purpose" : [ "update" ]
    }, {
      "publicKeyJwk" : {
        "kty" : "EC",
        "crv" : "secp256k1",
        "x" : "BZko1JeUbPtSI6RCKWxGhlW4yM34lC2kP2Y2VNitFzk",
        "y" : "qsN3Kt6I3x1jVG2VsJn_3r-Epq2nER-BULSQPvIotx0"
      },
      "type" : "JsonWebKey2020",
      "purpose" : [ "recovery" ]
    } ]
  }
}

""";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws JsonProcessingException {

        CreateRequest createRequest = objectMapper.readValue(INPUT, CreateRequest.class);
        Object nextRequest = objectMapper.convertValue(objectMapper.convertValue(createRequest, Map.class), createRequest.getClass());
        System.out.println(objectMapper.readValue(INPUT, CreateRequest.class));
    }
}
