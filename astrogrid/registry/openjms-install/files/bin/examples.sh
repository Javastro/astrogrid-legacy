#!/bin/sh

if [ -z "$JAVA_HOME" ] ; then
  echo JAVA_HOME not set
  exit 1
fi

JAVA=$JAVA_HOME/bin/java

if [ "$OPENJMS_HOME"x = x ]; then
  OPENJMS_HOME=..
fi

POLICY_FILE=$OPENJMS_HOME/src/etc/openjms.policy

CP=$OPENJMS_HOME/build/examples
CP=$OPENJMS_HOME/lib/openjms-client-0.7.5-rc1.jar:$CP
CP=$OPENJMS_HOME/lib/openjms-rmi-0.7.5-rc1.jar:$CP
CP=$OPENJMS_HOME/lib/jta_1.0.1.jar:$CP
CP=$CP:$CLASSPATH

$JAVA -classpath $CP openjms.examples.$*
