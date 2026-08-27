#!/bin/bash
TABLE_NAME="StudentTable"
REGION=$(aws configure get region)

aws dynamodb create-table --table-name "$TABLE_NAME" --attribute-definitions AttributeName=roll,AttributeType=S --key-schema AttributeName=roll,KeyType=HASH --billing-mode PAY_PER_REQUEST --region "$REGION"
aws dynamodb wait table-exists --table-name "$TABLE_NAME" --region "$REGION"
aws dynamodb describe-table --table-name "$TABLE_NAME" --region "$REGION" --query "Table.[TableName,TableArn]" --output table
