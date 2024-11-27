# godiddy-cli

A command-line interface for Godiddy.

## Run using Docker

```shell
docker pull godiddy/godiddy-cli
docker run -it godiddy/godiddy-cli
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

