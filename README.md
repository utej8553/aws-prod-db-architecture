# complete-aws-prod-db-architecture
```
Aurora PostgreSQL(Main relational database): users, products, orders, payments

DynamoDB(NoSQL data): activity/history/events

ElastiCache(Redis	Temporary caching): frequently requested data

MemoryDB(Persistent high-speed application state): carts, sessions, real-time state

S3(File/object storage): images, documents, etc.
```

DataBase Schema: 
```
Student
├── name
├── roll
└── branch
```
