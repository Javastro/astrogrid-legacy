#!/usr/bin/env python
#Noel Winstanley, AstroGrid, 2006
#gets around query size limitations by running a sequence of 
#queries, outputting a series of tables. Can then join manually using topcat.
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

queryTemplate = """
(: list the different UCD used in the registry - generate as a votable. :)
<VOTABLE>
<RESOURCE>
<TABLE>
<FIELD name="UCD" datatype="char" arraysize="*"  />
<FIELD name="Occurs"  datatype="int"/>
<DATA>
<TABLEDATA>
{
(: record-set worth searching :)
let $rs := //vor:Resource[not (@status='inactive' or @status='deleted')]
	
	
(:	and not (curation/publisher/@ivo-id='ivo://CDS')] :)


(:ucds:)
let $ucds := $rs/table/column/ucd[starts-with(.,'%s')] 
		| $rs/catalog/table/column/ucd[starts-with(.,'%s')]

(:calculate sonme stats on ucds :)
for $u in distinct-values($ucds)
	let $count := count($ucds[. = $u])
	
return <TR >
	<TD>{$u}</TD>
	<TD>{$count}</TD>
	</TR> 
}
</TABLEDATA>
</DATA>
</TABLE>
</RESOURCE>
</VOTABLE> 
""" 
from string import ascii_uppercase
for l in ascii_uppercase:
	print l
	s = acr.ivoa.registry.xquerySearchXML(queryTemplate % (l,l))
	f = file(l + ".vot",'w')
	f.write(s)
	f.close()


