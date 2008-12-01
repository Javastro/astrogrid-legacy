/*$Id: VizieRImpl.java,v 1.11 2008/12/01 23:31:16 nw Exp $
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

import java.net.MalformedURLException;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.io.IOUtils;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.VizieR;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.w3c.dom.Document;

/** Implementation of the vizier client.
 * 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 16-Aug-2005
 *
 */
public class VizieRImpl extends BaseCDSClient implements VizieR {

    public VizieRImpl(final HttpClient http,final Preference endpoint) throws javax.xml.rpc.ServiceException, MalformedURLException {        
        super(http,endpoint);

    }

    /**
     * @see org.astrogrid.acr.cds.VizieR#cataloguesMetaData(java.lang.String, double, java.lang.String, java.lang.String)
     */
    public Document cataloguesMetaData(final String target, final double radius, final String unit, final String text)
            throws ServiceException {
        final HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","cataloguesMetaData")
                ,new NameValuePair("target",target)
                ,new NameValuePair("radius",Double.toString(radius))
                ,new NameValuePair("unit",unit)
                ,new NameValuePair("text",text)
        });
        
        final String r=  executeHttpMethod(meth);
        try {
            return XMLUtils.newDocument(IOUtils.toInputStream(r));
        } catch (final Exception e) {
            throw new ServiceException(e);
        }      
    }

    public Document cataloguesMetaDataWavelength(final String target, final double radius, final String unit, final String text, final String wavelength)
            throws ServiceException {
        final HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","cataloguesMetaData")
                ,new NameValuePair("target",target)
                ,new NameValuePair("radius",Double.toString(radius))
                ,new NameValuePair("unit",unit)
                ,new NameValuePair("text",text)
                ,new NameValuePair("wavelength",wavelength)                
        });
        
        final String r=  executeHttpMethod(meth);
        try {
            return XMLUtils.newDocument(IOUtils.toInputStream(r));
        } catch (final Exception e) {
            throw new ServiceException(e);
        }      
    }

    /**
     * @see org.astrogrid.acr.cds.VizieR#cataloguesData(java.lang.String, double, java.lang.String, java.lang.String)
     */
    public Document cataloguesData(final String target, final double radius, final String unit, final String text)
            throws ServiceException {
        final HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","cataloguesData")
                ,new NameValuePair("target",target)
                ,new NameValuePair("radius",Double.toString(radius))
                ,new NameValuePair("unit",unit)
                ,new NameValuePair("text",text)
        });
        
        final String r=  executeHttpMethod(meth);
        try {
            return XMLUtils.newDocument(IOUtils.toInputStream(r));
        } catch (final Exception e) {
            throw new ServiceException(e);
        }      
    }

    
    public Document cataloguesDataWavelength(final String target, final double radius, final String unit, final String text, final String wavelength)
            throws ServiceException {
        final HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","cataloguesData")
                ,new NameValuePair("target",target)
                ,new NameValuePair("radius",Double.toString(radius))
                ,new NameValuePair("unit",unit)
                ,new NameValuePair("text",text)
                ,new NameValuePair("wavelength",wavelength)                 
        });
        
        final String r=  executeHttpMethod(meth);
        try {
            return XMLUtils.newDocument(IOUtils.toInputStream(r));
        } catch (final Exception e) {
            throw new ServiceException(e);
        }      
    }

    /**
     * @see org.astrogrid.acr.cds.VizieR#metaAll()
     */
    public Document metaAll() throws ServiceException {
        final HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","metaAll")
        });
        
        final String r=  executeHttpMethod(meth);
        try {
            return XMLUtils.newDocument(IOUtils.toInputStream(r));
        } catch (final Exception e) {
            throw new ServiceException(e);
        }            
    }


}


/* 
$Log: VizieRImpl.java,v $
Revision 1.11  2008/12/01 23:31:16  nw
used commons.io utilities

Revision 1.10  2008/11/04 14:35:50  nw
javadoc polishing

Revision 1.9  2008/07/16 17:33:57  nw
Complete - task 372: re-implement CDS tasks

Revision 1.8  2008/04/23 10:54:09  nw
removed implementations of these - as clashses with 1.5 'enum' reserved word.

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