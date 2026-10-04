# 🐾 Petshop IFPB

> *Breve Descrição:* Um sistema de gerenciamento de pet shop e clínica veterinária com interface gráfica (GUI), desenvolvido em Java utilizando o framework JavaFX para uma experiência de usuário rica e moderna.

-----

## 🌟 Recursos Principais

  * *Interface Gráfica Intuitiva (JavaFX):* Telas amigáveis para cadastro e busca de informações.
  * *Gerenciamento de Clientes e Pets:* Cadastro de proprietários e seus animais (raça, espécie, idade).
  * *Agendamento de Serviços:* Módulo para marcar banho, tosa e consultas veterinárias.
  

-----

## 🛠️ Tecnologias Utilizadas

  * *Linguagem:* *Java* (21 ou superior)
  * *Framework de UI:* *JavaFX* (21 ou superior)
  * *Sistema de Build:* *Apache Maven*
  * *Persistência de Dados:* Arquivos JSON na pasta `data/`.

-----

## ⚙️ Configuração e Execução

### 📋 Pré-requisitos

Para rodar o projeto, certifique-se de que possui:

1.  *Java Development Kit (JDK):* 21
2.  *Apache Maven:* Versão 3.6 ou superior (ou use o Maven Wrapper incluído).

### ⬇️ Instalação (Como Baixar)

Para baixar e preparar o código-fonte do *Petshop IFPB* em sua máquina, siga estas etapas:

Clone o repositório:

```bash
git clone https://github.com/Erickx00/PetShop-POO
cd PetShop-POO
```

### ☕ Usando (Como Rodar)

Após a instalação do código, use o Maven para compilar e executar o sistema:

1. Compile as dependências e o código:
```bash
./mvnw clean install
```

2. Execute a aplicação JavaFX:
```bash
./mvnw javafx:run
```

No Windows, use `mvnw.cmd` no lugar de `./mvnw`.

-----

## Gerando o JAR
```bash
# Limpa e gera o JAR com todas as dependências
./mvnw clean package

# O JAR executável será gerado na pasta target/.
```

## Executando JAR
```bash
# Navegue até a pasta target
cd target

# Execute o JAR sombreado gerado pelo Maven
java -jar PetShop-POO-1.0-SNAPSHOT.jar
```
## 📸 Telas do Sistema

| **Tela de Login** | **Tela de Cadastro** | **Menu Principal** |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/8440fde6-1c78-4c97-95df-baf3c72b9efc" width="300"> | <img src="https://github.com/user-attachments/assets/364e1fed-2d07-4915-908b-b4451212fbbc" width="300"> | <img src="https://github.com/user-attachments/assets/9e9febc0-356f-4dd6-8113-0bdd489e4925" width="300"> |


## 🔧 Desenvolvimento

Padrões Utilizados

* MVC - Separação Controller/Model/View
* Repository Pattern - Abstração de persistência
* Service Layer - Separação de lógica de negócio
* Factory Pattern - Criação de objetos

## 🧑‍💻 Equipe

| [<img src="https://github.com/joseaugusto302.png" width=115><br><sub>José</sub>](https://github.com/joseaugusto302) | [<img src="https://github.com/Erickx00.png" width=115><br><sub>Erick</sub>](https://github.com/Erickx00) | [<img src="https://github.com/LeandroS4nt.png" width=115><br><sub>Leandro</sub>](https://github.com/LeandroS4nt) |
| :---: | :---: | :---: |
| **José Augusto** | **Erick** | **Leandro** |
