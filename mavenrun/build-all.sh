#!/bin/bash
# $Id: build-all.sh,v 1.3 2004/12/20 12:02:26 jdt Exp $ 
##################################################
#
#  Build all the astrogrid components
#
#  This is a bit of a mess at the moment due 
#  to the dependencies begin completely tangled up
#
#  To use: find the component highest up the list
#  that's changed, and comment out all before it
###################################################
OLDDIR=$PWD

build-component.sh common $1
build-component.sh ui $1
build-component.sh workflow-objects $1
build-component.sh eXist $1
build-component.sh registry $1
build-component.sh filestore $1
build-component.sh community $1       #client
build-component.sh filemanager $1
build-component.sh slinger $1
build-component.sh security $1
build-component.sh mySpace $1
build-component.sh community $1       #server

build-component.sh jes $1 #client - will fail, build client only
cd $CHECKOUTHOME/astrogrid/jes
maven -p delegate-project astrogrid-install-artifact

build-component.sh applications $1
build-component.sh workflow $1
build-component.sh datacenter $1
build-component.sh scripting $1
build-component.sh jes $1
build-component.sh deployment $1
build-component.sh portal $1
build-component.sh aggregate $1