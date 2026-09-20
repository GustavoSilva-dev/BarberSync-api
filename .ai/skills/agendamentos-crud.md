# FINALIZAÇÃO DO CRUD DE AGENDAMENTOS

## Objetivo

Finalizar a criação dos endpoints restantes do fluxo de agendamentos:
- **PATCH**: Endpoint para cancelar agendamentos já criados, com verificacao no service (se o status ja for concluida, ele não pode mais cancelar)).
- **DELETE**: Endpoint para excluir um agendamento do banco de dados, com motores de verificação:
    1. Se for um agendamento concluido, não pode mais ser excluido.
  
## Refatoração e boas práticas

Refatore alguns blocos de código que podem ser aprimorados, dentre eles:
- **Construtor do DTO DadosRetornoAgendamento**: O ```AgendamentoController``` está com blocos grandes de código apenas para formatar este DTO no body das requisições, crie um construtor no DTO que aceite um objeto Agendamento e automatize o processo.