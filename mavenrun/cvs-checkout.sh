#!/bin/bash
# $Id: cvs-checkout.sh,v 1.3 2004/11/28 21:22:03 jdt Exp $ 
##############################################################
# Script to checkout a module, and maven-base
# First argument is module name
# If a second argument is supplied it is used as a tag for cvs
##############################################################
# 
# Environment variables that need to be set locally
# CHECKOUTHOME=/data/cvsDownloads/itn06

MAVENBASE=astrogrid/maven-base

if [-z "$CHECKOUTHOME"]; then
	echo "Value of CHECKOUTHOME (ie where to checkout sources) must be set"
	exit 1
fi

MODULE=$1
if [-z "$MODULE"]; then
	echo "Must supply a module name as first argument"
	exit 1
fi

TAG=$2
echo "Checking out $MODULE into "$CHECKOUTHOME"
if [-z "$TAG"]; then
	echo "from Head"
	# is there actually a branch called head?
else
	echo "from branch $TAG"
fi

#update from cvs 
OLDDIR=$PWD
cd $CHECKOUTHOME
rm -r $MAVENBASE
cvs checkout -r $TAG -P $MAVENBASE
rm -r $TESTMODULE
cvs checkout -r $TAG -P $TESTMODULE
cd $OLDDIR


