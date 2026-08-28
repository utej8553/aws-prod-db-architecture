#!/bin/bash
REGION=$(aws configure get region)
CACHE_NAME="student-cache"
aws elasticache create-serverless-cache --serverless-cache-name "$CACHE_NAME" --engine valkey --region "$REGION"
echo "Cache: $CACHE_NAME"
