CREATE TABLE servicos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(30) NOT NULL,
    preco NUMERIC CHECK(preco > 0) NOT NULL,
    duracaoEmMinutos NUMERIC CHECK(duracaoEmMinutos > 0) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT true
)