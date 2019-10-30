# Quake Parser Reader
Este projeto consiste em fazer uma leitura do log estático (ou seja o arquivo que já consta no servidor), definido nas propriedades (`application.properties`)
## Instruções de Configuração
### Configurações
## Configurações de IDE
### Intellij
[Baixe o Intellij Aqui](https://www.jetbrains.com/idea/download/#section=linux)

Lista de Plug-ins utilizados:

| Plugin | Descrição |
| ------ | ------ |
|Eclipse Code Formatter|Permite a formação de código no estilo do esclipse, mantendo o padrão de formatação entre Eclipse e Intellij|
|Lombok|Plug-in que permite a suporte a biblioteca do Lombok|
##### Eclipse Code Formatter
- Installe o plug-in eclipse code formatter no Intellij
- Reinicie o Intellij
- Vá em Configurações -> Other Settings -> Eclipse Code Formatter
- Selecione 'Use the Eclipse code formatter'
- Em Eclipse Java Formatter config file selecione em 'Browse' o arquivo de formatação de código, localizado na pasta principal do projeto

##### Lombok
- Installe o plug-in do lombok code formatter no Intellij
- Reinicie o Intellij

### Para Iniciar o projeto
para testar a aplicação vá em Run -> Edit Configurations -> Templates -> Application
  - No canto esquerdo desta janela clique em '+' -> Application
  - Defina um nome para a Application
  - Em Main Class defina a classe principal do projeto
  
#### Mudar Propriedades do projeto
Todas as propriedades configuráveis do projeto encontra-se em `resources/application.properties`

#### Testar Requisições
O controler das requisições se encontra na package `rest`

