### Padronização de Mensagens:
- `@NotBlank` / `@NotNull`: `"O campo {nome_do_campo} é obrigatório."`
- `@Email`: `"O e-mail informado deve ser válido, exemplo: {usuario}@dominio.com."`
- `@Size`: `"O campo {nome_do_campo} deve ter entre {min} e {max} caracteres."` / `"O campo {nome_do_campo} deve ter no mínimo {min} caracteres."`
- `@Future`: `"A data informada deve ser uma data futura."`
- `@Pattern`: `"O campo {nome_do_campo} possui um formato inválido."`

## Regra para DTOs e Validações Bean
- Todo DTO deve ser um Java `record`
- Todas as anotações do Bean Validation (`jakarta.validation.constraints.*`) devem possui o parâmetro `message` para expressar erros
- Cada `message` das anotações do DTO devem possuir uma frase amigável em português explicando o erro no campo, como tamanho da string, email incorreto, etc.