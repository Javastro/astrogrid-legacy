/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Service;

/** Interface to a retriever.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 25, 20088:13:17 AM
 */
public interface Retriever {

    /** return a string describing what kind of service this is */
    public String getServiceType();

    /** set a disambiguation string - distinguishes different retrievers 
     *  within the same service.  May be the empty string if only one retriever
     *  per parent service.
     */
    public void setSubName(String subName);

    /** return a disambiguation string - distinguishes different retrievers 
     *  within the same service.  May be the empty string if only one retriever
     *  per parent service.
     */
    public String getSubName();

    public Service getService();

    public Capability getCapability();

    /** returns terse string describing this retriever
     */
    public String getLabel();

}