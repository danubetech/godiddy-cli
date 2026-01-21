# godiddy-cli - did:ebsi examples

Also see https://docs.godiddy.com/apis/universal-registrar/ebsi-execute

```shell
# USing an existing EBSI Trusted Accreditation Organization (TAO), issue a VerifiableAuthorisationToOnboard
godiddy-cli execute -d did:ebsi:zjUnExsyyweQ9p4cy3nvrVc --op issueOnboardingVC
```

```shell
# Using the VerifiableAuthorisationToOnboard, create a new DID
godiddy-cli create -c -m ebsi -n pilot -c -s VerifiableAuthorisationToOnboard=<..data..>
```

```shell
# Using an existing EBSI Trusted Accreditation Organization (TAO), issue a VerifiableAccreditationToAttest
godiddy-cli execute -c -d did:ebsi:zjUnExsyyweQ9p4cy3nvrVc --op inviteForTrustRole --opdata trustRoleDid=<..did..> --opdata trustRoleType=TI
```

```shell
# Using the VerifiableAccreditationToAttest, add the "Trusted Issuer" role to the new DID
godiddy-cli execute -c -d <..did..> --op acceptTrustRole -c -s VerifiableAccreditationToAttest=
```
