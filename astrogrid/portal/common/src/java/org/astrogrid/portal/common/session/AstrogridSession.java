/*
 * Created on 26-Jan-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.portal.common.session;

/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface AstrogridSession {
    
    public void setAttribute( AttributeKey key, Object value ) ;
    public Object getAttribute( AttributeKey key ) ;
    public void removeAttribute( AttributeKey key ) ;
    
    
    /**
     * @author jeff lusted
     * 
     * @deprecated - provided only for backward compatibility
     * 
     * @param key
     * @param value
     */
    public void setObject( String key, Object value ) ;
    
    
    /**
     * @author jeff lusted
     * 
     * @deprecated - provided only for backward compatibility
     * 
     * @param key
     * @return Object
     */
    public Object getObject( String key ) ;
      
}
