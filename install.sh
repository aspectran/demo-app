#!/bin/bash

source $1

[ ! -d "$BUILD_DIR" ] && mkdir "$BUILD_DIR"

cd "$BUILD_DIR" || exit
git clone "$REPO"
cd "$REPO_DIR" || exit