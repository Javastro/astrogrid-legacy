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
import org.w3c.dom.Document;

/** AR Service: Query an arbitrary IVOA Registry.
 * 
 * This component gives access to a range of querying functions - for querying using keywords or XQuery. 
 * The functions either return a raw XML document, or a series of
 * datastructures that contain the parsed information of the registry entries.
 * 
 * {@stickyNote These functions are useful when you want to access records in a registry
 * other than the 'system configured' registry,
 * or if you wish to access the raw xml of the records.
 * For other cases, we recommend using the simple {@link Registry} service.}

 * The first parameter to each query method is the endpoint URL of the registry service to connect to.
 * These functions will also accept the Resource Identifier of a registry service - which 
 * will then be resolved into an endpoint URL using the System Registry.
 *
 * @see <a href="http://www.ivoa.net/Documents/latest/IDs.html">IVOA Identifiers</a>
 * @see <a href="http://www.ivoa.net/Documents/latest/VOResource.html">IVOA VOResource Definition</a>
 * @see <a href='http://www.ivoa.net/Documents/latest/RegistryInterface.html'>IVOA Registry Interface Standard</a>
 * @see <a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 18-Mar-2005
 * @service ivoa.externalRegistry
 * @see org.astrogrid.acr.ivoa.Registry Registry - simpler interface to system registry
 * @author Noel Winstanley

 */
public interface ExternalRegistry {
	
	/** Perform an ADQL/x query
	 * @exclude adql/x doesn't exist.
	 * Equivalent to  {@link #adqlsSearchXML} but expects the full xml form of ADQL - this is less
	 * error prone than the adql/s variant until someone defines adql/s properly and implements parsers for it.
	 */
	Document adqlxSearchXML(URI registry, Document adqlx, boolean identifiersOnly)  
		throws ServiceException, InvalidArgumentException;
	
	
	/** Perform an ADQL/x query, returning an array of datastructures.
	 *  @exclude adql/x doesn't exist.
	 * Equivalent to  {@link #adqlsSearch} but expects the full xml form of ADQL - which is less
	 * error prone than the adql/s variant until someone defines adql/s properly and implements parsers for it.
	 */
	Resource[] adqlxSearch(URI registry,Document adqlx)  throws ServiceException, InvalidArgumentException;
	
  
	/** Perform a ADQL/s query.
     * @exclude not working
     * <p />
     * {@example "Java Example"
     * import org.astrogrid.acr.*;
     * import org.astrogrid.acr.ivoa.ExternalRegistry;
     * import org.astrogrid.acr.builtin.ACR
     * Finder f = new Finder();
     * ACR acr = f.find();
     * ExternalRegistry reg = (ExternalRegistry)acr.getService(ExternalRegistry.class);
     * String query ="select * from Registry where vr:identifier='ivo://uk.ac.le.star/filemanager'"; 
     * Document d = reg.adqlsSearchXML(query);
     * }
     * 
     * @warning The ADQL support is poorly defined in the registry standard, and implementations vary. We recommend
     * using XQuery or keyword searching wherever possible.   
      * @param registry identifier or endpoint of the registry to connect to    
     * @param adqls a string query (string form of ADQL)
     * @return  xml document of search results -  a series of matching registry records contained within an element
     * called <tt>VOResources</tt> in the namespace <tt>http://www.ivoa.net/wsdl/RegistrySearch/v1.0</tt>
     * @throws ServiceException if an error occurs talking to the service
     * @throws InvalidArgumentException if the query is invalid in some way.
     * @xmlrpc will return a string containing the xml document 
     * 
     */
	Document adqlsSearchXML(URI registry,String adqls, boolean identifiersOnly)  throws ServiceException, InvalidArgumentException;

	
	/** Perform an ADQL/s query, returning an array of datastructures.
	 * @exclude not working
	 * Equivalent to {@link #adqlsSearchXML} but returning results in form that can be more easily used.
	 * 
	 * @warning The ADQL support is poorly defined in the registry standard, and implementations vary. We recommend
     * using XQuery or keyword searching wherever possible.   
	 */
	Resource[] adqlsSearch(URI registry,String adqls)  throws ServiceException, InvalidArgumentException;	
   
	
	
	/** Perform a keyword search, returning an XML Document.
     * 	@param registry resource identifier or endpoint URL  of the registry to connect to
     * @param keywords space separated list of keywords to search for
     * @param orValues - true to 'OR' together matches. false to 'AND' together matches
     * @return  xml document of search results -  A series of {@code VOResource} elements contained within an element
     * called {@code VOResources} in the namespace {@code http://www.ivoa.net/xml/RegistryInterface/v1.0}
     @see #getResourceXML(URI, URI) Example of registry resource XML
     * @throws ServiceException
     */
	Document keywordSearchXML(URI registry,String keywords, boolean orValues)  throws ServiceException;
	
	
	
	/** Perform a keyword search.
	 <p/>
	 A more convenient variant of {@link #keywordSearchXML}
	* <p />
	* {@example "Python Example"
     *  # connect to the AR
     * from xmlrpc import Server
     * from os.path import expanduser
     * ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
     *  #call this function
     * regEndpoint = 'http://www.my.registry.endpoint'
     * rs = ar.ivoa.externalRegistry.keywordSearch(regEndpoint,"abell",False)
     * #see what we've got	
     * print len(rs)
     * #list first 10 identifiers
     * for r in rs[:10]
     *    print r['id']:
	* }
	* The output is
	* <pre>
150
ivo://CDS.VizieR/J/A+A/486/755
ivo://uk.ac.le.star.tmpledas/ledas/ledas/abell
ivo://nasa.heasarc/wblgalaxy
ivo://nasa.heasarc/wbl
ivo://nasa.heasarc/twosigma
ivo://nasa.heasarc/rassebcs
ivo://nasa.heasarc/noras
ivo://nasa.heasarc/eingalclus
ivo://nasa.heasarc/abell
ivo://CDS.VizieR/VII/96	
	* </pre>
     *  @param registry resource identifier or endpoint URL  of the registry to connect to
     * @param keywords space separated list of keywords to search for
     * @param orValues - true to 'OR' together matches. false to 'AND' together matches
     * @return list of matching resources.
     * @throws ServiceException	
	 */
	Resource[] keywordSearch(URI registry,String keywords, boolean orValues)  throws ServiceException;
   
	
	
	/**Retrieve a named resource from a registry, as an XML document.
	 * 
	 * {@stickyNote Try to use {@link #getResource(URI, URI)} instead, which
	 * returns a result in a more usable format}
     * <p />
     * {@example "Python Example"
     *  # connect to the AR
     * from xmlrpc import Server
     * from os.path import expanduser
     * ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
     *  #call this function
     * regEndpoint = 'http://www.my.registry.endpoint'
     * resourceID = 'ivo://uk.ac.cam.ast/IPHAS/images/SIAP'
     * xml = ar.ivoa.externalRegistry.getResourceXML(regEndpoint,resourceID)
     * } 
     *      
     * {@example "Java Example" 
     * import org.astrogrid.acr.*;
     * import java.net.URI;
     * import org.astrogrid.acr.ivoa.ExternalRegistry;
     * import org.astrogrid.acr.builtin.ACR
     * Finder f = new Finder();
     * ACR acr = f.find();
     * ExternalRegistry reg = (ExternalRegistry)acr.getService(ExternalRegistry.class);
     * URI regEndpoint = new URI("http://www.my.registry.endpoint");
     * URI resourceID =new URI("ivo://uk.ac.cam.ast/IPHAS/images/SIAP");
     * Document xml = reg.getResourceXML(regEndpoint,resourceID);
     * }
     * 
     * The output will look something like
     * {@source
<ri:Resource xmlns:cea="http://www.ivoa.net/xml/CEA/v1.0rc1" xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v1.0" xmlns:va="http://www.ivoa.net/xml/VOApplication/v1.0rc1" xmlns:vg="http://www.ivoa.net/xml/VORegistry/v1.0" xmlns:vr="http://www.ivoa.net/xml/VOResource/v1.0" xmlns:vs="http://www.ivoa.net/xml/VODataService/v1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" created="2008-02-22T10:27:11" status="active" updated="2008-02-22T14:15:22" xsi:schemaLocation="http://www.ivoa.net/xml/VOResource/v1.0 http://software.astrogrid.org/schema/registry/RegistryInterface/v1.0/RegistryInterface.xsd http://www.ivoa.net/xml/VOResource/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd http://www.ivoa.net/xml/VODataService/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VODataService/v1.0/VODataService.xsd http://www.ivoa.net/xml/VOTable/v1.0 http://software.astrogrid.org/schema/vo-formats/VOTable/v1.0/VOTable.xsd" xsi:type="vr:Service">
        <title>IPHAS images</title>
        <identifier xmlns:sia="http://www.ivoa.net/xml/SIA/v1.0">ivo://uk.ac.cam.ast/IPHAS/images/SIAP</identifier>
        <curation>
            <publisher ivo-id="ivo://uk.ac.cam.ast/CASU">CASU</publisher>
            <creator>
                <name>IPHAS collaboration</name>
            </creator>
            <contact>
                <name>Guy Rixon</name>
                <email>gtr@ast.cam.ac.uk</email>
            </contact>
        </curation>
        <content>
            <subject>image, photometry, Halpha, INT-WFC</subject>
            <description>Images from the initial data release (IDR) of the INT Photometric Halpha Survey (IPHAS). The survey as a wholeis mapping the northern Galactic Plane in the latitude range |b|&lt;5 deg in the Halpha, r' and i' bands using the Wide Field Camera on the 2.5-m INT telescope at La Palma to a depth of r'=20 (10????). The IDR (Gonzalez-Solares et al. 2007) contains the data obtained between September 2003 and December 2005 during a total of 212 nights. Between these dates, approximately 60 percent of the total survey area was covered in terms of final survey quality.</description>
            <referenceURL>http://casu.ast.cam.ac.uk/surveys-projects/iphas</referenceURL>
            <type>Other</type>
            <contentLevel>Research</contentLevel>
        </content>
        <capability xmlns:sia="http://www.ivoa.net/xml/SIA/v1.0" standardID="ivo://ivoa.net/std/SIA" xsi:type="sia:SimpleImageAccess">
            <interface xsi:type="vs:ParamHTTP">
                <accessURL use="base">http://astrogrid.ast.cam.ac.uk/iphas/siap-atlas/queryImage?</accessURL>
            </interface>
            <imageServiceType>Pointed</imageServiceType>
            <maxQueryRegionSize>
                <long>360</long>
                <lat>360</lat>
            </maxQueryRegionSize>
            <maxImageExtent>
                <long>360</long>
                <lat>360</lat>
            </maxImageExtent>
            <maxImageSize>
                <long>4096</long>
                <lat>4096</lat>
            </maxImageSize>
            <maxFileSize>16800000</maxFileSize>
            <maxRecords>15000</maxRecords>
        </capability>
    </ri:Resource>      
     }
     * 
     *  @param registry resource identifier or endpoint URL  of the registry to connect to
     * @param id identifier of the registry resource to retrieve. e.g.{@code ivo://uk.ac.cam.ast/IPHAS/images/SIAP}
     * @return xml document of the registry entry - a {@code Resource} document 
     * in the {@code http://www.ivoa.net/xml/VOResource/v1.0} namespace
     * @throws NotFoundException if this resource does not exist
     * @throws ServiceException if an error occurs talking to the service
     * @xmlrpc will return a string containing the xml document
     */
	Document getResourceXML(URI registry,URI id) throws NotFoundException, ServiceException;
	
	
	  /** Retrieve a named resource from a registry.
     * 
     * For most uses, it's better to use this method instead of {@link #getResourceXML}, because the result is easier to work with.
     *  @param registry resource identifier or endpoint URL  of the registry to connect to
     * @param id identifier of the registry entry to retrieve
     * @return a  datastructue representing the registry entry - will be a {@link Resource} or one of it's 
     * subclasses depending on the registry entry type.
     * @throws NotFoundException if this resource does not exist
     * @throws ServiceException if an error occurs talking to the service
     * @xmlrpc will return a struct. See {@link Resource} for details of keys
     */
	Resource getResource(URI registry,URI id) throws NotFoundException, ServiceException;
	
    
	
	/** Search a registry using an XQuery, returning results as XML.
	 * <p />
	 * This method can accept an arbitrary XQuery, unlike {@link #xquerySearch(URI, String)}, which requires 
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
	 * regEndpoint = "http://www.my.registry.endpoint"
	 * xml = ar.ivoa.externalRegistry.xquerySearchXML(regEndpoint,xquery)
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
    *   @param registry resource identifier or endpoint URL  of the registry to connect to
     * @param xquery the query to perform. Must return a well-formed xml document - i.e. starting with a single root element.
     * @return the result of executing this xquery over the specified registry - a document of arbitrary structure.
         * @throws ServiceException if there's a problem connecting to the registry
    	 * @xmlrpc will return the string representation of the xml document.
    	 * @see <a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>
      * @note The XML view in VOExplorer is useful for constructing new XQueries. Enable it by {@code Preferences > System > Show Advanced Features}
     */
	Document xquerySearchXML(URI registry,String xquery)  throws ServiceException;
	
	
	
	/** Search a registry using an XQuery.
	 * <p />
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
     * regEndpoint = "http://www.my.registry.endpoint"
     * rs = ar.ivoa.externalRegistry.xquerySearch(regEndpoint,xquery)
     * }
     * 
     * The above XQuery could be written in a longer equivalent form, which is 
     * convenient when there are many filter clauses:
     * {@source
        for $r in //vor:Resource 
        where $r/@xsi:type  &=  '*DataCollection' 
        return $r     
     * }
     * 
     * 
    *   @param registry resource identifier or endpoint URL  of the registry to connect to
	 * @param xquery An XQuery that should return a document, or nodeset, containing whole {@code <Resource>} elements. 
	 * Results are not required to be single-rooted, and resource elements may be embedded within other elements - although the
	 * parser will fail in extreme cases.
	 * @return an array containing any registry records present in the query result.
	 * @xmlrpc will return an array of  struct. See {@link Resource} for details of keys
	 * @see <a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>	 	
	 * @throws ServiceException
     * @note The XML view in VOExplorer is useful for constructing new XQueries. Enable it by {@code Preferences > System > Show Advanced Features}
 	 
	 */
	Resource[] xquerySearch(URI registry,String xquery)  throws ServiceException;
	
	
	
	/** Access the  resource  describing a registry itself, as an XML Document.
	 * 
    *  @param registry resource identifier or endpoint URL  of the registry to connect to
	 * @return that registry's own service description - a single Resource documnt
	 * @throws ServiceException
     @see #getResourceXML(URI, URI) Example of registry resource XML	 
	 * @xmlrpc will return the string representation of the xml document.
	 */
	Document getIdentityXML(URI registry)  throws ServiceException;
	
	
	/**Access the  resource  describing a registry itself
	 * 
    *   @param registry resource identifier or endpoint URL  of the registry to connect to
	 * @return that registry's own service description 
	 * @throws ServiceException     	
	 * @xmlrpc will return a struct. See {@link Resource} for details of keys
	 */
	RegistryService getIdentity(URI registry)  throws ServiceException; 
	
	
	
	/** Build an array of resource objects from an XML document. 
	 * 
	 * @param doc an xml document of resources, for example one returned from a call to 
	 * {@link #keywordSearchXML(URI, String, boolean)}
	 * @return the parsed resource objects
	 * @throws ServiceException if the xml document cannot be parsed.
	 */
	Resource[] buildResources(Document doc)  throws ServiceException;

	
	
	/**
	 * 
	 * @exclude 
	 * @deprecated registry of registries does not provide a search interface
	 * @return always null
	 */
	@Deprecated
    URI getRegistryOfRegistriesEndpoint();

}
