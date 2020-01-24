# cmg-analytics project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application is packageable using `./mvnw package`.
It produces the executable `cmg-analytics-1.0.0-SNAPSHOT-runner.jar` file in `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/cmg-analytics-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or you can use Docker to build the native executable using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your binary: `./target/cmg-analytics-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide .

## Map Generation Requests

* requestId
* generationCount
* duration
* parameters
* mapType
* gameType
* timestamp
* host
* userAgent 

```json
{"time":"2020-01-02T16:29:55.146683937Z","id":"33789a6e-83c4-4968-99cf-7d03164c5701","remote_ip":"80.127.235.6","host":"catan-map-generator.herokuapp.com","method":"GET","uri":"/api/map/code","user_agent":"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:71.0) Gecko/20100101 Firefox/71.0","status":200,"error":"","latency":227652659,"latency_human":"227.652659ms","bytes_in":0,"bytes_out":73}
```