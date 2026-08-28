#!/bin/bash
TABLE_NAME="StudentTable"
REGION=$(aws configure get region)
aws dynamodb create-table --table-name "$TABLE_NAME" --attribute-definitions AttributeName=roll,AttributeType=S --key-schema AttributeName=roll,KeyType=HASH --billing-mode PAY_PER_REQUEST --region "$REGION"

