#! /bin/csh -f

# buildjava.csh
#
# Generate the Java classes necessary for a MySpace delegate to 
# communicate with a MySpace Manager Web Service from the WSDL for
# the Service.
#
# Author:
#   A C Davenhall (ACD) acd@roe.ac.uk
#
# History:
#   5/3/04  (ACD) Original version.
#   9/3/04  (ACD) Added options for document: DOCUMENT, ENCODED.
#   23/3/04 (ACD) Moved the delegate to the `store' directory.
#

cd ../../../../..

#
# Note that the following options for .WSDL2Java are appropriate for both
# `DOCUMENT, ENCODED' and `RPC' style.

java org.apache.axis.wsdl.WSDL2Java -o . -d Session -s -S true -a \
  -Nurn:Kernel org.astrogrid.store.delegate.myspaceItn05 wp.wsdl

cd org/astrogrid/store/delegate/myspaceItn05



