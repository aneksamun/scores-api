# Scores API  

The backend which registers game scores for different users at different levels, with the capability to return high score lists per level. There also simple login system to authorize users.   

The system is built from scratch using sockets and Java NIO Non-blocking API to implement custom REST layer. To dispatch and handle request concurrently the [Reactor pattern](https://web.archive.org/web/20100726184112/http://today.java.net/article/2007/02/08/architecture-highly-scalable-nio-based-server) has been used. 

## Endpoints

| Path                         | Method | Response    | Details                                                                                                                                         |
| ---------------------------- | ------ | ----------- | ----------------------------------------------------------------------------------------------------------------------------------------------- |
| /login                       | POST   | 200         | Generates new session key for user to authenticate. The key is valid for 10 minutes.                                                            |
| /levels/{level}/users/{name} | POST   | 200/401/400 | Posts a user's score to a level. Requires session key to be present in `Authorization` header. Similar the score should be present in the body. |
| /levels/{level}/scores       | GET    | 200         | Returns a high score list for a level.                                                                                                          |

## Build and run instructions

- build 
```
./mvnw clean install
```
- run JAR
```
cd code/score-api-daemon/target/
java -jar score-api-daemon-1.0.jar
```
- run and stop Docker image
```
cd code/score-api-daemon/
docker-compose up -d
docker ps
docker-compose stop
```