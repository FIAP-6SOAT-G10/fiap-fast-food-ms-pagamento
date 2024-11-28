# PosTech - Arquitetura de Software - FIAP
<img height="350" src="src/main/resources/fiap.png" title="Fiap Logo" width="1000"/>

# Documentação - Tech Challenge - Grupo 10 6SOAT
Repositório para o desafio do Tech Challenge da Pós-gradução em Software Architecture pela FIAP.

## Introdução
Uma lanchonete de bairro que está expandido sua operação devido seu grande sucesso. Porém, com a expansão e sem um sistema de controle de pedidos, o atendimento aos clientes pode ser caótico e confuso.
Para solucionar o problema, a lanchonete irá investir em um sistema de autoatendimento de fast food, que é composto por uma série de dispositivos e interfaces que permitem aos clientes selecionar e fazer pedidos sem precisar interagir com um atendente.

## Índice
<a name="membros"></a>
1. [Membros do Grupo](#membro)
   <a name="documentacoes"></a>
2. [Documentação do Projeto com Event Storming](#documentacao) 
   <a name="arquitetura"></a>
3. [Arquitetura do Projeto](#arquitetura)
   <a name="requisitos"></a>
4. [Requisitos para Execução](#requisito)
   <a name="local"></a>
5. [Execução do projeto localmente](#execucao-local)
   <a name="docker"></a>
6. [Execução do projeto pelo Docker](#execucao-docker)
   <a name="rotas"></a>
7. [Rotas da API](#rota)
   <a name="ordens"></a>
8. [Ordem de Execução das API](#ordem)
   <a name="recursos"></a>
9. [Recursos](#recurso)
   <a name="erros"></a>
10. [Códigos de Erro](#erro)

<a id="membro"></a>
## Membros do Grupo
- [Anderson Oliveira](https://github.com/anderson-solucoes)
- [Gabriel Fonte](https://github.com/sourceGabriel)
- [Gabriel Marcelino](https://github.com/GabsMarcelino)
- [Rafael Moura](https://github.com/magneon)
- [Thiago Getnerski](https://github.com/Getnerski)

## Apresentação do Projeto
<img height="50" width="200" src="src/main/resources/img/images.png" title="Fiap Arquitetura UML"/>

### [Vídeo da Fase 2](https://www.youtube.com/watch?v=y4m8ueFRydw)
### [Vídeo da Fase 3](https://www.youtube.com/watch?v=WleicW8Sx58)

<a id="documentacao"></a>
## Documentação do Projeto com Event Storming
Fizemos o mapeamento do fluxo de forma evolutiva, pensando a partir dos eventos passados, incrementando com os pontos de atenção para na sequência adicionar os comandos, modelos de leitura, políticas e sistemas externos, conforme o que foi aprendido em aula.

[Event Storming](https://miro.com/app/board/uXjVKVpAtxk=/?moveToWidget=3458764589554562116&cot=14)

<a id="arquitetura"></a>
## Arquitetura do Projeto
Abaixo é possível verificar nossa arquitetura em UML.
* `infra/controllers`: Responsável por receber todas requisições externas para realizar alguma ação em uma das entidades.
* `application/domain`: Responsável por centralizar todas nossa entidades que vão ser utilizadas no sistema.
* `application/usecases`: Responsável por criar casos de uso que vão realizar alguma ação na entidade, seja criação/atualização/remoção.
* `application/gateways`: Responsável pela nossa camada de abstração que será implementada em `infra/gateways`.
* `infra/gateways`: Responsável pela implementação das nossas abstrações de `application/gateways`.
* `infra/persistence`: Responsável epal persistências dos dados na entidades do banco de dados.

<img height="400" src="src/main/resources/img/arquitetura-uml.png" title="Fiap Arquitetura UML" width="800"/>

Agora que vimos como funciona nossa arquitetura em UML, migramos ela para Kubernetes:

* `deployment-application`: Essa arquitetura fica responsável por nossa API.
    * `Service (Load Balancer)`: É um tipo de serviço que distribui o tráfego de rede para os pods que executam sua aplicação
    * `Deployment`: É um recurso que fornece a maneira mais comum de gerenciar a implantação e a escalabilidade de aplicações em containers.
    * `ConfigMap`: É um recurso utilizado para armazenar dados de configuração em formato de pares chave-valor, nele vamos guardar nossas credenciais de URL para o banco de dados.
    * `Pods`: Cada pod encapsula um ou mais contêineres (containers).

* `sts-postgres`: Essa arquitetura fica responsável por nosso banco de dados.
    * `Service (ClusterIP)`: É um recurso que define uma forma lógica de agrupar um conjunto de pods e prover uma forma consistente de acessá-los.
    * `StatefulSet`: É um recurso que gerencia a implantação e o dimensionamento de um conjunto de pods e fornece garantias sobre a ordem e a identidade desses pods.
    * `Secret`: É um recurso utilizado para armazenar e gerenciar informações sensíveis, como senhas, tokens OAuth, etc.
    * `Pods`: Cada pod encapsula um ou mais contêineres (containers).

<img height="455" src="src/main/resources/img/arquitetura-k8s.png" title="Fiap Arquitetura K8S" width="1195"/>

#### Em Resumo
A arquitetura do sistema descrita envolve duas principais componentes: a aplicação de API e o banco de dados PostgreSQL.

* `Banco de dados`: Os serviços externos irão se comunicar com o nosso Service do tipo ClusterIP, que redirecionará as requisições para o nosso StatefulSet. O StatefulSet é responsável por gerenciar quatro réplicas, cada uma rodando uma instância do nosso banco de dados. O armazenamento persistente é gerenciado pelo PersistentVolume e pelo PersistentVolumeClaim, garantindo que os dados lidos e escritos estejam sempre disponíveis e consistentes.
* `Aplicação`: Quando uma requisição for feita ao nosso DNS, ela será direcionada ao nosso Service do tipo Load Balancer. Este, por sua vez, redirecionará a solicitação para o nosso Deployment, que gerencia quatro réplicas da nossa API, cada uma em um contêiner separado. Cada contêiner terá uma conexão direta com o ClusterIP do banco de dados, garantindo uma comunicação eficiente e segura entre a aplicação e o banco de dados.

## Arquitetura da Infraestrutura na Nuvem
<img height="500" src="src/main/resources/img/arquitetura-cloud.png" title="Fiap Arquitetura Cloud" width="1195"/>

<a id="requisito"></a>
## Requisitos para Execução
1. [Docker](https://www.docker.com/)
2. [DBeaver](https://dbeaver.io/download/)
3. [Maven](https://maven.apache.org/)
4. [Postman](https://www.postman.com/downloads/) (opcional, pois há Swagger em /api/api-docs ou /api/swagger-ui.html)
5. [Ngrok](https://ngrok.com/download) (opcional, para testar o webhook)

Antes de iniciar, certifique-se de que sua máquina atenda aos seguintes requisitos:<br/>

**JDK 11 instalado:**<br/>
Certifique-se de ter o JDK 11 instalado em sua máquina.<br/>
Para verificar a versão do JDK instalada, execute o seguinte comando no terminal:
```bash
java -version
```

**Maven >= 3 instalado:**<br/>
Verifique se você tem o Maven instalado em sua máquina. Para verificar a versão do Maven instalada, execute o seguinte comando no terminal:
```bash
mvn -version
``` 

**Docker desktop instalado:**<br/>
Verifique se você tem o Docker instalado em sua máquina. Para verificar a versão do Docker instalada, execute o seguinte comando no terminal:
```bash
docker -v
``` 
Se o Docker não estiver instalado, faça o download e siga as instruções de instalação do site oficial do Docker ou de outra fonte confiável.<br/><br/>


**Clonando o projeto:**<br/>
Clone o repositório do projeto em seu ambiente local.<br/>
```bash
git clone https://github.com/FIAP-6SOAT-G10/tech-challenge.git
```

## Configuração e Execução do Ngrok

### Instalação do Ngrok
Baixe e instale o Ngrok a partir do site oficial.

### Autenticação
Após a instalação, autentique-se com seu token de autenticação:
```sh
ngrok authtoken <seu_token_de_autenticação>
```

Iniciar o Ngrok:  
Execute o Ngrok para expor sua aplicação local na porta 8080:
```sh 
ngrok http 8080
```

### Atualizar a Variável _mercadopago.notification_url_:

Copie a URL gerada pelo Ngrok (algo como https://<subdomínio>.ngrok.io) e atualize a variável mercadopago.notification_url no seu arquivo de configuração:
```mercadopago.notification_url=https://<subdomínio>.ngrok.io/webhook/notifications ```

### Funcionamento do Webhook de Notificações do Mercado Pago

### Recebimento de Notificações:  

O Mercado Pago envia notificações para a URL configurada ```(mercadopago.notification_url)``` sempre que há uma atualização no status de um pagamento.

### Endpoint de Webhook:  
O endpoint ```/webhook/notifications``` recebe essas notificações e processa as informações enviadas pelo Mercado Pago.

### Processamento da Notificação:  

O controlador WebhookController lida com as notificações recebidas. Ele verifica a ação (action) e, se for payment.updated, consulta o status do pagamento e atualiza o status do pedido no sistema.

### Exemplo de Notificação:

Uma notificação típica contém informações sobre o pagamento, como o ID do pagamento e a ação realizada (payment.updated).

### Exemplo de Configuração do Webhook no application.properties:

```mercadopago.notification_url=https://<subdomínio>.ngrok.io/webhook/notifications```

### Exemplo de Uso do Ngrok

Iniciar o Ngrok:

```sh 
ngrok http 8080
```

<a id="execucao-local"></a>
## Execução do projeto localmente

### Passo 1: Iniciar o Postgres
Na raiz do projeto, execute o seguinte comando para criar o container com o banco de dados Postgres:

```bash
docker run --name=postgres -p 5432:5432 -e POSTGRES_USER=tech -e POSTGRES_PASSWORD=tech_passwd -e POSTGRES_DB=payments -d postgres
```

### Passo 2: Compilar o projeto
Após a criação do container do Postgres, você deve realizar o build do projeto utilizando o Maven:
```bash
mvn clean install -DskipTests -Plocal -q
```

### Passo 3: Executar o projeto
Após a criação do container do Postgres, você pode iniciar o projeto.

```bash
mvn spring-boot:run
```

<a id="execucao-docker"></a>
## Execução do projeto pelo Docker

### Passo 1: Executar o Docker Compose
Na raiz do projeto, execute o seguinte comando para criar os containers com o banco de dados Postgres, e realizar o build da aplicação via Docker:
```bash
docker-compose up -d
```

### Documentação
A documentação da API pode ser acessada através do Swagger em: [http://localhost:8080/api/swagger-ui.html](`http://localhost:8080/api/swagger-ui.html`)

### Testes
Para executar os testes unitários, basta executar o comando abaixo:

`mvn test`

### Observações
- O arquivo `application.yml` contém as configurações de conexão com o banco de dados. Caso seja necessário alterar a porta do banco de dados, basta alterar a propriedade `spring.datasource.url` no arquivo `application.yml`.

<a id="rota"></a>
## Rotas da API
Abaixo está descrito todas as rotas fornecidas da aplicação, bem como seu objetivo e possíveis códigos de retorno:

### Rotas de Categorias
- `GET /categorias`: Retorna a lista de categorias de produtos cadastradas no sistema.

### Rotas de Clientes
- `POST /clientes`: Cria um novo cliente. Retorna 201 se for bem-sucedido, 400 se houver uma solicitação ruim e 500 para erros internos do servidor.
- `GET /clientes`: Retorna uma lista de todos os clientes. Retorna 200 se for bem-sucedido, 204 se nenhum conteúdo for encontrado e 500 para erros internos do servidor.
- `PATCH /clientes`: Atualiza os dados do cliente. Retorna 200 se for bem-sucedido, 400 se houver uma solicitação ruim, 404 se não for encontrado e 500 para erros internos do servidor.
- `PUT /clientes`: Atualiza um cliente. Retorna 204 se for bem-sucedido, 400 se houver uma solicitação ruim, 404 se não for encontrado e 500 para erros internos do servidor.

### Rotas de Produtos
- `POST /produtos`: Cria um novo produto. Retorna 201 se for bem-sucedido, 400 se houver uma solicitação ruim e 500 para erros internos do servidor.
- `GET /produtos`: Retorna uma lista de todos os produtos. Retorna 200 se for bem-sucedido, 204 se nenhum conteúdo for encontrado e 500 para erros internos do servidor.
- `PATCH /produtos/{id}`: Atualiza os dados do produto. Retorna 200 se for bem-sucedido, 400 se houver uma solicitação ruim, 404 se não for encontrado e 500 para erros internos do servidor.
- `PUT /produtos/{id}`: Atualiza um produto. Retorna 204 se for bem-sucedido, 400 se houver uma solicitação ruim, 404 se não for encontrado e 500 para erros internos do servidor.
- `DELETE /produtos/{id}`: Exclui um produto. Retorna 200 se for bem-sucedido, 204 se nenhum conteúdo for encontrado, 400 se houver uma solicitação ruim, 404 se não for encontrado e 500 para erros internos do servidor.
- `GET /produtos/categoria/{categoria}`: Retorna uma lista de produtos por categoria. Retorna 200 se for bem-sucedido, 204 se nenhum conteúdo for encontrado, 400 se houver uma solicitação ruim, 404 se não for encontrado e 500 para erros internos do servidor.

### Rotas de Pedidos
- `POST /pedidos`: Realiza o cadastro de um novo pedido. Retorna 201 se for bem-sucedido, 400 se houver uma solicitação ruim e 500 para erros internos do servidor.
- `GET /pedidos/status/{status}`: Retorna todos os pedidos em um determinado status. Os status disponíveis são: Recebido (recebido), Em Preparação (preparacao), Pronto (pronto) e Finalizado (finalizado). Retorna 200 se for bem-sucedido, 400 se houver algum problema na solicitação e 500 para erros internos do servidor.
- `POST /pedidos/{id}/checkout`: Realiza o checkout de um pedido. Retorna 201 se for bem-sucedido, 400 se houver uma solicitação ruim e 500 para erros internos do servidor.
- `PATCH /pedidos/{id}/pagamento`: Atualiza o status de pagamento de um pedido. Retorna 200 se for bem-sucedido, 400 se houver algum problema na solicitação e 500 para erros internos do servidor.
- `PATCH /pedidos/{id}/status`: Atualiza o status de um pedido. Retorna 200 se for bem-sucedido, 400 se houver algum problema na solicitação e 500 para erros internos do servidor.
- `GET /pedidos`: Retorna uma lista de todos os pedidos. Retorna 200 se for bem-sucedido, 204 se nenhum conteúdo for encontrado e 500 para erros internos do servidor.
- `GET /pedidos/{id}`: Retorna um pedido específico. Retorna 200 se for bem-sucedido, 404 se não for encontrado e 500 para erros internos do servidor.

<a id="ordem"></a>
## Ordem de Execução das API
Para o funcionamento correto das APIs, a ordem abaixo deverá ser seguida à depender do cenário desejado:

### Realizar pedido para cliente _não identificado_
<a name="recurso"></a>
1. [Listar produtos por categoria](#recurso10)
2. [Criar pedido](#createPedido)
3. [Realizar checkout](#recurso13)
4. [Atualizar status do pedido para 'Em preparação'](#recurso14)
5. [Atualizar status do pedido para 'Pronto'](#recurso14)
6. [Atualizar status do pedido para 'Finalizado'](#recurso14)

### Realizar pedido para _cliente identificado_
1. [Cadastrar cliente](#recurso1)
2. [Listar produtos por categoria](#recurso10)
3. [Criar pedido](#createPedido)
4. [Realizar checkout](#recurso13)
5. [Atualizar status do pedido para 'Em preparação'](#recurso14)
6. [Atualizar status do pedido para 'Pronto'](#recurso14)
7. [Atualizar status do pedido para 'Finalizado'](#recurso14)

<a id="recurso"></a>
## Recursos

### Cliente

<a id="recurso1"></a>
#### Cadastrar Cliente
```sh
POST http://localhost:8080/api/clientes
{
    "cpf": "52001817983",
    "email": "rafaela-almada91@imail.com",
    "nome": "Rafaela Lorena Almada"
}
```
<a id="recurso2"></a>
#### Listar Clientes
```sh
GET http://localhost:8080/api/clientes
```

<a id="recurso3"></a>
#### Atualização Parcial de Clientes
```sh
PATCH http://localhost:8080/api/clientes
{
    "cpf": "52001817983",
    "email": "rafaela-almada91@imail.com",
    "nome": "Rafaela Lorena Almada"
}
```

<a id="recurso4"></a>
#### Atualização de Clientes
```sh
PUT http://localhost:8080/api/clientes
{
    "cpf": "52001817983",
    "email": "rafaela-almada91@imail.com",
    "nome": "Rafaela Lorena Almada"
}
```

### Produto

<a id="recurso5"></a>
#### Cadastrar Produto
```sh
POST http://localhost:8080/api/produtos
{
    "nome": "Bebida Láctea de Morango",
    "descricao": "Bebida Láctea de Morango 500ml",
    "categoria": {
      "nome": "Bebida",
      "descricao": "Coquinha Gelada"
    },
    "preco": 19.9,
    "imagem": "CDN:imagem"
}
```

<a id="recurso6"></a>
#### Listar Produtos
```sh
GET http://localhost:8080/api/produtos?nome=string&descricao=string&preco=string
```

<a id="recurso7"></a>
#### Atualização Parcial de Produto
```sh
PATCH http://localhost:8080/api/produtos/:id
[
    {
    "op": "replace",
    "path": "/nome",
    "value": "Novo Nome do Produto"
    }
]
```

<a id="recurso8"></a>
#### Atualização de Produto
```sh
PUT http://localhost:8080/api/produtos/:id
{
    "nome": "Bebida Láctea de Morango",
    "descricao": "Bebida Láctea de Morango 500ml",
    "categoria": {
      "nome": "Acompanhamento",
      "descricao": "Fritura"
    },
    "preco": 19.9,
    "imagem": "CDN:imagem"
}
```

<a id="recurso9"></a>
#### Exclusão de Produto
```sh
DELETE http://localhost:8080/api/produtos/:id
```

### Categoria

<a id="recurso10"></a>
#### Listar Produtos por Categoria
```sh
GET http://localhost:8080/api/produtos/categoria/:categoria
:categoria
- LANCHE
- BEBIDA
- ACOMPANHAMENTO
- SOBREMESA
```

<a id="findAllCategories"></a>
#### Listar Todas Categorias
```sh
GET http://localhost:8080/api/categorias
```

### Pedido

<a id="createPedido"></a>
#### Cadastrar Pedido
```shell
POST http://localhost:8080/api/pedidos
{
   "id":12345678900,
   "cliente":{
      "id":12,
      "cpf":"12312312312",
      "nome":"Joel",
      "email":"joel@email.com"
   },
   "status":{
     "id": 1,
     "nome": "Em Preparo"
   },
   "valor":89.25,
   "dataCriacao":"2024-05-02T00:00:00",
   "dataFinalizacao":"2024-05-02T00:00:00",
   "dataCancelamento":"2024-05-02T00:00:00",
   "statusPagamento": {
     "id": 1,
     "nome": "Pago"
   },
   "produtos":[
      {
         "produto":{
            "id":12345678900,
            "nome":"Product Name",
            "descricao":"Product Description",
            "preco":49.99,
            "imagem":"image_url"
         },
         "valorTotal":125.78,
         "quantidade":2.0
      }
   ],
   "items":{
      "lanches":[
         {
            "id":46,
            "quantidade":2
         }
      ],
      "acompanhamento":[
         {
            "id":782,
            "quantidade":5
         }
      ],
      "bebida":[
         {
            "id":1,
            "quantidade":1
         }
      ],
      "sobremesa":[
         {
            "id":968,
            "quantidade":2
         }
      ]
   }
}

```

<a id="recurso11"></a>
#### Obter Pedido por Identificador
```sh
GET http://localhost:8080/api/pedidos/:id
```

<a id="recurso12"></a>
#### Listar Pedidos
```sh
GET http://localhost:8080/api/pedidos
```

<a id="recurso13"></a>
#### Realizar Pagamento de Pedido
```sh
POST http://localhost:8080/api/pedidos/:id/checkout
```

<a id="recurso14"></a>
#### Atualizar Status do Pedido
```sh
PATCH http://localhost:8080/api/pedidos/:id/status
[
    {
        "op": "replace",
        "path": "/status",
        "value": "preparacao|pronto|finalizado"
    }
]
```

<a id="recurso15"></a>
#### Atualizar Status de Pagamento do Pedido
```sh
PATCH http://localhost:8080/api/pedidos/:id/pagamento
[
  {
    "op": "replace",
    "path": "/statusPagamentoEntity",
    "value": "pendente|pago|recusado"
  }
]
```

<a id="recurso16"></a>
#### Listar Pedidos por Status
```sh
GET http://localhost:8080/api/pedidos/status/:status
```

<a id="erro"></a>
## Códigos de Erro
### Erros genéricos:
- `001`: Parâmetro obrigatório não foi enviado.
### Erros de categoria:
- `100`: Categoria inválida.
### Erros de produtos:
- `200`: O nome do produto é obrigatório.
- `201`: A descrição do produto é obrigatória.
- `202`: O preço do produto é obrigatório.
- `203`: A imagem do produto é obrigatória.
- `204`: Identificador de produto inválido.
- `205`: O identificador fornecido não está relacionado a nenhum produto existente.
- `206`: Erro durante a atualização do produto no banco de dados.
- `207`: Erro genérico ao atualizar o produto.
- `208`: A categoria do produto é obrigatória.
- `209`: Não existem produtos registrados para a categoria informada.
### Erros de clientes:
- `300`: CPF inválido.
- `301`: O cliente com o CPF informado já existe.
- `302`: O email do cliente é obrigatório.
- `303`: O CPF do cliente é obrigatório.
- `304`: O campo CPF é obrigatório ao atualizar um cliente.
- `305`: O nome do cliente é obrigatório.
### Erros de pedidos:
- `400`: O pedido informado não foi localizado.
- `401`: Erro durante a atualização do status do pedido no banco de dados.
- `402`: O identificador do pedido é inválido.
- `403`: O status à ser atualizado é obrigatório.
- `404`: Não é possível atualizar o pedido para o status informado.
- `405`: Pedidos no status 'Recebido' só podem avançar para o status 'Em preparação'.
- `406`: Pedidos no status 'Em preparação' só podem avançar para o status 'Pronto'.
- `407`: Pedidos no status 'Pronto' só podem avançar para o status 'Finalizado'.
- `408`: Pedidos no status 'Finalizado' não podem ser alterados.
- `499`: Erro genérico ao atualizar o status do pedido.
