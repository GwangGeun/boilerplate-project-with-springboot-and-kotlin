# Note to make a boilerplate

## [Architecture]

- [Hexagonal Architecture](https://velog.io/@msung99/Hexagonal-Architecture-%ED%97%A5%EC%82%AC%EA%B3%A0%EB%82%A0-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98%EC%97%90%EC%84%9C-%EC%9C%A0%EC%A6%88%EC%BC%80%EC%9D%B4%EC%8A%A4UserCase-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0)

## [Test code]

### 1. Done
- Unit test & integration test are configured separately
- TestContainer for integration test

### 2. Todo
- Test-Fixtures

## [[Logging]](https://spring.io/blog/2022/10/12/observability-with-spring-boot-3)

### 1. Done

- By adding `spring-boot-starter-web`, we can
  use [logback](https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/html/howto-logging.html#:~:text=Spring%20Boot%20has,logging%20for%20you%3A).
- `logback-access.xml` : TraceID, SpanID are supposed to be generated.
- `logback-spring.xml` : Need to re-visit to clarify commented tag.
- I've not tried to configure `ObservedAspect` ( Reporter, Monitoring ) stuff

### 2. Todo

- Should be able to see the logs in cloudwatch 

## [ETC]

- Add FeignClient ( https://mangkyu.tistory.com/278, https://docs.spring.io/spring-cloud-openfeign/docs/4.0.4/reference/html/ )
- Inject Secret values from somewhere
