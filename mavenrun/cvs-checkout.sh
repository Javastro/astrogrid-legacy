#!/bin/bash
# $Id: cvs-checkout.sh,v 1.11 2004/12/01 22:18:01 jdt Exp $ 
##############################################################
# Script to checkout a module, and maven-base
# First argument is module name
# If a second argument is supplied it is used as a tag for cvs
##############################################################
# 
# Environment variables that need to be set locally
# CHECKOUTHOME=/data/cvsDownloads/itn06

MAVENBASE=astrogrid/maven-base

if [ -z "$CHECKOUTHOME" ]; then
	echo "Value of CHECKOUTHOME (ie where to checkout sources) must be set"
	exit 1
fi

MODULE=$1
if [ -z "$MODULE" ]; then
	echo "Must supply a module name as first argument"
	exit 1
fi

TAG=$2
TAG=${TAG:-HEAD}
echo "Checking out $MODULE($TAG) into $CHECKOUTHOME"

#update from cvs 
OLDDIR=$PWD
if [ -d $CHECKOUTHOME ]; then
   echo
else
   echo "Creating $CHECKOUTHOME"
   mkdir $CHECKOUTHOME
fi

cd $CHECKOUTHOME
rm -r $MAVENBASE
if cvs checkout -r $TAG -P $MAVENBASE; then
   echo "OK"
else
   exit 1
fi
   
rm -r $MODULE
if cvs checkout -r $TAG -P $MODULE; then
   echo "OK"
else
   exit 1
fi
cd $OLDDIR


