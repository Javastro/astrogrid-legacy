#!/bin/bash
####################################
# cvs-checkout-clean
# Gets a clean copy from cvs
# First argument: module to checkout
# Second argument: tag
####################################

MODULE=$1
TAG=$2

if [ -z "$MODULE" ]; then
	echo "Must supply a module name as first argument"
	exit 1
fi

if [ -z "$TAG" ]; then
	echo "Must supply a tag as second argument"
	exit 1
fi

rm -rf $MODULE
if cvs checkout -r $TAG -P $MODULE; then
   echo "OK"
else
   exit 1
fi
