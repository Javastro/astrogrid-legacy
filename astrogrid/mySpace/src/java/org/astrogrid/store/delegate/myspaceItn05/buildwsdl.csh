#! /bin/csh -f

# buildwsdl.csh
#
# Generate the WSDL for a MySpace Manager.
#
# Author:
#   A C Davenhall (ACD) acd@roe.ac.uk
#
# History:
#   5/3/04  (ACD) Original version.
#   9/3/04  (ACD) Added options for document: DOCUMENT, ENCODED.
#  11/3/04  (ACD) Reverted to `RPC' style (but preserved `DOCUMENT, ENCODED'
#     options as comments).
#   23/3/04 (ACD) Moved the delegate to the `store' directory.
#

cd ../../../../..

#
# Both RPC and DOCUMENT, ENCODED versions are given below.  Un-comment
# whichever is appropriate.

#
# RPC version.

java org.apache.axis.wsdl.Java2WSDL -o wp.wsdl   \
  -l"http://grendel12.roe.ac.uk:8080/astrogrid-myspace"   \
  -n  "urn:Kernel" \
  -p"org.astrogrid.store.delegate.myspaceItn05" "urn:Kernel"   \
  org.astrogrid.store.delegate.myspaceItn05.Manager
  
#
# DOCUMENT, ENCODED version.

# java org.apache.axis.wsdl.Java2WSDL -o wp.wsdl   \
#   -l"http://grendel12.roe.ac.uk:8080/astrogrid-myspace"   \
#   -n  "urn:Kernel"  \
#   -p"org.astrogrid.store.delegate.myspaceItn05" "urn:Kernel"   \
#   -y DOCUMENT  -u ENCODED  \
#   org/astrogrid/store/delegate/myspaceItn05.Manager

cd org/astrogrid/store/delegate/myspaceItn05


