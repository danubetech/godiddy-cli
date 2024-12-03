# godiddy-cli

A command-line interface for Godiddy.

## Run using Docker

```shell
docker compose pull
docker compose run --rm godiddy-cli
```

## Examples

### Set endpoint and API key

```
# Set either "godiddy" or "godiddy-dev" API endpoint
docker compose run --rm godiddy-cli config endpoint godiddy-dev

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

### Internal secret mode

By default, internal secret mode is used, i.e. the Godiddy wallet service.

```
# Show controllers in the wallet service
docker compose run --rm godiddy-cli wallet controllers

# Show keys in the wallet service
docker compose run --rm godiddy-cli wallet keys
```

### Client-managed secret mode

If the `-c` option is given in various commands, client-managed secret mode is used. The `-c` option is equivalent to `-o clientSecretMode=true`.

```
# Show the contents of the local wallet
docker compose run --rm godiddy-cli localwallet show
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

Also see https://docs.godiddy.com/advanced/universal-registrar-execute/ebsi-execute.

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
java -jar target/cli-*-jar-with-dependencies.jar -h
```
