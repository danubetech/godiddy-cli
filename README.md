# godiddy-cli

A command-line interface for [Godiddy](https://godiddy.com/), and any standards-compliant DID Resolver and DID Registrar service.

## Run using Docker

Download Docker image:

```shell
docker compose pull
```

Verify if installation was successful:

```shell
docker compose run --rm godiddy-cli --version
docker compose run --rm godiddy-cli --help
```

## Run on Ubuntu/Debian

Visit the [Releases page](https://github.com/danubetech/godiddy-cli/releases) and download the
`.deb` file for the latest version, or use the command line:

```bash
VERSION=$(curl -s https://api.github.com/repos/danubetech/godiddy-cli/releases/latest | grep "tag_name" | cut -d '"' -f 4)
curl -LO "https://github.com/danubetech/godiddy-cli/releases/download/${VERSION}/godiddy-cli_${VERSION}_amd64.deb"
sudo dpkg -i godiddy-cli_${VERSION}_amd64.deb
```

Verify if installation was successful:

```bash
godiddy-cli --version
godiddy-cli --help
```

## Setup

```shell
# Set API endpoint to Godiddy (default)
godiddy-cli config endpoint godiddy

# Set your API key
godiddy-cli config apikey b082c420-df67-4b06-899c-b7c51d75fba0
```

To use local Universal Resolver or Universal Registrar:

```shell
# Set API endpoint to local Universal Resolver
godiddy-cli config endpoint http://localhost:8080/1.0/
```

```shell
# Set API endpoint to local Universal Registrar
godiddy-cli config endpoint http://localhost:9080/1.0/
```

### Simple examples

```shell
# Simple resolution
godiddy-cli resolve did:ebsi:z24ipYA2KhjNLmuD52evuCh2

# Simple create
godiddy-cli create -m key

# Create with an option
godiddy-cli create -m key -o keyType=P-256

# Create with client-managed secret mode
godiddy-cli create -c -m key -o keyType=P-256

# Create with client-managed secret mode and interactive
godiddy-cli create -c -i -m key -o keyType=P-256
```

### Interactive

If the `-i` option is given in various commands, responses from the API (especially "action" states in client-managed secret mode) have
to be processed "manually" using additional commands.

```shell
# Process current local state
godiddy-cli state process

# Continue with ongoing job (interactive)
godiddy-cli continue -i
```

### Key Management (KMS)

By default, internal secret mode is used, i.e. the Godiddy Wallet Service.

If the `-c` option is given in various commands, client-managed secret mode is used.
The `-c` option is equivalent to `-o clientSecretMode=true`.

```shell
# Configure KMS (local wallet)
godiddy-cli config kms local

# Configure KMS (Wallet Service)
godiddy-cli config kms wallet_service
godiddy-cli config walletservice base http://localhost:12080/wallet-service/1.0.0

# Configure KMS (Hashicorp Vault)
godiddy-cli config kms hashicorp_vault
godiddy-cli config vaultendpoint http://172.17.0.1:8200/
godiddy-cli config vaulttoken hvs.<..token..>

# Show DIDs in the KMS
godiddy-cli kms controllers

# Show keys in the KMS
godiddy-cli kms keys

# Delete DIDs and keys in the KMS (be careful!)
godiddy-cli kms delete
```

Also see https://docs.godiddy.com/apis/universal-registrar/registrar-configuration.

### Additional key generation

The `-rvmi`, `-rvmt`, `-rvmp` options can be used to add additional keys to a DID document, i.e. keys that are not required by the DID method itself.

```shell
# Create a DID with an additional key
godiddy-cli create -m web -rvmi '#key-1' -rvmt 'Ed25519VerificationKey2020' -rvmp '["authentication", "assertionMethod"]'
```

```shell
# Update a DID with an additional key
godiddy-cli update -d did:example:123 --diddocop 'addToDidDocument' -rvmi '#key-3' -rvmt 'Ed25519VerificationKey2020' -rvmp '["authentication", "assertionMethod"]'
```

Also see https://docs.godiddy.com/apis/universal-registrar/additional-key-generation.

## More examples

See here for more examples:

- `did:web` examples - [./docs/did-web.md](./docs/did-web.md)
- `did:cheqd` examples - [./docs/did-cheqd.md](./docs/did-cheqd.md)
- `did:btcr2` examples - [./docs/did-btcr2.md](./docs/did-btcr2.md)
- `did:ebsi` examples - [./docs/did-ebsi.md](./docs/did-ebsi.md)
