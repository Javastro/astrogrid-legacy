export COCOON_WEBAPP_HOME=$PWD/build/webapp
export COCOON_HOME=/home/gps/projects/astrogrid/workspace/cocoon-2.1.2

./prepare.sh

$COCOON_HOME/cocoon.sh $*
