#!/bin/tcsh

# This is an example of invoking the TestDelegation harness.
# You will need a valid X.509 user certificate in your ~/.globus
# directory.

# The CLASSPATH will need to include the ag-ogsa-echo classes, plus
# all the jars that come with OGSA 3.0.
#
setenv CLASSPATH /home/kea/ksrc/ag-ogsa-echo/build/classes:`/home/gtr/bin/jarjar /data/cass123a/gtr/ogsa-3.0/lib`

# Remember to configure this to match your own tomcat installation location.
#
java org.astrogrid.ogsa.echo.TestDelegation http://cass111.ast.cam.ac.uk:8080/ogsa/services/astrogrid/echo/EchoFactory
