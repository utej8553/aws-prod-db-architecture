#!/bin/bash

REGION=$(aws configure get region)
OUTPUT_FILE="../spring/main/src/main/resources/application.properties"
cat > "$OUTPUT_FILE" <<EOF
spring.application.name=main
aws.region=$REGION
EOF
# DynamoDB
DYNAMO_TABLE="StudentTable"
aws dynamodb describe-table --table-name "$DYNAMO_TABLE" --region "$REGION" --query "Table.TableArn" --output text
cat >> "$OUTPUT_FILE" <<EOF
aws.dynamodb.table-name=$DYNAMO_TABLE
EOF
# Aurora
AURORA_CLUSTER="student-aurora"
AURORA_ENDPOINT=$(aws rds describe-db-clusters --db-cluster-identifier "$AURORA_CLUSTER" --region "$REGION" --query "DBClusters[0].Endpoint" --output text)
AURORA_PORT=$(aws rds describe-db-clusters --db-cluster-identifier "$AURORA_CLUSTER" --region "$REGION" --query "DBClusters[0].Port" --output text)
AURORA_DATABASE=$(aws rds describe-db-clusters --db-cluster-identifier "$AURORA_CLUSTER" --region "$REGION" --query "DBClusters[0].DatabaseName" --output text)
cat >> "$OUTPUT_FILE" <<EOF
spring.datasource.url=jdbc:postgresql://$AURORA_ENDPOINT:$AURORA_PORT/$AURORA_DATABASE
spring.datasource.username=postgres
spring.datasource.password=StudentDb123!
spring.datasource.driver-class-name=org.postgresql.Driver
EOF
# ElastiCache
CACHE_NAME="student-cache"
CACHE_ENDPOINT=$(aws elasticache describe-serverless-caches --serverless-cache-name "$CACHE_NAME" --region "$REGION" --query "ServerlessCaches[0].Endpoint.Address" --output text)
CACHE_PORT=$(aws elasticache describe-serverless-caches --serverless-cache-name "$CACHE_NAME" --region "$REGION" --query "ServerlessCaches[0].Endpoint.Port" --output text)
cat >> "$OUTPUT_FILE" <<EOF
aws.elasticache.endpoint=$CACHE_ENDPOINT
aws.elasticache.port=$CACHE_PORT
EOF
# MemoryDB
MEMORYDB_CLUSTER="student-memory"
MEMORYDB_ENDPOINT=$(aws memorydb describe-clusters --cluster-name "$MEMORYDB_CLUSTER" --region "$REGION" --query "Clusters[0].ClusterEndpoint.Address" --output text)
MEMORYDB_PORT=$(aws memorydb describe-clusters --cluster-name "$MEMORYDB_CLUSTER" --region "$REGION" --query "Clusters[0].ClusterEndpoint.Port" --output text)
cat >> "$OUTPUT_FILE" <<EOF
aws.memorydb.endpoint=$MEMORYDB_ENDPOINT
aws.memorydb.port=$MEMORYDB_PORT
aws.memorydb.username=student-user
aws.memorydb.password=StudentRedis123!
EOF
echo "application.properties updated:"
echo "$OUTPUT_FILE"
cat "$OUTPUT_FILE"
