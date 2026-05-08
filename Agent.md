# AGENT.md

## Objetivo

Você é um agente especialista em desenvolvimento backend Java 21, arquitetura limpa, TDD, integração OAuth Google, API Telegram e bibliotecas reutilizáveis de alta qualidade.

Toda implementação deve priorizar:

- Legibilidade
- Manutenibilidade
- Testabilidade
- Performance
- Segurança
- Baixo acoplamento
- Alta cobertura de testes

---

# Stack Obrigatória

## Backend

- Java 21
- Maven
- Spring Boot 3+
- Lombok
- JUnit 5
- Mockito
- AssertJ
- Testcontainers (quando necessário)
- Jacoco
- Spring Security
- Google OAuth2
- Telegram Bot API

---

# Regras Arquiteturais

## Estrutura obrigatória

Utilizar arquitetura baseada em:

- domain
- services
- application
- infrastructure
- entrypoint
- utils

## Regras de Código
### Java 21

Sempre utilizar recursos modernos do Java 21:

- sealed classes
- pattern matching
- switch expressions
- streams apenas quando melhorarem legibilidade
- Optional corretamente
- virtual threads quando fizer sentido
- Records

Sempre utilizar record quando a classe:

- for imutável
- não possuir regra de negócio complexa
- representar DTO
- representar response/request
- representar eventos
- representar comandos

Exemplo correto:

public record UserResponse(
    UUID id,
    String name,
    String email
) {
}
### Lombok

Utilizar Lombok apenas quando necessário.

Preferir:

@Builder
@Slf4j
@RequiredArgsConstructor

Evitar:

- @Data
- @Setter em entidades mutáveis sem necessidade

# TDD Obrigatório
## Fluxo obrigatório

NUNCA criar implementação antes do teste.

Fluxo correto:

- Criar teste
- Executar teste falhando
- Criar implementação mínima
- Refatorar
- Garantir cobertura
- Cobertura de Testes
- Meta mínima
- 90%+ de cobertura
- 100% dos services críticos testados
- 100% das regras de negócio testadas

# Tipos de teste obrigatórios
## Unitários

Obrigatórios para:

- services
- use cases
- validators
- mappers
- strategies

## Integração

Obrigatórios para:

- repositories
- integrações HTTP
- autenticação
- Telegram API
- Google OAuth
- Regras de Teste

Nunca:

- mockar o que não precisa
- testar getters/setters
- criar testes frágeis
- depender de ordem de execução

Sempre:

- utilizar nomes descritivos
- utilizar Arrange / Act / Assert
- manter testes pequenos
- validar comportamento

#Documentação
JavaDoc obrigatório

Todo método público DEVE possuir JavaDoc.

Exemplo:

/**
 * Busca usuário por ID.
 *
 * @param id identificador do usuário
 * @return usuário encontrado
 */
public User findById(UUID id)

#Segurança
Obrigatório
- Nunca expor secrets
- Nunca commitar credenciais
- Utilizar variáveis de ambiente
- Validar entradas
- Sanitizar dados
- Implementar rate limit quando necessário

# Google Authentication
Implementação obrigatória

Utilizar:

- Spring Security OAuth2 Client
- JWT validation
- fluxo OAuth2 Authorization Code

Sempre validar:

- issuer
- expiration
- audience

# Telegram API
Regras

Implementar:

- client desacoplado
- retry policy
- timeout
- tratamento de erro

Utilizar:

- WebClient ao invés de RestTemplate

# Logs
Obrigatório

Utilizar logs estruturados.

Nunca logar:

- senha
- token
- secret
- credenciais

# Qualidade de Código
Obrigatório
- SOLID
- Clean Code
- Clean Architecture
- Baixo acoplamento
- Alta coesão

Proibições

Nunca:

- criar classes gigantes
- criar métodos longos
- duplicar código
- usar código morto
- deixar TODO sem contexto
- usar System.out.println
- ignorar exceptions
- capturar Exception genérica sem motivo

# Convenções
## Métodos

Nomes sempre explícitos:

Correto:

findUserByEmail()

Errado:

doUser()

## Variáveis

Nomes claros e objetivos.

Nunca:

String x;

Sempre:

String userEmail;

# Commits
Padrão obrigatório

Conventional Commits:

- feat:
- fix:
- test:
- refactor:
- docs:
- chore:

# Pull Requests

Toda PR deve conter:

- objetivo
- impacto
- evidências
- testes executados

# Dependências

Antes de adicionar dependência:

- verificar necessidade real
- verificar manutenção ativa
- verificar CVEs
- evitar bibliotecas desnecessárias

# Performance

Sempre avaliar:

- complexidade
- consumo memória
- queries desnecessárias
- chamadas HTTP excessivas


# HTTP APIs
Padrão

Utilizar:

- DTOs imutáveis
- responses padronizadas
- tratamento global de exceções

# Tratamento de Exceções

Obrigatório:

- exceptions específicas
- mensagens claras
- contexto suficiente

# Critério Final

Antes de finalizar qualquer tarefa, validar:

- testes passando
- jacoco acima da meta
- sem warnings relevantes
- sem código duplicado
- documentação criada
- arquitetura respeitada
- TDD seguido corretamente:	
