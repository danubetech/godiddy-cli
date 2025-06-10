# godiddy-cli

A command-line interface for Godiddy.

## Installing on Ubuntu/Debian (via `.deb` package)

To install the latest version of `godiddy-cli` on your Ubuntu server:

1. **Download the latest `.deb` package**

   Visit the [Releases page](https://github.com/danubetech/godiddy-cli/releases) and download the `.deb` file for the latest version, or use the command line:

   ```bash
   VERSION=$(curl -s https://api.github.com/repos/danubetech/godiddy-cli/releases/latest | grep "tag_name" | cut -d '"' -f 4)
   curl -LO "https://github.com/danubetech/godiddy-cli/releases/download/${VERSION}/godiddy-cli_${VERSION}_amd64.deb"
   ```

2. **Install the package**

   ```bash
   sudo dpkg -i godiddy-cli_${VERSION}_amd64.deb
   ```

3. **Fix missing dependencies if needed**

   If you see any dependency issues:

   ```bash
   sudo apt-get install -f
   ```

4. **Verify installation**

   ```bash
   godiddy-cli --version
   ```

## Run using Docker

```shell
docker compose pull
docker compose run --rm godiddy-cli
```

## Examples

### Set endpoint and API key

```
# Set either "godiddy" or "godiddy-dev" API endpoint
docker compose run --rm godiddy-cli config endpoint godiddy

# Set your API key
docker compose run --rm godiddy-cli config apikey b082c420-df67-4b06-899c-b7c51d75fba0
```

### Simple examples

```
# Simple resolution
docker compose run --rm godiddy-cli resolve did:ebsi:z24ipYA2KhjNLmuD52evuCh2

# Simple create
docker compose run --rm godiddy-cli create -m key

# Create with an option
docker compose run --rm godiddy-cli create -m key -o keyType=P-256

# Create with client-managed secret mode
docker compose run --rm godiddy-cli create -m key -c

# Create with client-managed secret mode and interactive
docker compose run --rm godiddy-cli create -m key -c -i
```

### Key Management (KMS)

By default, internal secret mode is used, i.e. the Godiddy wallet service.

If the `-c` option is given in various commands, client-managed secret mode is used. The `-c` option is equivalent to `-o clientSecretMode=true`.

```
# Comfingure KMS (wallet or local)
docker compose run --rm godiddy-cli config kms wallet
docker compose run --rm godiddy-cli config kms local

# Show controllers in the KMS
docker compose run --rm godiddy-cli kms controllers

# Show keys in the wallet service
docker compose run --rm godiddy-cli kms keys
```

### Additional key generation

The `-rvmi`, `-rvmt`, `-rvmp` options can be used to add additional keys to a DID document, i.e. keys that are not required by the DID method itself.

```
# Create a DID with an additional key
docker compose run --rm godiddy-cli create -m web -rvmi '#key-1' -rvmt 'Ed25519VerificationKey2020' -rvmp '["authentication", "assertionMethod"]'
```

```
# Update a DID with an additional key
docker compose run --rm godiddy-cli update -d did:example:123 --diddocop 'addToDidDocument' -rvmi '#key-3' -rvmt 'Ed25519VerificationKey2020' -rvmp '["authentication", "assertionMethod"]'
```

### Interactive

If the `-i` option is given in various commands, responses from the API (especially "action" states in client-managed secret mode) have
to be processed "manually" using additional commands.

```
# Process current local state
docker compose run --rm godiddy-cli state process

# Continue with ongoing job (interactive)
docker compose run --rm godiddy-cli continue -i
```

### did:ebsi examples

Also see https://docs.godiddy.com/apis/universal-registrar/ebsi-execute

```
docker compose run --rm godiddy-cli execute -d did:ebsi:zjUnExsyyweQ9p4cy3nvrVc --op issueOnboardingVC

docker compose run --rm godiddy-cli create -m ebsi -n pilot -c -s VerifiableAuthorisationToOnboard= 

docker compose run --rm godiddy-cli execute -d did:ebsi:zjUnExsyyweQ9p4cy3nvrVc --op inviteForTrustRole --opdata trustModelDid=did:ebsi:zaFsXXoQ4ZrmiEsrtGyMvwJ --opdata trustModelRole=TI 

docker compose run --rm godiddy-cli execute -d did:ebsi:zaFsXXoQ4ZrmiEsrtGyMvwJ --op acceptTrustRole -c -s VerifiableAccreditationToAttest=
```

## Build Java

```shell
mvn -s settings.xml clean package
```

## Run Java

Get command line

```shell
java -jar target/godiddy-cli-*-jar-with-dependencies.jar -h
```
