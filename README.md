# NeowayDocumentoApi (neoway-documento-api)

API para projeto de gerenciamento de documentos (CPF / CNPJ).

## Build

- Requer Java 11
- Requer maven
- Requer MongoDB

### Propriedades de Sistema JAVA ([Java System Properties](https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html)).

| Propriedade                | Obrigatório | Valor padrão                                                          | Descrição                                                                                  |
|-------------------------|-------------|-----------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| page.size        | Não         | 10                                                                     | Quantidade de ítens por página exibidos na consulta de documentos.                                                                       |
| cors.origin        | Não         | http://localhost:4200                                                                     | Qual origin pode acessar os recursos [Access-Control-Allow-Origin](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Headers/Access-Control-Allow-Origin)  <br/>- Exemplo `-Dcors.origin=http://192.168.15.198:8081,http://192.168.15.198:8080` <br/>- Exemplo para teste permitir todas as origens `-Dcors.origin=*`                                                                     |

### Base

- Database: neoway

### Frontend

- [NeowayDocumentoWeb](https://github.com/renatoalvesbelem/neoway-documento-web)

### Dockerhub

- [neoway-documento-api](https://hub.docker.com/repository/docker/renatoalvesbelem/neoway-documento-api)

### Dockerfile para teste

- [Dockerfile](https://github.com/renatoalvesbelem/neoway-documento-dockerfile)

## Suporte

### Rota de suporte

- /status

### Swagger:

- /swagger-ui.html

### Documentação da API:

- /v2/api-docs
