# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
mvn verify                    # Full build + tests + JaCoCo coverage
mvn test                      # Run all unit tests
mvn test -pl <module>         # Run tests for a specific module
mvn test -Dtest=<TestClass>   # Run a single test class
mvn test -Dtest=<TestClass>#<method>  # Run a single test method
mvn verify -DskipTests        # Build without tests
mvn spotless:apply            # Apply code formatting (if Spotless is configured)
```

## Architecture

The codebase follows **Clean Architecture** with this package layout:

```
src/main/java/<base>/
  domain/       # Entities, value objects, aggregates — no framework dependencies
  services/     # Domain services, business rules
  application/  # Use cases, application services, ports (interfaces)
  infrastructure/  # Adapters: repositories, HTTP clients (Telegram, Google), persistence
  entrypoint/   # Controllers, REST endpoints
  utils/        # Shared utilities
```

**Dependency rule:** outer layers depend on inner layers, never the reverse. `domain` must have zero framework imports.

## Technology Stack

- **Java 21** — prefer sealed classes, pattern matching, switch expressions, records, virtual threads (when appropriate)
- **Maven** (pom.xml)
- **Spring Boot 3+** with Spring Security, OAuth2 Client
- **Lombok** — only `@Builder`, `@Slf4j`, `@RequiredArgsConstructor`; avoid `@Data` and blanket `@Setter`
- **Tests:** JUnit 5, Mockito, AssertJ, Testcontainers (for integration tests)
- **JaCoCo** — minimum 90% line coverage; 100% for critical services and business rules
- **HTTP:** WebClient (not RestTemplate) for external integrations (Telegram, Google OAuth)

## Key Conventions

### Records by default

Use `record` for immutable data carriers: DTOs, request/response objects, events, commands. Only use classes when there is complex business logic or mutability is required.

### TDD workflow (required)

1. Write the failing test first
2. Implement the minimum to pass
3. Refactor
4. Confirm test passes and coverage threshold is met
5. Never write implementation before a test exists

### Test structure

- **Unit tests** are mandatory for: services, use cases, validators, mappers, strategies
- **Integration tests** are mandatory for: repositories, HTTP integrations, authentication flows, Telegram API, Google OAuth
- Follow Arrange/Act/Assert; use descriptive names; test behavior, not implementation
- Never mock what you don't own unnecessarily; no fragile tests; no ordering dependencies

### Naming

- Methods: explicit and action-oriented (`findUserByEmail()`, not `doUser()`)
- Variables: descriptive (`String userEmail`, never `String x`)

### Error handling

- Throw specific (domain) exceptions, never catch generic `Exception` without reason
- Global exception handler for HTTP APIs; standardized response format
- Structured logging with `@Slf4j`; never log secrets, tokens, or credentials

### External integrations

- **Google OAuth:** use Spring Security OAuth2 Client + JWT validation; always validate issuer, expiration, and audience
- **Telegram API:** decoupled client with retry policy, timeout, and error handling; use WebClient

### Commits

Follow Conventional Commits (`feat:`, `fix:`, `test:`, `refactor:`, `docs:`, `chore:`).

### Final checklist per task

- Tests pass
- JaCoCo above 90% threshold
- No relevant warnings
- No duplicated code
- Architecture layers respected
- TDD flow followed
