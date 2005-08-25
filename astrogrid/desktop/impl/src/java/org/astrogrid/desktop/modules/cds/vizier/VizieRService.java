/**
 * VizieRService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.cds.vizier;

public interface VizieRService extends javax.xml.rpc.Service {
    public java.lang.String getVizieRAddress();

    public org.astrogrid.desktop.modules.cds.vizier.VizieR getVizieR() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.desktop.modules.cds.vizier.VizieR getVizieR(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
