/*
 * Created on 26-Jan-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.common.session;

import org.apache.cocoon.environment.Session;

/**
 * @author jeff lusted
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AstrogridSessionImpl implements AstrogridSession {
    
    private Session session ;
    
    protected AstrogridSessionImpl( Session session ) {
        this.session = session ;
    }
    
    public void setAttribute( AttributeKey key, Object value ) {
        session.setAttribute( key.toString(), value ) ;
    }
    
    public Object getAttribute( AttributeKey key ) {
        return session.getAttribute( key.toString() ) ;
    }
    
    public void removeAttribute( AttributeKey key ) {
        session.removeAttribute( key.toString() ) ;
    }
    
    /* (non-Javadoc)
     * @see org.astrogrid.portal.common.session.AstrogridSession#setObject(java.lang.String, java.lang.Object)
     */
    public void setObject( String key, Object value ) {
        session.setAttribute( key, value ) ;
    }
    
    /* (non-Javadoc)
     * @see org.astrogrid.portal.common.session.AstrogridSession#getObject(java.lang.String)
     */
    public Object getObject( String key ) {
        return session.getAttribute( key ) ;    
    }
      
}
