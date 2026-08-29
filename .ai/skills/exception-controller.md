### Código para Tratamento de Exceptions (ExceptionClass)
- Implementar tratamento de erros `@ExceptionHandler` no arquivo `ExceptionClass.java`
- Utilizar obrigatoriamente o DTO `DadosErro` para implementação de mensagem de erro no retorno do body.
- Implementar tratamento de erro personalizado para todo e qualquer tipo de erro possível (`UsuarioNotFoundException` `HttpStatus.BAD_REQUEST` `HttpStatus.UNAUTHORIZED`, etc) seguindo os padrões dos tratamentos já existentes na classe `ExceptionClass.java`
- Refatore cada uma das requisições e deixe-as simplificadas, SEM passagem de parâmetros.