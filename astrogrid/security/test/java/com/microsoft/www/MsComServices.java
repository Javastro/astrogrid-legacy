/**
 * MsComServices.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.microsoft.www;

public interface MsComServices extends javax.xml.rpc.Service {

    // <Table><TR><TD><IMG src='ws.gif' /></TD><TD><B>Microsoft.Com Platform
    // WebServices </B> provide opportunities to syndicate Microsoft Content,
    // Products Catalog, Downloads  and Communities.</TD></TR></Table>
    public java.lang.String getMsComServicesSoapAddress();

    public com.microsoft.www.MsComServicesSoap getMsComServicesSoap() throws javax.xml.rpc.ServiceException;

    public com.microsoft.www.MsComServicesSoap getMsComServicesSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
