To build

./mvnw clean install

To run JAR

cd code/score-api-daemon/target/
java -jar score-api-daemon-1.0.jar

To run as Docker

cd code/score-api-daemon/
docker-compose up -d
docker ps
docker-compose stop

REST

1. Login
POST localhost:8090/login

2. Post a user's score to a level
POST localhost:8090/levels/{any number}/users/{username}
requires
    - To have authorisation via header "Authorization" : "Session Key"
    - Have scores in body, for example, 33

3. Get a high score list for a level
GET localhost:8090/levels/{any number}/scores
