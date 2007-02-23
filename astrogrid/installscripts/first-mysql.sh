#!/bin/sh
#
# <meta:header>
#   <meta:title>
#     AstroGrid install script.
#   </meta:title>
#   <meta:description>
#     Initial setup script for the test machine.
#     This script needs to be run as root.
#   </meta:description>
# </meta:header>
#

#
# Get the install script name and path.
SCRIPT_NAME=`basename "$0"`
SCRIPT_PATH=`dirname "$0"`

#
# Root account settings.
ROOT_PASS=23rt87p

#
# JDBC account settings.
JDBC_USER=jdbc
JDBC_HOST=localhost
JDBC_PASS=23rt87p

#
# Install the mysql database server.
echo ""
echo "Installing mysql server"
yum -y install mysql mysql-server

#
# Start the mysql service on boot.
echo ""
echo "Configuring mysql to start on boot"
chkconfig mysqld on

#
# Start the mysql service
echo ""
echo "Starting mysql service"
service mysqld start

#
# Set the database root password
echo ""
echo "Setting root database password"
/usr/bin/mysqladmin -u root password ${ROOT_PASS}
/usr/bin/mysqladmin -u root -h `hostname -f` password ${ROOT_PASS}

#
# Download and install the database dump file.
echo ""
echo "Downloading database dump file"
curl http://ag03.ast.cam.ac.uk:8080/download/first.sqldump | mysql --user=root --password=${ROOT_PASS}

#
# Create JDBC user account
echo ""
echo "Creating JDBC user account"
mysql --user=root --password=${ROOT_PASS} << EOF
GRANT SELECT ON first.* TO '${JDBC_USER}'@'${JDBC_HOST}' IDENTIFIED BY '${JDBC_PASS}';
EOF

