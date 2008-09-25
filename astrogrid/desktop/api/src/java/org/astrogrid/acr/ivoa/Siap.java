/*$Id: Siap.java,v 1.9 2008/09/25 16:02:04 nw Exp $
 * Created on 17-Oct-2005
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
import org.astrogrid.acr.ivoa.resource.SiapCapability;

/** AR Service: Query for <b>Images</b> from Simple Image Access Protocol (SIAP) services (DAL).
 * <p />
 * {@stickyNote This class provides functions to construct a DAL query. 
 * To execute that query, see the examples and methods in the {@link Dal} class.
 * }
 * 
 * <h2>Example</h2>
 * The following example constructs a query URL, performs the query, and then downloads the 
 * resulting images. See {@link Dal} for other things that can be done with a query URL.
 * {@example "Query a SIAP service and download images (Python)"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')        
siap = ar.ivoa.siap #take a reference to the AR SIAP component

#the SIAP service to query (selected using voexplorer)
service = "ivo://irsa.ipac/MAST-Scrapbook"

#resolve an object name to a position
pos = ar.cds.sesame.resolve('m54')

#build a query
query = siap.constructQuery(service,pos['ra'],pos['dec'],0.001)
print "QueryURL",query

#execute the query
rows = siap.execute(query)

#inspect what we've got.
print "Rows Returned",len(rows)
print "Metadata Keys",rows[0].keys()
print "Image URLs"
for r in rows :
    print r['AccessReference']
    
#download these datasets into current directory
#compute url for current directory
from urlparse import urlunsplit
from os import getcwd
currentDirURL = urlunsplit(['file','',getcwd(),'',''])
print "Downloading images to",currentDirURL
siap.saveDatasets(query,currentDirURL)
 * } 
 * The output from this script is shown below. The result is that 3 files ({@code data-0.fits}, {@code data-1.fits}, {@code data-2.fits}) are downloaded to the current directory.
 * <blockquote><tt>
QueryURL http://irsa.ipac.caltech.edu/cgi-bin/Atlas/nph-atlas?mission=Scrapbook&hdr_location=%5CScrapbookDataPath%5C&collection_desc=The+MAST+Image%2FSpectra+Scrapbook+%28Scrapbook%29&SIAP_ACTIVE=1&POS=283.7636667%2C-30.4785&SIZE=0.0010<br />
Rows Returned 3<br />
Metadata Keys ['Scale', 'crpix1', 'crpix2', 'Title', 'inst_id', 'cd2_1', 'ctype2', 'ctype1', 'cd2_2', 'DEC', 'size', 'RADAR', 'Format', 'naxis1', 'naxis2', 'object_id', 'MAST', 'bandpass_id', 'fname', '2mass_fits', 'Naxes', 'RA', 'mjd', 'AccessReference', 'crval2', 'crval1', 'bandpass_unit', 'Naxis', 'GIF', 'ra4', 'bandpass_lolimit', 'ra2', 'ra3', 'ra1', 'dec4', 'dec1', 'dec2', 'dec3', 'cd1_2', 'VOX:WCS_CDMatrix', 'cd1_1', 'data_id', '2mass_jpeg', 'bandpass_refvalue', 'bandpass_hilimit']<br />
Image URLs<br />
http://archive.stsci.edu/cgi-bin/hst_preview_search?imfmt=fits&name=U37GA405R<br />
http://archive.stsci.edu/cgi-bin/hst_preview_search?imfmt=fits&name=U37GA40CR<br />
http://archive.stsci.edu/cgi-bin/hst_preview_search?imfmt=fits&name=O5HJT3DYQ<br />
Downloading images to file:///Users/noel/Documents/workspace/python 
 * </tt></blockquote>
 * 
 * @see Dal
 * @see <a href="http://www.ivoa.net/Documents/latest/SIA.html">IVOA SIAP Standard Document</a> 
     * @see Sesame Sesame - resolves object names to RA,Dec positions
     * @see #getRegistryXQuery() getRegistryXQuery() - a query to list all SIAP Services.  
 * @service ivoa.siap

 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Oct-2005
 */
public interface Siap extends Dal {
    /**
     * Construct a SIAP Query on Right Ascension, Declination and Search radius (decimal degrees).
     *  
     * @param service Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.
     *  <blockquote><dl>
     *  <dt>Resource Identifier</dt><dd>
     *  The resource ID of the SIAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://nasa.heasarc/skyview/halpha}
     *  <br/>The {@link Registry} will be queried to 
     *  resolve the resource ID into a {@link Resource} object, from which the {@link SiapCapability} will be found, from which in turn the first
     *  {@link AccessURL} will be used.
     *  </dd>
     *  <dt>URL of the Service</dt><dd>
     *  The endpoint URL. Can be any {@code http://} URL.
     *  </dd>
     *  </dl></blockquote>
     * @param ra right ascension  e.g {@code 6.950}
     * @param dec declination e.g. {@code -1.6}
     * @param size radius of cone  e.g. {@code 0.1}
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
    URL constructQuery(URI service,double ra, double dec, double size) throws InvalidArgumentException, NotFoundException;
   
    /** 
     * Construct a SIAP Query on Right Ascension, Declination, Search radius (decimal degrees), and Format.
     * 
     * @param service Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.
     *  <blockquote><dl>
     *  <dt>Resource Identifier</dt><dd>
     *  The resource ID of the SIAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://nasa.heasarc/skyview/halpha}
     *  <br/>The {@link Registry} will be queried to 
     *  resolve the resource ID into a {@link Resource} object, from which the {@link SiapCapability} will be found, from which in turn the first
     *  {@link AccessURL} will be used.
     *  </dd>
     *  <dt>URL of the Service</dt><dd>
     *  The endpoint URL. Can be any {@code http://} URL.
     *  </dd>
     *  </dl></blockquote>
     * @param ra right ascension  e.g {@code 6.950}
     * @param dec declination  e.g. {@code -1.6}
     * @param size radius of cone  e.g. {@code 0.1}
     * @param format format of images e.g. {@code FITS}
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
   URL constructQueryF(URI service, double ra,double dec,double size, String format) throws InvalidArgumentException, NotFoundException;
   
   
   /** 
     * Construct a SIAP Query on Right Ascension, Declination, and Search area in RA and Dec
     * 
     * @param service Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.
     *  <blockquote><dl>
     *  <dt>Resource Identifier</dt><dd>
     *  The resource ID of the SIAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://nasa.heasarc/skyview/halpha}
     *  <br/>The {@link Registry} will be queried to 
     *  resolve the resource ID into a {@link Resource} object, from which the {@link SiapCapability} will be found, from which in turn the first
     *  {@link AccessURL} will be used.
     *  </dd>
     *  <dt>URL of the Service</dt><dd>
     *  The endpoint URL. Can be any {@code http://} URL.
     *  </dd>
     *  </dl></blockquote>
    * @param ra right ascension  e.g {@code 6.950}
    * @param dec declination  e.g. {@code -1.6}
    * @param ra_size size of {@code ra}  e.g. {@code 0.1}
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
     * @throws InvalidArgumentException if <tt>service</tt> is not a {@code http://} or {@code ivo://} reference

    * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/   
   URL constructQueryS(URI service, double ra,double dec,double ra_size, double dec_size) throws InvalidArgumentException, NotFoundException;
   
   /** 
     * Construct a SIAP Query on Right Ascension, Declination, Search area in RA and Dec, and Format.
     * @param service Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.
     *  <blockquote><dl>
     *  <dt>Resource Identifier</dt><dd>
     *  The resource ID of the SIAP service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://nasa.heasarc/skyview/halpha}
     *  <br/>The {@link Registry} will be queried to 
     *  resolve the resource ID into a {@link Resource} object, from which the {@link SiapCapability} will be found, from which in turn the first
     *  {@link AccessURL} will be used.
     *  </dd>
     *  <dt>URL of the Service</dt><dd>
     *  The endpoint URL. Can be any {@code http://} URL.
     *  </dd>
     *  </dl>
     *  </blockquote>
    * @param ra right ascension  e.g {@code 6.950}
    * @param dec declination  e.g. {@code -1.6}
    * @param ra_size size of {@code ra} e.g. {@code 0.1}
    * @param dec_size size of {@code dec} e.g. {@code 0.2}
     * @param format format of images {@code FITS} 
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
   URL constructQuerySF(URI service, double ra, double dec, double ra_size, double dec_size, String format) throws InvalidArgumentException, NotFoundException;
 
   
}


/* 
$Log: Siap.java,v $
Revision 1.9  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.8  2007/11/12 13:36:27  pah
change parameter name to structure

Revision 1.7  2007/03/08 17:48:06  nw
tidied.

Revision 1.6  2007/01/24 14:04:45  nw
updated my email address

Revision 1.5  2006/10/10 14:07:44  nw
upgraded the dal interfaces.

Revision 1.4  2006/08/15 09:48:55  nw
added new registry interface, and bean objects returned by it.

Revision 1.3  2006/02/24 12:17:52  nw
added interfaces for skynode

Revision 1.2  2006/02/02 14:19:47  nw
fixed up documentation.

Revision 1.1  2005/10/18 07:58:21  nw
added first DAL interfaces
 
*/