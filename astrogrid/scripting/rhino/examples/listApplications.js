#!/bin/env js

Astrogrid = Packages.org.astrogrid.scripting.Astrogrid.getInstance()
app = Astrogrid.applications.get(0).createDelegate()
list =  app.listApplications()
for (i = 0; i < list.length; i++) {
	print(list[i]);
}
xmlDescr = app.getApplicationDescription(list[0]).xmlDescriptor
print("Information about " + list[0])
print(xmlDescr)
