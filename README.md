# cmg-analytics project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Package and running the application

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

## Build With GitHub Actions

* [GitHub Actions](https://github.com/features/actions)
* [GitHub Actions Awesome List](https://github.com/sdras/awesome-actions)

## Slow Test On MacOS

At some point in time, my test runs were taking minutes (over 5 minutes in some cases).

Turns out, it was a [MacOS problem](https://docs.micronaut.io/latest/guide/index.html#_it_is_taking_much_longer_to_start_my_application_than_it_should_macos) related Hostname.

I made two changes, one, in the `/etc/hosts` I've added my *hostname* to the mapping:

```bash
127.0.0.1       localhost <hostname>
::1             localhost <hostname>
```

Replacing `<hostname>` with the response of the this command:

```bash
hostname
```

Second thing, was to set the *hostname* and *port* of my tests by adding these properties to the maven build.

```bash
-Dmicronaut.server.port=-1 -Dmicronaut.server.host=localhost
```

Setting the server port to `-1` means that Micronaut will pick a random port.
Very useful when running `@Micronaut` tests.

## Authentication

For more info on how to do this with `httpie` and `curl`, [see this blog post](https://www.ctl.io/developers/blog/post/curl-vs-httpie-http-apis).

Retrieve the bearer token (using default config):

```shell
http POST :8081/login 'username=test' 'password=test'
```

```shell
HTTP/1.1 200 OK
Content-Type: application/json
connection: keep-alive
content-length: 274
date: Thu, 17 Mar 2022 20:55:33 GMT

{
    "access_token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwibmJmIjoxNjQ3NTUwNTMzLCJyb2xlcyI6W10sImlzcyI6ImNtZy1hbmFseXRpY3MyIiwiZXhwIjoxNjQ3NTU0MTMzLCJpYXQiOjE2NDc1NTA1MzN9.lSCepcfNEPgGUPvJp2gd5kozTaZo3g0bInJNUBDeOZg",
    "expires_in": 3600,
    "token_type": "Bearer",
    "username": "test"
}
```

Then use that bearer token, to access the other resources:

```shell
http :8081/hello 'Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwibmJmIjoxNjQ3NTUwNTMzLCJyb2xlcyI6W10sImlzcyI6ImNtZy1hbmFseX
RpY3MyIiwiZXhwIjoxNjQ3NTU0MTMzLCJpYXQiOjE2NDc1NTA1MzN9.lSCepcfNEPgGUPvJp2gd5kozTaZo3g0bInJNUBDeOZg'
```

```shell
HTTP/1.1 200 OK
Content-Type: text/plain
connection: keep-alive
content-length: 10
date: Thu, 17 Mar 2022 20:56:45 GMT

Hello test
```

### Using a Session

```shell
http POST :8082/login 'username=test' 'password=test' | jq .access_token
```

```shell
http --session=s1 :8082/hello 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwibmJmIjoxNjQ3NjkwODgwLCJyb2xlcyI6W10sImlzcyI6ImNtZy1hbmFseXRpY3MyIiwiZXhwIjoxNjQ3Njk0NDgwLCJpYXQiOjE2NDc2OTA4ODB9.9sVBnkA7TZpbjxs4sIHw8uYd5I64PkmCNBr12zTG0J8'
```

```shell
http --session=s1 :8082/hello
```

### Create New Entry

```shell
http --session=s1 GET :8082/generationRequest
```

```shell
http --session=s1 POST :8082/generationRequest

* requestId
* generationCount
* duration
* parameters
* mapType
* gameType
* timestamp
* host
* userAgent

```