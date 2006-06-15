/*$Id: ConeImpl.java,v 1.4 2006/06/15 09:49:21 nw Exp $
 * Created on 17-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.nvo;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.nvo.Cone;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.ivoa.DALImpl;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Oct-2005
 *
 */
public class ConeImpl extends DALImpl implements Cone {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ConeImpl.class);

    /** Construct a new ConeImpl
     * 
     */
    public ConeImpl(Registry reg, MyspaceInternal ms) {
        super(reg,ms);
    }

    /**
     * @see org.astrogrid.acr.nvo.Cone#constructQuery(java.net.URI, double, double, double)
     */
    public URL constructQuery(URI arg0, double arg1, double arg2, double arg3)
            throws InvalidArgumentException, NotFoundException {
        URL endpoint = resolveEndpoint(arg0);
        endpoint = addOption(
        					addOption( 
        							addOption(endpoint,"RA",Double.toString(arg1))
        						,"DEC",Double.toString(arg2))
        					,"SR",Double.toString(arg3));
        return endpoint;
      
    }


    /* 
     * @see org.astrogrid.acr.nvo.Cone#getRegistryQueryToListCone()
     */
    public String getRegistryQuery() {
        return "Select * from Registry where ( " +
        " @xsi:type like '%ConeSearch'  " +
        " or ( @xsi:type like '%TabularSkyService' " + 
		" and vr:identifier like 'ivo://CDS/%' " + 
		" and vs:table/vs:column/vs:ucd = 'POS_EQ_RA_MAIN'  ) " + 
        " )  ";
        //@todo and (not ( @status = 'inactive' or @status='deleted') )";
    }

}


/* 
$Log: ConeImpl.java,v $
Revision 1.4  2006/06/15 09:49:21  nw
improvements coming from unit testing

Revision 1.3  2006/04/21 13:48:12  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.2  2006/03/13 18:29:17  nw
fixed queries to not restrict to @status='active'

Revision 1.1  2005/10/17 16:02:44  nw
added siap and cone interfaces
 
*/