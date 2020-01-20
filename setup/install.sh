#!/bin/bash

source app.conf

if [ ! -d "$BUILD_DIR" ]; then
  mkdir "$BUILD_DIR"
  cd "$BUILD_DIR" || exit
  git clone "$REPO"
  cd "$REPO_DIR" || exit
fi

cp "$REPO_DIR"/setup/scripts/*.sh "$BASE_DIR"
chmod +x "$BASE_DIR"/*.sh