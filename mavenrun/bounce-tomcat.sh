#!/bin/bash
# $Id: bounce-tomcat.sh,v 1.6 2004/12/07 12:43:46 jdt Exp $ 
######################################################
# Script to do a hard bounce of tomcat
# Sometimes all processes fail to shutdown properly
# so this attempts to kill any hanging processes
######################################################
# Some reminders
echo Tomcat is installed at ${CATALINA_HOME?"Value of CATALINA_HOME must be set"}

$CATALINA_HOME/bin/shutdown.sh
echo "Waiting for Tomcat to shutdown..."
sleep 30

echo "Killing any hanging processes..."
ps -elf | grep $CATALINA_HOME
TOMCATPID=`ps -elf | grep $CATALINA_HOME | grep java | awk {'print $4'}`
echo "Killing process $TOMCATPID"
kill -9 $TOMCATPID

echo "Wiping logs..."
rm -rf $CATALINA_HOME/logs/*
echo "Starting up...."
$CATALINA_HOME/bin/startup.sh
echo "Waiting...."
sleep 10
