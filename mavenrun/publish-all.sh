#!/bin/bash
# $Id: publish-all.sh,v 1.4 2005/02/15 16:41:03 jdt Exp $ 
#
#####################################################
#  Assumes there won't be any dependency problems...
#  probably best to have everything built
#  locally first
#####################################################
publish-component.sh common $1
publish-component.sh xmldb $1
publish-component.sh ui $1
publish-component.sh workflow-objects $1
publish-component.sh eXist $1
publish-component.sh registry $1
publish-component.sh filestore $1
publish-component.sh filemanager $1
publish-component.sh community $1
publish-component.sh mySpace $1
publish-component.sh slinger $1
publish-component.sh security $1
publish-component.sh applications $1
publish-component.sh scripting $1
publish-component.sh jes $1
publish-component.sh workflow $1
publish-component.sh datacenter $1
publish-component.sh deployment $1
publish-component.sh portal $1
publish-component.sh aggregate $1