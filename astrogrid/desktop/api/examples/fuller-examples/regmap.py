#!/usr/bin/env python
#Noel Winstanley, AstroGrid, 2006
#nw@jb.man.ac.uk
# Produce a Mind-Map of the contents of the registry, suitable for display in FreeMind.
#Usage: start Workbench / ACR
# 	execute 'python regmap.py'
#	result left in 'regmap.mm'
import xmlrpclib 
import sys
import os
import os.path
import datetime
import urlparse
import urllib

import codecs


#usual boilerplate to parse the configuration file.
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
endpoint = prefix + "xmlrpc"
print "Endpoint to connect to is", endpoint

#connect to the acr 
acr = xmlrpclib.Server(endpoint)

print "Generating registry index - this will take a while - please wait"


#submit a xquery to the registry
#creates a result that can be input into freemind mindmap viewer.

s = acr.ivoa.registry.xquerySearchXML("""
(: write representation of a single registry entry :)		
declare function local:mkRecordNode($record,$xsi){
(: display logo :)
let $logo := normalize-space($record/vr:curation/vr:creator/vr:logo)
let $title := if (string-length($logo) != 0) then
 	      concat("&lt;html&gt;&lt;img src=&quot;"
	      	,$logo, "&quot;&gt;&lt;br&gt;", $record/vr:title)
   	      else $record/vr:title	
return <node TEXT="{$title}" ID="{$record/vr:identifier}" STYLE="fork" FOLDED="true" >
	<node TEXT="{$record/vr:identifier}"/>	
	{ (: display description :)
	let $descr := normalize-space(substring($record/vr:content/vr:description,1,200))
	return if (string-length($descr) != 0) then
		<node TEXT="{$descr}" />		
		else ()	
	}
	{ (: display tables for tdb :)
	for $table in $record/vods:table
	return <node TEXT="{$table/vods:name}&#xa;
		     {normalize-space(substring($table/vods:description,1,50))}"/>
	}
	{
	(: hyperlink to reference document :)
	if (string-length($record/vr:content/vr:referenceURL) != 0) then
	<node LINK="{$record/vr:content/vr:referenceURL}" TEXT="Reference" />
	else ()
	}
	{ (:link from cea services to applications :)	
	if ($record/cea:ManagedApplications/cea:ApplicationReference) then
		<node TEXT="provides" COLOR="#996600" FOLDED="true">
		{
		for $app in $record/cea:ManagedApplications/cea:ApplicationReference
		return <node TEXT="{$app}" >
			<arrowlink DESTINATION="{$app}" />
			</node>
		}
		</node>
	else ()
	}		
	{ (: links to related resources :)
	for $i in $record/vr:content/vr:relationship
	return <node TEXT="{$i/vr:relationshipType}" FOLDED="true" COLOR="#996600">
		<font NAME="SansSerif" SIZE="12" />
		{
		for $j in $i/vr:relatedResource
		return 	<node TEXT="{$j}">
			<arrowlink DESTINATION="{if ($j/@vr:ivo-id) then $j/@vr:ivo-id else $j}" />				
			</node>	
		}
		</node>	
	}				
</node>
};
<map version="0.8.0">
<node TEXT="VO Registry">
{
(: record-set worth searching :)
let $active := //vor:Resource[not (@status='inactive' or @status='deleted')]

(: list of distinct xsi:types :)
let $xsiList := (
	(: necessary trickery to work around differing namespace prefixes in attribute :)
	for $i in distinct-values($active/@xsi:type)
	let $j := if (contains($i,":")) then substring-after($i,":") else string($i)
	order by $j
	return $j
	)
(:for each xsi type :)
for $xsi in distinct-values($xsiList)
order by $xsi
return <node TEXT="{$xsi}" FOLDED="true" STYLE="bubble">
	{
	let $activeXsi := $active[@xsi:type = $xsi or ends-with(@xsi:type, concat(':',$xsi))]
	(: for each publisher :)
	for $publisher in distinct-values($activeXsi/vr:curation/vr:publisher)
	order by $publisher
	return <node TEXT="{$publisher}" FOLDED="true" STYLE="bubble">
		{
		 (: additional level of hierarchy for siap :) 
		 if ($xsi = 'SimpleImageAccess') then
		   let $activeXsiPublisher := $activeXsi[vr:curation/vr:publisher = $publisher]
		   for  $imageServiceType in distinct-values($activeXsiPublisher/sia:capability/sia:imageServiceType)
		   order by $imageServiceType
		   return <node TEXT="{$imageServiceType}" FOLDED="true" STYLE="bubble">
			   {
			   for $record in $activeXsiPublisher[sia:capability/sia:imageServiceType = $imageServiceType]
			   order by $record/vr:title
			   return local:mkRecordNode($record,$xsi)
			   }
			  </node>
		  else 
			for $record in $activeXsi[vr:curation/vr:publisher = $publisher]
			order by $record/vr:title
			return local:mkRecordNode($record,$xsi)
		}		
		</node>
	}
       </node>
}
</node>
</map> 
""" )
#
# save results to a file.
f = codecs.open("regmap.mm",'w',"UTF-8")
f.write(s)
f.close()




