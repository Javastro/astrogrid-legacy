/*$Id: ConeImpl.java,v 1.19 2009/03/26 18:04:12 nw Exp $
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
import java.util.List;

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.contracts.StandardIds;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/** IVOA Cone search client.
 * Complies with Cone spec v1.03 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Oct-2005
 *@TEST and tidy up.
 */
public class ConeImpl extends DALImpl implements Cone, org.astrogrid.acr.nvo.Cone {

    /** Construct a new ConeImpl
     * @param c 
     * 
     */
    public ConeImpl(final Registry reg, final FileSystemManager vfs, final UIContext c) {
        super(reg,vfs, c);
    }

    /**
     * @see org.astrogrid.acr.nvo.Cone#constructQuery(java.net.URI, double, double, double)
     */
    public URL constructQuery(final URI arg0, final double arg1, final double arg2, final double arg3)
            throws InvalidArgumentException, NotFoundException {
        URL endpoint = resolveEndpoint(arg0);

        endpoint = addOption(
        					addOption( 
        							addOption(endpoint,"RA",Double.toString(arg1))
        						,"DEC",Double.toString(arg2))
        					,"SR",Double.toString(arg3));
      //  }
        return endpoint;
      
    }

    @Override
    protected final URL findAccessURL(final Service s) throws InvalidArgumentException {
        if (s instanceof ConeService) {
            final ConeCapability cap = ((ConeService)s).findConeCapability();
            final Interface[] interfaces = cap.getInterfaces();
            Interface std = null;
            switch (interfaces.length) {
                case 0: throw new InvalidArgumentException(s.getId() + " does not provide an interface in it's cone capability");
                case 1:
                    std = interfaces[0];
                    break;
                default:    
                    for (int i = 0; i < interfaces.length; i++) {
                        final Interface cand = interfaces[i];
                        if ("std".equals(cand.getRole())) {
                            std = cand;
                        }
                        // none marked as std - just choose the first.
                        if (std == null) {
                            std = interfaces[0];
                        }
                    }
            }                            
            return std.getAccessUrls()[0].getValue();
        } 

            throw new InvalidArgumentException(s.getId() + " does not provide a Cone Search capability");
        
    }


    public String getRegistryQuery() {
       return "type = Cone";
    }

	public String getRegistryAdqlQuery() {
		 return "Select * from Registry r where ( " +
	        "r.capability/@xsi:type like '%ConeSearch'  " +
	        " or r.capability/@standardID = '" + StandardIds.CONE_SEARCH_1_0 + "'"
	        + " )  ";
	        //@issue and (not ( @status = 'inactive' or @status='deleted') )";
	}
	   public String getRegistryXQuery() {
	        return "//vor:Resource[(" +
	                "(capability/@xsi:type &= '*ConeSearch') " 
	                + " or " 
	                +"(capability/@standardID = '" + StandardIds.CONE_SEARCH_1_0 + "')"             
	                + ") and ( not ( @status = 'inactive' or @status='deleted'))]";
	    }
	   
	   
	   /** overridden to prevent use - doesn't apply to cone */
	   @Override
	public final int saveDatasets(final URL query, final URI root)
	        throws SecurityException, ServiceException,
	        InvalidArgumentException {
	    throw new ServiceException("Cone search results do not point to datasets");
	}

       /** overridden to prevent use - doesn't apply to cone */	   
	   @Override
	public final int saveDatasetsSubset(final URL query, final URI root, final List rows)
	        throws SecurityException, ServiceException,
	        InvalidArgumentException {
	        throw new ServiceException("Cone search results do not point to datasets");
	}
	   
}


/* 
$Log: ConeImpl.java,v $
Revision 1.19  2009/03/26 18:04:12  nw
source code improvements - cleaned imports, @override, etc.

Revision 1.18  2009/03/04 18:43:10  nw
Complete - taskMove DAL over to VFS

Revision 1.17  2009/02/13 17:46:20  nw
Complete - taskUse SRQL in AR

Revision 1.16  2008/11/04 14:35:51  nw
javadoc polishing

Revision 1.15  2008/10/01 09:53:43  nw
overridden non-sensical parent messages.

Revision 1.14  2008/05/09 11:32:21  nw
Complete - task 394: process reg query results in a stream.

Incomplete - task 392: joda time

Incomplete - task 391: get to grips with new CDS

Revision 1.13  2008/04/23 10:56:23  nw
marked as needing test.

Revision 1.12  2008/03/30 18:05:06  nw
checked the implementations against the latest standards,
noted in code comments what version of the standard is supported.

Revision 1.11  2008/03/10 14:14:01  nw
fixed fallthough case statements.

Revision 1.10  2008/01/30 08:38:37  nw
Incomplete - task 313: Digest registry upgrade.

RESOLVED - bug 2526: voexdesktop help needs to point to beta.astrogrid.org
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2526
Incomplete - task 314: get login working again.

Revision 1.9  2008/01/25 07:53:25  nw
Complete - task 134: Upgrade to reg v1.0

Revision 1.8  2007/10/22 11:59:40  nw
RESOLVED - bug 2372: VOScope cone search with +ve declination fails
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2372

Revision 1.7  2007/10/15 17:34:11  mbt
Vizier seems to require explicit leading '+' for declination

Revision 1.6  2007/09/21 16:35:15  nw
improved error reporting,
various code-review tweaks.

Revision 1.5  2007/04/18 15:47:05  nw
tidied up voexplorer, removed front pane.

Revision 1.3  2007/03/08 17:44:03  nw
first draft of voexplorer

Revision 1.2  2007/01/29 11:11:36  nw
updated contact details.

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
