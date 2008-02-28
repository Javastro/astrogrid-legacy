#!/bin/sh
# <meta:header>
#     <meta:title>
#         AstroGrid install script.
#     </meta:title>
#     <meta:description>
#         Initial setup script for the test system.
#         Note : This script needs to be run as root.
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
# Get the install script name and path.
SCRIPT_NAME=`basename "$0"`
SCRIPT_PATH=`dirname "$0"`

#
# Set the generic tester password
# perl -e 'print crypt("password", "salt")'
TESTER_CRYPT=Na0ccjTpZA8h6

#
# Check script is run as root.
isroot()
    {
    if [ $(id -u) -ne 0 ]
    then
        echo -e "The setup scripts must be run as root."
        exit 1
    fi
    }

#
# Get a users home directory.
homedir()
    {
    echo `finger -m -p $1 | sed -e '/^Directory/!d' -e 's/Directory: *//' -e 's/ *\t.*//g'`
    }

#
# Install a ssh key.
sshkey()
    {
    ACCOUNT_NAME=$1
    ACCOUNT_HOME=`homedir ${ACCOUNT_NAME}`
    KEY_URL=$2
    KEY_FILE=`basename ${KEY_URL}`

    echo ""
    echo "Checking ssh keys"
    echo "  User : ${ACCOUNT_NAME}"
    echo "  Home : ${ACCOUNT_HOME}"
    echo "  URL  : ${KEY_URL}"
    echo "  Key  : ${KEY_FILE}"

    echo "Checking home directory"
    if [ -e ${ACCOUNT_HOME} ]
    then
        echo "Checking ssh directory"
        if [ ! -d ${ACCOUNT_HOME}/.ssh ]
        then
            echo "Creating ssh directory"
            mkdir ${ACCOUNT_HOME}/.ssh
            chown ${ACCOUNT_NAME}.${ACCOUNT_NAME} ${ACCOUNT_HOME}/.ssh
            chmod u=rwx,g=,o= ${ACCOUNT_HOME}/.ssh
        fi

        echo "Checking ssh key"
        if [ ! -f ${ACCOUNT_HOME}/.ssh/${KEY_FILE} ]
        then
            echo "Downloading ssh key"
            pushd ${ACCOUNT_HOME}/.ssh
                wget ${KEY_URL}
            popd
            echo "Installing ssh key"
            cat ${ACCOUNT_HOME}/.ssh/${KEY_FILE} >> ${ACCOUNT_HOME}/.ssh/authorized_keys2

            echo "Setting permissions"
            chown ${ACCOUNT_NAME}.${ACCOUNT_NAME} ${ACCOUNT_HOME}/.ssh/authorized_keys2
            chmod u=rwx,g=r,o=r ${ACCOUNT_HOME}/.ssh/authorized_keys2

            chown ${ACCOUNT_NAME}.${ACCOUNT_NAME} ${ACCOUNT_HOME}/.ssh/${KEY_FILE}
            chmod u=rwx,g=,o= ${ACCOUNT_HOME}/.ssh/${KEY_FILE}
        fi
    else
        echo "Unable to locate user home ${ACCOUNT_HOME}"
    fi

    }

#
# Check this is being run as root.
isroot

#
# Install the root account keys.
sshkey root http://www.metagrid.co.uk/maven/ssh/zarquan.metagrid.co.uk.pub
sshkey root http://www.astrogrid.org/maven/ssh/id_rsa_clq2.pub
sshkey root http://www.astrogrid.org/gg.id_dsa.pub

#
# Install SVN.
yum -y install subversion

#
# Install CVS
yum -y install cvs


#
# Install  Xvfb.
yum -y install Xvfb

#
# Create our installs directory.
INSTALL_BASE=/var/local/installs
echo "Checking installs directory."
if [ ! -d ${INSTALL_BASE} ]
then
    echo "Creating installs directory."
    mkdir ${INSTALL_BASE}
    chown root.users ${INSTALL_BASE}
    chmod u=rwx,g=rwx,o=rx ${INSTALL_BASE}
fi

#
# Install the Sun JDK.
echo "Checking JDK"
if [ ! -d /usr/java/jdk1.6.0_02 ]
then

    if [ ! -f ${INSTALL_BASE}/jdk-6u2-linux-i586.rpm ]
    then
        echo "Downloading JDK"
        pushd ${INSTALL_BASE}
            #wget http://www.metagrid.co.uk/java/jdk/jdk-6u2-linux-i586.rpm
            wget http://www.astrogrid.org/maven/java--downloads/jdk-6u2-linux-i586.rpm
        popd
    fi
    
    echo "Installing JDK"
    #rpm -i ${INSTALL_BASE}/jdk-6u2-linux-i586.rpm
    # change yum config to allow install without gpg check failing...
    cp --reply=yes /etc/yum.conf /etc/yum.conf.flexibak
    sed s/gpgcheck=1/gpgcheck=0/ /etc/yum.conf > /etc/yum2.conf
    mv -f /etc/yum2.conf /etc/yum.conf
    yum -y localinstall ${INSTALL_BASE}/jdk-6u2-linux-i586.rpm
    # ...and change it back
    cp --reply=yes /etc/yum.conf.flexibak /etc/yum.conf
fi

#
# Set JAVA_HOME environment variable.
export JAVA_HOME=${JAVA_HOME:-/usr/java/default}

#
# Check the Java settings.
echo ""
echo "Checking Java settings .."
if [ ! -d ${JAVA_HOME} ]
then
    echo "Unable to locate JAVA_HOME"
    exit 1
fi

if [ ! -f ${JAVA_HOME}/bin/java ]
then
    echo "Unable to locate JAVA_HOME/bin/java"
    exit 1
fi

#
# Create the project directories.
PROJECT_BASE=/var/local/projects
echo "Checking projects directory."
if [ ! -d ${PROJECT_BASE} ]
then
    echo "Creating projects directory."
    mkdir ${PROJECT_BASE}
    chown root.users ${PROJECT_BASE}
    chmod u=rwx,g=rwx,o=rx ${PROJECT_BASE}
fi

VOSPACE_BASE=${PROJECT_BASE}/vospace
echo "Checking vospace directory."
if [ ! -d ${VOSPACE_BASE} ]
then
    echo "Creating vospace directory."
    mkdir ${VOSPACE_BASE}
    chown root.users ${VOSPACE_BASE}
    chmod u=rwx,g=rwx,o=rx ${VOSPACE_BASE}
fi

#
# Checkout a copy of the source code.
#VOSPACE_BRANCH=20080225.zrq
#VOSPACE_MODULE=webapp/myspace/testing/client/new
VOSPACE_MODULE=astrogrid/stresstest
echo "Checking vospace source"
if [ ! -d ${VOSPACE_BASE}/vospace ]
then
    pushd ${VOSPACE_BASE}
# need to change this so no prompting for password - use AG cvs?
        #svn checkout http://esavo02.esac.esa.int/svnvo/VOSpace/branches/${VOSPACE_BRANCH}/${VOSPACE_MODULE} vospace.${VOSPACE_BRANCH}
    CVS_RSH=ssh
    export CVS_RSH
    cvs -z3 -d :ext:flexiscale@cvs.astrogrid.org:/devel co ${VOSPACE_MODULE}
    popd
else
    pushd ${VOSPACE_BASE}
        #svn update vospace.${VOSPACE_BRANCH}
        cvs -z3 -d :ext:flexiscale@cvs.astrogrid.org:/devel update
    popd
fi
chown -R root.users ${VOSPACE_BASE}/${VOSPACE_MODULE}
chmod -R u=rwx,g=rwx,o=rx ${VOSPACE_BASE}/${VOSPACE_MODULE}

#
# Create the current vospace link.
VOSPACE_CURRENT=${VOSPACE_BASE}/current
if [ -L ${VOSPACE_CURRENT} ]
then
    rm -f ${VOSPACE_CURRENT}
fi
ln -s ${VOSPACE_BASE}/${VOSPACE_MODULE} ${VOSPACE_CURRENT}
chown root.users ${VOSPACE_CURRENT}

#
# Set the useradd binary.
USER_ADD=/usr/sbin/useradd

#
# Create our own user accounts.
echo ""
echo "Checking user accounts"
for ACCOUNT_NAME in Catherine Dave Gary ktn
do
    #
    # Check the account exists.
    echo "Checking ${ACCOUNT_NAME}"
    if [ "`id -u ${ACCOUNT_NAME} 2> /dev/null`" = "" ]
    then
        #
        # Create the missing account.
        echo "Creating ${ACCOUNT_NAME}"
        ACCOUNT_HOME=/home/${ACCOUNT_NAME}
        /usr/sbin/useradd -c "AstroGrid" -m -G users -p ${TESTER_CRYPT} -d ${ACCOUNT_HOME} ${ACCOUNT_NAME} 
        /usr/sbin/usermod -g users ${ACCOUNT_NAME}
        #
        # Install our ssh keys.
        # Not required when on local machine.
    fi

    #
    # Create the symbolic links.
    echo "Creating current link"
    if [ -L ${ACCOUNT_HOME}/current ]
    then
        rm -f ${ACCOUNT_HOME}/current
    fi
    ln -s ${VOSPACE_BASE}/current ${ACCOUNT_HOME}/current

done

#
# Add our ssh keys.
sshkey Dave      http://www.metagrid.co.uk/maven/ssh/zarquan.metagrid.co.uk.pub
sshkey Catherine      http://www.astrogrid.org/maven/ssh/id_rsa_clq2.pub
sshkey Gary http://www.astrogrid.org/gg.id_dsa.pub
sshkey ktn http://www.astrogrid.org/ag_ktn_dsa.pub

#
# Create the test user accounts.
echo ""
echo "Checking test accounts"
XVFB_DISPLAY=5
for ACCOUNT_NAME in agtester-000 agtester-001 agtester-002 agtester-003 
do
    #
    # Check the account exists.
    if [ "`id -u ${ACCOUNT_NAME} 2> /dev/null`" = "" ]
    then
        #
        # Create the missing account.
        echo ""
        echo "Creating ${ACCOUNT_NAME}"
        ACCOUNT_HOME=/home/${ACCOUNT_NAME}
        /usr/sbin/useradd -c "AstroGrid" -m -G users -p ${TESTER_CRYPT} -d ${ACCOUNT_HOME} ${ACCOUNT_NAME} 
        /usr/sbin/usermod -g users ${ACCOUNT_NAME}
        #
        # Install our ssh keys.
        # Not required when on local machine.
    fi
    #
    # Make the home directory read to all.
    # (enables normal user to peek at the contents)
    ACCOUNT_HOME=`homedir ${ACCOUNT_NAME}`
    if [ -e ${ACCOUNT_HOME} ]
    then
        chmod a+rx ${ACCOUNT_HOME}
    else
        echo "Unable to locate home directory"
        echo "  User : ${ACCOUNT_NAME}"
        echo "  Home : ${ACCOUNT_HOME}"
        exit 1
    fi
    #
    # Add our ssh keys.
    sshkey ${ACCOUNT_NAME} http://www.metagrid.co.uk/maven/ssh/zarquan.metagrid.co.uk.pub
    sshkey ${ACCOUNT_NAME} http://www.astrogrid.org/maven/ssh/id_rsa_clq2.pub
    sshkey ${ACCOUNT_NAME} http://www.astrogrid.org/gg.id_dsa.pub
    sshkey ${ACCOUNT_NAME} http://www.astrogrid.org/ag_ktn_dsa.pub

    #
    # Create the symbolic links.
    echo "Creating voexplorer link"
    if [ -L ${ACCOUNT_HOME}/voexplorer ]
    then
        rm -f ${ACCOUNT_HOME}/voexplorer
    fi
    ln -s ${VOSPACE_CURRENT}/voexplorer/voexplorer.sh ${ACCOUNT_HOME}/voexplorer

    echo "Creating python link"
    if [ -L ${ACCOUNT_HOME}/python ]
    then
        rm -f ${ACCOUNT_HOME}/python
    fi
    ln -s ${VOSPACE_CURRENT}/python ${ACCOUNT_HOME}/python

    #
    # Set the Xvfb display number.
    echo "Setting Xvfb display number."
    echo "  DISPLAY : ${XVFB_DISPLAY}"
    echo ${XVFB_DISPLAY} > ${ACCOUNT_HOME}/xvfb.display
    XVFB_DISPLAY=$[XVFB_DISPLAY + 1]

done

echo ""
echo "Setup done"
echo ""


