/*$Id: Ssap.java,v 1.8 2008/09/25 16:02:03 nw Exp $
 * Created on 26-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.ivoa;

import java.net.URI;
import java.net.URL;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SsapCapability;

/** AR Service: Query for <b>Spectra</b> from Simple Spectral Access Protool (SSAP) Services (DAL).
 * <p />
 * {@stickyNote This class provides functions to construct a DAL query. 
 * To execute that query, see the examples and methods in the {@link Dal} class.
 * }
 * 
 * <h2>Example</h2>
 * The following example constructs a query URL, performs the query, and then downloads the 
 * first three spectra. See {@link Dal} for other things that can be done with a query URL.
 * 
 * {@example "Query a SSAP service and download a subset of spectra (Python)"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')        
ssap = ar.ivoa.ssap #take a reference to the AR SSAP component

#the SSAP service to query (selected using voexplorer)
service = "ivo://archive.eso.org/ESO-SAF-SSAP"

#resolve an object name to a position
pos = ar.cds.sesame.resolve('m1')

#build a query
query = ssap.constructQuery(service,pos['ra'],pos['dec'],0.005)
print "QueryURL",query

#execute the query
rows = ssap.execute(query)

#inspect what we've got.
print "Rows Returned",len(rows)
print "Metadata Keys",rows[0].keys()

#download first three datasets into current directory
#compute url for current directory
from urlparse import urlunsplit
from os import getcwd
currentDirURL = urlunsplit(['file','',getcwd(),'',''])
print "Downloading images to",currentDirURL
ssap.saveDatasetsSubset(query,currentDirURL,[0,1,2])
 * }
 * The output of this script is shown below.
 * <blockquote><tt>
QueryURL http://archive.eso.org/apps/ssaserver/EsoProxySsap?POS=83.6332083%2C22.0144722&SIZE=0.0050&REQUEST=queryData <br />
Rows Returned 63<br />
Metadata Keys ['meta.bib.bibcode', 'SpectralLocation', 'time.duration;obs.exposure', 'meta.display.url', 'pos.eq', 'AssocID', 'time;meta.dataset', 'meta.id;obs.seq', 'SpectralAxisUnit', 'TimeCalibration', 'meta.code.class;obs', 'meta.ref.url', 'FluxCalibration', 'time.duration;obs', 'instr.fov', 'FovRef', 'Format', 'PublisherDate', 'Collection', 'meta.id;meta.dataset', 'meta.curation', 'meta.id;obs', 'DataLength', 'FluxAxisUnit', 'DatasetType', 'pos.eq.ra;meta.main', 'meta.id;instr', 'meta.title;obs.proposal', 'instr.bandwidth', 'meta.code.class;obs.param', 'time.start;obs', 'time.epoch', 'meta.version;meta.dataset', 'pos.eq.dec;meta.main', 'SpectralCalibration', 'instr.bandpass', 'time.equinox;pos.frame', 'PosAngle', 'meta.title;meta.dataset', 'Creator', 'meta.code.class', 'em;stat.min', 'CreationType', 'instr.setup', 'SpatialCalibration', 'meta.code.class;instr.setup', 'meta.id;src', 'SpaceFrameName', 'meta.code;obs.proposal', 'em;stat.max', 'DataSource', 'meta.id.PI', 'time.end;obs', 'DataModel', 'meta.id;instr.tel']<br />
Downloading images to file:///Users/noel/Documents/workspace/python
 * </tt></blockquote>
 * @see Dal
     * @see Sesame Sesame - resolves object names to RA,Dec positions
     * @see #getRegistryXQuery() getRegistryXQuery() - a query to list all SSAP Services. 
     * @see <a href='http://www.ivoa.net/Documents/latest/SSA.html'>IVOA SSAP Standard Document</a>  
 * @service ivoa.ssap

 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 26-Jan-2006
 *
 */
public interface Ssap extends Dal {
    
    /** 
     * Construct a SSAP Query on Right Ascension, Declination and Search radius (decimal degrees).
     *  
     * @param service Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.
     *  <blockquote><dl>
     *  <dt>Resource Identifier</dt><dd>
     *  The resource ID of the SSAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://archive.eso.org/ESO-SAF-SSAP]
     *  <br/>The {@link Registry} will be queried to 
     *  resolve the resource ID into a {@link Resource} object, from which the {@link SsapCapability} will be found, from which in turn the first
     *  {@link AccessURL} will be used.
     *  </dd>
     *  <dt>URL of the Service</dt><dd>
     *  The endpoint URL. Can be any {@code http://} URL.
     *  </dd>
     *  </dl></blockquote>
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


     * @throws InvalidArgumentException if <tt>service</tt> is not a {@code http://} or {@code ivo://} reference
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/ 
    URL constructQuery(URI service,double ra, double dec, double size) throws InvalidArgumentException, NotFoundException;

    /** 
     * Construct a SSAP Query on Right Ascension, Declination, and Search area in RA and Dec
     * 
     * 
     * @param service Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.
     *  <blockquote><dl>
     *  <dt>Resource Identifier</dt><dd>
     *  The resource ID of the SSAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://archive.eso.org/ESO-SAF-SSAP}
     *  <br/>The {@link Registry} will be queried to 
     *  resolve the resource ID into a {@link Resource} object, from which the {@link SsapCapability} will be found, from which in turn the first
     *  {@link AccessURL} will be used.
     *  </dd>
     *  <dt>URL of the Service</dt><dd>
     *  The endpoint URL. Can be any {@code http://} URL.
     *  </dd>
     *  </dl></blockquote>
     * @param ra right ascension e.g {@code 6.950}
     * @param dec declination e.g. {@code -1.6}
     * @param ra_size size of {@code ra} e.g. {@code 0.1}
     * @param dec_size size of {@code dec} e.g. {@code 0.2}
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
      * 
     * @throws InvalidArgumentException if <tt>service</tt> is not a {@code http://} or {@code ivo://} reference
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/   
    URL constructQueryS(URI service, double ra,double dec,double ra_size, double dec_size) throws InvalidArgumentException, NotFoundException;
    
}


/* 
$Log: Ssap.java,v $
Revision 1.8  2008/09/25 16:02:03  nw
documentation overhaul

Revision 1.7  2008/04/14 09:40:30  nw
documentation tweak.

Revision 1.6  2007/03/08 17:48:06  nw
tidied.

Revision 1.5  2007/01/24 14:04:45  nw
updated my email address

Revision 1.4  2006/10/10 14:07:44  nw
upgraded the dal interfaces.

Revision 1.3  2006/08/15 09:48:55  nw
added new registry interface, and bean objects returned by it.

Revision 1.2  2006/02/24 12:17:52  nw
added interfaces for skynode

Revision 1.1  2006/02/02 14:19:47  nw
fixed up documentation.
 
*/