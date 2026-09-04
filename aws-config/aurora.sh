#!/bin/bash

REGION=$(aws configure get region)
CLUSTER_ID="student-aurora"
DB_INSTANCE_ID="student-aurora-instance"
DB_NAME="studentdb"
DB_USER="postgres"
DB_PASSWORD="StudentDb123!"
aws rds create-db-cluster --db-cluster-identifier "$CLUSTER_ID" --engine aurora-postgresql --database-name "$DB_NAME" --master-username "$DB_USER" --master-user-password "$DB_PASSWORD" --region "$REGION"
aws rds create-db-instance --db-instance-identifier "$DB_INSTANCE_ID" --db-cluster-identifier "$CLUSTER_ID" --engine aurora-postgresql --db-instance-class db.t4g.medium --region "$REGION"
