#!/bin/sh
#

# check tools needed for install scripts are there
# setup only: rpm, useradd
# which, cat, cp, chmod, curl, echo, grep, mkdir, patch, rm, sleep, unzip, wget

# built in bash commands - pushd popd 

for i in which cat cp chmod curl echo grep mkdir patch rm sleep unzip wget
do
    if [ `which $i` ]
    then
        echo "  $i already installed"
    else
        echo "  ERROR : $i not installed"
        exit 1
    fi
done

