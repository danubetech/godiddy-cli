# godiddy-cli

A command-line interface for Godiddy.

## Run using Docker

```shell
docker compose pull
docker compose run --rm godiddy-cli
```

## Build manually

```shell
mvn -s settings.xml clean package
```

## Run manually

Get command line

```shell
java -jar target/cli-*-jar-with-dependencies.jar -h
```

Set endpoint

