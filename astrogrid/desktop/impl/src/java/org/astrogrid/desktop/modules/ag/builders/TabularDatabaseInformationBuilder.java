/*$Id: TabularDatabaseInformationBuilder.java,v 1.3 2005/10/12 13:30:10 nw Exp $
 * Created on 12-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag.builders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.DatabaseBean;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.astrogrid.TabularDatabaseInformation;
import org.astrogrid.desktop.modules.ag.XPathHelper;

import org.apache.xpath.CachedXPathAPI;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.transform.TransformerException;

/** information builder for tablular database registry entries
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Sep-2005
 *
 */
public class TabularDatabaseInformationBuilder implements InformationBuilder {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(TabularDatabaseInformationBuilder.class);

    /** Construct a new TabularDatabaseInformationBuilder
     * 
     */
    public TabularDatabaseInformationBuilder() {
        super();
        try {
            nsNode = XPathHelper.createNamespaceNode();
        } catch (Exception e) {
           logger.error("Could not create static namespace node",e);       
        }                 
    }

public boolean isApplicable(CachedXPathAPI xpath, Element el) {
    try {
        String type = xpath.eval(el, "@xsi:type",nsNode ).str();
        return StringUtils.contains(type,"TabularDB");
    } catch (TransformerException e) {        
        logger.debug("TransformerException",e);
        return false;
    }
}

private    Element nsNode;    

    /**
     * @see org.astrogrid.desktop.modules.ag.builders.InformationBuilder#build(org.apache.xpath.CachedXPathAPI, org.w3c.dom.Element)
     */
    public ResourceInformation build(CachedXPathAPI xpath, Element element) throws ServiceException {
        try {
            URI uri;
        try {
            uri = new URI(xpath.eval(element,"vr:identifier",nsNode).str());
        } catch (URISyntaxException e) {
            
            uri = null;
        }
        String name = xpath.eval(element,"vr:title",nsNode).str();
        String description = xpath.eval(element,"vr:content/vr:description",nsNode).str();
        URL accessURL ;
        try {
            accessURL =  new URL(xpath.eval(element,"vr:interface/vr:accessURL",nsNode).str());
        } catch (MalformedURLException e) {
            accessURL = null;
        }        
        NodeList l = xpath.selectNodeList(element,".//tdb:db",nsNode);
        DatabaseBean[] databases = new DatabaseBean[l.getLength()];
        for (int i = 0; i < databases.length; i++) {
            databases[i] = buildBeanFromDatabaseElement(xpath,((Element)l.item(i)));
        }
        return new TabularDatabaseInformation(uri,name,description,accessURL,databases);
        } catch (TransformerException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @param xpath
     * @param element
     * @return
     * @throws TransformerException
     */
    private DatabaseBean buildBeanFromDatabaseElement(CachedXPathAPI xpath, Element element) throws TransformerException {
        NodeList l = xpath.selectNodeList(element,"tdb:table",nsNode);
       TableBean[] tables = new TableBean[l.getLength()];
       for (int i = 0; i < tables.length; i++) {
           tables[i] = buildBeanFromTableElement(xpath,(Element)l.item(i));
       }
        return new DatabaseBean(
                xpath.eval(element,"tdb:name",nsNode).str()
                ,xpath.eval(element,"tdb:description",nsNode).str()
                ,tables
                );
    }

    /**
     * @param xpath
     * @param element
     * @return
     */
    private TableBean buildBeanFromTableElement(CachedXPathAPI xpath, Element element) throws TransformerException {
        NodeList l = xpath.selectNodeList(element,"vods:column",nsNode);
        ColumnBean[] columns = new ColumnBean[l.getLength()];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = buildBeanFromColumnElement(xpath,(Element)l.item(i));
        }
         return new TableBean(
                 xpath.eval(element,"vods:name",nsNode).str()
                 ,xpath.eval(element,"vods:description",nsNode).str()
                 ,columns
                 );
    }

    /**
     * @param xpath
     * @param element
     * @return
     * @throws TransformerException
     */
    private ColumnBean buildBeanFromColumnElement(CachedXPathAPI xpath, Element element) throws TransformerException {
        return new ColumnBean(
                xpath.eval(element,"vods:name",nsNode).str()
                ,xpath.eval(element,"vods:description",nsNode).str()
                ,xpath.eval(element,"vods:ucd",nsNode).str()
                ,xpath.eval(element,"vods:datatype",nsNode).str()
                ,xpath.eval(element,"vods:unit",nsNode).str()                
                );        
    }

}


/* 
$Log: TabularDatabaseInformationBuilder.java,v $
Revision 1.3  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.1.6.1  2005/10/10 16:24:29  nw
reviewed phils workflow builder
skeletal javahelp

Revision 1.2  2005/10/05 08:48:20  pjn3
String contains (1.5) changed to StringUtils.contains (1.4)

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/