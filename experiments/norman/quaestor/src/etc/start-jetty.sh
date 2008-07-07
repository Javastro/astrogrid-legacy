#! /bin/sh -
#
# Start the Jetty Scheme server.
#
# Usage:
#
#    start-jetty.sh [--help] [--verbose] [--port=n]
#
# This should be removed in the medium term, and replaced with 
# a single jar file which can be run standalone.

PATHTOHERE=`dirname $0`
D=`cd $PATHTOHERE; pwd`
cd $D

echo "Start Quaestor jetty server, v@VERSION@, @RELEASEDATE@"

exec java -cp $D/@QUAESTORLIBJAR@:$D/war/WEB-INF/classes org.eurovotech.quaestor.JettySchemeServer "$@"

# shouldn't get here
echo "Exec failed!" >&2
exit 1
