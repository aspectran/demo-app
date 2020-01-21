#!/bin/bash

source app.conf

echo "Installing application to $BASE_DIR ..."

if [ ! -d "$BUILD_DIR" ]; then
  mkdir "$BUILD_DIR"
  cd "$BUILD_DIR" || exit
  git clone "$REPO"
  cd "$REPO_DIR" || exit
fi

[ ! -f "$BASE_DIR"/app.conf ] && cp app.conf "$BASE_DIR"
cp "$REPO_DIR"/setup/scripts/*.sh "$BASE_DIR"
chmod +x "$BASE_DIR"/*.sh

echo "Your application installation is complete."