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

#echo java -cp $D/quaestor-lib-@VERSION@.jar:$D/war/WEB-INF org.eurovotech.quaestor.SchemeJettyServer "$@"
exec java -cp $D/quaestor-lib-@VERSION@.jar:$D/war/WEB-INF org.eurovotech.quaestor.SchemeJettyServer "$@"

# shouldn't get here
echo "Exec failed!" >&2
exit 1
