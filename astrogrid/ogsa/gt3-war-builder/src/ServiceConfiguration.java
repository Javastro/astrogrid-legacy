// (c) International Business Machines Corporation, 2002, 2003. (c) University of Edinburgh 2002, 2003.
// See OGSA-DAI-Licence.txt for licencing information.
package uk.org.ogsadai.common;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.org.ogsadai.porttype.gds.dataresource.DataResourceImplementation;
import uk.org.ogsadai.service.OGSADAIConstants;
import uk.org.ogsadai.xml.dataresourceconfig.ActivityMapType;

/**
 * @author Amy Krause 
 * Date: Mar 27, 2003
 * 
 * ServiceConfiguration stores the information that is passed to the GDS on
 * construction.
 */
public class ServiceConfiguration
{
	// Copyright statement
	private static final String copyrightNotice = "(c) IBM Corp. 2002, 2003. (c) University of Edinburgh 2002, 2003";

	// Logger object for logging in this class
	private static Logger mLog = Logger.getLogger(ServiceConfiguration.class.getName());

	// the data resource implementation this GDS can connect to
	private DataResourceImplementation mDataResourceImpl;
	// ActivityMapTypes indexed by data resource name
	private ActivityMapType[] mActivityMaps;
	// GSH of the factory that created this GDS instance
	private String mFactoryHandle;

	// activity classes indexed by activity name
	private Map mActivityClassMap;

	// the default data resource if there is only one in the list
	private String mDefaultDataResource = null;

	// the schema map that will be used to validate docs against the schema for the service
	private SchemaMap mPerformDocumentSchema = null;

	// the schema document to be exposed as service data
	private Document mPerformDocumentSchemaDocument = null;

	public ServiceConfiguration(
		DataResourceImplementation dataResourceImpl,
		ActivityMapType[] activityMaps,
		String factoryHandle)
		throws SystemException, UserException
	{
		mDataResourceImpl = dataResourceImpl;
		mActivityMaps = activityMaps;
		mFactoryHandle = factoryHandle;


		mActivityClassMap = new Hashtable();
		// preload the language maps
		loadActivities();

		constructPerformDocumentSchema();
	}

	protected void loadActivities() throws SystemException
	{

		mLog.debug("Loading " + mActivityMaps.length + " activity maps...");
		for (int i = 0; i < mActivityMaps.length; i++)
		{
			String name = mActivityMaps[i].getName();
			String className = null;
			try
			{
				className = mActivityMaps[i].getImplementation();
				mLog.debug(name + " activity uses " + className);
				Class activity = Class.forName(className);
				mActivityClassMap.put(name, activity);
			}
			catch (NullPointerException e)
			{
				mLog.debug("Could not find class name for " + name + " Activity");
				throw new SystemException("Could not find class name for " + name + " Activity", mLog);
			}
			catch (ClassNotFoundException e)
			{
				mLog.debug("Could not load " + name + " Activity [" + className + "]");
				throw new SystemException("Could not load " + name + " Activity [" + className + "]", mLog);
			}
			catch (Throwable e) {
				mLog.debug("Unexpected exception while retrieving class for " + name + " activity.");
				mLog.debug("Message: " + e.getMessage());
				throw new SystemException("Unexpected exception while retrieving class for " + name + " activity.", mLog);
			}
		}
	}

	/**
	 * Returns a hashtable mapping activity names to activity classes for the given data resource.
	 * @return Map Activity classes indexed by activity names.
	 */
	public Map getActivities() throws UserException
	{
		return (Map) mActivityClassMap;
	}

	/**
	 * The data resource implementation this GDS is associated with.
	 * @return DataResourceImplementation  - The data resource implementation.
	 */
	public DataResourceImplementation getDataResourceImplementation()
	{
		return mDataResourceImpl;
	}

	private void constructPerformDocumentSchema() throws SystemException
	{
		Map availableActivities = new Hashtable();
		for (int i = 0; i < mActivityMaps.length; i++)
		{
			// retrieve the activity name
			if (availableActivities.put(mActivityMaps[i].getName(), mActivityMaps[i].getSchemaFileName()) != null)
			{
				// Message 1043:More than one activity present with name {0}
				mLog.warn( MessageLoader.getMessage(1043, new Object[] {mActivityMaps[i].getName()}) );
			}
				if (mLog.isDebugEnabled())
			{
				mLog.debug("Added activity " + mActivityMaps[i].getName());
			}
		}

		// now that all of the available activities are listed, assemble them into a schema
		Iterator iter = availableActivities.keySet().iterator();
        // get the base schema
        try
        {
            Document baseSchemaDoc;
            if (!iter.hasNext())
            {
                // now get the file
                String base = "http://localhost:8080";
                
                // Message 1044:No activities defined in activity map file. Falling back on default host, {0}
				mLog.warn( MessageLoader.getMessage(1044, new Object[] {base}) );
                URL baseSchema = new URL(base+OGSADAIConstants.GDS_TYPE_SCHEMA);
                Map cache = new Hashtable();
                baseSchemaDoc = getDocumentWithResolvedIncludes(baseSchema.openStream(), baseSchema, cache);                

            }
            else
            {
                String activityName = (String)iter.next();
                String urlString = (String)availableActivities.get(activityName);
                URL url = null;
                try
                {
                    url = new URL(urlString);
                }
                catch (MalformedURLException e)
                {
					// Message 1045:There is a problem with a url provided in the activity map file (urls must be absolute, not relative): {0}
                    throw new SystemException(MessageLoader.getMessage(1045, new Object[] {urlString}), mLog);
                }


                // Work out where the schemata are kept using the absolute URL 
                // of one of the activity schemata as a guide.
                URL baseSchema = this.deduceSchemataRootUrl(url);
    
                
                // now get the file
                Map cache = new Hashtable();
                baseSchemaDoc = getDocumentWithResolvedIncludes(baseSchema.openStream(), baseSchema, cache);
                
                // and add in any bits that arn't referenced directly from that
                do
        		{
                    Document schemaDoc = getDocumentWithResolvedIncludes(url.openStream(), url, cache);

                    // TODO: check whether any of the schema referenced here have been included from the
                    // base doc, the current test will miss files that are the same, but with one having
                    // ./ or ../ within the path
                    
                    Node parent = baseSchemaDoc.getDocumentElement();
                    String path = url.toExternalForm();
                                               
                    // add the important bit at the top of the doc
                    Element includeRoot = schemaDoc.getDocumentElement();
                    NodeList children = includeRoot.getChildNodes();
                    boolean renamedElement = false;
                    for (int j = 0; j < children.getLength(); j++)
                    {
                        Node content = baseSchemaDoc.importNode(children.item(j), true);
                        // check to see if this is an element tag... if it is, then we alter the name
                        if (content.getNodeType() == Node.ELEMENT_NODE && content.getLocalName().equals("element"))
                        {
                            if (renamedElement)
                            {
                                // there is more than one element declaration at the top level of the schema.
                                throw new SystemException("Schema for activity "+activityName+" must not contain more than one top level element declaration", mLog);
                            }
                            ((Element)content).setAttribute("name", activityName);
                            renamedElement = true;
                        }
                        parent.insertBefore(content, parent.getFirstChild());
                    }
                    
                    // check that we actually have the activity linked into the document
                    if (!renamedElement)
                    {
                        throw new SystemException("There is no top level element declaration in schema for activity "+activityName, mLog);
                    }

                    // and add the namespace mappings to the new owner
                    Element docRoot = baseSchemaDoc.getDocumentElement();
                    NamedNodeMap mappings = includeRoot.getAttributes();
                    for (int j = 0; j < mappings.getLength(); j++)
                    {
                        Attr attr = (Attr)mappings.item(j);
                        String namespaceURI = attr.getNamespaceURI();
                        if (namespaceURI == null)
                        {
                            continue;
                        }
                            
                        if (namespaceURI.equals("http://www.w3.org/2000/xmlns/"))
                        {
                            Attr attribute = (Attr)baseSchemaDoc.importNode(attr, false);
                            docRoot.setAttributeNode(attribute);
                        }                                                
                    }
                    
                    if (mLog.isDebugEnabled())
                    {
                        mLog.debug("Added "+path+" to the base schema");
                    }
                    
                    url = null;
                    
                    if( iter.hasNext() )
                    {
                        activityName = (String)iter.next();
                    	url = new URL((String)availableActivities.get(activityName));
                    }
        		}
                while (url != null );
            }

            // now we have a full schema, save it
            mPerformDocumentSchemaDocument = baseSchemaDoc;
            mPerformDocumentSchema = new SchemaMap();
            String uberSchema = XMLUtilities.xmlDOMToString(baseSchemaDoc);
            mLog.debug(uberSchema);
            mPerformDocumentSchema.put(baseSchemaDoc.getDocumentElement().getAttribute("targetNamespace"), uberSchema);
        }
        catch (SystemException e)
        {
//            mLog.debug(e);
            throw e;
        }
        catch (Exception e)
        {
			// Message 1046:Error while building fully resolved schema. Please check that the schema file locations in your activity map file
            throw new SystemException(MessageLoader.getMessage(1046), e, mLog);
        }
	}

    private Document getDocumentWithResolvedIncludes(InputStream location, URL base, Map cache) throws SystemException
    {
        try
        {
            // get the document
            Document schema = XMLUtilities.xmlStreamToDOM(location, false);
            cache.put(base.toExternalForm(), schema);
            
            // locate all the includes in the doc
            NodeList includes = schema.getDocumentElement().getElementsByTagNameNS("*", "include");
            for (int i = 0; i < includes.getLength(); i++)
            {
                Element include = (Element)includes.item(i);
                StringBuffer path = new StringBuffer(include.getAttribute("schemaLocation"));
                
                // make the path fully qualified
                if (path.toString().indexOf("://") == -1)
                {
                    String baseString = base.toExternalForm();
                    baseString = baseString.substring(0, baseString.lastIndexOf('/')+1);
                    
                    // path is relative so strip out any ./'s and ../'s
                    path.insert(0, baseString);

                    while (true)
                    {
                        int index = path.toString().indexOf("../");
                        if (index == -1)
                        {
                            break;
                        }
                                                                       
                        path.replace(index, index + 3, "");
                        // and remove the dir that .. moved us out of
                        int dirStart = path.substring(0, index - 1).lastIndexOf('/');
                        path.replace(dirStart + 1, index, "");
                    }                    

                    while (true)
                    {
                        int index = path.toString().indexOf("./");
                        if (index == -1)
                        {
                            break;
                        }
                        
                        path.replace(index, index + 2, "");
                    }

                }
                
                // now check to see if we need to add it or it is a cyclic reference
                Node parent = include.getParentNode();
                if (!cache.containsKey(path.toString()))
                {
                    if (mLog.isDebugEnabled())
                    {
                        mLog.debug("importing schema at "+path+" into schema at "+base.toExternalForm());
                    }
                    
                    // needs to be added
                    URL url = new URL(path.toString());
                    cache.put(path.toString(), "placeholder".intern());
                    
                    // get the document
                    Document doc = getDocumentWithResolvedIncludes(url.openStream(), url, cache);
                    cache.put(path, doc);
                    
                    // add the important bit into where the include node was
                    Element includeRoot = doc.getDocumentElement();
                    NodeList children = includeRoot.getChildNodes();
                    for (int j = 0; j < children.getLength(); j++)
                    {
                        Node content = schema.importNode(children.item(j), true);                        
                        parent.insertBefore(content, include);
                    }                                       

                    // and add the namespace mappings to the new owner
                    Element docRoot = schema.getDocumentElement();
                    NamedNodeMap mappings = includeRoot.getAttributes();
                    for (int j = 0; j < mappings.getLength(); j++)
                    {
                        Attr attr = (Attr)mappings.item(j);
                        String namespaceURI = attr.getNamespaceURI();
                        if (namespaceURI == null)
                        {
                            continue;
                        }
                            
                        if (namespaceURI.equals("http://www.w3.org/2000/xmlns/"))
                        {
                            Attr attribute = (Attr)schema.importNode(attr, false);
                            docRoot.setAttributeNode(attribute);
                        }                                                
                    }
                }
                parent.removeChild(include);
            }
            
            location.close();
            return schema;
        }
        catch (Exception e)
        {
            try
            {
                location.close();
            }
            catch (Exception e2)
            {}
            throw new SystemException("Error while building fully resolved schema", e, mLog);
        }
    }

	/**
	 * @return SchemaMap that will be used to validate docs against the schema for the service.
	 */
	public SchemaMap getPerformDocumentSchema()
	{
		return mPerformDocumentSchema;
	}

	/**
	 * @return Document representing the perform document schema of this GDS.
	 */
	public Document getPerformDocumentSchemaDocument()
	{
		return mPerformDocumentSchemaDocument;
	}
        
     /**
     * Guesses at the root URL for all schemata in the web-app running OGSA-DAI based on
     * the syntax of a given, sample URL. This method assumes that all the schemata are in the same tree.
     * its also assumes that the root of the tree is at the directory named by the path-fragment
     * set in OGSADAIConstants.SCHEMA_BASE at compile time.  E.g., if SCHEMA_BASE is set to
     * /schema/ogsadai, then the packaging of the web-app must be such that the sample URL
     * includes that string in its path: http:localhost:8080/ROOT/schema/ogsadai/xsd/activities/xxx.xsd
     * or similar.
     * 
     * This is a vile thing to be doing and badly needs to be replaced with proper design.
     *
     * @param sampleUrl the URL on which the deduction is based.
     *
     * @return the deduced URL for the root of the schemata tree.
     *
     * @throws MalformedURLException if it doesn't like the sample URL.
     */
    private URL deduceSchemataRootUrl (URL sampleUrl) throws MalformedURLException {
    
        // The constant SCHEMA_BASE indicates the start of the path leading to the
        // schemata inside the web-app where they are installed.  See the method
        // comments above for assumptions made here.
        String urlString = sampleUrl.toExternalForm();
        int i = urlString.indexOf(OGSADAIConstants.SCHEMA_BASE);
        if (i == -1) {
          throw new MalformedURLException(urlString +
                                          " does not contain the path-fragment " +
                                          OGSADAIConstants.SCHEMA_BASE);
        }
          
        // The URL for the schemata can now be based on the sub-string derived above.  
        String baseUrlString = urlString.substring(0, i) + OGSADAIConstants.GDS_TYPE_SCHEMA;
        URL baseUrl = new URL(baseUrlString);
        if (mLog.isDebugEnabled())
        {
           System.out.println("Using prefix "+ baseUrlString +") from " + urlString +
                      " (as defined in the activity map file) as the base for accessing "+
                      OGSADAIConstants.GDS_TYPE_SCHEMA);
           mLog.debug("Using prefix "+ baseUrlString +") from " + urlString +
                      " (as defined in the activity map file) as the base for accessing "+
                      OGSADAIConstants.GDS_TYPE_SCHEMA);
        }
        return baseUrl;
    }      
}
