#!/bin/bash
REGION=$(aws configure get region)
OUTPUT_FILE="../aws-values.env"
cat > "$OUTPUT_FILE" <<EOF
AWS_REGION=$REGION
EOF
DYNAMO_TABLE="StudentTable"
DYNAMO_ARN=$(aws dynamodb describe-table --table-name "$DYNAMO_TABLE" --region "$REGION" --query "Table.TableArn" --output text)
cat >> "$OUTPUT_FILE" <<EOF

DYNAMODB_TABLE=$DYNAMO_TABLE
DYNAMODB_ARN=$DYNAMO_ARN
EOF
AURORA_CLUSTER="student-aurora"
AURORA_ARN=$(aws rds describe-db-clusters --db-cluster-identifier "$AURORA_CLUSTER" --region "$REGION" --query "DBClusters[0].DBClusterArn" --output text)
AURORA_ENDPOINT=$(aws rds describe-db-clusters --db-cluster-identifier "$AURORA_CLUSTER" --region "$REGION" --query "DBClusters[0].Endpoint" --output text)
AURORA_PORT=$(aws rds describe-db-clusters --db-cluster-identifier "$AURORA_CLUSTER" --region "$REGION" --query "DBClusters[0].Port" --output text)
AURORA_DATABASE=$(aws rds describe-db-clusters --db-cluster-identifier "$AURORA_CLUSTER" --region "$REGION" --query "DBClusters[0].DatabaseName" --output text)
cat >> "$OUTPUT_FILE" <<EOF

AURORA_CLUSTER=$AURORA_CLUSTER
AURORA_ARN=$AURORA_ARN
AURORA_ENDPOINT=$AURORA_ENDPOINT
AURORA_PORT=$AURORA_PORT
AURORA_DATABASE=$AURORA_DATABASE
AURORA_USERNAME=admin
AURORA_PASSWORD=StudentDb123!
EOF
CACHE_NAME="student-cache"
CACHE_ARN=$(aws elasticache describe-serverless-caches --serverless-cache-name "$CACHE_NAME" --region "$REGION" --query "ServerlessCaches[0].ARN" --output text)
CACHE_ENDPOINT=$(aws elasticache describe-serverless-caches --serverless-cache-name "$CACHE_NAME" --region "$REGION" --query "ServerlessCaches[0].Endpoint.Address" --output text)
CACHE_PORT=$(aws elasticache describe-serverless-caches --serverless-cache-name "$CACHE_NAME" --region "$REGION" --query "ServerlessCaches[0].Endpoint.Port" --output text)
CACHE_STATUS=$(aws elasticache describe-serverless-caches --serverless-cache-name "$CACHE_NAME" --region "$REGION" --query "ServerlessCaches[0].Status" --output text)
cat >> "$OUTPUT_FILE" <<EOF

ELASTICACHE_NAME=$CACHE_NAME
ELASTICACHE_ARN=$CACHE_ARN
ELASTICACHE_ENDPOINT=$CACHE_ENDPOINT
ELASTICACHE_PORT=$CACHE_PORT
ELASTICACHE_STATUS=$CACHE_STATUS
EOF
MEMORYDB_CLUSTER="student-memory"
MEMORYDB_ARN=$(aws memorydb describe-clusters --cluster-name "$MEMORYDB_CLUSTER" --region "$REGION" --query "Clusters[0].ARN" --output text)
MEMORYDB_ENDPOINT=$(aws memorydb describe-clusters --cluster-name "$MEMORYDB_CLUSTER" --region "$REGION" --query "Clusters[0].ClusterEndpoint.Address" --output text)
MEMORYDB_PORT=$(aws memorydb describe-clusters --cluster-name "$MEMORYDB_CLUSTER" --region "$REGION" --query "Clusters[0].ClusterEndpoint.Port" --output text)
MEMORYDB_STATUS=$(aws memorydb describe-clusters --cluster-name "$MEMORYDB_CLUSTER" --region "$REGION" --query "Clusters[0].Status" --output text)
cat >> "$OUTPUT_FILE" <<EOF

MEMORYDB_CLUSTER=$MEMORYDB_CLUSTER
MEMORYDB_ARN=$MEMORYDB_ARN
MEMORYDB_ENDPOINT=$MEMORYDB_ENDPOINT
MEMORYDB_PORT=$MEMORYDB_PORT
MEMORYDB_STATUS=$MEMORYDB_STATUS
EOF
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
BUCKET_NAME="student-db-architecture-$ACCOUNT_ID-$REGION"
S3_ARN="arn:aws:s3:::$BUCKET_NAME"
cat >> "$OUTPUT_FILE" <<EOF

S3_BUCKET=$BUCKET_NAME
S3_ARN=$S3_ARN
EOF
cat "$OUTPUT_FILE"
