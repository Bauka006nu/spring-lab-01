# Lab 12 evidence

Student: Nurstan Bauyrkhan, group 2411 - variant 5 (MemberService, a membership that expired yesterday, fixed Clock bean).
Branch: lab12.

## 1. Build result

Command: `./mvnw clean verify`

```
<paste the "Tests run: ..., Failures: 0, Errors: 0, Skipped: ..." summary line here>
```

## 2. Coverage of kz.iitu.springlab.membership

| | Line | Branch |
|---|---|---|
| Before the extra test | __ % | __ % |
| After the extra test  | __ % | __ % |

The yellow line was `LocalDate base = member.expiresOn().isBefore(today) ? today : member.expiresOn();`
in `MemberService.renew`: the four tests only renewed an active membership.
The extra test `renewingAnExpiredMembershipStartsFromToday` takes the other way through the condition.

Screenshot / excerpt of the class view (yellow before, green after): <insert here>

## 3. A branch that is still uncovered

The `EXPIRES_TODAY` branch of `statusOf` is not covered. It should be tested: a membership that ends today is a
boundary case, and a wrong comparison (`isBefore` vs `isEqual`) would put the whole day on the wrong side.

## 4. Docker was not available

Docker is not installed on my machine. The command and the exact error:

```
PS C:\work\spring-lab-01> docker info
docker : Имя "docker" не распознано как имя командлета, функции, файла сценария или выполняемой программы. ...
CategoryInfo          : ObjectNotFound: (docker:String) [], CommandNotFoundException
```

`PostgresIntegrationTest` is written and compiles; it is annotated `@Testcontainers(disabledWithoutDocker = true)`,
so without Docker it is reported as skipped (not failed) and the build stays green.

What this test would prove that `@DataJpaTest` does not:

- `@DataJpaTest` replaces the database with an embedded H2 and loads only the JPA slice. The Flyway migrations
  V1-V5 would run on H2, not on the engine used in production, so differences in SQL dialect, types, identity
  columns and name case would stay hidden.
- The container test starts the complete application (security filter chain, MVC, JPA, Flyway) against a real
  PostgreSQL, applies all migrations to an empty database and then checks over real HTTP that an anonymous request
  is redirected to the login form.

H2 is used in the `@DataJpaTest` slice (it is the embedded database of the slice), not as the integration database.
