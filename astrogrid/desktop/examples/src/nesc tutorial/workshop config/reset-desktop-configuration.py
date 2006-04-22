#!/usr/bin/env python
#reset workbench endpoints.
# 
import xmlrpclib as x
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
s.system.configuration.setKey(
	"org.astrogrid.registry.query.endpoint"
	 , "http://galahad.star.le.ac.uk:8080/galahad-registry/services/RegistryQuery")
s.system.configuration.setKey(
	"org.astrogrid.registry.admin.endpoint"
	 , "http://galahad.star.le.ac.uk:8080/galahad-registry/services/RegistryAdmin")
s.system.configuration.setKey(
	"jes.job.controller.endpoint"
	 , "http://galahad.star.le.ac.uk:8080/astrogrid-jes/services/JobControllerService"	 
	)	
print "Reset endpoints to"
print "Registry query - http://galahad.star.le.ac.uk:8080/galahad-registry/services/RegistryQuery"
print "Registry admin - http://galahad.star.le.ac.uk:8080/galahad-registry/services/RegistryAdmin"
print "Job Controller - http://galahad.star.le.ac.uk:8080/astrogrid-jes/services/JobControllerService"
print "Don't forget to adjust job controller endpoint to a workgroup server if needed"
print "** Restart your workbench to make the changes take effect **"
	