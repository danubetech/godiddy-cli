# did-cli - did:btcr2 examples

Also see https://youtu.be/yu_jIu0Wg-w

```shell
# Create a DID
did-cli create -c -m btcr2 -n mutinynet
```

```shell
# Resolve a DID
did-cli resolve did:btcr2:k1q5ppmnfjqp0qe5klmnll9tazz9jd5ds43x5xfsr3hu9jdgaldu0d3jgs0vj4r
```

```shell
# Resolve a DID with sidecar data
did-cli resolve -p '{"sidecar":{"updates":[{"@context":["https://btcr2.dev/context/v1","https://w3id.org/json-ld-patch/v1","https://w3id.org/zcap/v1","https://w3id.org/security/data-integrity/v2"],"patch":[{"op":"add","path":"/service/3","value":{"id":"#didcomm","type":"DIDCommMessaging","serviceEndpoint":"http://example.com/didcomm/"}}],"sourceHash":"osaUrsl3XhLlm-J4hKrqmxP6G0y9sfqseaF4HPPUe_8","targetHash":"eS9wA7p1qiQHGEuyz4swjnZNnFz9VkBSPff6i6XeP_M","targetVersionId":2,"proof":{"type":"DataIntegrityProof","cryptosuite":"bip340-jcs-2025","verificationMethod":"did:btcr2:k1q5p7drc8y5hhmvs2nncyuq73ts98arnqv5ce446vwydafuu2mp9rp6szethjk#initialKey","proofPurpose":"capabilityInvocation","capability":"urn:zcap:root:did%3Abtcr2%3Ak1q5p7drc8y5hhmvs2nncyuq73ts98arnqv5ce446vwydafuu2mp9rp6szethjk","capabilityAction":"Write","proofValue":"z5DhhjMk8gXULWNKRwqJqGbqZF94ov7zjKS6YkaCGxoDa2f3WT3zprMUc1p62w2cRRdkNAhR4rfGJpxZZFfHsdrJB"}}]}}' did:btcr2:k1q5p7drc8y5hhmvs2nncyuq73ts98arnqv5ce446vwydafuu2mp9rp6szethjk
```
