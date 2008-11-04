/*$Id: DalProtocol.java,v 1.21 2008/11/04 14:35:48 nw Exp $
 * Created on 27-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.Image;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.ParamHttpInterface;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal.ResourceConsumer;

import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * Abstract description of an entire data access protocol - naming, listing suitable services, querying them, and adding results.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jan-2006
 * @TEST
 */
public abstract class DalProtocol {

    public DalProtocol(final String name, final Image img, final RegistryInternal reg) {
        super();
        this.name = name;
        this.reg = reg;
        this.primaryNode = new ImageTreeNode();
        primaryNode.setAttribute(AbstractRetriever.LABEL_ATTRIBUTE,name);
        primaryNode.setImage(img);
        this.checkBox = new JCheckBox(name);
        this.checkBox.putClientProperty(OWNER,this);
        this.menuCheckBox = new JCheckBoxMenuItem(name);
        this.menuCheckBox.setModel(this.checkBox.getModel());
        this.menuCheckBox.putClientProperty(OWNER,this);
        // setting shared between two models.
        this.checkBox.setSelected(true);
    }
    public static final Class OWNER = DalProtocol.class;
    private final String name;
    private final ImageTreeNode primaryNode;
    private final JCheckBox checkBox;
    private final JCheckBoxMenuItem menuCheckBox;
    protected final RegistryInternal reg;
    
    public String getName() {
        return name;
    }
    
    
    /** access the primary node - from where all other results from this protocol
     * will be rooted
     */
    public final ImageTreeNode getPrimaryNode() {
        return primaryNode;
    }
    
    /** get a UI component used for selecting / deselecting this protocol */
    public final JCheckBox getCheckBox() {
        return checkBox;
    }
    /** get a menu item for selecting / deselecting this protocol */
    public final JCheckBoxMenuItem getMenuItemCheckBox() {
        return menuCheckBox;
    }
    
    // back-to-front setter injection - neccessary, as there's a
    // circular dependency between vizModel and dalProtocol
    
    private VizModel vizModel;
    /** to be only called by VizModel */
    public void setVizModel(final VizModel vm) {
        this.vizModel = vm;
    }
    
    protected VizModel getVizModel() {
        return vizModel;
    }
    
    /** return an xquery that will produce a list of all known services of this protocol
     * when passed to the registry.
      */
    public abstract String getXQuery();
    
    
    public final void processAllServices(final ResourceConsumer p) throws ServiceException {
        reg.consumeXQuery(getXQuery(),p);
    }
    
	/** produce a list of all services suitable to this protocol
	 * @param resourceList input list to filter
	 * @param p a processor that is passed each suitable service
	 *  this method obeys the calling contract for resourceConsumer
	 */
	public final void processSuitableServicesInList(final List<? extends Resource> resourceList,final ResourceConsumer p) {
        p.estimatedSize(resourceList.size());
        for (final Iterator<? extends Resource> i = resourceList.iterator(); i.hasNext();) {
            final Resource r =i.next();
            if (isSuitable(r)) { 
                p.process(r);
            }
        }	    
	}
	
	/** abstract test to be implemented by subclasses, used within <tt>processSuitableServicesInLIst()</tt>*/
	protected abstract boolean isSuitable(Resource r) ;

	
    /** returns a NodeSocket which will join nodes directly to the primary 
     *  node for this protocol.
     */
    public NodeSocket getDirectNodeSocket() {
        if (directNodeSocket == null) {
            directNodeSocket = new NodeSocket() {
                public void addNode(final TreeNode child) {
                    final DefaultEdge edge = new DefaultEdge(primaryNode, child);
                    edge.setAttribute(AbstractRetriever.WEIGHT_ATTRIBUTE, "2");
                    vizModel.getTree().addChild(edge);
                }
                public boolean isService() {
                    return false;
                }
            };
        }
        return directNodeSocket;
    }
    private NodeSocket directNodeSocket;

    /**
     * Returns a new NodeSocket which will join nodes to the primary node
     * for this protocol indirectly via a node corresponding to a given Service.
     * The service node is only inserted into the tree when the addNode method
     * of the returned socket is called for the first time.
     */
    public NodeSocket createIndirectNodeSocket(final Service service) {
        return new NodeSocket() {
            private TreeNode serviceNode;
            private String baseLabel;
            private int nChild;
            private synchronized TreeNode getServiceNode() {
                if (serviceNode == null) {
                    serviceNode = new DefaultTreeNode();
                    baseLabel = service.getShortName();
                    if (baseLabel == null || baseLabel.trim().length() == 0) {
                        baseLabel = service.getTitle();
                    }
                    serviceNode.setAttribute(AbstractRetriever.LABEL_ATTRIBUTE, baseLabel);
                    serviceNode.setAttribute(AbstractRetriever.SERVICE_ID_ATTRIBUTE, service.getId().toString());
                    final StringBuffer tbuf = new StringBuffer()
                        .append("<html>")
                        .append(service.getTitle())
                        .append("<br>ID: ")
                        .append(service.getId());
                    serviceNode.setAttribute(AbstractRetriever.TOOLTIP_ATTRIBUTE, tbuf.toString());
                    getDirectNodeSocket().addNode(serviceNode);
                }
                return serviceNode;
            }
            public void addNode(final TreeNode child) {
                nChild++;
                final TreeNode servNode = getServiceNode();
                servNode.setAttribute(AbstractRetriever.LABEL_ATTRIBUTE, baseLabel + " - " + nChild + ((nChild == 1) ? " search" : " searches"));
                final DefaultEdge edge = new DefaultEdge(servNode, child);
                edge.setAttribute(AbstractRetriever.WEIGHT_ATTRIBUTE, "2");
                vizModel.getTree().addChild(edge);
            }
            public boolean isService() {
                return true;
            }
        };
    }

    /** return a ParamHTTP-type base url for a given capability
     * @param  cap  capability
     * @return   first likely-looking base url, or null if there is none
     */
    public static URI findParamUrl(final Capability cap) {
        final Interface[] ifs = cap.getInterfaces();
        for (int i = 0; i < ifs.length; i++) {
            if (ifs[i] instanceof ParamHttpInterface) {
                final AccessURL[] urls = ((ParamHttpInterface) ifs[i]).getAccessUrls();
                for (int j = 0; j < urls.length; j++) {
                    final URI uri = urls[i].getValueURI();
                    if (uri != null) {
                        return uri;
                    }
                }
            }
        }
        return null;
    }

    /** Sets up subnames for a list of retrievers.  These are more-or-less
     *  human-readable diambiguation strings which distinguish between
     *  different capabilities provided by the same service.
     * @param capabilities  all capabilities provided by a service
     * @param retrievers  retrievers associated with a subset of capabilities
     */
    public static void setSubNames(final Capability[] capabilities, final Retriever[] retrievers) {
        if (capabilities.length > 1) {
            if (retrievers.length == 1) {
                retrievers[0].setSubName(retrievers[0].getServiceType());
            }
            else {
                for (int i = 0; i < retrievers.length; i++) {
                    String subName = capabilities[i].getDescription();
                    if (subName == null || subName.trim().length() == 0) {
                        subName = retrievers[i].getServiceType() + (i+1);
                    }
                    retrievers[i].setSubName(subName);
                }
            }
        }
    }
}


/* 
$Log: DalProtocol.java,v $
Revision 1.21  2008/11/04 14:35:48  nw
javadoc polishing

Revision 1.20  2008/05/28 12:27:49  nw
Complete - task 408: Adjust count reporting in astroscope and voexplorer.

Revision 1.19  2008/05/09 11:33:04  nw
Complete - task 394: process reg query results in a stream.

Incomplete - task 391: get to grips with new CDS

Complete - task 393: add waveband column.

Revision 1.18  2008/04/25 08:59:36  nw
extracted interface from retriever, to ease unit testing.

Revision 1.17  2008/04/23 11:17:53  nw
marked as needing test.

Revision 1.16  2008/03/18 14:38:19  mbt
Fallback for missing description

Revision 1.14  2008/02/25 13:27:48  mbt
Improve node labelling

Revision 1.13  2008/02/22 17:03:35  mbt
Merge from branch mbt-desktop-2562.
Basically, Retrievers rather than Services are now the objects (associated
with TreeNodes) which communicate with external servers to acquire results.
Since Registry v1.0 there may be multiple Retrievers (even of a given type)
per Service.

Revision 1.12.18.3  2008/02/22 15:12:19  mbt
Add utility NodeSocket dispatch methods

Revision 1.12.18.2  2008/02/21 15:35:15  mbt
Now does multiple-capability-per-service for all known protocols

Revision 1.12.18.1  2008/02/21 11:06:09  mbt
First bash at 2562.  AstroScope now runs multiple cone searches per Service

Revision 1.12  2007/12/12 13:54:12  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.11  2007/06/18 16:42:36  nw
javadoc fixes.

Revision 1.10  2007/05/03 19:20:42  nw
removed helioscope.merged into uberscope.

Revision 1.9  2007/03/08 17:43:56  nw
first draft of voexplorer

Revision 1.8  2007/01/29 10:43:49  nw
documentation fixes.

Revision 1.7  2006/08/15 10:01:12  nw
migrated from old to new registry models.

Revision 1.6  2006/05/26 15:11:58  nw
tidied imported.corrected number formatting.

Revision 1.5  2006/05/17 15:45:17  nw
factored common base class out of astroscope and helioscope.improved error-handline on astroscope input.

Revision 1.4  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.3  2006/03/24 10:30:15  KevinBenson
new checkboxes on heliosope for the Format, and the ability to query by Format
for stap services on helioscope

Revision 1.2  2006/03/13 14:55:09  KevinBenson
New first draft of helioscope and the stap spec protocol

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/
