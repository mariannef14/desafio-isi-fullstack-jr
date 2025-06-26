# 💻 Desafio ISI Fullstack Jr

---

### 🎯 Objetivo do Projeto
Desenvolver uma aplicação de ponta a ponta simulando um fluxo real de vendas dentro de um ambiente corporativo, focando em funcionalidades como:
- Cadastro, edição, atualização e remoção de um produto
- Cadastro, edição, atualização e remoção de um cupom
- Aplicação e remoção de descontos/cupons ao produto

<br>

### 🚀 Tecnologias Utilizadas
[![Tecnologias utilizadas](https://skillicons.dev/icons?i=java,spring,postgresql,angular)](https://skillicons.dev)

<br>

### 📁 Estrutura do Projeto
A estutura do projeto contém os seguintes pacotes:
- Config
  - Exceptions: exceções personalizadas
  - Swagger: documentação da aplicação
- Controller 
  - Dto: classes records para responses e requests
  - Query: classes que contém parâmetros para os filtros
  - Classes que recebem, processam e respondem solicitações HTTP
- Model
  - Entities: classes que representam as entidades
  - Enum: classes com valores fixos
- Repository: interfaces para acessar e manipular dados no banco 
- Service: regras de negócio da aplicação
- Utils: códigos úteis utilizados na aplicação

### 🎉 Como rodar o projeto
Requisitos necessários para rodar o projeto
- Java version >=17

Instruções para executar o projeto localmente:
- Baixar o projeto back, abrir em um editor de código e colocar pra rodar

O backend foi documentado com o Swagger, então para fins de teste ou entender como a API funciona basta rodar 
o projeto e acessar o link http://localhost:8080/swagger-ui/index.html#/

### ✔ Melhorias
- Documentar melhor a aplicação com o Swagger
- Refatorar partes do código que possam estar duplicadas
- Implementar Testes Unitários e de Integração
- Unir o back com o front
