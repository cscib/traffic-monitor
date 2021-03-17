Assumptions:
If filenames are not specified the app will be stopped
Run $ docker run -p 16379:6379 -d redis:6.0 redis-server --requirepass "mypass"
sudo docker exec -i ceed4edf9177 redis-cli -a "mypass" FLUSHALL


docker build -t myrabbitmqimage:v1 .
docker run -d --hostname my-rabbit --name some-rabbit -p 7777:15672 -p 5672:5672 -p 15674:15674 myrabbitmqimage:v1