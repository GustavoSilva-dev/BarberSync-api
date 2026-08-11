# Skills e Convenções - BarberSync API

## Diretrizes Globais
1. Respeite as convenções de código do Java 21 e do Spring Boot 4
2. Não altere a lógica de negócio ou os nomes de variáveis existentes, apenas refatore se necessário

## Regra para DTOs e Validações Bean
- Todo DTO deve ser um Java `record`
- Todas as anotações do Bean Validation (`jakarta.validation.constraints.*`) devem possui o parâmetro `message` para expressar erros
- Cada `message` das anotações do DTO devem possuir uma frase amigável em português explicando o erro no campo, como tamanho da string, email incorreto, etc.


### Padronização de Mensagens:
- `@NotBlank` / `@NotNull`: `"O campo {nome_do_campo} é obrigatório."`
- `@Email`: `"O e-mail informado deve ser válido, exemplo: {usuario}@dominio.com."`
- `@Size`: `"O campo {nome_do_campo} deve ter entre {min} e {max} caracteres."` / `"O campo {nome_do_campo} deve ter no mínimo {min} caracteres."`
- `@Future`: `"A data informada deve ser uma data futura."`
- `@Pattern`: `"O campo {nome_do_campo} possui um formato inválido."`


## Padrões para Testes de Controller/CRUD

### Frameworks & Ferramentas
- Utilizar obrigatoriamente JUnit 5 e Spring Boot Test (`@SpringBootTest` com `@AutoConfigureMockMvc`).
- Utilizar `MockMvc` para simular as requisições HTTP nos Controllers.
- Utilizar `MockitoBean` para simular repositories.
- Utilizar `JacksonTester` com `@AutoConfigureJsonTesters` para organizar retornos JSON
- Utilizar `@WithMockUser` para cada um dos testes
- Utilize banco de dados em memória `H2` para realização dos testes de unidade.

### Convenções de Código nos Testes
- **Documentação:** Todo método de teste deve ter a anotação `@DisplayName` com um texto claro em PT-BR descrevendo o cenário e o resultado esperado.
- **Estrutura de Teste:** Organizar o corpo do teste no padrão **Given-When-Then** (ou Arrange-Act-Assert).
- Mockar um objeto `USUÁRIO` e um objeto `ADMIN/CLIENTE/BARBEIRO` nos métodos POST/PUT
- Programar o when/then de cada um dos services, leve em consideração o estilo do `AdminControllerTest.java`
- Não é necessário mockar o método GET de listagem de usuários.
- **Validação de HTTP Status:** Validar sempre o código HTTP exato do contrato REST:
    - Criação de recurso: `status().isCreated()` (HTTP 201).
    - Remoção/Desativação com sucesso: `status().isNoContent()` (HTTP 204).
    - Busca com sucesso: `status().isOk()` (HTTP 200).
    - Dados inválidos/Regra violada: `status().isBadRequest()` (HTTP 400).
    - Recurso não encontrado: `status().isNotFound()` (HTTP 404).