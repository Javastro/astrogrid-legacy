export JAVA_OPTIONS="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=y"

./cocoon.sh $*
