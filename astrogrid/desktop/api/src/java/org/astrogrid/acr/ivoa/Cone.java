/**
 * 
 */
package org.astrogrid.acr.ivoa;

import java.net.URI;
import java.net.URL;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.Resource;

/** AR Service: Query for <b>Catalogs</b> from Cone-Search Services (DAL).
 * 
 * <p />
 * {@stickyNote This class provides functions to construct a DAL query. 
 * To execute that query, see the examples and methods in the {@link Dal} class.
 * }
 * <h2>Constructing a Query</h2>
 * The first stage in querying a cone-service is to select the service to query, find the position to query at, and then call the {@link #constructQuery(URI, double, double, double)}
 * function:
 * {@example "Contructing a Cone Query (Python)"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')        
cone = ar.ivoa.cone #take a reference to the AR Cone component

#the Cone service to query (selected using voexplorer)
service = "ivo://irsa.ipac/2MASS-PSC"
#resolve an object name to a position
pos = ar.cds.sesame.resolve('m54')
#build a query
query = cone.constructQuery(service,pos['ra'],pos['dec'],0.001)
print "QueryURL",query
}
This script produces a query URL (shown below), which can the be passed to the methods in the {@link Dal} class.
<blockquote><tt>
QueryURL http://irsa.ipac.caltech.edu/cgi-bin/Oasis/CatSearch/nph-catsearch?CAT=fp_psc&RA=283.7636667&DEC=-30.4785&SR=0.0010
</tt></blockquote>
 
 <h2>Adding the VERB parameter</h2>
 The optional {@code VERB} parameter determines how many columns are to be returned by the service. From the Cone specification:
 <blockquote><i>
 The query MAY contain the optional parameter, VERB, whose value is an integer - either {@code 1}, {@code 2}, or {@code 3} -  which determines 
 how many columns are to be returned in the resulting table. Support for this parameter by a Cone Search service implementation is optional. 
 If the service supports the parameter, then when the value is {@code 1}, the response should include the bare minimum of columns that the provider 
 considers useful in describing the returned objects. 
 When the value is {@code 3}, the service should return all of the columns that are available for describing the objects. A value of {@code 2} is 
 intended for requesting a medium number of columns between the minimum and maximum (inclusive) that are considered by the provider to 
 most typically useful to the user. When the VERB parameter is not provided, the server should respond as if {@code VERB=2}. 
 If the {@code VERB} parameter is not supported by the service, the service should ignore the parameter and should always return the same columns for every request.
</i></blockquote>

A query that includes the VERB parameter can be constructed as follows: 
{@example "Constructing a Query with the VERB parameter (Python)"
#build a query, as before.
query = cone.constructQuery(service,pos['ra'],pos['dec'],0.001)
#add a 'verb' parameter
query = cone.addOption(query,"VERB",3)
}
 
 * @author Noel Winstanley
 * @service ivoa.cone
 * @see <a href='http://www.ivoa.net/Documents/latest/ConeSearch.html'>IVOA Cone Search Standard Document</a>
 * @note This service extends the {@link Dal} interface, which provides functions to execute queries. 
 * The functions listed within this interface are just for <i>constructing</i> that query.

 * @see Dal
 */
public interface Cone extends Dal {
	 /** Construct a Cone-Search Query.
	  * 
	  * The cone search standard allows queries on Right Ascension, Declination and Search Radius,
	  * all given in decimal degrees.
     * 
     * @param service Resource Identifier <i>or</i> URL of the Service to query.  <i>Prefer providing a Resource Identifier, as this insulates against changes in service endpoint</i>.
     *  <blockquote><dl>
     *  <dt>Resource Identifier</dt><dd>
     *  The resource ID of the Cone Search service to query, as returned by {@link Resource#getId()}. Example: {@code ivo://irsa.ipac/2MASS-XSC}
     *  <br/>The {@link Registry} will be queried to 
     *  resolve the resource ID into a {@link Resource} object, from which the {@link ConeCapability} will be found, from which in turn the first
     *  {@link AccessURL} will be used.
     *  </dd>
     *  <dt>URL of the Service</dt><dd>
     *  The endpoint URL. Can be any {@code http://} URL.
     *  </dd>
     *  </dl></blockquote>
     * @param ra right ascension e.g {@code 6.950}
     * @param dec declination e.g. {@code -1.6}
     * @param sr search radius e.g. {@code 0.1}
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
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in reg)
     * @see Sesame Sesame - resolves object names to RA,Dec positions
     * @see #getRegistryXQuery() getRegistryXQuery() - a query to list all Cone Search Services.  
     * 
     */
    URL constructQuery(URI service, double ra, double dec, double sr) throws InvalidArgumentException, NotFoundException;
 
        
    
    
}
