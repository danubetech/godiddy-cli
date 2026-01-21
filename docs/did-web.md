# godiddy-cli - did:web examples

Also see https://docs.godiddy.com/apis/universal-registrar/additional-key-generation

```shell
# Create a DID with an additional key
godiddy-cli create -c -m web -rvmi '#key-1' -rvmt 'Ed25519VerificationKey2020' -rvmp '["authentication", "assertionMethod"]'
```
    
```shell
# Update a DID with an additional key
godiddy-cli update -c -d did:example:123 --diddocop 'addToDidDocument' -rvmi '#key-3' -rvmt 'Ed25519VerificationKey2020' -rvmp '["authentication", "assertionMethod"]'
```
