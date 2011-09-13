/*
 * $Id: NamespacePrefixMapperImpl.java,v 1.2 2011/09/13 13:43:32 pah Exp $
 * 
 * Created on 17 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package net.ivoa.jaxb;

import org.astrogrid.contracts.Namespaces;

/**
 * Produce nice namespace prefixes for JAXB output.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 17 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class NamespacePrefixMapperImpl extends AbstractNamespaceMapper {

    
    private final static String[] uris = new String[]{
        Namespaces.VR.getNamespace()
        ,Namespaces.RI.getNamespace()
        ,Namespaces.CEA.getNamespace()
        ,Namespaces.CEAB.getNamespace()
        ,Namespaces.UWS.getNamespace()
        ,Namespaces.VS.getNamespace()
        ,Namespaces.VA.getNamespace()
        
};
    /* (non-Javadoc)
     * @see com.sun.xml.bind.marshaller.NamespacePrefixMapper#getPreDeclaredNamespaceUris()
     * 
     */
    @Override
    public String[] getPreDeclaredNamespaceUris() {
	return uris;
    }

}


/*
 * $Log: NamespacePrefixMapperImpl.java,v $
 * Revision 1.2  2011/09/13 13:43:32  pah
 * result of merge of branch ivoa_pah_2931
 *
 * Revision 1.1.2.2  2011/09/01 09:38:26  pah
 * include more schema
 *
 * Revision 1.1.2.1  2011/06/09 22:18:56  pah
 * basic VOResource schema nearly done - but not got save/recall working
 *
 * Revision 1.2  2009/06/05 13:06:20  pah
 * RESOLVED - bug 2921: add capabilities to the automatic registration
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2921
 * marshalling of capabilities and namespace changes
 *
 * Revision 1.1  2008/10/06 12:12:37  pah
 * factor out classes common to server and client
 *
 * Revision 1.2  2008/09/03 14:19:02  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/03/19 23:10:55  pah
 * First stage of refactoring done - code compiles again - not all unit tests passed
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
