#!/bin/sh

if [ -z "$JAVA_HOME" ] ; then
  echo JAVA_HOME not set
  exit 1
fi

JAVA=$JAVA_HOME/bin/java

if [ "$OPENJMS_HOME"x = x ]; then
  OPENJMS_HOME=..
fi

if [ $# != 2 ]; then 
  echo
  echo Converts an existing OpenJMS configuration to the latest release format
  echo
  echo 'usage: convertcfg <input> <output>'
  echo '  input  - the configuration file to convert'
  echo '  output - the output file to create'
  echo
  exit 1
fi 

POLICY_FILE=$OPENJMS_HOME/src/etc/openjms.policy

CP=$OPENJMS_HOME/lib/xerces-2.3.0.jar
CP=$OPENJMS_HOME/lib/$XSLP$:$CP
CP=$CP:$CLASSPATH

$JAVA -Djava.security.policy=$POLICY_FILE -classpath $CP com.kvisco.xsl.XSLProcessor -h -s $OPENJMS_HOME/bin/convertcfg.xsl -i $1 -o $2
