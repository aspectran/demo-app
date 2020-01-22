#!/bin/bash

source app.conf

[ ! -d "$BUILD_DIR" ] && mkdir "$BUILD_DIR"

if [ ! -d "$REPO_DIR" ]; then
  cd "$BUILD_DIR" || exit
  git clone "$REPO" "$APP_NAME"
  cd "$REPO_DIR" || exit
else
  cd "$REPO_DIR" || exit
  # git fetch --all
  # git reset --hard origin/master
  git pull origin master
fi