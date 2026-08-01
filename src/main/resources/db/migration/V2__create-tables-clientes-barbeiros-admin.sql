CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    telefone VARCHAR(30),

    CONSTRAINT fk_usuario_cliente FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE barbeiros (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    cpf VARCHAR(33) NOT NULL UNIQUE,
    telefone VARCHAR(30) NOT NULL UNIQUE,

    CONSTRAINT fk_usuario_barbeiro FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE admin (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    admin_key INTEGER NOT NULL UNIQUE,

    CONSTRAINT fk_usuario_admin FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
)