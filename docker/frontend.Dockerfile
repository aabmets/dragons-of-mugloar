FROM oven/bun:latest

USER root
WORKDIR /app

COPY ./frontend/package.json ./
COPY ./frontend/bun.lock ./

RUN bun install --dev

COPY ./docker/frontend-entrypoint.sh /scripts/

RUN chmod 755 /scripts/frontend-entrypoint.sh

ENTRYPOINT ["/scripts/frontend-entrypoint.sh"]
