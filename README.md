## Introduction

SwiftPay is an API developed to make building digital platforms simpler for developers, while also providing a robust and secure system for end users.

Currently, the API includes the following features:

- User creation and management;

- Transactions via PIX;

- Virtual assistant with a chat system for user support;

- Sandbox environment for transaction testing and integration with external APIs;

- Automatic deletion of inactive users.

SwiftPay is constantly evolving. We are already working on new functionalities that will be added soon, such as:

- [ ] Credit and debit card transactions
- [ ] Creating transactions via virtual assistant
- [ ] Issuing boletos and invoices

## Technologies

Our idea when creating the project was not only to make it practical, but also secure. With that in mind, we used the following technologies:

- Springboot;
- Spring Security;
- Spring AI;
- Spring Data JPA;
- JWT (Json Web Token);
- Mockito/JUnit;
- Docker;
- Swagger;
- AWS (Amazon Web Services).

## Architecture

### 🔍 What is a monolithic architecture?

Monolithic architecture is a model where the entire application — including backend, frontend, business logic, and database integration — is centralized in a single application.

### Why the monolithic architecture?

We chose to use monolithic architecture in SwiftPay’s current phase because it’s a simpler, more efficient, and appropriate approach for the development stage of the project.

With all components integrated into a single codebase, we were able to speed up development, simplify testing, and maintain a more direct and reliable deployment flow.

## Design Patterns

For this project in particular, we chose to implement the Repository Pattern — but why?

First and foremost, this pattern aligns with the SOLID principles, making it both easy to implement and maintain. It provides a clean separation between the data access logic and the business logic of the application.

The main purpose of the Repository Pattern is to abstract the data layer, acting as a mediator between the domain and the data source. This allows us to manage domain objects without needing to know the underlying database implementation, which results in more testable, modular, and flexible code.

## How to run the project

First of all, build your container with this command

```bash

docker build -t swiftpay .

docker run swiftpay

```

Then, navigate to the root of the project via command line and execute the command

```bash

mvn spring-boot:run

```

## How to access the Swagger Documentation

```bash

http://localhost:8080/swagger-ui/index.html#/

```

## License

The MIT License (MIT)

Copyright (c) 2015 Samuel Nóbrega

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

