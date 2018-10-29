echo -n "Starting starting webapp "
java8 -jar -Dspring.profiles.active=prod /home/ec2-user/target/api-gateway-1.0-SNAPSHOT.jar >> /home/ec2-user/webserver.log 2>&1  &
RETVAL=$?
PID=$!
[ $RETVAL -eq 0 ] && echo $PID > /home/ec2-user/webapp.pid