#!/bin/sh

if [ -z "$JAVA_HOME" ] ; then
  echo JAVA_HOME not set
  exit 1
fi

if [ "$OPENJMS_HOME"x = x ]; then
  OPENJMS_HOME=..
fi

CP=$OPENJMS_HOME/lib/openjms-0.7.5-rc1.jar
CLASSPATH=$CLASSPATH:$CP

$JAVA_HOME/bin/rmiregistry $*
