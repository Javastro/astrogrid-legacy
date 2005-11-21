/*
 * Created on 25-Aug-2005
 * This class is something of a kludge. The processing has been moved out of
 * login to first use of MySpace. But I'm uncertain whether I can remove the 
 * accountSpace ivorn from the session. I tried it and got an error when 
 * displaying a complex query in the Query Editor. But couldnt tell why.
 * For the moment, I believe this is all the first-time stuff that can be
 * safely removed from login.
 * 
 */
package org.astrogrid.portal.myspace.generation;

import org.apache.cocoon.ProcessingException;
import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.portal.common.session.AstrogridSession ;
import org.astrogrid.portal.common.session.AttributeKey ;
import org.astrogrid.store.Ivorn;
import org.astrogrid.portal.myspace.filesystem.*;
/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeHelper {

    /**
     * 
     */
    private TreeHelper() {}
    
    public static Tree getTree( AstrogridSession session ) throws ProcessingException {
        Tree fileTree = (Tree)session.getAttribute(AttributeKey.MYSPACE_TREE) ;
        if( fileTree == null ) {
            
            Ivorn 
            	userIvorn = (Ivorn)session.getAttribute(AttributeKey.USER_IVORN ),
            	accountSpace = null ;
            if( userIvorn == null ) {
                throw new ProcessingException( "Session timeout may have occurred" ) ;
            }
            try {
              CommunityAccountSpaceResolver accSpaceResolver = new CommunityAccountSpaceResolver();
              accountSpace = accSpaceResolver.resolve( userIvorn );        
              assert accountSpace != null : "Account Space should not be null";
              session.setAttribute( AttributeKey.ACOUNTSPACE_IVORN, accountSpace) ;
            }
            catch(Exception e) {
              accountSpace = null;  
              throw new ProcessingException("Failed to resolve account space for <" + userIvorn.toString() + ">", e);
            }
            
            FileManagerClient fmc = (FileManagerClient)session.getAttribute( AttributeKey.FILE_MANAGER_CLIENT ) ;
            try {
                fileTree = Tree.constructTree( userIvorn, fmc ) ;
                session.setAttribute( AttributeKey.MYSPACE_TREE, fileTree ) ;
            }
            catch( Exception ex ) {
                throw new ProcessingException("Tree.constructTree( userIvorn, fmc ) failed",ex);
            }      
        }
        return fileTree ;
    }

}
