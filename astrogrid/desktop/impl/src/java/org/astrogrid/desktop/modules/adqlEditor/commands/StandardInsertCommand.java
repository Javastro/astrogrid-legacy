/* StandardInsertCommand.java
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

import javax.swing.tree.TreePath;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;

/**
 * @author jl99@star.le.ac.uk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StandardInsertCommand extends AbstractCommand {
    
    private static final Log log = LogFactory.getLog( StandardInsertCommand.class ) ;
    private static final boolean DEBUG_ENABLED = false ;
    private static final boolean TRACE_ENABLED = false ;
    
    private XmlObject sourceObject = null ;
   
    /**
     * @param target
     * @param 
     */
    public StandardInsertCommand( AdqlTree adqlTree
            			        , UndoManager undoManager
                                , AdqlNode target
                                , SchemaType childType
                                , SchemaProperty childElement ) {
        super( adqlTree, undoManager, target, childType, childElement ) ;
    }
    
    public StandardInsertCommand( StandardInsertCommand sic ) {
        super( sic.adqlTree, sic.undoManager, sic.getParentEntry(), sic.childType, sic.childElement ) ;
}
    
    protected StandardInsertCommand() {
        super() ;
    }
    
    public void setSelectedValue( String value ) {}
    
    @Override
    public Result execute() {      
        Result result = _execute() ;
        if( result != CommandExec.FAILED ) {
            undoManager.addEdit( this ) ;
            adqlTree.expandPath( new TreePath( this.getChildEntry().getPath() ) ) ;
        }
        return result ;
    }
   
    protected Result _execute() {
        if( TRACE_ENABLED ) log.debug( "StandardInsertCommand._execute() entry" ) ;
        Result result = CommandExec.OK ;
        try {   
            AdqlNode child = getParentEntry().insert( this ) ;  
            if( childToken != null )
                exchangeInEditStore( childToken, child ) ;
            else 
                childToken = addToEditStore( child ) ;       
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.debug( "StandardInsertCommand._execute() failed.", exception ) ;
            if( log.isDebugEnabled() ) {
                log.debug(  this.toString() ) ;
            }
        }
        finally {
            if( TRACE_ENABLED ) log.debug( "StandardInsertCommand._execute() exit" ) ;
        }
        return result ;
    }
    
    protected Result _unexecute() {
        if( TRACE_ENABLED ) log.debug( "StandardInsertCommand._unexecute() entry" ) ;
        Result result = CommandExec.OK ;
        try {
	        getParentEntry().remove( this ) ;       
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.debug( "StandardInsertCommand._unexecute() failed.", exception ) ;
            if( log.isDebugEnabled() ) {
                log.debug(  this.toString() ) ;
            }
        }
        finally {
            if( TRACE_ENABLED ) log.debug( "StandardInsertCommand._unexecute() exit" ) ;
        }
        return result ;
    }
    
    
    @Override
    public void die() {
        super.die();
    }
    
    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        if( _execute() == CommandExec.FAILED ) {
            throw new CannotRedoException() ;
        }  
    }
    
    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        if( _unexecute() == CommandExec.FAILED ) {
            throw new CannotUndoException() ;
        }
    }
     
    @Override
    public String getPresentationName() {
        return "Insert into" ;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer( 512 ) ;
        buffer.append( "\nStandardInsertCommand" ) ;
        buffer.append( super.toString() ) ;
        if( sourceObject == null ) {
            buffer.append( "\nsourceObject: null" ) ;
        }
        else {
            try {
                buffer.append( "\nsourceObject: \n" ).append( sourceObject.toString() ) ;
            }
            catch( Throwable th ) {
                buffer
                    .append( "\nsourceObject toString() produced exception: " )
                    .append(  th.getClass() ) ;
            }
        }
        return buffer.toString() ; 
    }
   
}