/*
 * Created on 27-Jan-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.common.session;

import org.apache.cocoon.environment.Session;

/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public final class AstrogridSessionFactory {
    
    private AstrogridSessionFactory () {}
    
    public static AstrogridSession getSession( Session session ) {
        return new AstrogridSessionImpl( session ) ;
    }

}
