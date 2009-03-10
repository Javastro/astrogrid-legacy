/**
 * 
 */
package org.astrogrid.acr.ivoa;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.file.Systems;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.util.Tables;
import org.w3c.dom.Document;

/** Abstract Class: Functionality common to all Data Access Services.
 * 
 *  The class provides the functionality common to Catalog, Image, Spectra and Time access services. It addresses:
 * <dl>
 * <dt>Locating DAL services</dt>
 *  <dd>Use {@link #getRegistryQuery()} or {@link #getRegistryXQuery()} to query for suitable services in the {@link Registry}</dd>
 * <dt>Performing a query</dt>
 *  <dd>{@link #execute(URL)}, {@link #executeVotable(URL)}, {@link #executeAndSave(URL, URI)}</dd>  
 * <dt>Saving any referenced data</dt>
 *  <dd>{@link #saveDatasets(URL, URI)}, {@link #saveDatasetsSubset(URL, URI, List)}</dd>  
 * </dl>
 * 
 * <h2>Construct a Query</h2>
 * This {@code DAL} class does not address <i>constucting</i> the DAL query  - this is specific to each protocol, and so the 
 * query construction functions are
 * factored out into protocol-specific subclasses:
 * <dl>
 *  <dt>{@link Cone}</dt><dd>for catalogs</dd>
 *  <dt>{@link Siap}</dt><dd>for images</dd>
 *  <dt>{@link Ssap}</dt><dd>for spectra</dd>
 *  <dt>{@link Stap}</dt><dd>for time-series data</dd>
 * </dl> 
 * 
 * However, all the query construction functions follow the same form - from inputs of which service to query, search position and other query parameters 
 * they produce a <i>query URL</i>. This can then be passed to the functions in this class to perform the query and save the results. An example of constructing a cone 
 * query is shown below. Consult the protocol-specific subclasses for further examples.
 *  
 * {@example "Contructing a Cone Query (Python)"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')     

#the Cone service to query (selected using voexplorer)
service = "ivo://irsa.ipac/2MASS-PSC"
#build a query - service, RA, Dec, Search Radius.
query = ar.ivoa.cone.constructQuery(service,283.76,-30.47,0.001)
print "QueryURL",query
}
This script produces:
<blockquote><tt>
QueryURL http://irsa.ipac.caltech.edu/cgi-bin/Oasis/CatSearch/nph-catsearch?CAT=fp_psc&RA=283.76&DEC=-30.47&SR=0.0010
</tt></blockquote> 

* <h2>Execute a Query</h2>
* Once the query has been constructed, it can be executed in a number of ways.
*  
 * <h3>Produce a List of Maps</h3>
 * The simplest is to execute the query to produce a
 * list of map objects by using {@link #execute(URL)}. Each map object corresponds to a single row in the returned table, and contains
 * a mapping from column-name to value for each column in the table.
 * {@example "Execute to produce a list of maps (Python)"
rows = ar.ivoa.cone.execute(query)
#inspect what we've got.
print "Rows Returned",len(rows)
print "Row 1:"
import pprint
pprint.pprint(rows[0])
}
This script produces:
{@source
Rows Returned 2
Row 1:
{'ID_MAIN': '0',
 'POS_EQ_DEC_MAIN': -30.479257583618164,
 'POS_EQ_RA_MAIN': 283.76409912109375,
 'a': '0',
 'b_m_opt': 'null',
 'bl_flg': '220',
 'cc_flg': 'cc0',
 'cntr': 1306987739,
 'coadd': 245,
 'coadd_key': 1617887,
 'date': '1998-08-19',
 ...
 'use_src': 1,
 'vr_m_opt': 'null',
 'x_scan': -108.20999999999999}
}

<h3>Produce a VOTable</h3>
The query URL can also be executed using {@link #executeVotable(URL)} to return the raw 
XML VOTable data, which could be useful if it 
was to be passed to an external votable parser.

{@example "Produce a VOTable (Python)"
votable = ar.ivoa.cone.executeVotable(query)
print votable
}
The output is
{@source
<?xml version="1.0" encoding="UTF-8"?>
<VOTABLE version="v1.0">
<DEFINITIONS>
<COOSYS ID="J2000" epoch="2000." equinox="2000." system="eq_FK5"/>
</DEFINITIONS>
<RESOURCE>
<PARAM arraysize="*" datatype="char" name="fixlen" value="T"/>
...
</VOTABLE>
}

<h3>Save as a VOTable</h3>
The result from the previous example could then be saved manually to disk. However, 
the AstroRuntime can be instructed to execute a query and save the votable to a location 
- a local file, or a myspace or ftp 
network location - using the {@link #executeAndSave(URL, URI)} function. 
This has the advantage of not requiring that the script handle the 
potentially large query result.

<p />
The python example below instructs the AR to save the query result to a file in the current
local directory. Note that the 
local file must be specified as a {@code file:/} url - the example shows how to produce such an url from
a conventional file path. To save to a myspace location or ftp, just alter the destination url.

{@example "Save a VOTable to Disk (Python)"
#compute url for current directory
from urlparse import urlunsplit
from os import getcwd
currentDirURL = urlunsplit(['file','',getcwd(),'',''])
destinationURL = currentDirURL + "/results.vot"
ar.ivoa.cone.executeAndSave(query,destinationURL)
}

<h3>Convert to a different table format</h3>
The query response can be converted to different table formats using the {@link Tables} AR service. 
The following example uses {@link Tables#convertFromFile(URI, String, String)}
performs a query and converts the result to comma-separated values format.
The CSV table  is then returned to the script. 
{@example "Produce a CSV table (Python)"    
csv = ar.util.tables.convertFromFile(query,"votable","csv")
}

Alternately, the query can be performed and saved as CSV to a file by using {@link Tables#convertFiles(URI, String, URI, String)}
{@example "Save as a CSV table (Python)"
#compute url for current directory
from urlparse import urlunsplit
from os import getcwd
currentDirURL = urlunsplit(['file','',getcwd(),'',''])
destinationURL = currentDirURL + "/results.vot"
ar.util.tables.convertFile(query,"votable",destinationURL,"csv")
}

<h3>Display in a browser</h3>
The following snippet saves the query result to a temporary file in HTML format (using {@link Tables}), 
and then displays this file in the system webbrowser using {@link BrowserControl}. This is a useful technique to quickly display 
query results.

{@example "Display result in webbrowser (Python)"
from tempfile import mktemp
from urlparse import urlunsplit
from os.path import abspath
#create a temporary file
tmpFile = mktemp("html")
# find the file URL for the temporary file
tmpURL = urlunsplit(['file','',abspath(tmpFile),'','']) 
#convert votable result to html, saving to a local file
ar.util.tables.convertFiles(query,"votable",tmpURL,"html")
#display temporary html file in browser
ar.system.browser.openURL(tmpURL)
}

<h3>Send to Topcat (or any other PLASTIC application</h3>
The query URL can also be passed via PLASTIC to another application for download and display.
For example, if Topcat to the AstroRuntime plastic hub, then the following example will
cause topcat to display the result votable. 

{@example "Pass the query to a plastic application (Python)"
plastic = ar.plastic.hub
try:
    myId = plastic.registerNoCallBack("exampleScript")
    # broadcast a message. - tells the plastic apps to go get this table themselves.
    plastic.requestAsynch(myId,'ivo://votech.org/votable/loadFromURL',[query,query])            
finally:
    #clean up, by unregistering
    plastic.unregister(myId) 
}

<h2>Full Example</h2>
The following script queries an image service, sends results to a plastic viewer, and saves images to myspace. The script
can be downloaded <a href="doc-files/adass2006-example.py">here</a>.
{@source "Example of working with Siap services (Python)"
#!/usr/bin/env python
# example shown at ADASS 2006
#A simple workflow based around a SIAP query.
import xmlrpclib as x
import sys
import os
import time
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
ar = x.Server(prefix + "xmlrpc")

# Resolve an object name to a position.
p = ar.cds.sesame.resolve("m32")  # returns a structure

# Build a Simple Image Access Protocol (SIAP) query.
s = 'ivo://nasa.heasarc/skyview/dss2'  # resource ID of the SIAP service
q = ar.ivoa.siap.constructQuery(s,p['ra'],p['dec'],1.0)  # returns a query URL

# Execute the SIAP query
r = ar.ivoa.siap.execute(q) #returns an array of row structures 
print "Rows returned: ", len(r) , "Column Names: ", r[0].keys() 

# Display the query response in a votable viewer 
me = ar.plastic.hub.registerNoCallBack("script") #register with PLASTIC
m = 'ivo://votech.org/votable/loadFromURL'  # message to emit 
ar.plastic.hub.requestAsynch(me,m,[q]) #sends the message

# Save images selected by the query to user's workspace (that is, vospace or myspace)
dir = 'workspace:/dalexample'
ar.ivoa.siap.saveDatasets(q,dir) #save the images

# Save an ascii version of the SIAP query response 
ar.util.tables.convertFiles(q, "votable",dir + "/resp.txt","ascii")
}

<h2>Querying resources with multiple similar capabilities</h2>
In some cases, a registry resource may possess more than one cone-search capability. If the identifier for that resource is passed to {@code constructQuery}
than it is the <b>first suitable</b> capability in the resource that will be used. If you wish to query against one of the other cone-search capabilities, 
the service endpoint URL for that capability must be manually extracted and passed to {@code constructQuery}. This can be done by using the {@link Registry} component.

{@example "Extracting the endpoint for a capability"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')        
cone = ar.ivoa.cone #take a reference to the AR Cone component
registry = ar.ivoa.registry # a reference to the AR Registry component.

#A registry resource with multiple capabilities
service = "ivo://an.example.multiple.capability/resource"

#retrieve the resource metadata for this service.
resource = registry.getResource(service)

#traverse the resource to select the capability we're interersted in (the 3rd in the resource)
capability = resource['capabilities'][2]
#extract the accessURL for this capabilitiy
url = capability['interfaces'][0]['accessUrls'][0]['value']

#use this URL to build a query
query = cone.constructQuery(url,140.0,10.0,0.001)

#execute the query as normal..
print cone.execute(query)
}

 * @author Noel Winstanley

 * @see Tables Tables - convert betwee votable and other formats.
 * @see Registry Registry - to query for lists of Dal services.
 */
public interface Dal {

	
	   /** Add an additional option to a previously constructed query.
	    * <p/>
	    * Sometimes neccessary, for some DAL protocols, to provide optional query parameters.
	    * @param query the query url
	    * @param optionName name of the option to add
	    * @param optionValue value for the new option
	    * @return {@code query} with the option appended.
	 * @throws InvalidArgumentException if the parameter cannot be added.
	    */
	   URL addOption(URL query, String optionName, String optionValue) throws InvalidArgumentException;

	   /** Execute a DAL query, returning a datastructure
	    * @param query query url to execute
	    * @return  The service response parsed as a list of
	    * of rows. Each row is represented is a map between UCD or datamodel keys 
	    *  and values from the response
	    * @throws ServiceException if an error occurs while communicating with the  service, or if the service is unable to process the request
	    * @note When querying a SIAP service, the result uses keys drawn from the SIAP dataset datamodel

	    */
	   Map[] execute(final URL query) throws ServiceException;
	    
	   /** Execute a DAL query, returning a Votable document.
	    * 
	    * @note This is a convenience method that just performs a 'GET' on the query url. Many programming languages support this functionality internally.
	 * @param query query url to execute
	 * @return a votable document.
	 *  @throws ServiceException if an error occurs while communicating with the  service, or if the service is unable to process the request

	    * 
	    */
	   Document executeVotable(URL query) throws ServiceException;	 
	   
	   /** @exclude
	    *  @deprecated - use executeVotable() */
	   @Deprecated
    Document getResults(URL url) throws ServiceException;
	   

	   /**Execute a DAL query and save the resulting document.
	    * 
	    * @note in the case of saving to myspace, the myspace server performs the query - the data does not pass through the user's computer.
	 * @param query query url to execute
	 * @param saveLocation location to save result document - For example a {@code file:/}, {@code ivo://} (myspace), {@code ftp://} location
	 * @throws SecurityException if the user is not permitted to write to the save location
     * @throws ServiceException if an error occurs while communicating with the  service, or if the service is unable to process the request	 * @throws InvalidArgumentException if the save location cannot be written to
     * @see Systems#listSchemes() List of supported URI schemes for {@code saveLocation}
	 */
	void executeAndSave(URL query, URI saveLocation) throws SecurityException, ServiceException, InvalidArgumentException;
	/** @exclude
	 *  @deprecated - use executeAndSave() */
	@Deprecated
    void saveResults(URL url, URI saveLocation) throws SecurityException, ServiceException, InvalidArgumentException;
	
	/** Execute a DAL query, and save the datasets referenced by the response. 
	 * <p />
	 * Applies to those DAL protocols ({@link Siap}, {@link Ssap}, {@link Stap}) where the response points to external data files.
	 * 
	 * @note In the case of saving to myspace or vospace, the myspace server performs the query and dataset download - the data does not pass through the
	 * user's computer.
	 * @param query query url to execute
	 * @param saveLocation location of a directory in which to save the datasets. May be any of the filesystem types listed in {@link Systems#listSchemes()}.
	 * @throws SecurityException if the user is not permitted to write to the save location
	 * @throws ServiceException if either the query service or data service are unavailable
	 * @throws InvalidArgumentException if the save location cannot be written to.
	 * @return number of datasets saved.
	 * @see Siap Example of Use
     * @see Systems#listSchemes() List of supported URI schemes for {@code saveLocation}	 
	 */
	int saveDatasets(URL query, URI saveLocation) throws SecurityException, ServiceException, InvalidArgumentException;
	
	/** 
	 * Execute a DAL query, and save a subset of the datasets referenced by the response.
     * <p />
     * Applies to those DAL protocols ({@link Siap}, {@link Ssap}, {@link Stap}) where the response points to external data files.
     * 	 
     * @note in the case of saving to myspace or vospace, the myspace server performs the query and dataset download - the data does not pass through the
     * user's computer.	 
	 * @param query the DAL query
     * @param saveLocation location of a directory in which to save the datasets. May be any of the filesystem types listed in {@link Systems#listSchemes()}.
	 * @param rows list of Integers - indexes of the rows in the query response for which to save the dataset. (0= first row) 
	 * @throws SecurityException if the user is not permitted to write to the save location
	 * @throws ServiceException if either the query service or data service are unavailable
	 * @throws InvalidArgumentException if the save location cannot be written to, or the <tt>rows</tt>
	 * refers to invalid row indexes.
	 * @return number of datasets saved.
     * @see Systems#listSchemes() List of supported URI schemes for {@code saveLocation}	 
	 */
	int saveDatasetsSubset(URL query, URI saveLocation, List rows) throws SecurityException, ServiceException, InvalidArgumentException;
	
	   /** Returns a SRQL query that, when passed to the registry, will return all know services of that type. 
	    * <br/>
	    * can be used as a starting point for filters, etc.
        * 
        * {@stickyWarning In the case of {@link Cone} the registry query will return far too many to be useful - it is necessary to use this xquery as a starting point
        * for building a more tightly-constrained query.}
        * {@example "Example of querying for cone services related to 'dwarf'"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')   
#call this method to get a query to list all Cone-search services.   
coneQuery = ar.ivoa.cone.getRegistryQuery()

#combine it into a more tightly contrained query
myQuery = coneQuery + ' and subject=dwarf'

# perform the query
rs = ar.ivoa.registry.searcg(myQuery)
#inspect the results
print len(rs)
for r in rs:
    print r['id']       
        * }   
	    * @return a SRQL query string
	    * @see <a href='http://eurovotech.org/twiki/bin/view/VOTech/SimpleRegistryQueryLanguage'>SRQL Language Description</a> 	    
        * @see #getRegistryXQuery() alternative that returns an XQuery
	    */
    String getRegistryQuery();
	   
	   /** helper method - returns an ADQL/s query that should be passed to a registry to list all 
	    * available DAL services of this type. 
	    * <br/>
	    * can be used as a starting point for filters, etc.
	    * @return an adql query string

	    * @exclude
	    * @deprecated
	    */
	   @Deprecated
    String getRegistryAdqlQuery();
	   
	   /** 
	    * Return an XQuery that, when passed to the registry, will return all known services of that type.
	    * 
	    * {@stickyWarning In the case of {@link Cone} the registry query will return far too many to be useful - it is necessary to use this xquery as a starting point
	    * for building a more tightly-constrained query.}
	    * {@example "Example of querying for cone services related to 'dwarf'"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc') 	 
#call this method to get a query to list all Cone-search services.   
coneQuery = ar.ivoa.cone.getRegistryXQuery()

#combine it into a more tightly contrained query
myQuery = "let $cq := " + coneQuery + """
for $r in $cq
where contains($r/content/subject,'dwarf')
return $r
"""

# perform the query
rs = ar.ivoa.registry.xquerySearch(myQuery)
#inspect the results
print len(rs)
for r in rs:
    print r['id']	    
	    * } 
	    * the output of this script is
	    * <pre>
2
ivo://nasa.heasarc/rasswd
ivo://nasa.heasarc/mcksion
</pre>	    
	    * @return an xquery string
        * @see #getRegistryQuery() alternative that returns a SRQL query.
	    * @see <a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>  
	    */
	   String getRegistryXQuery();
	
}
