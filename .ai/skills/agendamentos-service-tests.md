# Guia de Implementação: AgendamentoServiceTest

O objetivo do teste de unidade do **Service** é validar as **regras de negócio da aplicação** em isolamento total, sem subir o contexto do Spring ou conectar no banco de dados real.

---

## Arquitetura do Teste de Unidade (Mockito Puro)

Para garantir que os testes rodem em milissegundos, utilize o runner do **Mockito** em vez de `@SpringBootTest`.

* **`@ExtendWith(MockitoExtension.class)`**: Inicializa os mocks rapidamente sem carregar a JVM inteira do Spring.
* **`@Mock`**: Simula as dependências da Service (`AgendamentoRepository`, `ClienteRepository`, `List<ValidadorAgendamento>`, etc.).
* **`@InjectMocks`**: Cria a instância real da `AgendamentoService` injetando os mocks declarados.

---

## O Papel do `JacksonTester` na Testagem de DTOs

- Onde e por que usar?
1. **Validação de Formatação de Datas (`LocalDateTime`):** Garante que datas digitadas no DTO sejam corretamente convertidas para o padrão ISO-8601 (ex: `2026-09-24T15:00:00`).
2. **Método `.write()`:** Converte o objeto DTO Java em um objeto `JsonContent`. Com ele, você valida se nenhum campo sensível foi exposto no DTO de resposta e se os nomes dos atributos JSON correspondem ao contrato.