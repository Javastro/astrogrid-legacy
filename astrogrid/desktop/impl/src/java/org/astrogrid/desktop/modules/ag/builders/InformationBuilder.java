/*$Id: InformationBuilder.java,v 1.3 2006/08/02 13:29:19 nw Exp $
 * Created on 07-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.ag.builders;

import org.apache.xpath.CachedXPathAPI;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.w3c.dom.Element;

/** interface to something that will build some kind of information bean from a reg entry
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Sep-2005
 * * @deprecated part of the obsolete registry infrastructure
 */
public interface InformationBuilder {
    /**
     * test whether this builder is suitable for a particular kind of reg entry
     * @param xpath xpath engine to use - as it'll probably involve some kind of xpath test
     * @param el the Resource Element of the registry entry to test for.
     * @return true if this builder is applicabe for the parameter registry entry
     */
    boolean isApplicable(CachedXPathAPI xpath, Element el);

    /**
     * build an information bean from a registry entry
     * @param xpath xpath engine to use.
     * @param element the resource element of the registry entry to build a bean from
     * @return a resource information bean, or some subclass.. never null.
     * @throws ServiceException if the builder fails to build a bean.
     */
    ResourceInformation build(CachedXPathAPI xpath, Element element) throws ServiceException;
}

/* 
 $Log: InformationBuilder.java,v $
 Revision 1.3  2006/08/02 13:29:19  nw
 marked all as obsolete.

 Revision 1.2  2006/04/18 23:25:47  nw
 merged asr development.

 Revision 1.1.56.1  2006/04/14 02:45:03  nw
 finished code.extruded plastic hub.

 Revision 1.1  2005/09/12 15:21:16  nw
 reworked application launcher. starting on workflow builder
 
 */