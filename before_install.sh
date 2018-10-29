sudo yum install -y java-1.8.0
pid=$(cat /home/ec2-user/webapp.pid)
if ! kill $pid > /dev/null 2>&1; then
        echo "Could not send SIGTERM to process $pid" >&2
fi