# Project 0 - RESTful API
Poject 0 create is for Revature training purpose. It's simple api for bank. The RESTful API application supports two main resources: clients and accounts.

### Technologies
1. Java
2. Javalin 
3. JDBC
4. Logback / SLF4J
5. JUnit
6. Mockito
7. MariaDB 
8. Maven


### AWS Instance
User: ec2-user

HostName: 
http://ec2-18-223-190-254.us-east-2.compute.amazonaws.com:7000/

Port: 22


### Endpoints
- `POST /client`: Creates a new client
- `GET /client`: Gets all clients
- `GET /client/{client_id}`: Get client with an id of X (if the client exists)
- `PUT /client/{client_id}`: Update client with an id of X (if the client exists)
- `DELETE /client/{client_id}`: Delete client with an id of X (if the client exists)
- `POST /client/{client_id}/accounts`: Create a new account for a client with id of X (if client exists)
- `GET /client/{client_id}/account`: Get all accounts for client with id of X (if client exists)
- `GET /client/{client_id}/account?amountLessThan=2000&amountGreaterThan=400`: Get all accounts for client id of X with balances between 400 and 2000 (if client exists)
- `GET /client/{client_id}/account/{account_id}`: Get account with id of Y belonging to client with id of X (if client and account exist AND if account belongs to client)
- `PUT /client/{client_id}/account/{account_id}`: Update account with id of Y belonging to client with id of X (if client and account exist AND if account belongs to client)
- `DELETE /client/{client_id}/account/{account_id}`: Delete account with id of Y belonging to client with id of X (if client and account exist AND if account belongs to client)
-`DELETE /client/{client_id}/accounts/all`: Delete all account
-`GET /client/{client_id}/account/accounts/all`: Get all account from a client with client name
