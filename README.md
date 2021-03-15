# Reactive Microservice

## Introduction
A reactive microservice is independent and can be deployed and updated at their own pace. They also interact using a lightweight protocol (HTTP) and are loosely coupled. They have the below characteristics:
* Autonomous - They adapt to availability/unavailability of the surrounding services. They can handle failures locally, act independently and coordinate with other services.
* Asynchronous - They use asynchronous message-passing to interact with its peers. They also receive messages and can respond to them.
* Resilient - Asynchronous message-passing help microservices to face failures, adapt and prevent propagation of the failure by isolating it from the whole system. It not only manages failures but also implement recovery compensation strategies.
* Elastic - They are elastic to manage the load across the instances by applying various constraints to avoid in-memory state, sharing state between instances if required, or are able to route messages to the same instances for stateful services.


## Vert.x
Vert.x is a toolkit for building reactive and distributed systems using an asynchronous nonblocking development model. Vert.x is very flexible. It can be used as a standalone  application or embedded in a larger one.

Command to run the application:
```
mvn compile vertx:run
```

The code is based on the book '*Building Reactive Microservices in Java*'.
