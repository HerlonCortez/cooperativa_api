API de Votação desenvolvida em Java com Spring Boot, projetada sob a arquitetura Server-Driven UI (SDUI) para guiar a navegação e telas de aplicativos móveis em alta escala, alinhada aos princípios da Clean Architecture.

Arquitetura do Projeto (Clean Architecture)

A aplicação segue a divisão em camadas para isolar regras de negócio das dependências de infraestrutura, frameworks e acesso a dados[cite: 4]:

```text
src/main/java/com/cooperativa/voto/api/
├── controller/                  # Camada de Entrada/Apresentação (REST Controllers)
│   ├── api/                     # Interfaces dos contratos de API
│   ├── dto/                     # DTOs para requisição e resposta do controller
│   ├── AgendaControllerImpl     # Controlador para gerenciamento de pautas
│   ├── VoteControllerImpl       # Controlador para registro de votos
│   ├── VotingSessionControllerImpl # Controlador para sessões de votação
│   └── VotingUIControllerImpl   # Controlador com suporte a Server-Driven UI (SDUI)
│
├── domain/                      # Camada de Domínio (Regras de Negócio)
│   ├── dto/                     # Objetos de transferência internos do domínio
│   ├── entity/                  # Entidades de negócio
│   ├── exception/               # Exceções de negócio personalizadas
│   └── service/                 # Serviços com as regras e casos de uso da aplicação
│
├── infrastructure/              # Camada de Infraestrutura e Integrações
│   ├── client/                  # Clientes de comunicação externa (ex: OpenFeign)
│   ├── enums/                   # Enumeradores auxiliares de infraestrutura
│   ├── exception/               # Handlers e exceções de infraestrutura
│   └── repository/              # Interfaces e implementações de persistência (Spring Data JPA)
│
└── Application                  # Classe principal de inicialização (Spring Boot)

Server-Driven UI (SDUI)

A interface do usuário é conduzida integralmente pelo backend. O fluxo de navegação do usuário é gerido via transição de contratos entre endpoints:

Tela 1 (FORMULARIO): Endpoint GET /v1/telas/votacao/{agendaId} retorna a estrutura da tela de identificação (solicitando o CPF no campo inputText).

Transição de Tela: O aplicativo lê as propriedades url e body do botão Avançar e faz um POST /v1/telas/selecao-voto enviando o CPF preenchido.

Tela 2 (SELECAO): A API valida a elegibilidade do associado e retorna os componentes da tela de voto (botões Votar SIM, Votar NÃO e Voltar).

Como Rodar o Projeto com Docker Compose

O projeto está totalmente containerizado para facilitar a execução local e de testes.

Pré-requisitos
Docker e Docker Compose instalados.

Passos para execução
Clone o repositório:


