#!/bin/bash

cp -r site/* build/webapp/
rm -fr build/dest/*
cp site/WEB-INF/menu/menu-schema.xsd build/dest/

COCOON_HOME=/home/gps/projects/astrogrid/workspace/cocoon-2.1.2

if [ -e "$PWD/cli.xconf" ]
then
  echo "local config file"
  COCOON_CONF=$PWD/cli.xconf
else
  echo "cocoon config file"
  COCOON_CONF=$COCOON_HOME/cli.xconf
fi

if [ "$1" != "" ]
then
  COCOON_FILE=$1
fi

echo ". $COCOON_HOME/cocoon.sh cli -x$COCOON_CONF $COCOON_FILE"

cd $COCOON_HOME
$COCOON_HOME/cocoon.sh cli -x$COCOON_CONF $COCOON_FILE

cd $OLDPWD
