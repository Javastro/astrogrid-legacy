/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/registry/Attic/RegistryHelper.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:44:03 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: RegistryHelper.java,v $
 *   Revision 1.2  2005/01/28 10:44:03  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.3  2005/01/23 05:39:44  dave
 *   Added initial implementation of FileManagerClient ...
 *
 *   Revision 1.1.2.2  2005/01/21 06:00:02  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.1.2.1  2005/01/21 05:57:32  dave
 *   Added registry helper to get service kind entries ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client.registry;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

import java.net.URL;

import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import java.io.IOException ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.util.DomHelper;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.query.RegistryService ;
import org.astrogrid.registry.client.RegistryDelegateFactory ;

/**
 * A helper toolkit to handle getting service information from the registry.
 *
 */
public class RegistryHelper
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(RegistryHelper.class);

    /**
     * Our registry query service.
     *
     */
    private RegistryService registry ;

    /**
     * Helper method to create a registry delegate.
     * @param endpoint The registry endpoint URL.
     * @return A new RegistryService connected to the endpoint.
     *
     */
    private static RegistryService registry(URL endpoint)
        {
        RegistryDelegateFactory factory = new RegistryDelegateFactory();
        return factory.createQuery(
            endpoint
            );
        }

    /**
     * Public constructor, using a specific registry endpoint.
     * @param endpoint The registry endpoint URL.
     *
     */
    public RegistryHelper(URL endpoint)
        {
        this(
            registry(
                endpoint
                )
            );
        }

    /**
     * Public constructor, using a specific registry service.
     * @param registry The registry service.
     *
     */
    public RegistryHelper(RegistryService registry)
        {
        this.registry = registry ;
        }

    /**
     * Get the registry information for a service Ivorn.
     * @todo Eliminate the duplicate call to the registry.
     *
     */
    public ServiceInformation service(Ivorn ivorn)
        throws RegistryException
        {
        log.debug("RegistryHelper.service(Ivorn)");
        log.debug("  Ivorn : " + ivorn.toString());
        //
        // Create a new ServiceInformation.
        ServiceInformation info =
            new ServiceInformation(
                ivorn
                ) ;
        //
        // Try to resolve the service endpoint.
        info.endpoint(
            registry.getEndPointByIdentifier(
                ivorn.getPath()
                )
            );
        //
        // Try to resolve the service resource.
        Document doc = registry.getResourceByIdentifier(
            ivorn.getPath()
            );
        //
        // Get a list of related resource identifiers.
        NodeList nodes = 
            getNodeList(
                getNodeList(
                    getNodeList(
                        doc,
                        "RelatedResource",
                        "vr"
                        ),
                    "RelatedTo",
                    "vr"
                    ),
                "Identifier",
                "vr"
                );
        //
        // Build the list of service kind entries.
        for (int i = 0 ; i < nodes.getLength() ; i++)
            {
            Node node = nodes.item(i) ;
            info.provides(
                new ServiceKindEntry(
                    getNodeText(
                        node,
                        "AuthorityID",
                        "vr"
                        ),
                    getNodeText(
                        node,
                        "ResourceKey",
                        "vr"
                        )
                    )
                );
            }
        return info ;
        }

    /**
     * Get a list of matching descendant nodes.
     *
     */
    public NodeList getNodeList(Node node, String name, String prefix)
        throws RegistryException
        {
        try {
            return DomHelper.getNodeListTags(node, name, prefix);
            }
        catch(IOException ouch)
            {
            throw new RegistryException(
                "Unable to process node list"
                );
            }
        }

    /*
     * Get a list of matching descendant nodes.
     *
     */
    public NodeList getNodeList(NodeList list, String name, String prefix)
        throws RegistryException
        {
        try {
            CompoundNodeList result = new CompoundNodeList();
            for (int i = 0 ; i < list.getLength() ; i++)
                {
                result.add(
                    DomHelper.getNodeListTags(
                        list.item(i),
                        name,
                        prefix
                        )
                    );
                }
            return result ;
            }
        catch(IOException ouch)
            {
            throw new RegistryException(
                "Unable to process node list"
                );
            }
        }

    /**
     * Get the text content of a node.
     *
     */
    public String getNodeText(Node node, String name, String prefix)
        throws RegistryException
        {
        try {
            return DomHelper.getNodeTextValue(
                node,
                name,
                prefix
                );
            }
        catch(IOException ouch)
            {
            throw new RegistryException(
                "Unable to process node list"
                );
            }
        }

    /**
     * A component to contain information about a service.
     *
     */
    public class ServiceInformation
        {
        /**
         * Our internal HashMap of entries.
         *
         */
        public Map map = new HashMap();

        /**
         * Public constructor.
         *
         */
        public ServiceInformation(Ivorn ivorn)
            {
            log.debug("ServiceInformation(Ivorn)");
            log.debug("  Ivorn : " + ivorn);
            this.ivorn = ivorn ;
            }

        /**
         * Our service Ivorn.
         *
         */
        private Ivorn ivorn ;

        /**
         * Access to the service Ivorn.
         *
         */
        public Ivorn ivorn()
            {
            return this.ivorn ;
            }

        /**
         * The service endpoint.
         *
         */
        private String endpoint ;

        /**
         * Set the service endpoint.
         *
         */
        protected void endpoint(String endpoint)
            {
            log.debug("ServiceInformation.endpoint(String)");
            log.debug("  Endpoint : " + endpoint);
            this.endpoint = endpoint ;
            }

        /**
         * Access to the service endpoint.
         *
         */
        public String endpoint()
            {
            return this.endpoint ;
            }

        /**
         * Add an entry to our service type list.
         *
         */
        protected void provides(ServiceKindEntry entry)
            {
            log.debug("ServiceInformation.provides(ServiceKindEntry)");
            log.debug("  Auth  : " + entry.getAuthority());
            log.debug("  Ident : " + entry.getResource());
            map.put(
                key(entry),
                entry
                );
            }

        /**
         * Check if the service implements a specific service type.
         *
         */
        public boolean provides(String authority, String resource)
            {
            return map.containsKey(
                key(
                    authority,
                    resource
                    )
                );
            }

        /**
         * Create a map key for an entry.
         *
         */
        protected String key(ServiceKindEntry entry)
            {
            return key(
                entry.getAuthority(),
                entry.getResource()
                );
            }

        /**
         * Create a map key for an entry.
         *
         */
        protected String key(String authority, String resource)
            {
            StringBuffer buffer = new StringBuffer();
            buffer.append(authority);
            buffer.append("::");
            buffer.append(resource);
            return buffer.toString();
            }
        }

    /**
     * A component to represent a service kind entry.
     *
     */
    public class ServiceKindEntry
        {
        /**
         * The authority identifier.
         *
         */
        private String authority ;

        /**
         * The resource identifier.
         *
         */
        private String resource ;

        /**
         * Public constructor.
         *
         */
        public ServiceKindEntry(String authority, String resource)
            {
            log.debug("ServiceKindEntry()");
            log.debug("  Auth  : " + authority);
            log.debug("  Ident : " + resource);
            this.authority = authority ;
            this.resource  = resource  ;
            }

        /**
         * Acces to the authority identifier.
         *
         */
        public String getAuthority()
            {
            return this.authority ;
            }

        /**
         * Acces to the resource identifier.
         *
         */
        public String getResource()
            {
            return this.resource ;
            }
        }

    /**
     * A component to aggregate the contents of more than one NodeList.
     *
     */
    public class CompoundNodeList
        implements NodeList
        {
        /**
         * Our internal vector of Nodes.
         *
         */
        private Vector vector = new Vector();

        /**
         * Public constructor.
         *
         */
        public CompoundNodeList()
            {
            }

        /**
         * Add the contents of a NodeList.
         *
         */
        public void add(NodeList list)
            {
            for (int i = 0 ; i < list.getLength() ; i++)
                {
                Node node = list.item(i);
                vector.add(
                    node
                    );
                }
            }

        /**
         * Get the total numner of nodes.
         *
         */
        public int getLength()
            {
            return vector.size() ;
            }

        /**
         * Get a specific node.
         *
         */
        public Node item(int index)
            {
            return (Node) vector.get(index);
            }
        }
    }


