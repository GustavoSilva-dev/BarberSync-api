# TESTES UNITÁRIOS DO CONTROLLER DE AGENDAMENTOS

## Objetivo

Escrever testes unitários cobrindo todas as possibilidades de validação da criação/alteração/exclusão de agendamentos.
- Ler `copilot-instructions.md` antes de prosseguir.

## Tipos de testes

Realize a criação de testes unitários cobrindo os seguintes cenários:
- **Status 200**: Alteração correta de um agendamento.
- **Status 200**: Cancelamento correto de um agendamento.
- **Status 204**: Exclusão correta de um agendamento.
- **Status 400**: Criação inválida de um agendamento.

## Padrões de Projeto

- Testes unitários feitos em JUnit e Mockito.
- Utilizar `JacksonTester` para padronizar respostas de API e DTOs.
- Seguir os padrões do `criarAgendamentoStatus201()` criado anteriormente.
- Mockar respostas dos services com `when` e `thenReturn`.
- Utilizar `WithMockUser` para simular usuário autenticado.
- Utilizar `mockMvc.perform()...` para simular as requisições à API.

## Em caso de erros no build

- Informar o erro ocorrido e as alterações sugeridas, antes de prosseguir.