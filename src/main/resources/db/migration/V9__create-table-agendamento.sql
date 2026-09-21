CREATE TABLE agendamentos (
    id_agendamento BIGSERIAL NOT NULL PRIMARY KEY,
    data_hora_inicio TIMESTAMP NOT NULL,
    status_agendamento VARCHAR(15) NOT NULL,
    id_barbeiro BIGSERIAL,
    id_cliente BIGSERIAL,
    id_servico BIGSERIAL,
    CONSTRAINT fk_barbeiro_agendamento FOREIGN KEY(id_barbeiro) REFERENCES barbeiros(id),
    CONSTRAINT fk_cliente_agendamento FOREIGN KEY(id_cliente) REFERENCES clientes(id),
    CONSTRAINT fk_servico_agendamento FOREIGN KEY(id_servico) REFERENCES servicos(id)
);

ALTER TABLE agendamentos ENABLE ROW LEVEL SECURITY;