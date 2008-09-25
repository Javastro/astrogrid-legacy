/*$Id: Stap.java,v 1.9 2008/09/25 16:02:04 nw Exp $
 * Created on 17-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.astrogrid;

import java.net.URI;
import java.net.URL;
import java.util.Date;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.ivoa.Dal;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.StapCapability;

/** AR Service: Query for <b>Time series data</b> from Simple Time Access Protocol (STAP) services (DAL).
 * <p />
 * {@stickyNote This class provides functions to construct a DAL query. 
 * To execute that query, see the examples and methods in the {@link Dal} class.
 * }
 * 
 * <h2>Example</h2>
 * The following example constructs a queryURL, performs the query, and then downloads
 * the first of the resulting datasets. See {@link Dal} for other things that 
 * can be done with a query URL.
* {@example "Query a Stap service and download a subset of the data (Python)"
# connect to the AR
from xmlrpc import Server, DateTime
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')        
stap = ar.ivoa.stap #take a reference to the AR STAP component

#the STAP service to query (selected using voexplorer)
service = "ivo://mssl.ucl.ac.uk/stap-lasco"

#build a query
start = DateTime('20000101T00:00:00')
end = DateTime('20000102T00:00:00')
query = stap.constructQuery(service,start,end)
print "QueryURL",query

#execute the query
rows = stap.execute(query)

#inspect what we've got.
print "Rows Returned",len(rows)
import pprint
pprint.pprint(rows[0])


#download first datasets into current directory
#compute url for current directory
from urlparse import urlunsplit
from os import getcwd
currentDirURL = urlunsplit(['file','',getcwd(),'',''])
print "Downloading data to",currentDirURL
stap.saveDatasetsSubset(query,currentDirURL,[0])
 * } 
 * The output from this script is shown below. 
  * <pre>
 QueryURL http://msslxx.mssl.ucl.ac.uk:8080/stap-lasco/StapSearch?service=astrogrid_stap&START=2000-01-01T00%3A00%3A00&END=2000-01-02T00%3A00%3A00
Rows Returned 3
{'Concept': ' Coronal Mass Ejection',
 'Contact Email': ' eca@mssl.ucl.ac.uk',
 'Contact Name': ' Elizabeth Auden',
 'INST_ID': 'SOHO_LASCO',
 'IVORN': 'ivo://mssl.ucl.ac.uk/LASCO#LASCO20000101T065405_21',
 'Name': ' LASCO_20000101T065405_21',
 'Parameters': '  Central Position Angle=21 deg, Angular Width=76 deg, Linear Speed=337 km/s, Acceleration=8.8 m/s^2, Mass=5.0e+15 g, Kinetic Energy=2.8e+30 erg, Measurement Position Angle=11 deg',
 'References': '  C2 movie=http://lasco-www.nrl.navy.mil/daily_mpg/2000_01/000101_c2.mpg, C3 movie=http://lasco-www.nrl.navy.mil/daily_mpg/2000_01/000101_c3.mpg, SXT movie=http://cdaw.gsfc.nasa.gov/CME_list/daily_mpg/2000_01/sxt_almg.20000101.mpg, PHTX movie=http://cdaw.gsfc.nasa.gov/CME_list/daily_plots/sephtx/2000_01/sephtx.20000101.png, Java movie=http://cdaw.gsfc.nasa.gov/CME_list/daily_movies/2000/01/01, C2 movie / GOES light curve=http://cdaw.gsfc.nasa.gov/CME_list/UNIVERSAL/2000_01/jsmovies/2000_01/20000',
 'VOX:AccessReference': ' http://msslxx.mssl.ucl.ac.uk:8080/voevent/xml/LASCO/LASCO_20000101T065405_21.xml',
 'VOX:Format': 'VOEVENT',
 'meta': 'LASCO CME VOEvents',
 'meta.curation': 'CDAW (NASA/Goddard), via MSSL query',
 'meta.ref.url': 'http://cdaw.gsfc.nasa.gov/CME_list/',
 'meta.title': 'LASCO CMEs',
 'time.obs.end': '2000-01-01T06:54:05',
 'time.obs.start': '2000-01-01T06:54:05'}
Downloading data to file:///Users/noel/Documents/workspace/python
 * </pre>
 * 
 * @see <a href="http://wiki.astrogrid.org/bin/view/Astrogrid/SimpleTimeAccessProtocol">Proposed STAP Standard</a>
 * @see Dal
     * @see Sesame Sesame - resolves object names to RA,Dec positions
     * @see #getRegistryXQuery() getRegistryXQuery() - a query to list all STAP Services.  
 * @service astrogrid.stap

 * @author Kevin Benson
 * @modified Noel Winstanley - changed from Calendar to Date and removed overloading of methods names for XMLRPC compatability
 */
public interface Stap extends Dal  {
    
    /** 
     * Construct a STAP Query on Time.
     * @param service Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.
     *  <blockquote><dl>
     *  <dt>Resource Identifier</dt><dd>
     *  The resource ID of the STAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://mssl.ucl.ac.uk/stap-hinode-eis_2}
     *  <br/>The {@link Registry} will be queried to 
     *  resolve the resource ID into a {@link Resource} object, from which the {@link StapCapability} will be found, from which in turn the first
     *  {@link AccessURL} will be used.
     *  </dd>
     *  <dt>URL of the Service</dt><dd>
     *  The endpoint URL. Can be any {@code http://} URL.
     *  </dd>
     *  </dl></blockquote>
     * @param start start date and time
     * @param end end date and time
    * @return A query URL. The query can then be performed by either 
     * <ul>
     * <li>
     * programmatically performing a HTTP GET on the query URL
     * </li>
     * <li>
     * passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}
     * </li>   
     * </ul> 
    * 
     * @throws InvalidArgumentException if <tt>service</tt> is not a {@code http://} or {@code ivo://} reference
     * 
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)    
    */ 
    URL constructQuery(URI service,Date start, Date end) throws InvalidArgumentException, NotFoundException;

    /** Construct a STAP Query on Time and Format.
     * @param service Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.
     *  <blockquote><dl>
     *  <dt>Resource Identifier</dt><dd>
     *  The resource ID of the STAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://mssl.ucl.ac.uk/stap-hinode-eis_2}
     *  <br/>The {@link Registry} will be queried to 
     *  resolve the resource ID into a {@link Resource} object, from which the {@link StapCapability} will be found, from which in turn the first
     *  {@link AccessURL} will be used.
     *  </dd>
     *  <dt>URL of the Service</dt><dd>
     *  The endpoint URL. Can be any {@code http://} URL.
     *  </dd>
     *  </dl></blockquote>
     * @param start start date and time
     * @param end end date and time
     * @param format format of images or time series data e.g. {@code ALL} 
    * @return A query URL. The query can then be performed by either 
     * <ul>
     * <li>
     * programmatically performing a HTTP GET on the query URL
     * </li>
     * <li>
     * passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}
     * </li>   
     * </ul> 
    * 
     * @throws InvalidArgumentException if <tt>service</tt> is not a {@code http://} or {@code ivo://} reference
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/ 
    URL constructQueryF(URI service,Date start, Date end, String format) throws InvalidArgumentException, NotFoundException;

    
    /** 
     * Construct a STAP Query on Time and Position
     * @param service Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.
     *  <blockquote><dl>
     *  <dt>Resource Identifier</dt><dd>
     *  The resource ID of the STAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://mssl.ucl.ac.uk/stap-hinode-eis_2}
     *  <br/>The {@link Registry} will be queried to 
     *  resolve the resource ID into a {@link Resource} object, from which the {@link StapCapability} will be found, from which in turn the first
     *  {@link AccessURL} will be used.
     *  </dd>
     *  <dt>URL of the Service</dt><dd>
     *  The endpoint URL. Can be any {@code http://} URL.
     *  </dd>
     *  </dl></blockquote>
     * @param start start date and time
     * @param end end date and time 
     * @param ra right ascension  e.g {@code 6.950}
     * @param dec declination e.g. {@code -1.6}
     * @param size radius of cone e.g. {@code 0.1}
    * @return A query URL. The query can then be performed by either 
     * <ul>
     * <li>
     * programmatically performing a HTTP GET on the query URL
     * </li>
     * <li>
     * passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}
     * </li>   
     * </ul> 
    * 
     * @throws InvalidArgumentException if <tt>service</tt> is not a {@code http://} or {@code ivo://} reference
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/ 
    URL constructQueryP(URI service, Date start, Date end, double ra, double dec, double size) throws InvalidArgumentException, NotFoundException;
   
    /** Construct a STAP Query on Time, Position and Format
     * @param service Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.
     *  <blockquote><dl>
     *  <dt>Resource Identifier</dt><dd>
     *  The resource ID of the STAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://mssl.ucl.ac.uk/stap-hinode-eis_2}
     *  <br/>The {@link Registry} will be queried to 
     *  resolve the resource ID into a {@link Resource} object, from which the {@link StapCapability} will be found, from which in turn the first
     *  {@link AccessURL} will be used.
     *  </dd>
     *  <dt>URL of the Service</dt><dd>
     *  The endpoint URL. Can be any {@code http://} URL.
     *  </dd>
     *  </dl></blockquote>
     * @param start start date and time
     * @param end end date and time
     * @param ra right ascension  e.g {@code 6.950}
     * @param dec declination e.g. {@code -1.6}
     * @param size radius of cone e.g. {@code 0.1}
     * @param format format of images or time series data e.g. {@code ALL}
    * @return A query URL. The query can then be performed by either 
     * <ul>
     * <li>
     * programmatically performing a HTTP GET on the query URL
     * </li>
     * <li>
     * passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}
     * </li>   
     * </ul> 
    * 
     * @throws InvalidArgumentException if <tt>service</tt> is not a {@code http://} or {@code ivo://} reference
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/ 
   URL constructQueryPF(URI service, Date start, Date end, double ra,double dec, double size, String format) throws InvalidArgumentException, NotFoundException;
   
   /** Construct a STAP Query on Time and full Position
     * @param service Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.
     *  <blockquote><dl>
     *  <dt>Resource Identifier</dt><dd>
     *  The resource ID of the STAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://mssl.ucl.ac.uk/stap-hinode-eis_2}
     *  <br/>The {@link Registry} will be queried to 
     *  resolve the resource ID into a {@link Resource} object, from which the {@link StapCapability} will be found, from which in turn the first
     *  {@link AccessURL} will be used.
     *  </dd>
     *  <dt>URL of the Service</dt><dd>
     *  The endpoint URL. Can be any {@code http://} URL.
     *  </dd>
     *  </dl></blockquote>
    * @param start start date and time
    * @param end end date and time 
    * @param ra right ascension  e.g {@code 6.950}
    * @param dec declination e.g. {@code -1.6}
    * @param ra_size size of ra e.g. {@code 0.1}
    * @param dec_size size of dec  e.g. {@code 0.2}
    * @return A query URL. The query can then be performed by either 
     * <ul>
     * <li>
     * programmatically performing a HTTP GET on the query URL
     * </li>
     * <li>
     * passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}
     * </li>   
     * </ul> 
    * 
     * @throws InvalidArgumentException if <tt>service</tt> is not a {@code http://} or {@code ivo://} reference
    * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/   
   URL constructQueryS(URI service, Date start, Date end, double ra,double dec,double ra_size, double dec_size) throws InvalidArgumentException, NotFoundException;
   
   /** Construct a STAP Query on Time, full Position, and Format
     * @param service Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.
     *  <blockquote><dl>
     *  <dt>Resource Identifier</dt><dd>
     *  The resource ID of the STAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://mssl.ucl.ac.uk/stap-hinode-eis_2}
     *  <br/>The {@link Registry} will be queried to 
     *  resolve the resource ID into a {@link Resource} object, from which the {@link StapCapability} will be found, from which in turn the first
     *  {@link AccessURL} will be used.
     *  </dd>
     *  <dt>URL of the Service</dt><dd>
     *  The endpoint URL. Can be any {@code http://} URL.
     *  </dd>
     *  </dl></blockquote>
    * @param start start date and time
    * @param end end date and time  
    * @param ra right ascension  e.g {@code 6.950}
    * @param dec declination e.g. {@code -1.6}
    * @param ra_size size of ra e.g. {@code 0.1}
    * @param dec_size size of dec  e.g. {@code 0.2}
    * @param format format of images or time series data e.g. {@code ALL} 
    * @return A query URL. The query can then be performed by either 
     * <ul>
     * <li>
     * programmatically performing a HTTP GET on the query URL
     * </li>
     * <li>
     * passing the query URL to one of the {@link Dal} {@code execute} methods - such as {@link #executeAndSave(URL, URI)}
     * </li>   
     * </ul> 
    * 
     * @throws InvalidArgumentException if <tt>service</tt> is not a {@code http://} or {@code ivo://} reference
    * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/ 
   URL constructQuerySF(URI service,Date start, Date end, double ra, double dec, double ra_size, double dec_size, String format) throws InvalidArgumentException, NotFoundException;   
   
}


/* 
$Log: Stap.java,v $
Revision 1.9  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.8  2007/10/23 09:10:11  nw
RESOLVED - bug 2189: How to query stap services
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2189

Revision 1.7  2007/10/23 07:49:06  nw
ASSIGNED - bug 2189: How to query stap services
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2189

Revision 1.6  2007/03/08 17:46:56  nw
removed deprecated interfaces.

Revision 1.5  2006/10/10 14:07:56  nw
upgraded the dal interfaces.

Revision 1.4  2006/08/15 09:48:55  nw
added new registry interface, and bean objects returned by it.

Revision 1.3  2006/03/21 10:49:33  nw
replaced & with 'and' - xml gets generated from the javadoc, and loose '&' make the build break

Revision 1.2  2006/03/16 09:14:21  KevinBenson
usually comment/clean up type changes such as siap to stap

Revision 1.1  2006/03/13 14:47:21  KevinBenson
This is the first rough draft of Helioscope which deals with the STAP spec.

 
*/