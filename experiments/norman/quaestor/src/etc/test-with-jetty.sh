#! /bin/sh -

# Script to start up the jetty version of quaestor, run the protocol-test
# regression test, then take down the server

if test $# -lt 2; then
    echo "Usage: $0 <quaestor-build-file> <quaestor-jetty.zip>" >&2
    exit 1
fi
buildfile=$1
zipfile=$2

# we happen to know that this is the correct URL for the server
PORT=8081
URL=http://localhost:$PORT

if ! expr "$zipfile" : /; then
    # $zipfile doesn't start with a slash, so is relative
    zipfile=$PWD/$zipfile
fi
# we presume that the file path/foo.zip contains a directory 'foo'
zipname=`expr "$zipfile" : '.*/\([^/]*\).zip'`

WORK=`mktemp -d -t jetty` || exit 1

echo "Work directory $WORK, zipfile=$zipfile, zipname=$zipname"

cd $WORK
unzip $zipfile
sh $zipname/start-jetty.sh --port=$PORT >jetty.log 2>&1 &
jettypid=$!

jetty_status=0

echo "Jetty server is PID $jettypid"
echo "Giving it a chance to get going..."
while ! curl $URL >page.html 2>page.stderr; do
    echo "Not going yet..."
    sleep 2
    if ! ps | grep '^ *'$jettypid >/dev/null; then
        echo "The jetty server appears to have died!"
        jetty_status=1
        break
    fi
done

if test $jetty_status -eq 0; then
    echo "Vroom..."
    ant -buildfile $buildfile -Dquaestor.url=$URL protocol-tests
    ant_status=$?
else
    echo "Unable to start the jetty server"
    echo "See $WORK/jetty.log"
    ant_status=1
fi

if test $ant_status -eq 0; then
    echo "tidying up..."
    kill $jettypid
    rm -Rf $WORK
    echo "Done!"
else
    echo "Test failed"
    echo "Jetty URL is $URL : PID $jettypid"
    echo "Work directory is $WORK"
fi

exit $ant_status
