# godiddy-cli - did:cheqd examples

```shell
# Create a DID
godiddy-cli create -c -m cheqd -n testnet
```

```shell
# Create a DID URL resource
godiddy-cli resource create -d <..did..> -u /resources/<..uuid..> -o name=myname -o type=mytype --content="SGVsbG8gaG93IGFyZSB5b3UK" -c
```
