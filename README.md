# Dragons of Mugloar – Proof of Concept

## Overview
This repository contains a full-stack proof of concept for automating the "Dragons of Mugloar" freelance dragons trainer workflow described in the coding exercise brief. The solution wraps the public Mugloar game API with a Spring Boot middleware and a Vue 3 single-page application that helps players start games, analyse quests, purchase upgrades, and climb a community leaderboard. The stack is containerized for rapid local spin-up and relies on Redis for short-lived state persistence between frontend and backend interactions.

## Running the Solution
Execute the `run.sh` script in a Unix OS to build and run the stack locally (WSL, Linux, or macOS). It requires Docker and Docker Compose to be installed.
```shell
chmod 755 ./run.sh && ./run.sh
```


## System Architecture
| Layer      | Technology                    | Purpose                                                                                                                                                                            |
|------------|-------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Frontend   | Vue 3 + Vite + Vuetify        | Presents the playable interface, animation, and decision helpers such as difficulty visualisers, trap warnings, and a live leaderboard.                                            |
| Backend    | Spring Boot 3 + WebFlux       | Proxies the official Mugloar API, augments responses (e.g., UUID tracking, message decoding), exposes OpenAPI-documented REST endpoints, and stores transient game state in Redis. |
| Data cache | Redis 8                       | Keeps the latest `GameState` snapshots keyed by UUID so subsequent requests (solving ads, shopping) can reuse the original Mugloar `gameId`.                                       |
| Tooling    | Docker Compose + Bun + Gradle | Provides reproducible local development with hot reload for both services.                                                                                                         |

The `run.sh` helper loads optional environment secrets (e.g., OpenAI key), checks Docker availability, and boots the Compose stack that wires the backend, frontend, Redis, and RedisInsight UI together for local play.

## Backend Service
The backend is a Spring Boot application (`ee.bigbank.dragons`) that centralizes communication with the remote Mugloar servers and the Vue client. Key features include:

* **Game lifecycle endpoints.** `NewGameController` starts a fresh game, injects a UUID/username/timestamp, and caches the enriched `GameState` in Redis for future calls.
* **Message board handling.** `MessageBoardController` proxies the ads feed and, when solving a quest, reloads the cached UUID, calls the upstream solver, refreshes metadata (timestamp, level, username), and writes the new snapshot back to Redis. The `MessageBoard` model automatically decodes encrypted adverts by attempting Base64 and falling back to ROT13 so the frontend always receives readable content together with the decoding method used.
* **Shop and turn management.** `ShopController` exposes shop inventory, performs purchases against the Mugloar API, updates cached currency/lives/turn information, stores a human-readable message flag for the frontend, and also offers a "skip turn" helper implemented via the remote shop endpoint.
* **Reputation telemetry.** `ReputationController` surfaces the Mugloar reputation investigation endpoint so the frontend can keep faction standings current.
* **Historical leaderboard.** `GameHistoryController` streams cached game states from Redis, filters out zero-score runs, sorts them, and returns the top N entries for the leaderboard widget.
* **Client abstraction.** `DragonsApiClient` wraps the official `https://dragonsofmugloar.com/api/v2` endpoints with WebClient to provide strongly typed operations for starting games, reading boards, solving ads, buying items, and fetching reputation.
* **Infrastructure configuration.** `RedisConfig` supplies a `RedisTemplate<String,String>` bean for serializing cached game snapshots, while `WebConfig` redirects the backend root to the generated Swagger UI for easy manual inspection. Runtime properties allow host/port overrides and surface actuator health probes for Docker health checks.
* **Testing.** A Redis configuration context test verifies that the cache template is wired with a connection factory, supplementing the standard Spring Boot context smoke test.

## Frontend Application
The Vue 3 SPA (`frontend/src`) focuses on presenting actionable information with minimal clicks and several quality-of-life improvements:

* **Game entry flow.** `NewGameButton` animates into a name input, calls the backend to create a game, and raises an event that transitions the layout from the landing leaderboard to the interactive board. The root `App.vue` coordinates the fade/scale animations, resets state when the logo is clicked, and toggles between the leaderboard and game board views.
* **Status and decision aids.** `GameBoard` centralises player stats, shop access, difficulty sorting, and reputation monitoring in a glassmorphism panel. `StatusDisplay` visualises lives, gold, level, and score via icon "pills", while `ActionMessage` highlights the latest quest or purchase outcome and gracefully resets on skipped turns.
* **Dynamic message board.** `MessageBoard` fetches ads whenever the Mugloar turn counter changes, provides reward/expiry/probability sorting, and handles network errors until the dragon runs out of lives. Each `MessageCard` surfaces decoded adverts with tooltip metadata, warns about traps using heuristic detection, and disables the 'Solve' button accordingly to prevent accidental failures. Difficulty is re-rendered with color-coded rating bars derived from the canonical probability list.
* **Economy management.** The tavern `Shop` dialog presents product tiles backed by `const.ts` definitions, maps backend purchase responses to readable success/failure toasts, and includes a "Skip a Turn" button that forces a message board refresh without risking a life.
* **Reputation insights.** `ReputationBars` retrieves faction reputation after each turn and renders a bi-directional bar chart so players can adjust quest choices based on political standing.
* **Community leaderboard.** `LeaderBoard` polls the backend every three seconds for top runs, shows rank trophies for the podium, and lists live high scores, scores, and levels. Trophy icons are colour/size coded per rank for quick scanning.
* **Quality-of-life touches.** Floating dock shortcuts open the backend Swagger UI and RedisInsight dashboards, aiding debugging during the exercise. Vuetify theming, fonts, and proxy configuration are centralised in `main.ts`, `plugins/vuetify.ts`, and `vite.config.ts`, while Pinia `gameStore` keeps the current game and reputation state reactive across components.

## Middleware Data Contract
The backend enriches Mugloar responses with extra metadata fields (`uuid`, `timestamp`, `username`) captured by the shared `GameState` model and mirrored in the frontend TypeScript types. Purchase responses are normalized into success/failure cues, and encrypted adverts arrive decoded with their `decodedWith` flag so the UI can display trust hints.

## Deployment & Local Development
* **Docker-first workflow.** Compose builds two dev images: a Gradle-based backend container that runs `bootRun` with continuous compilation and a Bun-based frontend container serving Vite dev mode. Volumes mount the source directories for hot reload during development. Health checks ensure Redis and the backend are ready before the SPA starts.
* **Configuration.** Hosts/ports for both services are parameterized via environment variables (consumed by both Spring Boot and Vite), and the optional `.env` file can inject extra secrets such as an OpenAI API key referenced in `application.yml`.

## Gameplay Experience
Launching the stack presents a landing screen with animated branding, a polling leaderboard, and a call-to-action to start a game. Once a game begins, players can:

1. Track live stats and reputation to inform mission choices.
2. Sort and inspect quests, with automatic decryption, trap alerts, and difficulty heatmaps guiding safer selections.
3. Purchase upgrades or skip risky turns without leaving the board thanks to the integrated tavern dialog and backend shop endpoints.
4. See immediate feedback on actions through contextual alerts, refreshed stats, and leaderboard updates whenever Redis records improved scores.
