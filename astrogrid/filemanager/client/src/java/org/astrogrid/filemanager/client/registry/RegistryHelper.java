/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/registry/Attic/RegistryHelper.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:06 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: RegistryHelper.java,v $
 *   Revision 1.3  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.2.4.3  2005/02/18 15:50:14  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.2.4.2  2005/02/11 14:28:28  nw
 *   refactored, split out candidate classes.
 *
 *   Revision 1.2.4.1  2005/02/10 16:23:14  nw
 *   formatted code
 *
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

import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.store.Ivorn;
import org.astrogrid.util.DomHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.URL;

/**
 * A helper toolkit to handle getting service information from the registry.
 *  
 */
public class RegistryHelper {
    /**
     * Our debug logger.
     *  
     */
    private static Log log = LogFactory.getLog(RegistryHelper.class);

    /**
     * Our registry query service.
     *  
     */
    private RegistryService registry;

    /**
     * Helper method to create a registry delegate.
     * 
     * @param endpoint
     *            The registry endpoint URL.
     * @return A new RegistryService connected to the endpoint.
     *  
     */
    private static RegistryService registry(URL endpoint) {
        RegistryDelegateFactory factory = new RegistryDelegateFactory();
        return factory.createQuery(endpoint);
    }

    /**
     * Public constructor, using a specific registry endpoint.
     * 
     * @param endpoint
     *            The registry endpoint URL.
     *  
     */
    public RegistryHelper(URL endpoint) {
        this(registry(endpoint));
    }

    /**
     * Public constructor, using a specific registry service.
     * 
     * @param registry
     *            The registry service.
     *  
     */
    public RegistryHelper(RegistryService registry) {
        this.registry = registry;
    }

    /**
     * Get the registry information for a service Ivorn.
     * 
     * @todo Eliminate the duplicate call to the registry.
     *  
     */
    public ServiceInformation service(Ivorn ivorn) throws RegistryException {
        log.debug("RegistryHelper.service(Ivorn)");
        log.debug("  Ivorn : " + ivorn.toString());
        //
        // Create a new ServiceInformation.
        ServiceInformation info = new ServiceInformation(ivorn);
        //
        // Try to resolve the service endpoint.
        info.setEndpoint(registry.getEndPointByIdentifier(ivorn.getPath()));
        //
        // Try to resolve the service resource.
        Document doc = registry.getResourceByIdentifier(ivorn.getPath());
        //
        // Get a list of related resource identifiers.
        NodeList nodes = getNodeList(getNodeList(getNodeList(doc,
                "RelatedResource", "vr"), "RelatedTo", "vr"), "Identifier",
                "vr");
        //
        // Build the list of service kind entries.
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            info.addProvides(new ServiceKindEntry(getNodeText(node, "AuthorityID",
                    "vr"), getNodeText(node, "ResourceKey", "vr")));
        }
        return info;
    }

    /**
     * Get a list of matching descendant nodes.
     *  
     */
    public NodeList getNodeList(Node node, String name, String prefix)
            throws RegistryException {
        try {
            return DomHelper.getNodeListTags(node, name, prefix);
        } catch (IOException ouch) {
            throw new RegistryException("Unable to process node list");
        }
    }

    /*
     * Get a list of matching descendant nodes.
     *  
     */
    public NodeList getNodeList(NodeList list, String name, String prefix)
            throws RegistryException {
        try {
            CompoundNodeList result = new CompoundNodeList();
            for (int i = 0; i < list.getLength(); i++) {
                result.add(DomHelper
                        .getNodeListTags(list.item(i), name, prefix));
            }
            return result;
        } catch (IOException ouch) {
            throw new RegistryException("Unable to process node list");
        }
    }

    /**
     * Get the text content of a node.
     *  
     */
    public String getNodeText(Node node, String name, String prefix)
            throws RegistryException {
        try {
            return DomHelper.getNodeTextValue(node, name, prefix);
        } catch (IOException ouch) {
            throw new RegistryException("Unable to process node list");
        }
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[RegistryHelper:");
        buffer.append(" registry: ");
        buffer.append(registry);
        buffer.append("]");
        return buffer.toString();
    }
}

