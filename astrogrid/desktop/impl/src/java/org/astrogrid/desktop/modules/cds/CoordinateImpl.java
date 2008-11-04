/*$Id: CoordinateImpl.java,v 1.10 2008/11/04 14:35:50 nw Exp $
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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.Coordinate;
import org.astrogrid.desktop.modules.system.pref.Preference;

/** Implementation of the Coordinate client.s
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 16-Aug-2005
 *
 */
public class CoordinateImpl extends BaseCDSClient implements Coordinate {


    /** Construct a new CoordinateImpl
     * @throws MalformedURLException 
     * 
     */
    public CoordinateImpl(final HttpClient http,final Preference endpoint)  {
        super(http,endpoint);
    }

    /**
     * @see org.astrogrid.acr.cds.Coordinate#convert(double, double, double, int)
     */
    public String convert(final double arg0, final double arg1, final double arg2, final int arg3) throws ServiceException {
            final HttpMethod meth = buildHttpMethod();
            meth.setQueryString(new NameValuePair[] {
                    new NameValuePair("method","convert")
                    ,new NameValuePair("x",Double.toString(arg0))
                    ,new NameValuePair("y",Double.toString(arg1))
                    ,new NameValuePair("z",Double.toString(arg2))
                    ,new NameValuePair("precision",Integer.toString(arg3))
            });
            return executeHttpMethod(meth);
    }

    

    public String convertL(final double lon, final double lat, final int precision) throws ServiceException {
        final HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","convert")
                ,new NameValuePair("lon",Double.toString(lon))
                ,new NameValuePair("lat",Double.toString(lat))
                ,new NameValuePair("precision",Integer.toString(precision))
        });
        return executeHttpMethod(meth);
    }

    public String convertE(final int frame1, final int frame2, final double x, final double y, final double z
            , final int precision, final double equinox1, final double equinox2)
            throws ServiceException {
        final HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","convert")
                ,new NameValuePair("frame1",Integer.toString(frame1))
                ,new NameValuePair("frame2",Integer.toString(frame2))                
                ,new NameValuePair("x",Double.toString(x))
                ,new NameValuePair("y",Double.toString(y))
                ,new NameValuePair("z",Double.toString(z))
                ,new NameValuePair("precision",Integer.toString(precision))
                ,new NameValuePair("equinox1",Double.toString(equinox1))
                ,new NameValuePair("equinox2",Double.toString(equinox2))                
        });
        return executeHttpMethod(meth);
    }

    public String convertLE(final int frame1, final int frame2, final double lon, final double lat, 
            final int precision, final double equinox1, final double equinox2)
            throws ServiceException {
        final HttpMethod meth = buildHttpMethod();
        meth.setQueryString(new NameValuePair[] {
                new NameValuePair("method","convert")
                ,new NameValuePair("frame1",Integer.toString(frame1))
                ,new NameValuePair("frame2",Integer.toString(frame2))                
                ,new NameValuePair("lon",Double.toString(lon))
                ,new NameValuePair("lat",Double.toString(lat))
                ,new NameValuePair("precision",Integer.toString(precision))
                ,new NameValuePair("equinox1",Double.toString(equinox1))
                ,new NameValuePair("equinox2",Double.toString(equinox2))                
        });
        return executeHttpMethod(meth);
    }

    

}


/* 
$Log: CoordinateImpl.java,v $
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