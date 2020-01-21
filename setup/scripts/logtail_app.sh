#!/bin/bash

source app.conf

tail -f "$DEPLOY_DIR/logs/app.log"