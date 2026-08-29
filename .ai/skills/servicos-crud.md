# CRUD de Serviços do BarberSync

### Objetivo

Você irá estruturar os endpoints para o CRUD de Serviços do BarberSync no `ServicoController`:
1. `GET` - Estruturar a rota get para listagem de serviços da barbearia, apenas serviços com `ativo = true`, não é necessário nenhum nível de autorização, utilize o **Page** para realizar a paginação do metódo a partir do dto `DadosDetalhamentoServico`.
2. `POST` - Rota para criar serviços na Barbearia, utilize `@SecurityRequirement` e `@PreAuthorize` para permitir apenas usuários ADMIN e BARBEIRO de acessarem este endpoint, utilize o dto `DadosCadastroServico`, retorne status `201` com a URI criada.
3. `PUT` - Rota para editar serviços na Barbearia, utilize `@SecurityRequirement` e `@PreAuthorize` para permitir apenas usuários ADMIN e BARBEIRO de acessarem este endpoint, utilize o dto `DadosAlterarServico`.
4. `DELETE` - Rota para realizar um safe delete de um servico, **Observação**: O delete de um serviço se baseia em alterar o campo `ativo` para `false`.

### SecurityConfigurations

Proteger as rotas para criação de serviços.
1. Proteja as rotas para os endpoints `PUT`, `DELETE` e `POST` para permitir APENAS usuários ADMIN e BARBEIRO de acessarem.
2. Utilize `hasAnyAuthority`, em hipótese alguma utilize `hasAnyRole`.