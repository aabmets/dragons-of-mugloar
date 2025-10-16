#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd -P)"
ENV_FILE="$SCRIPT_DIR/.env"

if [ -f "$ENV_FILE" ]; then
  set -a
  source "$ENV_FILE"
  set +a
fi

if [ -z "${OPENAI_API_KEY:-}" ] || [ "${OPENAI_API_KEY:-}" = "none" ]; then
  read -r -s -p "Enter an OpenAI API key to enable all app features (or press Enter to skip): " api_key
  echo
  if [ -n "${api_key:-}" ]; then
    printf '%s=%s\n' OPENAI_API_KEY "$api_key" > "$ENV_FILE"
    set -a; source "$ENV_FILE"; set +a
    echo "Saved OpenAI API key to $ENV_FILE."
  else
    printf '%s=%s\n' OPENAI_API_KEY "none" > "$ENV_FILE"
    echo "Proceeding without an OpenAI API key."
  fi
else
  echo "Found OpenAI API key in .env file."
fi

if ! command -v docker >/dev/null 2>&1 || ! docker compose version >/dev/null 2>&1; then
  echo "Error: 'docker compose' command is not available." >&2
  exit 1
fi

DOCKER_DIR="$SCRIPT_DIR/docker"
cd "$DOCKER_DIR"

printf "Running: docker compose up -d in %s\n\n" "$DOCKER_DIR"
docker compose up -d

printf '\nNavigate in your browser to http://localhost:30011 to play the game!\n\n'
