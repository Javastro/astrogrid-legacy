#!/usr/bin/env python
#Noel Winstanley, AstroGrid, 2006
#nw@jb.man.ac.uk
#Produce HTML report of assets available in registry.
#Usage: start Workbench / ACR
# 	execute 'python assets.py'
#	result left in 'assets.html'
import xmlrpclib 
import sys
import os
import os.path
import datetime
import urlparse
import urllib


#usual boilerplate to parse the configuration file.
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
endpoint = prefix + "xmlrpc"
print "Endpoint to connect to is", endpoint

#connect to the acr 
acr = xmlrpclib.Server(endpoint)

print "Querying registry to list assets.."


#do submit an xquery to the registry
# actually a xhtml page, with embedded queries.
s = acr.ivoa.registry.xquerySearchXML("""
<html>
<head>	
</head>
  <body>
  <h1>Astrogrid Assets,  %s </h1>
	  This page lists the services available in the galahad registry.<br/>
	    None of the services have been tested to verify they are correctly registered and operational.
  <h2> Totals</h2>
<ul>
<li>Total Catalog (Cone) Services: <b>
{
let $hits := (
  for $x in //vor:Resource
  where $x/@xsi:type  &=  '*ConeSearch' and (not ($x/@status='inactive' or $x/@status='deleted'))
  return $x)
return count($hits)
}
</b></li>
<li>Total Image (SIAP) Services: <b>
{
let $hits := (
  for $x in //vor:Resource
  where $x/@xsi:type &= '*SimpleImageAccess' and  (not ($x/@status='inactive' or $x/@status='deleted'))
  return $x)
return count($hits)
}
</b></li>	
<li>Total Spectra (SSAP) Services: <b>
{
let $hits := (
  for $x in //vor:Resource
  where $x/@xsi:type &= '*SimpleSpectrumAccess' and  (not ($x/@status='inactive' or $x/@status='deleted'))
  return $x)
return count($hits)
}
</b></li>	
	
<li>Total CEA Applications: <b>
{
let $hits := (
  for $x in //vor:Resource
  where ($x/@xsi:type &= '*CeaApplicationType' or  $x/@xsi:type &= '*CeaHttpApplicationType') and  (not ($x/@status='inactive' or $x/@status='deleted'))
  return $x)
return count($hits)
}
</b></li>	
<li>Total CEA Servers: <b>
{
let $hits := //vor:Resource[cea:ManagedApplications and  (not (@status='inactive' or @status='deleted'))]
return count($hits)
}
</b></li>	
	
</ul>
<h2>Catalog Services: Detail</h2>
<table>
<tr><th>Registry Key</th><th>Short Name</th><th>Title</th><th>cone search url</th></tr>
{
for $x in //vor:Resource
where $x/@xsi:type  &=  '*ConeSearch' and (not ($x/@status='inactive' or $x/@status='deleted'))
return <tr>
	<td>{data($x/vr:identifier)}</td>
	<td>{data($x/vr:shortName)}</td>
	<td><a href="{data($x/vr:content/vr:referenceURL)}">{data($x/vr:title)}</a></td>
	<td>{data($x/vr:interface/vr:accessURL)}</td>
	</tr>
}
</table>
		
<h2>Image Services: Detail</h2>
<table>
<tr><th>Registry Key</th><th>Short Name</th><th>Title</th><th>cone search url</th></tr>
{
for $x in //vor:Resource
 where $x/@xsi:type &= '*SimpleImageAccess' and  (not ($x/@status='inactive' or $x/@status='deleted'))
return <tr>
	<td>{data($x/vr:identifier)}</td>
	<td>{data($x/vr:shortName)}</td>
	<td><a href="{data($x/vr:content/vr:referenceURL)}">{data($x/vr:title)}</a></td>
	<td>{data($x/vr:interface/vr:accessURL)}</td>
	</tr>
}
</table>

<h2>Spectrum Services: Detail</h2>
<table>
<tr><th>Registry Key</th><th>Short Name</th><th>Title</th><th>cone search url</th></tr>
{
for $x in //vor:Resource
 where $x/@xsi:type &= '*SimpleSpectrumAccess' and  (not ($x/@status='inactive' or $x/@status='deleted'))
return <tr>
	<td>{data($x/vr:identifier)}</td>
	<td>{data($x/vr:shortName)}</td>
	<td><a href="{data($x/vr:content/vr:referenceURL)}">{data($x/vr:title)}</a></td>
	<td>{data($x/vr:interface/vr:accessURL)}</td>
	</tr>
}
</table>

<h2>CEA Applications: Detail</h2>
<table>
<tr><th>Registry Key</th><th>Short Name</th><th>Title</th><th>Servers providing this application</th></tr>
{
let $server := //vor:Resource[cea:ManagedApplications and  (not (@status='inactive' or @status='deleted'))]
for $x in //vor:Resource
 where ($x/@xsi:type &= '*CeaApplicationType' or $x/@xsi:type &= '*CeaHttpApplicationType') and $x/@status = 'active'
return <tr>
	<td>{data($x/vr:identifier)}</td>
	<td>{data($x/vr:shortName)}</td>
	<td><a href="{data($x/vr:content/vr:referenceURL)}">{data($x/vr:title)}</a></td>
	<td>{$server[cea:ManagedApplications/cea:ApplicationReference = $x/vr:identifier]/vr:identifier}</td>
	</tr>
}
</table>

<h2>CEA Servers: Detail</h2>
<table>
<tr><th>Registry Key</th><th>Short Name</th><th>Title</th><th>Applications</th></tr>
{
for $x in //vor:Resource[cea:ManagedApplications and  (not (@status='inactive' or @status='deleted'))]
return <tr>
	<td>{data($x/vr:identifier)}</td>
	<td>{data($x/vr:shortName)}</td>
	<td><a href="{data($x/vr:content/vr:referenceURL)}">{data($x/vr:title)}</a></td>
	<td>{$x/cea:ManagedApplications/cea:ApplicationReference}</td>
	</tr>
}
</table>
</body>
</html>
	 
""" % datetime.date.today() )

# save results to a file.
f = file("assets.html",'w')
f.write(s)
f.close()
print "done - see assets.html"


