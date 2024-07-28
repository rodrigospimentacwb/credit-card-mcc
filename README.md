# credit-card-mcc

# Poc simulandoum endpoint de transação de cartão de credito

## Descrição
Este é um pequeno projeto REST testando alguns conhecimentos em Java e Spring Boot. Foi utilizado também o design
pattern Solid e Inversão de Controle (IoC) e o Strategy Pattern como uma forma de deixar o código simples e
de fácil manutenção.

## Tecnologias Utilizadas
- Java 21
- Spring Boot 3
- JUnit 5
- Docker
- Mysql
- Liquibase
- Git Actions CI com SonarCloud 

## Banco de Dados
- MySQL (utilizado via Docker Compose)
- Banco H2 para contexto de testes unitários e de integração

## Organização do Projeto
Os pacotes foram separados em domínios e utilizando o modelo MVC que é bastante comum a todos os desenvolvedores.

## Arquivos na Raiz do Projeto
- Docker compose com as configurações do MySQL ([compose.yaml](compose.yaml))
- Migration do Liquibase organizada por ano/mes facilitando os commits e evitando PRs com conflitos ([changelog](src%2Fmain%2Fresources%2Fdb%2Fchangelog))
- Collection do Postman ([credit-card-mcc-collection.postman_collection.json](credit-card-mcc-collection.postman_collection.json))

## Configurações Necessárias
- Java 21
- Intellij ou IDE de sua preferencia (No momento não concluí a geração do projeto em uma imagem docker)
- Docker

## Comandos uteis

```
# subir o banco de dados
docker-compose up -d    
```

```
# atualizar/criar o banco de dados
mvn liquibase:update
```

```
# build e testes
mvn clean install
```

## Pontos de Melhoria

- Adicionar um logstash para enviar os logs ao Kibana, poderia ter um local no docker compose.
- Corrigir o contexto de limpeza das classes de banco pois usando o @DirtiesContext temos perda de performance para reiniciar o contexto a cada teste com o H2.

## Em caso de transações simultâneas

Caso o projeto fosse utilizado em produção e escalado teríamos alguns pontos de melhoria:

- Prevenção de Transações Simultâneas: Para evitar que duas transações sejam feitas ao mesmo tempo e gerem um saldo negativo, podemos implementar um lock distribuído usando o ID da conta como chave. Além disso, adicionar uma chave de idempotência no cabeçalho ou corpo da requisição ajudaria a garantir que a mesma transação não seja processada mais de uma vez.

- Uso de Cache Distribuído: Podemos utilizar um cache de banco de dados distribuído para reduzir consultas desnecessárias ao banco de dados. Esse cache seria atualizado apenas quando as entidades fossem alteradas, e não nas leituras, ajudando a evitar sobrecarga do sistema e lentidão.

- Circuit Breaker para Estabilidade: Adicionar um circuit breaker pode proteger o sistema contra sobrecarga de requisições desnecessárias, evitando que ele fique lento ou comece a apresentar erros.

- Gerenciamento de Índices: É fundamental gerenciar bem os índices do banco de dados para garantir que as consultas sejam rápidas e eficientes, evitando qualquer tipo de lentidão.

- Considerar Banco de Dados NoSQL: Utilizar um banco de dados NoSQL para armazenar transações pode ser uma boa ideia, pois ele é mais rápido para leituras e escritas. No entanto, vale lembrar que o NoSQL pode não ser tão eficiente para consultas complexas, como as que envolvem a geração de relatórios.

- Se estiver utilizando algum provedor de serviços em nuvem, como AWS ou Google Cloud, é crucial verificar a região correta onde deixara sua infraestrutura disponível evitando latência de rede. 