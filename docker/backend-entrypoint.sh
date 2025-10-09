#!/usr/bin/env bash

set -euo pipefail
cd /app
gradle --configuration-cache -t classes &
exec gradle --configuration-cache bootRun
