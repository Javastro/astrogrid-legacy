/**
 * 
 */
package org.astrogrid.acr.ivoa;

import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.RegistryService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SearchCapability;
import org.astrogrid.acr.system.Configuration;
import org.w3c.dom.Document;

/** AR Service: Query  the system-configured Registry.
 * 
 * AstroRuntime uses an IVOA-compliant registry to retrieve details of available resources
 *  - servers, applications, catalogues, etc.
 *  <p/>
 *  This component provides a range of different methods to search the registry. The
 *  most convenient is to use the {@link #search(String)} method, which accepts a simple
 *  query in the form used by VOExplorer's smart-lists.
 *  <p/>
 *  The endpoint of this registry service can be inspected by calling {@link #getSystemRegistryEndpoint()}.
 *  In cases where this service is unavailable, registry queries will automatically fall-back to the
 *  backup registry service, whose endpoint is defined by {@link #getFallbackSystemRegistryEndpoint()}.
 * The query functions in this interface are the equivalent to their counterparts in the 
 * {@link ExternalRegistry} interface, but operate against the System and Fallback registries.
 *  {@stickyNote These endpoints can be altered by using the UI preferences pane, or the web interface, or via commandline properties, or
 *  programmatically using the {@link Configuration} service.}
 *  
 * @see <a href="http://www.ivoa.net/Documents/latest/IDs.html">IVOA Identifiers</a>
 * @see <a href="http://www.ivoa.net/Documents/latest/VOResource.html">IVOA VOResource Definition</a>
 * @see <a href='http://www.ivoa.net/Documents/latest/RegistryInterface.html'>IVOA Registry Interface Standard</a>
 * @see <a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 18-Mar-2005
 * @service ivoa.registry
 * @see ExternalRegistry External Registry - fuller interface to an arbitrary registry
 * @author Noel Winstanley

 */
public interface Registry {
 
    
    /** Perform a search using the query language used by VOExplorer SmartLists (SRQL)
    * <p/>
    * {@example "Search for IR Redshift resources (Python)"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
#call this function
rs = ar.ivoa.registry.search("ucd = redshift AND waveband = infrared")
#see what we've got  
print len(rs)
#list first 10 identifiers
for r in rs[:10]:
    print r['id']
    * }
    * 
    * The output is
    * <pre>
    * 109
    * ivo://CDS.VizieR/J/ApJ/655/51
    * ivo://CDS.VizieR/J/ApJ/653/1004
    * ivo://CDS.VizieR/J/ApJ/649/63
    * ivo://CDS.VizieR/J/ApJ/634/128
    * ivo://CDS.VizieR/J/AJ/117/102
    * ivo://CDS.VizieR/J/AN/329/418
    * ivo://CDS.VizieR/J/ApJS/166/470
    * ivo://CDS.VizieR/VII/173
    * ivo://CDS.VizieR/VII/157
    * ivo://CDS.VizieR/J/other/PBeiO/18.7
    * </pre>         
     * @param srql   query to perform
     * @return list of matching resources
     * @throws ServiceException if registry cannot be queried.
     * @throws InvalidArgumentException if query passed in cannot be parsed as SRQL
     * @see <a href='http://eurovotech.org/twiki/bin/view/VOTech/SimpleRegistryQueryLanguage'>SRQL Language Description</a> 
     * @equivalence xquerySearch(toXQuery(srql)) 
     */
     Resource[] search(String srql) throws ServiceException, InvalidArgumentException;
    
    
    /** Perform an ADQL/x registry search, return a list of matching resources
     * @exclude adql/x doesnt exist anymore
     * @warning The ADQL support is poorly defined in the registry standard, and implementations vary. We recommend
     * using XQuery or keyword searching wherever possible.
     * @see ExternalRegistry#adqlxSearch*/
	Resource[] adqlxSearch(Document adqlx)  throws ServiceException, InvalidArgumentException;
 
	
	
	/** Perform an ADQL/s registry search, return a list of matching resources.
	 * @exclude not working.
     * <p />
	 * {@example "Java Example"	 
	 * import org.astrogrid.acr.*;
	 * import org.astrogrid.acr.ivoa.Registry;
	 * import org.astrogrid.acr.builtin.ACR
	 * Finder f = new Finder();
	 * ACR acr = f.find();
	 * Registry reg = (Registry)acr.getService(Registry.class);
	 * String query ="select * from Registry where shortName='HST.GOODS.Cutout'"; 
	 * Document d = reg.adqlsSearchXML(query);
	 * </pre>
     * @warning The ADQL support is poorly defined in the registry standard, and implementations vary. We recommend
     * using XQuery or keyword searching wherever possible.
     * @param adqls a string query (string form of ADQL)
     * @return a list of matching resources

     * @throws ServiceException if an error occurs talking to the service
     * @throws InvalidArgumentException if the query is invalid in some way.
     * 
     * @see ExternalRegistry#adqlsSearch*/
	Resource[] adqlsSearch(String adqls)  throws ServiceException, InvalidArgumentException;	
 
	
	
	/** Perform a keyword  search.
    * <p/>
    * {@example "Python Example"
# connect to the AR
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
#call this function
rs = ar.ivoa.registry.keywordSearch("abell",False)
#see what we've got  
print len(rs)
#list first 10 identifiers
for r in rs[:10]
    print r['id']:
    * }
    * 
    * The output is
    * <pre>
    * 150
    * ivo://CDS.VizieR/J/A+A/486/755
    * ivo://uk.ac.le.star.tmpledas/ledas/ledas/abell
    * ivo://nasa.heasarc/wblgalaxy
    * ivo://nasa.heasarc/wbl
    * ivo://nasa.heasarc/twosigma
    * ivo://nasa.heasarc/rassebcs
    * ivo://nasa.heasarc/noras
    * ivo://nasa.heasarc/eingalclus
    * ivo://nasa.heasarc/abell
    * ivo://CDS.VizieR/VII/96 
    * </pre>	 
    * @param keywords space separated list of keywords to search for
    * @param orValues - true to 'OR' together matches. false to 'AND' together matches
    * @return list of matching resources.
    * 
    * @throws ServiceException if an error occurs talking to the service     
    * 
    */
    Resource[] keywordSearch(String keywords, boolean orValues)  throws ServiceException;
	
    

     
     /** Translate a SRQL query (as used by VOExplorer SmartLists) into an equivalent XQuery 
     * @param srql  the query to translate 
     * @return the equivalent XQuery 
     * @throws InvalidArgumentException if the query cannot be parsed as SRQL. 
     * @see <a href='http://eurovotech.org/twiki/bin/view/VOTech/SimpleRegistryQueryLanguage'>SRQL Language Description</a> 
 
      * 
      * */
     String toXQuery(String srql) throws InvalidArgumentException;
     
    /** Retrieve a named resource from the registry.
   * @param id identifier of the registry resource to retrieve. e.g.{@code ivo://uk.ac.cam.ast/IPHAS/images/SIAP}
     * @return a  datastructure representing the registry entry - will be a {@link Resource} or one of it's 
     * subclasses depending on the registry entry type.
     * @xmlrpc will return a struct. See {@link Resource} for details of keys
     */
    Resource getResource(URI id)  throws NotFoundException, ServiceException;

    
    
    /**Search the registry using an XQuery.
     * <p/>
     * {@stickyWarning This method returns an array of matching {@link Resource} objects - so the XQuery
     * used must produce whole {@code VOResource} elements}
     * 
     * {@example "Python Example"    
     *  # connect to the AR
     * from xmlrpc import Server
     * from os.path import expanduser
     * ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
     *  #call this function
     * xquery ="//vor:Resource[@xsi:type &= '*DataCollection']" 
     * rs = ar.ivoa.registry.xquerySearch(xquery)
     * }
     * 
     * The above XQuery could be written in a longer equivalent form, which is 
     * convenient when there are many filter clauses:
     * {@source
     *   for $r in //vor:Resource 
     *   where $r/@xsi:type  &=  '*DataCollection' 
     *   return $r     
     * }
     * 
     * @param xquery An XQuery that should return a document, or nodeset, containing whole {@code <Resource>} elements. 
     * Results are not required to be single-rooted, and resource elements may be embedded within other elements - although the
     * parser will fail in extreme cases.
     * @return an array containing any registry records present in the query result.
     * @xmlrpc will return an array of  struct. See {@link Resource} for details of keys
     * @see <a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>      
     * @throws ServiceException
     * @note The XML view in VOExplorer is useful for constructing new XQueries. Enable it by {@code Preferences > System > Show Advanced Features}     
     * @see Dal#getRegistryXQuery() Dal.getRegistryXQuery() - produces XQueries to list all DAL services.  
     * */
    Resource[] xquerySearch(String xquery)  throws ServiceException;
    
    
    
    /** Search the registry using an XQuery, returning results as XML.
     * <p />
     * This method can accept an arbitrary XQuery, unlike {@link #xquerySearch(String)}, which requires 
     * that the XQuery return a list of VOResource elements.
     * 
     * {@example "Python Example"    
     *  # connect to the AR
     * from xmlrpc import Server
     * from os.path import expanduser
     * ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
     *  #call this function
     * xquery ="""
     <ssap-wavebands> 
          {
          (:find all spectral services :)
           let $ssap := //vor:Resource[capability/@standardID="ivo://ivoa.net/std/SSA"]          
           (: find the distinct set of wavebands these services cover  (no duplicates) :)
           for $waveband in distinct-values($ssap/coverage/waveband)
           order by $waveband
           (: print each waveband in turn :)
           return <band name="{data($waveband)}">
                  {
                  (: list IDs of all services that cover this band :)
                    for $r in $ssap[coverage/waveband=$waveband]
                    return $r/identifier
                  }
                  </band> 
          }
       </ssap-wavebands>
          """   
     * xml = ar.ivoa.registry.xquerySearchXML(regEndpoint,xquery)
     * }
     * 
     *This will return the following result
    {@source    
<ssap-wavebands>
    <band name="EUV">
        <identifier>ivo://iap.fr/FUSE/SSA</identifier>
        <identifier>ivo://www.g-vo.org/ssa.service.tmap</identifier>
    </band>
    <band name="Infrared">
        <identifier>ivo://archive.eso.org/ESO-SAF-SSAP</identifier>
        <identifier>ivo://basebe.obspm.fr/bess0.1</identifier>
        ...
    </band>
    <band name="Millimeter">
        <identifier>ivo://svo.laeff/models/dalessio</identifier>
        <identifier>ivo://voparis.obspm.gepi/BeStars/BeSS/SSAP</identifier>
    </band>
    <band name="Optical">
        <identifier>ivo://archive.eso.org/ESO-SAF-SSAP</identifier>
        <identifier>ivo://basebe.obspm.fr/bess0.1</identifier>
        ...
    </band>
    <band name="Radio">
        <identifier>ivo://obspm.fr/SSA_HIG</identifier>
        <identifier>ivo://voparis.obspm.gepi/BeStars/BeSS/SSAP</identifier>
    </band>
    <band name="UV">
        <identifier>ivo://archive.eso.org/ESO-SAF-SSAP</identifier>
        <identifier>ivo://basebe.obspm.fr/bess0.1</identifier>
        ...
    </band>
    <band name="X-ray">
        <identifier>ivo://svo.iaa/models/SSP/Xray</identifier>
        <identifier>ivo://www.g-vo.org/ssa.service.tmap</identifier>
    </band>
</ssap-wavebands>
     }
      * @param xquery the query to perform. Must return a well-formed xml document - i.e. starting with a single root element.
     * @return the result of executing this xquery over the specified registry - a document of arbitrary structure.
         * @throws ServiceException if there's a problem connecting to the registry
         * @xmlrpc will return the string representation of the xml document.
         * @see <a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>
      * @note The XML view in VOExplorer is useful for constructing new XQueries. Enable it by {@code Preferences > System > Show Advanced Features}
 
     @see ExternalRegistry#getResourceXML(URI, URI) Example of registry resource XML     
     */
    Document xquerySearchXML(String xquery)  throws ServiceException;
    
    
    
    /** Access the resource that describing the system registry itself.
     * 
     * <p />
     * This returned resource describes what search capabilities are provided by the registry
     * @see SearchCapability
     * @see ExternalRegistry#getIdentity
     */
	RegistryService getIdentity()  throws ServiceException; 

	
	
	/** Access the endpoint of the system registry */
	URI getSystemRegistryEndpoint()  throws ServiceException;
	
	
	/** Access the endpoint of the fallback system registry */
	URI getFallbackSystemRegistryEndpoint()  throws ServiceException;
}
