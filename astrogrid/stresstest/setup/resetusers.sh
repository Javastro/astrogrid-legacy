#!/bin/sh
#

#
# Set the project directory.
PROJECT_BASE=/var/local/projects/vospace
CURRENT_BASE=${PROJECT_BASE}/current


#
# Check this is being run as root.
if [ $(id -u) -ne 0 ]
then
    echo -e "The resetuser script must be run as root."
    exit 1
fi

#
# Set the userdel binary.
USER_DEL=/usr/sbin/userdel
USER_MOD=/usr/sbin/usermod

#
# Function to get a users home directory.
get_homedir()
    {
    echo `finger -m -p $1 | sed -e '/^Directory/!d' -e 's/Directory: *//' -e 's/ *\t.*//g'`
    }


for ACCOUNT_NAME in Gary Dave Catherine ktn agtester-000 agtester-001 agtester-002 agtester-003
do
    # reset default group to user so /etc/group gets tidied up too
    ${USER_MOD} -g ${ACCOUNT_NAME} ${ACCOUNT_NAME}
    rm -rf `get_homedir $ACCOUNT_NAME` 
    ${USER_DEL} ${ACCOUNT_NAME}
done





