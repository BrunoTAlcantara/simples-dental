# Projeto

API desenvolvida com Spring Boot para gestão de produtos e categorias, com autenticação baseada em usuários (`admin` e `user`), controle de permissões, cache, documentação com Swagger e práticas recomendadas de arquitetura e segurança.

# Arquitetura aplicada

O projeto segue uma **estrutura modular em camadas**

### Camadas e responsabilidades:

- **Controller**: lida com as requisições HTTP.
- **Service**: contém a lógica de negócio da aplicação.
- **Repository**: abstrai o acesso ao banco de dados.
- **DTOs / Mapper**: fazem a mediação entre as entidades e o que é exposto na API.
- **Exception / Shared / Config**: englobam tratamento de exceções, modelos auxiliares, configurações globais e componentes reutilizáveis.

Essa organização garante **baixo acoplamento**, **alta coesão** e facilita a **manutenção**, **testes** e **evolução** da aplicação com segurança.

### Decisões de Projeto

#### Por que usar DTOs?

O uso de DTOs (*Data Transfer Objects*) ajuda a manter uma separação clara entre a camada de domínio (entidades) e a API pública da aplicação. Essa escolha traz diversos benefícios:

- **Segurança**: evita a exposição de dados sensíveis (ex: senhas, IDs internos).
- **Flexibilidade**: permite que a API evolua de forma independente do modelo de dados interno.
- **Validações específicas**: facilita a aplicação de validações diferentes entre criação, atualização e exibição.
- **Padrão de comunicação**: garante consistência nas entradas e saídas da API.
- **Facilidade nos testes**: trabalhar com dados simulados (mockados) nos testes fica mais simples.

---

#### Por que usar exceções globais?

O tratamento global de exceções, geralmente implementado com `@ControllerAdvice`, permite capturar e tratar erros de forma centralizada. Isso foi adotado no projeto pelos seguintes motivos:

- **Padronização de respostas de erro**: evita retornos genéricos ou inconsistentes.
- **Melhora a experiência do front-end**: facilita o tratamento de erros, já que todos seguem o mesmo formato.
- **Redução de repetição**: evita try-catch espalhado por todo o código.
- **Melhor monitoramento**: facilita o rastreamento de erros nos logs, com mensagens claras e estruturadas.
- **Separação de responsabilidades**: deixa a lógica de erro fora da regra de negócio, mantendo o código limpo.

# Estrutura do projeto

```text
com.simplesdental.product
├── auth          # Lógica de autenticação (login, registro, JWT, contexto do usuário)
├── bootstrap     # Carga inicial de dados (ex: admin padrão)
├── config        # Configurações globais (Swagger, Redis, CORS, segurança)
├── controller    # Camada de exposição da API (REST controllers)
├── dto           # Objetos de transferência de dados (requests e responses)
├── enums         # Enums usados no domínio (ex: status de produto)
├── exception     # Exceções customizadas e tratamento global de erros
├── logging       # Configurações e lógica de logging estruturado
├── mapper        # Conversão entre entidades e DTOs
├── model         # Entidades JPA (Product, Category, User, etc.)
├── repository    # Interfaces JPA para acesso ao banco de dados
├── service       # Lógica de negócio da aplicação
├── shared        # Modelos genéricos e utilitários (ex: paginação, resposta padrão)
└── ProductApplication.java  # Classe principal da aplicação
```

# Requisitos

## 1. Validações

Você deve ajustar as entidades (model e SQL) de acordo com as regras abaixo:

- ✔ `Product.name` é obrigatório, não pode ser vazio e deve ter no máximo 100 caracteres.
- ✔ `Product.description` é opcional e pode ter no máximo 255 caracteres.
- ✔ `Product.price` é obrigatório e deve ser > 0.
- ✔ `Product.status` é obrigatório.
- ✔ `Product.category` é obrigatório.
- ✔ `Category.name` deve ter no máximo 100 caracteres.
- ✔ `Category.description` é opcional e pode ter no máximo 255 caracteres.

## 2. Otimização de Performance

- ✔ Analisar consultas para identificar possíveis gargalos.
- ✔ Utilizar índices e restrições de unicidade quando necessário.
- ✔ Implementar paginação nos endpoints para garantir escalabilidade conforme o volume de dados crescer.
- ✔ Utilizar cache com `Redis` no endpoint `/auth/context`, garantindo que a invalidação ocorra em caso de alteração dos dados.

## 3. Logging

- ✔ Registrar logs em arquivos utilizando um formato estruturado (ex.: JSON).
- ✔ Implementar níveis de log: DEBUG, INFO, WARNING, ERROR, CRITICAL.
- ✔ Utilizar logging assíncrono.
- ✔ Definir estratégias de retenção e compressão dos logs.

## 4. Refatoração

- ✔ Atualizar a entidade `Product`:
    - ✔ Alterar o atributo `code` para o tipo inteiro.
- ✔ Versionamento da API:
    - ✔ Manter o endpoint atual (v1) em `/api/products` com os códigos iniciados por `PROD-`.
    - ✔ Criar uma nova versão (v2) em `/api/v2/products`, onde `code` é inteiro.

## 5. Integração com Swagger

- ✔ Documentar todos os endpoints com:
    - ✔ Descrições detalhadas.
    - ✔ Exemplos de JSON para requisições e respostas.
    - ✔ Listagem de códigos HTTP e mensagens de erro.

## 6. Autenticação e Gerenciamento de Usuários

- ✔ Criar a tabela `users` com as colunas:
    - ✔ `id` (chave primária com incremento automático)
    - ✔ `name` (obrigatório)
    - ✔ `email` (obrigatório, único e com formato válido)
    - ✔ `password` (obrigatório)
    - ✔ `role` (obrigatório, com valores permitidos: `admin` ou `user`)
- ✔ Inserir um usuário admin inicial:
    - ✔ Email: `contato@simplesdental.com`
    - ✔ Senha: `KMbT%5wT*R!46i@@YHqx`
- ✔ Endpoints:
    - ✔ `POST /auth/login` - Realiza login.
    - ✔ `POST /auth/register` - Registra novos usuários (se permitido).
    - ✔ `GET /auth/context` - Retorna `id`, `email` e `role` do usuário autenticado.
    - ✔ `PUT /users/password` - Atualiza a senha do usuário autenticado.

## 7. Permissões e Controle de Acesso

- ✔ Usuários com `role` admin podem criar, alterar, consultar e excluir produtos, categorias e outros usuários.
- ✔ Usuários com `role` user podem:
    - ✔ Consultar produtos e categorias.
    - ✔ Atualizar apenas sua própria senha.
    - ✔ Não acessar ou alterar dados de outros usuários.

## 8. Testes

- ✔ Desenvolver testes unitários para os módulos de autenticação, autorização e operações CRUD.

## Extras

- ✔ Implementar tratamento de exceções personalizados.
- ✔ Implementar Filtros por name
- ✔ Implementar testes de integração

---

# Perguntas e Respostas

### 1. **Se tivesse a oportunidade de criar o projeto do zero ou refatorar o projeto atual, qual arquitetura você utilizaria e por quê?**

Depende do contexto. Se o projeto for pequeno e pontual, uma arquitetura em camadas modularizada, como essa que estou utilizando, já atende bem pela simplicidade e rapidez de entrega.

Agora, pensando em um cenário de longo prazo ou em uma aplicação mais robusta, eu optaria por uma abordagem baseada em Clean Architecture, separando as responsabilidades entre domínio, aplicação, infraestrutura e interface. Isso garante baixo acoplamento e alta coesão, o que facilita a manutenção, os testes e a evolução do sistema ao longo do tempo.

---

### 2. **Qual é a melhor estratégia para garantir a escalabilidade do código mantendo o projeto organizado?**

O principal é manter o código simples e bem dividido desde o início. Usar boas práticas como SOLID, ter responsabilidades bem definidas entre as camadas e evitar acoplamentos desnecessários.

Manter uma boa cultura de code review, onde os desenvolvedores sejam capazes de avaliar e propor melhorias.

Testes unitários, tentar manter sempre um bom coverage do projeto.

---

### 3. **Quais estratégias poderiam ser utilizadas para implementar multitenancy no projeto?**

A estratégia vai depender do nível de isolamento que o projeto precisa. Em cenários mais simples, usar uma coluna `tenant_id` nas tabelas, junto com filtros automáticos do Hibernate, já resolve bem. Mas se o projeto exige um isolamento maior ou precisa seguir regras de compliance mais rígidas, o ideal é separar os dados por schema ou até mesmo por instância de banco de dados.

---

### 4. **Como garantir a resiliência e alta disponibilidade da API durante picos de tráfego e falhas de componentes?**

Usaria circuit breaker, timeouts, retries e fallback onde fizer sentido. Implementaria cache em pontos estratégicos (como nas chamadas mais frequentes). Na infraestrutura, adotaria balanceamento de carga, autoscaling e garantiria que o sistema tenha logs, métricas (Datadog ou similares) e alertas ativos para monitoramento contínuo.

---

### 5. **Quais práticas de segurança essenciais você implementaria para prevenir vulnerabilidades como injeção de SQL e XSS?**

Utilizaria validações com anotações do Bean Validation, como `@NotBlank`, `@Email`, entre outras, para garantir que os dados de entrada estejam corretos desde o início. Para evitar SQL injection, evitaria concatenar queries e usaria sempre JPA ou Prepared Statements. Já para prevenir XSS, aplicaria escaping adequado nas saídas, configuraria o CORS de forma restrita e usaria headers de segurança
como o `Content-Security-Policy`.

---

### 6. **Qual a abordagem mais eficaz para estruturar o tratamento de exceções de negócio, garantindo um fluxo contínuo desde sua ocorrência até o retorno da API?**

Usaria exceções customizadas (como `BusinessException`, `ModelNotFoundException`) e retornaria um modelo padronizado com código de erro, mensagem e timestamp. Isso facilita tanto para o front-end quanto para o time de back-end monitorar e tratar erros de forma consistente.

---

### 7. **Considerando uma aplicação composta por múltiplos serviços, quais componentes você considera essenciais para assegurar sua robustez e eficiência?**

- **Uma arquitetura bem definida** (como a Arquitetura Limpa), com separação clara de responsabilidades entre as camadas.
- **Logs estruturados e centralizados**, que ajudem a entender o comportamento da aplicação, mesmo em produção.
- **Monitoramento com alertas**, usando ferramentas como Prometheus, Grafana ou similares, para detectar falhas rapidamente.
- **Cache bem utilizado** (como Redis), para melhorar o desempenho nos pontos críticos.
- Uma **estratégia sólida de autenticação e autorização**, mesmo que simples, usando JWT ou sessão, conforme o cenário.

---

### 8. **Como você estruturaria uma pipeline de CI/CD para automação de testes e deploy, assegurando entregas contínuas e confiáveis?**

Criaria uma pipeline com etapas de build, testes unitários, análise estática (SonarQube) e geração de artefatos Docker. Depois disso, faria o deploy automático para o ambiente de homologação, com deploy em produção condicionado à aprovação manual (por dois responsáveis, ex: owner + dev). Também implementaria uma estratégia de rollback em caso de falhas, tudo versionado com tags.


---



