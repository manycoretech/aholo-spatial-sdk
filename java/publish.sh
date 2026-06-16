#!/usr/bin/env bash
set -euo pipefail

# Publish Aholo Java SDK artifacts to Maven Central (Sonatype Central Portal).
#
# Prerequisites:
#   1. Maven >= 3.6, JDK 17 or 21 for building (artifacts target Java 8+)
#   2. Sonatype Central User Token in settings.xml:
#        <server>
#          <id>central</id>
#          <username>TOKEN_USER</username>
#          <password>TOKEN_PASS</password>
#        </server>
#   3. Release signing GPG secret key imported (gpg --import /path/to/private-key.asc)
#   4. Use a dedicated settings.xml for Maven Central only. Do not pass a
#      corporate or default settings file that sets altReleaseDeploymentRepository
#      or altSnapshotDeploymentRepository (deploy would go to a private registry).
#
# Usage:
#   ./publish.sh
#   ./publish.sh --settings /path/to/maven-central-settings.xml
#   ./publish.sh --dry-run
#   MAVEN_SETTINGS=/path/to/maven-central-settings.xml ./publish.sh
#
# Environment:
#   MAVEN_SETTINGS   Path to settings.xml (overridden by --settings)
#   GPG_PASSPHRASE   Optional. If unset, the script prompts once in the terminal.
#                    Maven forks gpg without pinentry; loopback avoids
#                    "Device not configured". Do not commit this value.

ROOT="$(cd "$(dirname "$0")" && pwd)"
DRY_RUN=false
SETTINGS_FILE="${MAVEN_SETTINGS:-}"
GPG_PROFILE=""
GPG_OPTS=()

MODULES=(
  aholo-sdk-core
  aholo-sdk-asset
  aholo-sdk-world
  aholo-sdk-lux3d
)

log() { echo "[publish] $*"; }
die() { echo "[publish] ERROR: $*" >&2; exit 1; }

cleanup() {
  unset GPG_PASSPHRASE
}
trap cleanup EXIT

while [[ $# -gt 0 ]]; do
  case "$1" in
    --dry-run) DRY_RUN=true; shift ;;
    --settings)
      [[ $# -ge 2 ]] || die "--settings requires a path"
      SETTINGS_FILE="$2"
      shift 2
      ;;
    -h|--help)
      sed -n '2,22p' "$0"
      exit 0
      ;;
    *) die "Unknown argument: $1" ;;
  esac
done

command -v mvn >/dev/null 2>&1 || die "mvn not found"
command -v gpg >/dev/null 2>&1 || die "gpg not found. Install gnupg and import the release signing key."

if [[ -n "$SETTINGS_FILE" ]]; then
  [[ -f "$SETTINGS_FILE" ]] || die "Settings file not found: $SETTINGS_FILE"
  log "Using settings: $SETTINGS_FILE"
else
  log "Using default Maven settings (~/.m2/settings.xml)"
  log "Tip: use --settings with a Maven Central-only file (no private-registry deploy redirect)."
fi

if ! gpg --list-secret-keys 2>/dev/null | grep -q '^sec'; then
  die "No GPG secret key found. Import the release signing key: gpg --import /path/to/private-key.asc"
fi

enable_gpg_loopback() {
  GPG_PROFILE="gpg-loopback"
  GPG_OPTS=(-Dgpg.passphrase="$GPG_PASSPHRASE")
}

setup_gpg_for_maven() {
  GPG_PROFILE=""
  GPG_OPTS=()
  if [[ -n "${GPG_PASSPHRASE:-}" ]]; then
    log "GPG: loopback mode (GPG_PASSPHRASE from environment)"
    enable_gpg_loopback
    return
  fi
  if ! tty -s </dev/tty 2>/dev/null; then
    die "No TTY. Run in iTerm/Terminal, or: GPG_PASSPHRASE='...' ./publish.sh ..."
  fi
  # Maven subprocesses cannot use pinentry (Device not configured / ioctl errors).
  printf "[publish] GPG passphrase (release signing key): " >&2
  read -rs GPG_PASSPHRASE </dev/tty
  echo >&2
  [[ -n "$GPG_PASSPHRASE" ]] || die "Empty GPG passphrase"
  log "GPG: loopback mode (passphrase entered)"
  enable_gpg_loopback
}

run_mvn() {
  # macOS bash 3.2 + set -u: do not expand empty GPG_OPTS[@] directly
  local -a cmd=("${MVN_SETTINGS[@]}" "$@" -P release)
  if [[ -n "$GPG_PROFILE" ]]; then
    cmd+=(-P "$GPG_PROFILE")
  fi
  if ((${#GPG_OPTS[@]} > 0)); then
    cmd+=("${GPG_OPTS[@]}")
  fi
  cmd+=(-pl "$module_list")
  mvn "${cmd[@]}"
}

setup_gpg_for_maven

MVN_SETTINGS=()
if [[ -n "$SETTINGS_FILE" ]]; then
  MVN_SETTINGS=(--settings "$SETTINGS_FILE")
fi

cd "$ROOT"
version="$(mvn "${MVN_SETTINGS[@]}" -q help:evaluate -Dexpression=project.version -DforceStdout)"
group_id="$(mvn "${MVN_SETTINGS[@]}" -q help:evaluate -Dexpression=project.groupId -DforceStdout)"

log "Coordinates: ${group_id}:${MODULES[*]} @ ${version}"

module_list="$(IFS=,; echo "${MODULES[*]}")"

if [[ "$DRY_RUN" == true ]]; then
  log "Dry-run: verify (compile, package, sign) — no upload to Central"
  run_mvn clean verify
else
  log "Deploying to Maven Central ..."
  run_mvn clean deploy
  log "Check status: https://central.sonatype.com → Deployments"
fi

log "Done."
