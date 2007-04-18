/* EditCommand.java
 * Created on 17-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.adqlEditor.commands;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.adql.AdqlStoX;
import org.astrogrid.adql.ParseException;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
/**
 * @author jl99@star.le.ac.uk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EditCommand extends AbstractCommand {
    
    private static final Log log = LogFactory.getLog( EditCommand.class ) ;
    private XmlObject newValue ;
    private CopyHolder preserved ;
    private AdqlStoX compiler ;

    /**
     * @param target
     * @param source
     */
    public EditCommand( AdqlTree adqlTree, UndoManager undoManager, AdqlNode target, AdqlStoX compiler ) {
        super( adqlTree, undoManager, target ) ;
        preserved = CopyHolder.holderForEditPurposes( target ) ;
        this.compiler = compiler ;
    }
        
    public Result execute() {   
        if( log.isTraceEnabled() ) log.trace( "execute() entry" ) ;
        Result result = CommandExec.FAILED ;
        try {  
            String elementContextPath = getFromEditStore( childToken ).getElementContextPath() ;
            log.debug( "child.getElementContextPath(): " + elementContextPath ) ;
            newValue = compiler.compileFragmentToXmlBean( elementContextPath ) ;
            adqlTree.getNodeFactory().setForcedNotToUseModel( true ) ;
            exchangeInEditStore( childToken, getParentEntry().replace( this, newValue ) ) ;
            adqlTree.getNodeFactory().setForcedNotToUseModel( false ) ;
            ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( getChildEntry() ) ;
            preserved.openBranchesOn( getChildEntry() ) ;
            result = CommandExec.OK ;
            undoManager.addEdit( this ) ;
        }
        catch( ParseException pex ) {
            String message = pex.getLocalizedMessage() ;
            if( message != null && message.length() > 0) {
                this.setMessages( new String[]{ message } ) ;
            }
            else {
                this.setMessages ( new String[] { "Internal compiler error. See log."} ) ;
                log.debug( pex ) ;
            }              
        }
        catch( Exception ex ) {
            this.setMessages ( new String[] { "Internal compiler error. See log."} ) ;
            log.debug( ex ) ;
        }
        finally {
            if( log.isTraceEnabled() ) log.trace( "EditCommand.execute() exit" ) ;
        }
        return result ;
    }
   
    private Result _execute( XmlObject srcValue ) {
        Result result = CommandExec.OK ;
        try {    
            adqlTree.getNodeFactory().setForcedNotToUseModel( true ) ;
            exchangeInEditStore( childToken, getParentEntry().replace( this, srcValue ) ) ;
            adqlTree.getNodeFactory().setForcedNotToUseModel( false ) ;
            ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( getChildEntry() ) ;
            preserved.openBranchesOn( getChildEntry() ) ;
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
        }
        return result ;
    }
     
    public void die() {
        super.die();
    }
    public void redo() throws CannotRedoException {
        super.redo();
        if( _execute( newValue ) == CommandExec.FAILED ) {
            throw new CannotRedoException() ;
        }
            
    }
    
    public void undo() throws CannotUndoException {
        super.undo();
        if( _execute( preserved.getSource() ) == CommandExec.FAILED ) {
            throw new CannotUndoException() ;
        }
    }
    
    public String getPresentationName() {
        return "Edit" ;
    }
   
}