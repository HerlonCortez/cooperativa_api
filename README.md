# **API Cooperativa**

API de Votação desenvolvida em Java com Spring Boot, projetada sob a arquitetura Server-Driven UI (SDUI) para guiar a navegação e telas de aplicativos móveis em alta escala, alinhada aos princípios da Clean Architecture.

## **Arquitetura do Projeto (Clean Architecture)**

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

#**Server-Driven UI (SDUI)**

A interface do usuário é conduzida integralmente pelo backend. O fluxo de navegação do usuário é gerido via transição de contratos entre endpoints:

Tela 1 (FORMULARIO): Endpoint GET /v1/telas/votacao/{agendaId} retorna a estrutura da tela de identificação (solicitando o CPF no campo inputText).

Transição de Tela: O aplicativo lê as propriedades url e body do botão Avançar e faz um POST /v1/telas/selecao-voto enviando o CPF preenchido.

Tela 2 (SELECAO): A API valida a elegibilidade do associado e retorna os componentes da tela de voto (botões Votar SIM, Votar NÃO e Voltar).

Como Rodar o Projeto com Docker Compose

O projeto está totalmente containerizado para facilitar a execução local e de testes.

#**Pré-requisitos**
Docker e Docker Compose instalados.

**Passos para execução**

1 - Clone o repositório:

git clone git@github.com:HerlonCortez/cooperativa_api.git

2 - Suba o ambiente (Aplicação + PostgreSQL) via Docker Compose:

docker-compose up -d --build

**A API estará disponível em http://localhost:8080.**
**Swagger em: http://localhost:8080/swagger-ui/index.html**

#**Testes e Qualidade de Código**

1. Testes Unitários e de Integração

Para rodar a suíte de testes automatizados (MockK/Mockito, JUnit):

./mvnw clean test

2. Testes de Performance com Apache JMeter (Tarefa Bônus 2)
Para garantir resiliência e alta vazão do sistema sob concorrência intensa na escrita de votos, realizamos simulações de carga com o Apache JMeter.

Cenário do Teste Executado:
- Volume de Requisições: 1.000 requisições simultâneas submetendo votos em paralelo.
- Endpoint Testado: POST /v1/votos
- Geração de Dados: Uso de funções estocásticas do JMeter (${__Random}) para simulação de múltiplos CPFs únicos por requisição.

#**Como Executar o Teste de Carga e Gerar o Relatório HTML**
Execute o script de estresse armazenado na pasta do projeto através do JMeter:

**OBS: O teste que foi realizado encontra-se na pasta: relatorio-teste-performance na raiz do projeto.**

2.1 Limpa resultados de testes anteriores
rm -rf performance-tests/resultado.jtl performance-tests/relatorio-html

2.2 Executa o teste de estresse e gera o relatório completo em HTML
./apache-jmeter-5.6.3/bin/jmeter -n -t performance-tests/Teste_votacao.jmx -l performance-tests/resultado.jtl -e -o ./performance-tests/relatorio-html
