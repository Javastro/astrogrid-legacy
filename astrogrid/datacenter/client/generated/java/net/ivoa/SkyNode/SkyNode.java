/**
 * SkyNode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.SkyNode;

public interface SkyNode extends javax.xml.rpc.Service {

    // Interface for basic and full SkyNodes.
    public java.lang.String getSkyNodeSoapAddress();

    public net.ivoa.SkyNode.SkyNodeSoap getSkyNodeSoap() throws javax.xml.rpc.ServiceException;

    public net.ivoa.SkyNode.SkyNodeSoap getSkyNodeSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
