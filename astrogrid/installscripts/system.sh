#!/bin/sh
#

# check tools needed for install scripts are there
# setup only: rpm, useradd
# which curl, wget, mkdir, echo, patch, unzip, cat, sleep, rm, chmod
# built in bash commands - pushd popd 

for i in which wget mkdir echo patch unzip cat sleep rm chmod curl
do
    if [ `which $i` ]
    then
        echo "  $i already installed"
    else
        echo "  ERROR : $i not installed"
        exit 1
    fi
done

