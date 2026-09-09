# Controle de Itens

Aplicativo Android para ajudar a organizar os itens que você precisa levar em diferentes saídas do dia a dia.

O projeto nasceu como uma solução pessoal para facilitar a conferência de itens antes de sair de casa, mas pode ser útil para qualquer pessoa que precise levar conjuntos de itens recorrentes para lugares como faculdade, academia, trabalho, viagens e outros compromissos.

## 📱 Sobre o aplicativo

O **Controle de Itens** permite cadastrar itens, criar saídas, utilizar modelos pré-configurados e conferir os itens conforme eles são preparados.

A ideia é simples: em vez de tentar lembrar tudo o que precisa levar, você cria uma lista e deixa o aplicativo fazer essa parte por você.

## ✨ Funcionalidades

### 📦 Itens

- Cadastrar itens
- Editar itens
- Excluir itens

### 🚶 Saídas

- Criar uma nova saída
- Adicionar itens à saída
- Definir a quantidade de cada item
- Conferir itens individualmente
- Desmarcar itens já conferidos
- Remover itens de uma saída
- Finalizar uma saída

### 📋 Modelos

- Criar modelos de saída
- Definir itens e quantidades para cada modelo
- Editar modelos
- Excluir modelos
- Criar uma nova saída a partir de um modelo

### 🕒 Histórico

- Consultar saídas anteriores
- Visualizar os itens de cada saída
- Consultar o estado de cada item

### 🏠 Tela inicial

A tela inicial apresenta um resumo das atividades:

- Saídas pendentes
- Itens que ainda precisam ser conferidos
- Saídas recentes

### ⚙️ Ajustes

- Definir o nome do usuário
- Alterar o nome posteriormente
- Consultar informações sobre o aplicativo

## 🎨 Interface

O aplicativo utiliza uma interface escura inspirada no **Catppuccin**, adaptada aos componentes do **Material 3**.

A interface foi projetada para ser simples e objetiva, mantendo as informações importantes facilmente acessíveis.

<!--
Adicione screenshots aqui futuramente.

Exemplo:

![Tela inicial](docs/screenshots/home.png)
![Tela de saída](docs/screenshots/saida.png)
![Modelos](docs/screenshots/modelos.png)
-->

## 📥 Download

A versão mais recente do aplicativo pode ser encontrada na seção de **Releases** do GitHub.

### Versão atual

**1.0.0 — Primeira versão**

Esta versão foi disponibilizada para testes e tem como objetivo validar o funcionamento do aplicativo e coletar feedback sobre possíveis problemas e melhorias.

👉 **[Baixar a versão mais recente](../../releases/latest)**

## 🧪 Primeira versão

Esta é a primeira versão pública do aplicativo.

O objetivo neste momento é testar:

- Funcionamento geral
- Persistência dos dados
- Criação e edição de saídas
- Conferência de itens
- Modelos de saída
- Histórico
- Experiência de uso

Caso encontre algum problema, comportamento inesperado ou tenha alguma sugestão, fique à vontade para abrir uma **Issue** no repositório.

## 🛠️ Tecnologias

O aplicativo foi desenvolvido utilizando:

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **Room**
- **DataStore Preferences**
- **Android SDK**

## 🏗️ Arquitetura

O projeto é organizado em camadas, separando a lógica de negócio, a persistência dos dados e a interface do aplicativo.

```text
com.example.controleitens/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   ├── database/
│   │   └── entity/
│   └── preferences/
│
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
│
└── ui/
    ├── screens/
    ├── theme/
    └── viewmodel/
