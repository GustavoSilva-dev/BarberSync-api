# APRIMORAMENTO DOS ENDPOINTS DE AGENDAMENTO

## DESCRIÇÃO E AUTORIZAÇÃO DE ENDPOINTS

Descreva os endpoints pendentes do `AgendamentoController` usando a anotação Operation e que limite o acesso dos endpoints:
- Endpoint de concluir (PATCH): limite apenas para usuários autenticados (de qualquer tipo)
- Endpoint de cancelar (PATCH): limite apenas para usuários autenticados (de qualquer tipo)
- Endpoint de excluir (DELETE): limite apenas para usuários do tipo barbeiro e admin.

Faça as alterações necessárias no `SecurityConfigurations`, desde que não afete outros endpoints.

## VALIDAÇÕES PENDENTES

1. **Validação de Propriedade (Quem é o dono do agendamento?)**
- Se for CLIENTE: O backend precisa checar se o cliente.id do agendamento é exatamente igual ao id do usuário logado do token JWT. Se um cliente tentar cancelar o agendamento de outra pessoa, lança uma exceção de acesso negado (403 Forbidden).
- Se for BARBEIRO ou ADMIN: A validação de propriedade do cliente é ignorada (pois eles têm autoridade sobre a agenda).

2. Janela Mínima de Antecedência (Política de Cancelamento)
- Se for CLIENTE: Deve existir uma regra de antecedência mínima. Se o cliente tentar cancelar faltando 30 minutos para o corte, o sistema bloqueia e lança uma ValidacaoException avisando que o prazo expirou e que ele precisa contatar a barbearia diretamente.
- Se for BARBEIRO ou ADMIN: Eles podem cancelar a qualquer momento sem restrição de horário.

Utilize `instanceof` para realizar os comparativos de classe.