# fiap-taykarus-motors

Trabalho Substitutivo para o Tech Challenge Fase 1 devido a não entrega no prazo. Curso de Pós-Graduação em Software Architecture na Pós-Tech FIAP.

## Sumário
1. [Sobre o Projeto](#sobre-o-projeto)
2. [Tecnologias Utilizadas](#tecnologias-utilizadas)
3. [Pré-requisitos](#pré-requisitos)
4. [Como Executar](#como-executar)
5. [Documentação](#documentação)
6. [Autor](#autor)

---

## Sobre o Projeto

A Taykarus Motors é uma aplicação de backend desenvolvida para gerenciar o ciclo de vida de vendas de uma concessionária de veículos. Este projeto foi criado com o objetivo de aplicar padrões de arquitetura de software modernos.

A aplicação segue os princípios da Arquitetura Hexagonal (Ports and Adapters) e Domain-Driven Design (DDD) para garantir o desacoplamento entre as regras de negócio e os detalhes de infraestrutura.

### Estrutura de Pastas

A organização do código reflete a separação de responsabilidades da arquitetura:

```
src/main/java/br/com/fiap/taykarus/motors/
├── adapter/             # Adaptadores
│   ├── in/web/          # Adaptadores de Entrada: Controladores REST e Handlers de Exceção
│   └── out/persistence/ # Adaptadores de Saída: Entidades JPA e Implementação de Repositórios
├── application/         # Camada de Aplicação
│   ├── port/in/         # Portas de Entrada: Interfaces de Casos de Uso e DTOs
│   ├── port/out/        # Portas de Saída: Interfaces para acesso a persistência e serviços externos
│   └── service/         # Serviços de Aplicação: Implementação da lógica de orquestração
├── domain/              # Camada de Domínio contendo entidades, objetos de valor e regras de negócio
└── infrastructure/      # Configuração e Infraestrutura
```

---

## Tecnologias Utilizadas 

Este projeto foi construído utilizando as seguintes tecnologias e bibliotecas:

- **Java 21:** Linguagem base do projeto, utilizando recursos modernos como Records.
- **Spring Boot 3:** Framework para criação da aplicação web, injeção de dependência e configuração.
- **Spring Data JPA:** Para persistência de dados e abstração de repositórios.
- **PostgreSQL:** Banco de dados relacional (executado via container).
- **Lombok:** Para redução de código boilerplate (Getters, Construtores, Builders).
- **JUnit 5 & Mockito:** Para testes unitários da camada de domínio e aplicação.
- **Docker & Docker Compose:** Para orquestração de containers e facilidade de execução do ambiente.
- **Maven:** Gerenciamento de dependências e build.

---

## Pré-requisitos

Antes de começar, certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

- **Docker:** Essencial para rodar a aplicação e o banco de dados.
- **Docker Compose:** (Geralmente incluído no Docker Desktop) para orquestrar os serviços.
- **Git:** Para clonar o repositório.

*Nota: Não é necessário ter o Java ou Maven instalados localmente para executar o projeto, pois o build é realizado dentro do Docker (Multi-stage build), mas é recomendado caso queira rodar os testes unitários via IDE.*

--

## Como Executar

A aplicação foi projetada para ser executada com um único comando, que irá subir tanto a API quanto o banco de dados.

1. **Clone o repositório:**
```
git clone https://github.com/taykarus/fiap-taykarus-motors.git
cd taykarus-motors
```

2. **Execute a aplicação com Docker Compose:** Este comando irá compilar o projeto (build), criar as imagens necessárias e iniciar os containers da API e do banco de dados.
```
docker compose up
```

*Aguarde alguns instantes até que os logs indiquem que a aplicação iniciou.*


3. **Acesse a Aplicação:**
- API estará disponível em: http://localhost:8080
- Utilize a [Collection do Postman](./docs/Taykarus_Motors.postman_collection.json) para maior facilidade na interação com a API.

4. **Parar a execução:** Pressione Ctrl + C no terminal ou execute:
```
docker compose down
```

---

## Documentação

- **Descrição do Problema**: Consulte os documentos para entender os requisitos e o contexto do projeto.
  - Trabalho Substitutivo (Fase 1) [`docs/trabalho_substitutivo_fase_1.pdf`](docs/trabalho_substitutivo_fase_1.pdf)

- **Domain Storytelling**: Utilizada a ferramenta [egon.io](https://egon.io/app-latest/), arquivos .png e .egn disponíveis:
    - [docs/domain_storytelling.png](docs/domain_storytelling.png)
    - [docs/domain_storytelling.egn](docs/domain_storytelling.egn)

- **Linguagem Ubíqua e Event Storming**: Consulte o arquivo [`docs/ubiquitous_language_and_event_storming.pdf`](./docs/ubiquitous_language_and_event_storming.pdf) para uma visão detalhada do
  processo de *Event Storming* utilizado no projeto.

    - **Miro**: Explore o quadro colaborativo no Miro para mais
      detalhes: [Taykarus Motors - Event Storming](https://miro.com/app/board/uXjVJjaCVrg=/).

- **Coleção Postman**: Utilize a coleção disponível em [
  `docs/Taykarus_Motors.postman_collection.json`](./docs/Taykarus_Motors.postman_collection.json) para facilitar o uso e a validação das
  APIs.

---

## Autor

- **Alison Israel - RM358367**  
  *Discord*: @taykarus | E-mail: taykarus@gmail.com
