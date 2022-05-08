## Начало работы с ReactiveMongo для Spring Data

* `Tutorial`: https://dev.to/iuriimednikov/how-to-build-custom-queries-with-spring-data-reactive-mongodb-1802
* `Repo`: https://github.com/mednikoviurii/spring-reactive-examples

## Posts

In this series are already published following posts:

* [Custom update queries with Reactive MongoDB in Spring](https://www.andreevi.ch/custom-update-queries-with-reactive-mongodb-for-spring/)
* [Unit testing of Spring Boot services with Project Reactor and Mockito](https://andreevi.ch/unit-testing-of-spring-services-reactor-test/)

## How to use

TBD

## Installation instructions

- `Java 11`
- `collection = "users"`: User
- `WebRouter -> PersonHandler`: PersonApiTest
- `PersonServiceImpl -> PersonRepository -> ReactiveCrudRepository`: PersonServiceImplTest
- `CustomUserRepositoryImpl -> ReactiveMongoTemplate`: UserServiceImplTest
- **(** `PostClientImpl -> WebClient`: PostClientImplTest **)**
