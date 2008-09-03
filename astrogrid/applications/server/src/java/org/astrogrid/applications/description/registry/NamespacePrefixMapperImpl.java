/*
 * $Id: NamespacePrefixMapperImpl.java,v 1.2 2008/09/03 14:19:02 pah Exp $
 * 
 * Created on 17 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.registry;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import org.astrogrid.contracts.Namespaces;

/**
 * Produce nice namespace prefixes for JAXB output.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 17 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class NamespacePrefixMapperImpl extends NamespacePrefixMapper {

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
	String retval;
	Namespaces namespace = Namespaces.getNameSpaceFromURI(namespaceUri);
	if ((namespace != null)) {
	    retval = namespace.getPrefix();
	} else
	{
	    retval = suggestion;
	}
	return retval;
    }

    /* (non-Javadoc)
     * @see com.sun.xml.bind.marshaller.NamespacePrefixMapper#getPreDeclaredNamespaceUris()
     */
    @Override
    public String[] getPreDeclaredNamespaceUris() {
	return new String[]{
		Namespaces.VR.getNamespace(),
		Namespaces.RI.getNamespace(),
		Namespaces.CEA.getNamespace(),
		Namespaces.CEAB.getNamespace()
		
	};
    }

}


/*
 * $Log: NamespacePrefixMapperImpl.java,v $
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
