#!/bin/bash

REGION=$(aws configure get region)
CLUSTER_NAME="student-memory"
ACL_NAME="student-acl"
USER_NAME="student-user"
SUBNET_GROUP="student-memory-subnet"
PASSWORD="StudentRedis123!"
VPC_ID=$(aws ec2 describe-vpcs --filters Name=is-default,Values=true --query "Vpcs[0].VpcId" --output text --region "$REGION")
SUBNET_IDS=$(aws ec2 describe-subnets --filters Name=vpc-id,Values="$VPC_ID" Name=availability-zone,Values=us-east-1a,us-east-1b,us-east-1c,us-east-1d,us-east-1f --query "Subnets[].SubnetId" --output text --region "$REGION")
echo "VPC: $VPC_ID"
echo "Subnets: $SUBNET_IDS"
aws  create-user --user-name "$USER_NAME" --authentication-mode "Passwords=$PASSWORD,Type=password" --access-string "on ~* +@all" --region "$REGION"
aws memorydb create-acl --acl-name "$ACL_NAME" --user-names "$USER_NAME" --region "$REGION"
aws memorydb create-subnet-group --subnet-group-name "$SUBNET_GROUP" --description "Student MemoryDB subnet group" --subnet-ids $SUBNET_IDS --region "$REGION"
aws memorydb create-cluster --cluster-name "$CLUSTER_NAME" --node-type db.t4g.small --num-shards 1 --acl-name "$ACL_NAME" --subnet-group-name "$SUBNET_GROUP" --engine valkey --region "$REGION"
