#!/bin/sh

PIDFILE=/var/run/bloottorrent.pid

java -jar target/bloodtorrent-1.0.0-SNAPSHOT.jar server src/main/resources/configurations.yml &
echo $! > $PIDFILE
