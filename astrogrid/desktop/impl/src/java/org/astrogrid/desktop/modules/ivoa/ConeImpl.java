/*$Id: ConeImpl.java,v 1.1 2006/10/11 10:39:01 nw Exp $
 * Created on 17-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.net.URL;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Oct-2005
 *
 */
public class ConeImpl extends DALImpl implements Cone {


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
       return getRegistryAdqlQuery();
    }

	public String getRegistryAdqlQuery() {
		 return "Select * from Registry where ( " +
	        " @xsi:type like '%ConeSearch'  " +
	 /*       " or ( @xsi:type like '%TabularSkyService' " + 
			" and vr:identifier like 'ivo://CDS/%' " + 
			" and vs:table/vs:column/vs:ucd = 'POS_EQ_RA_MAIN'  ) " + 
	    */    " )  ";
	        //@issue and (not ( @status = 'inactive' or @status='deleted') )";
	}
	
	public String getRegistryXQuery() {
		return "//vor:Resource[" +
				"(" +
				"@xsi:type &= '*ConeSearch' " +
				// @future - find out how to add CDS in.
				//" or (@xsi:type &= '*TabularSkyService'  and vods:table/vods:column/vods:ucd = 'POS_EQ_RA_MAIN' and vr:identifier &= 'ivo://CDS/*')" + 
				") " +
				"and ( not ( @status = 'inactive' or @status='deleted'))]";
	}

}


/* 
$Log: ConeImpl.java,v $
Revision 1.1  2006/10/11 10:39:01  nw
enhanced dal support.

Revision 1.7  2006/08/31 21:32:49  nw
doc fixes.

Revision 1.6  2006/08/15 10:12:27  nw
migrated from old to new registry models.

Revision 1.5  2006/06/27 19:13:17  nw
adjusted todo tags.

Revision 1.4  2006/06/15 09:49:21  nw
improvements coming from unit testing

Revision 1.3  2006/04/21 13:48:12  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.2  2006/03/13 18:29:17  nw
fixed queries to not restrict to @status='active'

Revision 1.1  2005/10/17 16:02:44  nw
added siap and cone interfaces
 
*/