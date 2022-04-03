# cmg-analytics project

This project is build with Micronaut.

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

## Running/Building Locally

### Dev Run

When building locally via a `./mvnw build` or a micronaut run `./mvnw mn:run` you need to have postgres available.

It assumes postgresql is available at `localhost:8432`.

There's a `docker-compose.yml` for running that database.

```shell
docker-compose up db -d
./mvnw mn:run
```

### Local Build/Test

It is now using Test Containers, which will use a local docker daemon to spin up postgresql up for you.
We use `flyway` to configure the database and then generate the JOOQ sources via the database.
To ensure a proper order, we have a custom script via the Groovy Maven plugin. 
This is copied from here: https://github.com/testcontainers/testcontainers-java/issues/4397

```xml
<plugin>
    <groupId>org.codehaus.gmaven</groupId>
    <artifactId>groovy-maven-plugin</artifactId>
    <version>2.1.1</version>
    <executions>
      <execution>
        <phase>initialize</phase>
        <goals>
          <goal>execute</goal>
        </goals>
        <configuration>
          <source>
            db = new org.testcontainers.containers.PostgreSQLContainer("postgres:12")
                    .withUsername("${db.username}")
                    .withDatabaseName("cmg")
                    .withPassword("${db.password}");
            db.start();
            project.properties.setProperty('db.url', db.getJdbcUrl());
          </source>
        </configuration>
      </execution>
    </executions>
    <dependencies>
      <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>postgresql</artifactId>
        <version>${testcontainers.version}</version>
      </dependency>
      <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.1</version>
      </dependency>
    </dependencies>
</plugin>
```

### Multi-arch Multi-stage Native Build with Test Containers

Building a Native Graal Image is cumbersome and can require get some local setup.
We can also do so via Docker Multi-stage build, so can skip the setup.

Unfortunately, once you run such a build, you don't have a docker daemon to talk to.
In order to have this re-usable in other places, such as Kubernetes, we run a Docker DIND container.

```shell
docker run --rm --tty --name dockerdind -p 2375:2375 -e DOCKER_TLS_CERTDIR='' --privileged docker:20.10.13-dind  
```

We need the IP of this container, so our multi-stage build can talk to the daemon.

```shell
export DIND_IP=$(docker inspect dockerdind --format='{{.NetworkSettings.Networks.bridge.IPAddress}}')
```

We can then issue a multi-arch multi-stage build:

```shell
docker buildx build . --platform linux/amd64,linux/arm64 --tag caladreas/cmg-analytics:0.2.12 --file Dockerfile.multi-stage --build-arg DIND_IP="${DIND_IP}" --push
```

Sometimes this fails. 
The only solution I've seen so far, is to first build the `--platform linux/amd64` version, then build both again.