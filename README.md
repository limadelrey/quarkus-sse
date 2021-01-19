# Quarkus SSE

The goal of this repository is to provide a Quarkus application that is able to produce server-sent events w/ multiple requirements:
1. SimpleSSE produces events to 1 subscriber;
2. BroadcastSSE produces events to n subscribers;
3. BroadcastWithEventIdSSE produces events to n subscribers while providing latest events if subscriber missed them;
4. BroadcastWithMultiTenancySSE produces events to n subscribers segregating them by different tenants.

# Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

## Prerequisites

```bash
Java 11
Maven 3.6.1
```

# Running

## Build & run Quarkus application
Run with live reload:
```bash
$ mvn compile quarkus:dev
```

## Other informations
Useful endpoints:

- POST http://localhost:8080/api/v1/sse
- GET http://localhost:8080/api/v1/sse
- POST http://localhost:8080/api/v1/broadcast-sse
- GET http://localhost:8080/api/v1/broadcast-sse
- POST http://localhost:8080/api/v1/broadcast-sse-event-id
- GET http://localhost:8080/api/v1/broadcast-sse-event-id
- POST http://localhost:8080/api/v1/broadcast-sse-multi-tenancy
- GET http://localhost:8080/api/v1/broadcast-sse-multi-tenancy

## Built With

* [Java](https://www.java.com/)
* [Maven](https://maven.apache.org/)
* [Quarkus](https://quarkus.io/)
