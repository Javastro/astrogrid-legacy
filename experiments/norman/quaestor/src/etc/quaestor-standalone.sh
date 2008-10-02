#! /bin/sh -
#
# Start the Jetty Scheme server.~
#
# Usage:
#
#    quaestor-standalone.sh [--help] [--verbose] [--port=n]
#
# This should be removed in the medium term, and replaced with 
# a single jar file which can be run standalone.

PATHTOHERE=`dirname $0`
D=`cd $PATHTOHERE; pwd`
cd $D

if test -n "$JAVA_HOME"; then
    PATH=$JAVA_HOME/bin:$PATH
fi

echo "Start Quaestor jetty server, v@VERSION@, @RELEASEDATE@"

CP=
for j in lib/*.jar; do CP=${CP}$j:; done

echo java -cp ${CP}war/WEB-INF/classes org.eurovotech.quaestor.JettySchemeServer "$@"
exec java -cp ${CP}war/WEB-INF/classes org.eurovotech.quaestor.JettySchemeServer "$@"

# shouldn't get here
echo "Exec failed!" >&2
exit 1
