# BarberSync API

> **Status do Projeto:** ⚠️ Em Desenvolvimento (In-Progress)

O **BarberSync API** é o backend de um ERP completo e modular projetado para a gestão operacional de barbearias e salões de beleza. O sistema centraliza o controle de acessos, gestão de profissionais, clientes e agendamentos, garantindo uma arquitetura robusta, testável e segura.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 4
* **Acesso a Dados:** Spring Data JPA / Hibernate
* **Banco de Dados:** PostgreSQL (Hospedado via Supabase)
* **Controle de Versão do Banco:** Flyway Migration
* **Segurança & Validação:** Spring Security / Bean Validation
* **Testes Automatizados:** JUnit 5, Mockito e MockMvc

---

## 🏛️ Arquitetura & Boas Práticas

A API foi projetada seguindo os princípios de código limpo e arquitetura em camadas:

* **DTOs (Data Transfer Objects):** Utilização de Java `records` para immutabilidade e transferência limpa de dados.
* **Isolamento de Regras de Negócio:** Camada de `@Service` dedicada para validações e orquestração.
* **Padronização de Rest:** Uso correto de status HTTP (`201 Created`, `200 OK`, `204 No Content`, `400 Bad Request`).
* **Testes de Integração e Unidade:** Cobertura de controllers utilizando `MockMvc` e `JacksonTester` para validação de contratos JSON.
* **Governança do Código via AI:** Utilização de arquivos `@SKILLS.md` e `@AGENTS.md` para padronização técnica de assistentes de desenvolvimento.

---

## 🚧 Status de Desenvolvimento & Funcionalidades Implementadas

### 👥 Gestão de Usuários (Domínio Principal)
- [x] **Módulo Admin:** Cadastro, listagem e controle de permissões administrativas.
- [x] **Módulo Barbeiro:** Estruturação da entidade, repositório e serviços dos profissionais.
- [x] **Módulo Cliente:** Gestão de cadastros.
- [x] **Testes de Controller & Service:** Suíte de testes unitários com mocks para todos os perfis de usuário.
- [x] **Banco de Dados Relacional:** Migrações versionadas via Flyway conectadas ao PostgreSQL/Supabase.

---

## 🔮 Próximos Passos & Futuras Funcionalidades

O projeto segue um roadmap contínuo de evolução:

- [ ] **Autenticação & Autorização:**
  - Implementação de segurança stateless via **JWT (JSON Web Token)**.
  - Controle de acesso baseado em perfis **(RBAC - Roles: ADMIN, BARBEIRO, CLIENTE)**.
- [ ] **Módulo de Agendamentos:**
  - Validação de horários de funcionamento e conflitos de agenda.
  - Cancelamento e remarcação com regras de antecedência.
- [ ] **Módulo Financeiro:**
  - Controle de comissões por barbeiro e relatórios de faturamento diário.
- [ ] **Conteinerização & Deploy:**
  - Criação de `Dockerfile` e `docker-compose.yml`.
  - Deploy em ambiente de produção (Render / Cloud).

---

## 🚀 Como Executar o Projeto Localmente

### Pré-requisitos
* Java 21 JDK
* Maven 3.8+
* Conta no PostgreSQL ou instância ativa no Supabase

### Passos
1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/GustavoSilva-dev/BarberSync-api.git](https://github.com/GustavoSilva-dev/BarberSync-api.git)
