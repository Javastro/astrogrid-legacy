#!/bin/bash
# $Id: bounce-tomcat.sh,v 1.3 2004/11/28 21:35:06 jdt Exp $ 
######################################################
# Script to do a hard bounce of tomcat
# Sometimes all processes fail to shutdown properly
# so this attempts to kill any hanging processes
######################################################
# Some reminders
if [ -z "$CATALINA_HOME" ]; then
   echo "Value of CATALINA_HOME must be set"
   exit 1
fi

$CATALINA_HOME/bin/shutdown.sh
echo "Waiting for Tomcat to shutdown..."
sleep 10

# Should find a more subtle way of determining what's Tomcat and what's not!
echo "Killing any hanging processes..."
killall -9 java
echo "Wiping logs..."
rm -rf $CATALINA_HOME/logs/*
echo "Starting up...."
$CATALINA_HOME/bin/startup.sh
echo "Waiting...."
sleep 10
