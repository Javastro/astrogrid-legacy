/*$Id: Adql074Impl.java,v 1.1 2005/09/12 15:21:16 nw Exp $
 * Created on 09-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ivoa;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.Adql074;
import org.astrogrid.desktop.modules.ivoa.adql.ADQLTranslatorLocator;
import org.astrogrid.desktop.modules.ivoa.adql.ADQLTranslatorSoap;
import org.astrogrid.query.sql.Sql2Adql;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;



/** Implementation of the adql interface.
 * 
 * <p>
 * uses martin's libraries to translate. internally, could call these classes directly, but going via this interface allows us to 
 * substitute a different implementaton later (or call the nvo web service even)
 * @todo can't work out how to translate from xadql back to sql using martin's lib - will do it via web service for now..
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Sep-2005
 *
 */
public class Adql074Impl implements Adql074{

    /** Construct a new AdqlImpl
     * 
     */
    public Adql074Impl() {
        super();
    }

    /**
     * @see org.astrogrid.acr.ivoa.Adql#s2x(java.lang.String)
     */
    public Document s2x(String arg0) throws ServiceException{
        try {
        String adqlString = Sql2Adql.translateToAdql074(arg0);
        InputStream is = new ByteArrayInputStream(adqlString.getBytes());
        return XMLUtils.newDocument(is);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }
    
    private ADQLTranslatorSoap service = null;
    private ADQLTranslatorSoap getTranslatorService() throws javax.xml.rpc.ServiceException {
        if (service == null) {
            ADQLTranslatorLocator loc = new ADQLTranslatorLocator();
            service = loc.getADQLTranslatorSoap();
        }
        return service;
    }

    
    /**
     * @throws ServiceException
     * @see org.astrogrid.acr.ivoa.Adql#x2s(org.w3c.dom.Document)
     */
    public String x2s(Document arg0) throws ServiceException {
        try {
            String adqlString = XMLUtils.DocumentToString(arg0);
            return getTranslatorService().ADQLStringtoSQL(adqlString);
        } catch (Throwable e) {
            throw new ServiceException(e);
        }
    }

}


/* 
$Log: Adql074Impl.java,v $
Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/