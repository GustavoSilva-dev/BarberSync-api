# TESTES UNITÁRIOS DE SERVIÇOS

## Objetivo

Ajeitar os testes unitários restantes a para os endpoints de `ServicoController` no arquivo `ServicoControllerTest`

### Missão

Sua principal missão é a de implementar os testes unitários de êxito (`STATUS 200 / 201`) para os endpoints faltantes, já realizei o testes do `STATUS 201` do endpoint `POST`, agora, você deve:
1. Criar o teste de exito para o endpoint `PUT`
2. Criar o teste de exito para o endpoint `DELETE`
3. Criar o teste de exito para o endpoint `GET`

E especialmente:
4. Criar um teste verificando se o mínimo/máximo de precificação de Serviços (Determinado por `@Min` e `@Max` no DTO `DadosCadastroServico`) está funcionando, passe um serviço na qual o preço seja equivalente a 0, este teste deve retornar um erro de status 400.

### Regras

1. Siga rigidamente as regras de arquitetura pre-estabelecidas no `controller-tests.md`
2. Siga como padrão o teste unitário de cenário 1 do arquivo `ServicoControllerTest` como principal inspiração
3. No `@WithMockUser`, utilize a anotação **authorities** para determinar nivel de acesso, nunca **roles**.