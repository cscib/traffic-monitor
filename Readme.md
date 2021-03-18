# NOTE 1:  Had to annotate spring with @CrossOrigin(origins="http://localhost:4201") just to wrap the
# project because I am not proxying all frontend originating requests through a single proxy. I have nodeJS
# serving the frontend static and spring boot rest api's

# Note 2: For the same as reason as Note 1 had to give up using docker-compose due to time limits while
# troubleshooting a docker networking issue


#1. To Run the backend
docker run -p 16379:6379 -d redis:6.0 redis-server --requirepass "mypass"
docker build -t myrabbitmqimage:v1 .
docker run -d --hostname my-rabbit --name some-rabbit -p 7777:15672 -p 5672:5672 -p 15674:15674 myrabbitmqimage:v1

#2. Create Jar in build/libs
./gradlew build
java -jar build/libs/traffic-monitor-0.0.1-SNAPSHOT.jar

#3. Download the front end module from https://github.com/cscib/drones-simulator-ui
