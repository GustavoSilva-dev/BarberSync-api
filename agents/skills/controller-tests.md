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