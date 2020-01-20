#!/bin/bash

source app.conf

cd "$REPO_DIR" || exit
git pull origin master

rm -rf "${DEPLOY_DIR:?}"/config/*
rm -rf "${DEPLOY_DIR:?}"/webapps/*
cp -pR "$REPO_DIR"/app/config/* "$DEPLOY_DIR"/config
cp -pR "$REPO_DIR"/app/webapps/* "$DEPLOY_DIR"/webapps

echo "Done."