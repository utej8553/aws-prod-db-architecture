#!/bin/bash
REGION=$(aws configure get region)
CLUSTER_NAME="student-memory"
ACL_NAME="student-acl"
SUBNET_GROUP="student-memory-subnet"
aws memorydb create-acl --acl-name "$ACL_NAME" --region "$REGION" 2>/dev/null || \
VPC_ID=$(aws ec2 describe-vpcs --filters Name=is-default,Values=true --query "Vpcs[0].VpcId" --output text --region "$REGION")
SUBNET_IDS=$(aws ec2 describe-subnets --filters Name=vpc-id,Values="$VPC_ID" --query "Subnets[].SubnetId" --output text --region "$REGION")
aws memorydb create-subnet-group --subnet-group-name "$SUBNET_GROUP" --subnet-ids $SUBNET_IDS --description "Student MemoryDB subnet group" --region "$REGION" 2>/dev/null || \
aws memorydb create-cluster --cluster-name "$CLUSTER_NAME" --node-type db.t4g.small --num-shards 1 --acl-name "$ACL_NAME" --subnet-group-name "$SUBNET_GROUP" --engine valkey --region "$REGION"
