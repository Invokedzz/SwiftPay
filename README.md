## Introdução

A SwiftPay é uma API desenvolvida para tornar a criação de plataformas digitais mais simples para os desenvolvedores, ao mesmo tempo em que oferece um sistema robusto e seguro para os usuários finais.

Atualmente, a API conta com as seguintes features:

- Criação e gerenciamento de usuários;

- Transações via PIX;

- Assistente virtual com sistema de chat para suporte ao usuário;

- Ambiente sandbox para testes de transações e integração com APIs externas à SwiftPay;

- Deleção automática de usuários inativos.

A SwiftPay está em constante evolução. Já estamos trabalhando em novas funcionalidades que serão adicionadas em breve, como:

- [ ] Transações via cartão de crédito e débito
- [ ] Criação de transações via assistante virtual
- [ ] Emissão de boletos e faturas

## Tecnologias

Nossa ideia quando fizemos o projeto, não era torná-lo só algo prático, mas também algo seguro. Tendo isso em mente, fizemos uso das seguintes tecnologias:

- Springboot;
- Spring Security;
- Spring AI;
- Spring Data JPA;
- JWT (Json Web Token);
- Mockito/JUnit;
- Docker;
- Swagger;
- AWS (Amazon Web Services).

## Arquitetura

Optamos por utilizar a **arquitetura monolito** na fase atual da SwiftPay por ser uma abordagem mais simples, eficiente e adequada para o estágio de desenvolvimento do projeto.

Com todos os componentes integrados em um único código-base, conseguimos acelerar o desenvolvimento, facilitar testes e manter um fluxo de deploy mais direto e confiável.

### 🔍 O que é uma arquitetura monolito?

A arquitetura monolito é um modelo em que toda a aplicação — incluindo backend, frontend, lógica de negócios e integrações com o banco de dados — está centralizada em uma única aplicação.

## Design Patterns

## Como rodar o projeto

## Licença



