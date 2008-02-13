#! /bin/sh -
#
# Start the Jetty Scheme server.
#
# This should be removed in the medium term, and replaced with 
# a single jar file which can be run standalone.

PATHTOHERE=`dirname $0`
D=`cd $PATHTOHERE; pwd`

echo java -cp $D/quaestor-lib-@VERSION@.jar:$D/war/WEB-INF org.eurovotech.quaestor.SchemeJettyServer "$@"
java -cp $D/quaestor-lib-@VERSION@.jar:$D/war/WEB-INF org.eurovotech.quaestor.SchemeJettyServer "$@"

exit 0
