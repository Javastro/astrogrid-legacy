#!/bin/sh
# -----------------------------------------------------------------------------
# Start script for OpenJMS Administrator.
#
# $Id: admin.sh,v 1.1 2003/07/25 12:44:38 gps Exp $
# -----------------------------------------------------------------------------

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '.*/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done
 
PRGDIR=`dirname "$PRG"`
EXECUTABLE=openjms.sh

# Check that target executable exists
if [ ! -x "$PRGDIR"/"$EXECUTABLE" ]; then
  echo "Cannot find $PRGDIR/$EXECUTABLE"
  echo "This is required to run OpenJMS Administrator."
  exit 1
fi

exec "$PRGDIR"/"$EXECUTABLE" admin "$@"
