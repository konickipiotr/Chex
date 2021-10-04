ps axf | grep 'java -jar Chex-0.0.1-SNAPSHOT.jar' | grep -v grep | sudo awk '{print "sudo kill -9 " $1}' | sh
java -jar target/Chex-0.0.1-SNAPSHOT.jar &
disown -h %1
