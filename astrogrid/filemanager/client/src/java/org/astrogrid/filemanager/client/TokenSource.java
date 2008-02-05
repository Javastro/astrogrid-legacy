/*$Id: TokenSource.java,v 1.3 2008/02/05 11:37:59 pah Exp $
 * Created on 10-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.client;

import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.registry.RegistryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Class to manage tokens - ensures that we never accidentally use our last token.
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Feb-2005
 *
 */
class TokenSource {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(TokenSource.class);

    /** Construct a new TokenSource
     * 
     */
    public TokenSource(SecurityToken token) {
        if (token == null) {
            throw new IllegalArgumentException("cannot start with a null token");
        } 
        this.token = token;
   }
    
    /**
     * Our current security token.
     *  
     */
    private SecurityToken token;
    /**
     * Split our security token to create a new one.
     * 
     * @return A new security token.
     * @throws RegistryException
     * @throws CommunityException
     *  @deprecated this is now a no-op since the {@link org.astrogrid.community.resolver.CommunityTokenResolver} has gone - not sure that it ever served a useful purpose anyway
     */
    public SecurityToken nextToken() throws CommunityException,  RegistryException {
      logger.debug("  Token : " + token.toString());
                // Split our current token.              
                // Return the other.
                return token;
    }


    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[TokenSource:");
        buffer.append(" logger: ");
        buffer.append(logger);
        buffer.append(" token: ");
        buffer.append(token);
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: TokenSource.java,v $
Revision 1.3  2008/02/05 11:37:59  pah
RESOLVED - bug 2545: Problem with IVORN resolution
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2545

Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.2  2005/02/18 15:50:15  nw
lots of changes.
introduced new schema-driven soap binding, got soap-based unit tests
working again (still some failures)

Revision 1.1.2.1  2005/02/11 14:27:52  nw
refactored, split out candidate classes.
 
*/