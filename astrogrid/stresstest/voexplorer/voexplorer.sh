#!/bin/sh
# <meta:header>
#     <meta:title>
#         AstroGrid runtime startup script.
#     </meta:title>
#     <meta:description>
#         This script configures and starts the AstroGrid runtime in a repeatable way.
#         Note : This script needs to be run from a test user account.
#     </meta:description>
#     <meta:licence>
#         Copyright (C) AstroGrid. All rights reserved.
#         This software is published under the terms of the AstroGrid Software License version 1.2.
#         See [http://software.astrogrid.org/license.html] for details. 
#     </meta:licence>
#     <svn:header>
#         $LastChangedRevision: 286 $
#         $LastChangedDate: 2007-09-06 12:49:40 +0100 (Thu, 06 Sep 2007) $
#         $LastChangedBy: dmorris $
#     </svn:header>
# </meta:header>

#
# Set JAVA_HOME environment variable.
export JAVA_HOME=${JAVA_HOME:-/usr/java/default}

#
# Test settings.
TEST_DATE=`date "+%Y-%m-%d %H:%M"`
TEST_USER=`whoami`
TEST_WORK=~/work
TEST_HOME=~

#
# Log file settings.
LOG_HOME=~/logs
SHARED_LOG_HOME=/var/shared/logs
LOG_FILE="`host \`hostname\` | gawk '{print $4}'`-`whoami`-`date "+%Y%m%d-%H%M$S"`-voexplorer.log"

#
# VOExplorer version and repo.
VOEXPLORER_REPO=http://software.astrogrid.org/voexplorer
#VOEXPLORER_VERSION=c1eng-snapshot-3
VOEXPLORER_VERSION=c1eng-snapshot-5
VOEXPLORER_JAR=voexplorer-${VOEXPLORER_VERSION}-app.jar

#
# VOExplorer memory settings.
VOEXPLORER_MEM=${VOEXPLORER_MEM:-128M}

#
# The delay interval for polling job status.
#CEA_POLLING=10

#
# Check the Java settings.
echo ""
echo "Checking Java settings .."
echo " JAVA_HOME : ${JAVA_HOME}"
if [ ! -e ${JAVA_HOME} ]
then
    echo "Unable to locate JAVA_HOME"
    exit 1
fi

if [ ! -e ${JAVA_HOME}/bin/java ]
then
    echo "Unable to locate JAVA_HOME/bin/java"
    exit 1
fi

if [ -e ${JAVA_HOME}/bin/java ]
then
    ${JAVA_HOME}/bin/java -version
fi

#
# Download the VOExplorer jar.
echo ""
echo "Checking VOExplorer version"
if [ ! -f ${TEST_HOME}/${VOEXPLORER_JAR} ]
then
    echo ""
    echo "Downloading VOExplorer jar file"
    echo "${VOEXPLORER_REPO}/${VOEXPLORER_JAR}"
	wget -q -P ${TEST_HOME} ${VOEXPLORER_REPO}/${VOEXPLORER_JAR}
fi

#
# Create the logging directory.
echo ""
echo "Checking log directory"
echo "  ${LOG_HOME}"
if [ ! -d ${LOG_HOME} ]
then
#    echo "Creating new log directory"
#	mkdir ${LOG_HOME}
    echo "Creating symlink to shared log directory"
        ln -s ${SHARED_LOG_HOME} ${LOG_HOME}
fi

#
# Create the working directory.
echo ""
echo "Checking work directory"
echo "  ${TEST_WORK}"
#if [ -d ${TEST_WORK} ]
#then
#    echo "Deleting old work directory"
#	rm -rf ${TEST_WORK}
#fi
if [ ! -d ${TEST_WORK} ]
then
    echo "Creating new work directory"
	mkdir ${TEST_WORK}
fi

#
# Create the log4j config.
if [ ! -e ${TEST_HOME}/log4j.properties ]
then
echo ""
echo "Creating log4j config"
cat > ${TEST_HOME}/log4j.properties << EOF
#
# The default CONSOLE output.
log4j.rootLogger=CONSOLE
#
# Configure the AstroGrid messages.
log4j.logger.org.astrogrid=DEBUG, CONSOLE, DEBUGLOG
#
# Configure HiveMind messages.
log4j.logger.org.apache.hivemind=DEBUG, CONSOLE, DEBUGLOG

#
# Set the Axis enterprise logger.
log4j.logger.org.apache.axis.enterprise=FATAL, CONSOLE, DEBUGLOG
#
# Ignore Axis debug messages.
log4j.category.org.apache.axis=WARN, CONSOLE, DEBUGLOG
#
# The default CONSOLE output.
log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender
log4j.appender.CONSOLE.Threshold=DEBUG
log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout
log4j.appender.CONSOLE.layout.ConversionPattern=%d{ISO8601} voexplorer %5p %m%n
#
# DEBUGLOG is written to the local logs directory.
log4j.appender.DEBUGLOG=org.apache.log4j.FileAppender 
log4j.appender.DEBUGLOG.File=${LOG_HOME}/${LOG_FILE}
log4j.appender.DEBUGLOG.Append=true 
log4j.appender.DEBUGLOG.Threshold=DEBUG 
log4j.appender.DEBUGLOG.layout=org.apache.log4j.PatternLayout 
log4j.appender.DEBUGLOG.layout.ConversionPattern=%d{ISO8601} voexplorer %5p %m%n
EOF
fi

#
# Check for orphaned astrogrid-desktop.
if [ -f ~/.astrogrid-desktop ]
then
    rm .astrogrid-desktop
fi

#
# Check for an Xvfb display number.
if [ -e ~/xvfb.display ]
then
    XVFB_DISPLAY=`cat ~/xvfb.display`
else
    XVFB_DISPLAY=0
fi

#
# Run Xvfb and grab the process ID.
XVFB_EXE=/usr/X11R6/bin/Xvfb
if [ ${XVFB_DISPLAY} -ne 0 ]
then
    echo ""
    echo "Starting Xvfb"
    echo "  DISPLAY "
    ${XVFB_EXE} :${XVFB_DISPLAY} &
    XVFB_PID=$!
    export DISPLAY=:${XVFB_DISPLAY}
else
    XVFB_PID=0
fi

echo ""
echo "Starting VOExplorer ..."
cd ${TEST_HOME}
${JAVA_HOME}/bin/java \
    -Xmx${VOEXPLORER_MEM} \
    -Dlog4j.configuration=file:${TEST_HOME}/log4j.properties \
    -Dsystem.systray.disabled=true \
    -Dsystem.doSnitch=false \
    -Dastrogrid.workdir=${TEST_WORK} \
    -jar ${VOEXPLORER_JAR}
#
# Stop the Xvfb instance.
echo "Stopping Xvfb"
if [ ${XVFB_PID} -ne 0 ]
then
    kill ${XVFB_PID}
fi 

#
# Wait for use to say ok.
echo ""
echo "Test done ..."
read OK


