/*$Id: TabularDatabaseInformationBuilder.java,v 1.7 2006/04/18 23:25:47 nw Exp $
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

import javax.xml.transform.TransformerException;

import org.apache.xpath.CachedXPathAPI;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.DatabaseBean;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.astrogrid.TabularDatabaseInformation;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/** information builder for tablular database registry entries
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Sep-2005
 *
 */
public class TabularDatabaseInformationBuilder extends ResourceInformationBuilder {
  
 

public boolean isApplicable(CachedXPathAPI xpath, Element el) {
    try {
        return xpath.eval(el, "contains(@xsi:type,'TabularDB')",nsNode ).bool();
        //return StringUtils.contains(type,"TabularDB");
    } catch (TransformerException e) {        
        logger.debug("TransformerException",e);
        return false;
    }
}
  

    /**
     * @see org.astrogrid.desktop.modules.ag.builders.InformationBuilder#build(org.apache.xpath.CachedXPathAPI, org.w3c.dom.Element)
     */
    public ResourceInformation build(CachedXPathAPI xpath, Element element) throws ServiceException {
        try {
        NodeList l = xpath.selectNodeList(element,".//tdb:db",nsNode);
        DatabaseBean[] databases = new DatabaseBean[l.getLength()];
        for (int i = 0; i < databases.length; i++) {
            databases[i] = buildBeanFromDatabaseElement(xpath,((Element)l.item(i)));
        }
        return new TabularDatabaseInformation(
                findId(xpath,element)
                ,findName(xpath,element)
                ,findDescription(xpath,element),
                findAccessURL(xpath,element)
                ,findLogo(xpath,element)
                ,databases);
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
                xpath.eval(element,"normalize-space(tdb:name)",nsNode).str()
                ,xpath.eval(element,"normalize-space(tdb:description)",nsNode).str()
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
                 xpath.eval(element,"normalize-space(vods:name)",nsNode).str()
                 ,xpath.eval(element,"normalize-space(vods:description)",nsNode).str()
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
                xpath.eval(element,"normalize-space(vods:name)",nsNode).str()
                ,xpath.eval(element,"normalize-space(vods:description)",nsNode).str()
                ,xpath.eval(element,"normalize-space(vods:ucd)",nsNode).str()
                ,xpath.eval(element,"normalize-space(vods:datatype)",nsNode).str()
                ,xpath.eval(element,"normalize-space(vods:unit)",nsNode).str()                
                );        
    }

}


/* 
$Log: TabularDatabaseInformationBuilder.java,v $
Revision 1.7  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.6.26.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.6  2005/12/02 13:40:32  nw
optimized, and made more error-tolerant

Revision 1.5  2005/11/04 10:14:26  nw
added 'logo' attribute to registry beans.
added to astroscope so that logo is displayed if present

Revision 1.4  2005/10/18 16:53:34  nw
refactored common functionality.
added builders for siap and cone.

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