#!/bin/sh
# <meta:header>
#     <meta:title>
#         AstroGrid runtime startup script.
#     </meta:title>
#     <meta:description>
#         This script configures the AstroGrid runtime in a repeatable way.
#     </meta:description>
#     <meta:licence>
#         Copyright (C) AstroGrid. All rights reserved.
#         This software is published under the terms of the AstroGrid Software License version 1.2.
#         See [http://software.astrogrid.org/license.html] for details. 
#     </meta:licence>
#     <svn:header>
#         $LastChangedRevision: 286 $
#         $LastChangedDate: 2007-09-06 12:49:40 +0100 (Thu, 06 Sep 2007) $
#         $LastChangedBy: dmorris $
#     </svn:header>
# </meta:header>
#

#
# Set the project directory.
PROJECT_BASE=/var/local/projects/vospace
CURRENT_BASE=${PROJECT_BASE}/current

#
# Set the generic tester password
# perl -e 'print crypt("password", "salt")'
TESTER_CRYPT=Na0ccjTpZA8h6

#
# Check this is being run as root.
if [ $(id -u) -ne 0 ]
then
    echo -e "The setup scripts must be run as root."
    exit 1
fi

#
# Set the useradd binary.
USER_ADD=/usr/sbin/useradd

#
# Function to get a users home directory.
get_homedir()
    {
    echo `finger -m -p $1 | sed -e '/^Directory/!d' -e 's/Directory: *//' -e 's/ *\t.*//g'`
    }

#
# Function to setup a test account.
setup_tester()
    {
    ACCOUNT_NAME=$1
    ACCOUNT_PASS=$2
    #
    # Check the account exists.
    echo "Checking Unix account [${ACCOUNT_NAME}]"
    if [ "`id -u ${ACCOUNT_NAME} 2> /dev/null`" = "" ]
    then
        #
        # Create the missing account.
        echo "Creating Unix account [${ACCOUNT_NAME}]"
        /usr/sbin/useradd \
            --comment "AstroGrid tester" \
            --create-home \
            --groups users \
            --password ${ACCOUNT_PASS} \
            ${ACCOUNT_NAME} 
    fi
    #
    # Check the users home directory.
    ACCOUNT_HOME=`get_homedir ${ACCOUNT_NAME}`
    echo "Checking home directory [${ACCOUNT_HOME}]"
    if [ ! -d ${ACCOUNT_HOME} ]
    then
        echo "Unable to locate home directory [${ACCOUNT_HOME}]"
        exit 1
    else
        #
        # Install our ssh keys.

        #
        # Create symbolic link to current source.
        echo "Creating symbolic link to current [${CURRENT_BASE}]"
        if [ -L ${ACCOUNT_HOME}/current ]
        then
            rm -f ${ACCOUNT_HOME}/current
        fi
        ln -s ${CURRENT_BASE} ${ACCOUNT_HOME}/current

        #
        # Create symbolic link to voexplorer.
        echo "Creating symbolic link to voexplorer in [${CURRENT_BASE}]"
        if [ -L ${ACCOUNT_HOME}/voexplorer.sh ]
        then
            rm -f ${ACCOUNT_HOME}/voexplorer.sh
        fi
        ln -s ${ACCOUNT_HOME}/current/webapp/myspace/testing/client/registry-new/voexplorer/voexplorer.sh \
            ${ACCOUNT_HOME}/voexplorer.sh

        #
        # Create symbolic link to python scripts.
        echo "Creating symbolic link to Python scripts in [${CURRENT_BASE}]"
        if [ -L ${ACCOUNT_HOME}/python ]
        then
            rm -f ${ACCOUNT_HOME}/python
        fi
        ln -s ${ACCOUNT_HOME}/current/webapp/myspace/testing/client/registry-new/python \
            ${ACCOUNT_HOME}/python

    fi
    }

#
# Install the Sun JDK.

#
# Install the test scripts.


#
# Setup the user accounts.
setup_tester C1tester ${TESTER_CRYPT}






