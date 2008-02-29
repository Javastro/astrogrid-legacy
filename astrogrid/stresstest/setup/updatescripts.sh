#!/bin/sh
#

# refresh the scripts checked out of cvs.
# 


#
# Set the project directory.
PROJECT_BASE=/var/local/projects
ASTROGRID_BASE=${PROJECT_BASE}/astrogrid

pushd ${ASTROGRID_BASE}
   CVS_RSH=ssh
   export CVS_RSH
   cvs -z3 -d :ext:flexiscale@cvs.astrogrid.org:/devel update
popd


