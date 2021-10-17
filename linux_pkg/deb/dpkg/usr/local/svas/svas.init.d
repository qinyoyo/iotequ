#!/bin/sh
#chkconfig:345 9999 99
#description: svas web service

PROG="svas"
PROG_PATH="/usr/local/svas/"
PID_PATH="/var/run"
 
start() {
   PID=`ps -ef | grep '/usr/local/svas/svas.jar' | grep -v grep |awk '{print $2}' | head -n 1`
   if [ ! $PID ]; then
      cd $PROG_PATH
      sudo runuser -u svas ./svas.jar 2>&1 >>log/iotequ.log &
      echo "$PROG started"
    else
      ## Program is running, exit with error.
      echo "Error! $PROG is currently running!" 1>&2
      exit 1
    fi
}
 
stop() {
     PID=`ps -ef | grep '/usr/local/svas/svas.jar' | grep -v grep |awk '{print $2}' | head -n 1`
     if [ ! $PID ]; then
        echo "Error! $PROG not started!" 1>&2
        exit 1
     else
        kill $PID       
        echo "$PROG stopped"
     fi
}

status() {
     PID=`ps -ef | grep '/usr/local/svas/svas.jar' | grep -v grep |awk '{print $2}' | head -n 1`
     if [ ! $PID ]; then
        echo "$PROG not started!"
     else 
        echo "$PROG started, pid = $PID"
     fi
}
 
## Check to see if we are running as root first.
## Found at http://www.cyberciti.biz/tips/shell-root-user-check-script.html
if [ "$(id -u)" != "0" ]; then
    echo "This script must be run as root" 1>&2
    exit 1
fi
 
case "$1" in
    start)
        start
        exit 0
    ;;
    stop)
        stop
        exit 0
    ;;
    status)
        status
        exit 0
    ;;
    reload|restart|force-reload)
        stop
        start
        exit 0
    ;;
    **)
        echo "Usage: $0 {start|stop|reload|status}" 1>&2
        exit 1
    ;;
esac
