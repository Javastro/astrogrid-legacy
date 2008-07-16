/*$Id: UCDImpl.java,v 1.10 2008/07/16 17:33:57 nw Exp $
 * Created on 16-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.cds;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.UCD;
import org.astrogrid.desktop.modules.system.pref.Preference;

/** Implementaton of the UCD service
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 16-Aug-2005
 *
 */
public class UCDImpl extends BaseCDSClient implements UCD{

    public UCDImpl(HttpClient http,Preference endpoint)  {
        super(http,endpoint);
    } 

    /**
     * @see org.astrogrid.acr.cds.UCD#UCDList()
     */
    public String UCDList() throws ServiceException {
        HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","UCDList")
        });
        return executeHttpMethod(meth);
    }

    /**
     * @see org.astrogrid.acr.cds.UCD#resolveUCD(java.lang.String)
     */
    public String resolveUCD(String arg0) throws ServiceException {
        HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","resolveUCD")
                ,new NameValuePair("ucd",arg0)
        });
        return executeHttpMethod(meth);
    }

    /**
     * @see org.astrogrid.acr.cds.UCD#UCDofCatalog(java.lang.String)
     */
    public String UCDofCatalog(String arg0) throws ServiceException {
        HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","UCDofCatalog")
                ,new NameValuePair("catalog_designation",arg0)
        });
        return executeHttpMethod(meth);     
    }

    /**
     * @see org.astrogrid.acr.cds.UCD#translate(java.lang.String)
     */
    public String translate(String arg0) throws ServiceException {
        HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","translate")
                ,new NameValuePair("ucd",arg0)
        });
        return executeHttpMethod(meth);      
    }

    /**
     * @see org.astrogrid.acr.cds.UCD#upgrade(java.lang.String)
     */
    public String upgrade(String arg0) throws ServiceException {
        HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","upgrade")
                ,new NameValuePair("ucd",arg0)
        });
        return executeHttpMethod(meth);     
    }

    /**
     * @see org.astrogrid.acr.cds.UCD#validate(java.lang.String)
     */
    public String validate(String arg0) throws ServiceException {
        HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","validate")
                ,new NameValuePair("ucd",arg0)
        });
        return executeHttpMethod(meth);       
    }

    /**
     * @see org.astrogrid.acr.cds.UCD#explain(java.lang.String)
     */
    public String explain(String arg0) throws ServiceException {
        HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","explain")
                ,new NameValuePair("ucd",arg0)
        });
        return executeHttpMethod(meth);     
    }

    /**
     * @see org.astrogrid.acr.cds.UCD#assign(java.lang.String)
     */
    public String assign(String arg0) throws ServiceException {
        HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","assign")
                ,new NameValuePair("descr",arg0)
        });
        return executeHttpMethod(meth);       
    }


}


/* 
$Log: UCDImpl.java,v $
Revision 1.10  2008/07/16 17:33:57  nw
Complete - task 372: re-implement CDS tasks

Revision 1.9  2008/04/23 10:54:10  nw
removed implementations of these - as clashses with 1.5 'enum' reserved word.

Revision 1.8  2007/05/10 19:35:27  nw
reqwork

Revision 1.7  2007/04/18 15:47:05  nw
tidied up voexplorer, removed front pane.

Revision 1.6  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.5  2007/01/09 16:20:12  nw
updated to use preference.

Revision 1.4  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.3.60.2  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.3.60.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/31 15:30:46  nw
added glu tags.

Revision 1.1  2005/08/25 16:59:58  nw
1.1-beta-3
 
*/