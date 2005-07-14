#!/bin/bash
# $Id: bounce-tomcat.sh,v 1.13 2005/07/14 15:29:13 jdt Exp $ 
######################################################
# Script to do a hard bounce of tomcat
# Sometimes all processes fail to shutdown properly
# so this attempts to kill any hanging processes
######################################################
# Some reminders
echo Tomcat is installed at ${CATALINA_HOME?"Value of CATALINA_HOME must be set"}


echo Shutting down tomcat
$CATALINA_HOME/bin/shutdown.sh
echo "Waiting for Tomcat to shutdown..."
sleep 30
#30 seconds seems right at the moment...

echo "Killing any hanging processes..."
ps -elf | grep $CATALINA_HOME
TOMCATPID=`ps -elf | grep $CATALINA_HOME | grep java | awk {'print $4'}`

if [ "$TOMCATPID" ] 
then
	echo "Killing process $TOMCATPID"
	kill -9 $TOMCATPID
	echo "******THERE WERE HANGING PROCESSES!*********"
fi

echo "Wiping logs..."
rm -rf $CATALINA_HOME/logs/*
echo "Starting up...."
$CATALINA_HOME/bin/startup.sh
echo "Waiting...."
sleep 40


