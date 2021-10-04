#!/bin/bash
echo 'pre'
ps axf | grep 'java -jar Chex-0.0.1-SNAPSHOT.jar' | grep -v grep | sudo awk '{print "sudo kill -9 " $1}' | sh
echo 'mid'
cd target
java -jar Chex-0.0.1-SNAPSHOT.jar &
echo 'post'
