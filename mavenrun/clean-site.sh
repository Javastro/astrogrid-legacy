#!/bin/sh
OLDDIR=$PWD

PROJECT_NAME_2GO=$1

DATE=`date`

DOCSLOC=/var/www/www/maven/build
OLDLOG=$DOCSLOC/LOGS

echo "Cleaning old site from $DOCSLOC "
cd $DOCSLOC
ll
echo "deleting $DOCSLOC/$PROJECT_NAME_2GO"

echo "back to start dir: $OLDDIR"
cd $OLDDIR
echo
