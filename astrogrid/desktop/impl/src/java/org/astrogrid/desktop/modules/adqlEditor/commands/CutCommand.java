/* CutCommand.java
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

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;

/**
 * @author jl99@star.le.ac.uk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CutCommand extends AbstractCommand {
    
    private static final Log log = LogFactory.getLog( CutCommand.class ) ;

    private CopyHolder preserved ;
    private int arrayIndex = -1 ;

    /**
     * @param target
     * @param source
     */
    public CutCommand( AdqlTree adqlTree
                     , UndoManager undoManager
                     , AdqlNode cutTarget ) {
        super( adqlTree, undoManager, cutTarget ) ;
        preserved = CopyHolder.holderForCopyPurposes( cutTarget ) ;
        if( isChildHeldInArray() ) {
            AdqlNode parent = getParentEntry() ;
            AdqlNode child = getChildEntry() ;
            arrayIndex = parent.getIndex( child ) ;
        }
    }
    
    
    @Override
    public Result execute() {      
        Result result = _execute() ;
        if( result != CommandExec.FAILED )
            this.adqlTree.getCommandFactory().getUndoManager().addEdit( this ) ;
        return result ;
    }
    
    public CopyHolder getCopy() {
        return this.preserved ;
    }
   
    private Result _execute() {
        if( log.isTraceEnabled() ) log.trace( "_execute() entry" ) ;
        // NB: This invalidates the Adql parent/child relationship.
        // So affects somewhat the integrity of the CommandInfo interface.
        Result result = CommandExec.OK ;
        try {  
            AdqlNode parent = getFromEditStore( parentToken ) ;
            parent.remove( this ) ;
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.debug( "CutCommand execution failed,", exception ) ;
            if( log.isDebugEnabled() ) {
                log.debug(  this.toString() ) ;
            }
        }
        finally {
            if( log.isTraceEnabled() ) log.trace( "_execute() exit" ) ;
        }
        return result ;
    }
    
    
    private Result _unexecute() {
        if( log.isTraceEnabled() ) log.trace( "_unexecute() entry" ) ;
        Result result = CommandExec.OK ;
        try {
            // This restores the integrity of the CommandInfo interface.
            AdqlNode parent = getFromEditStore( parentToken ) ;
            AdqlNode child ;
            if( isChildHeldInArray() ) {
                child = parent.insert( this, preserved.getSource(), arrayIndex ) ;
            }
            else {
                child = parent.insert( this, preserved.getSource() ) ;
            }          
	        exchangeInEditStore( childToken, child ) ;
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.debug( "CutCommand _unexecute() failed.", exception ) ;
            if( log.isDebugEnabled() ) {
                log.debug(  this.toString() ) ;
            }
        }
        finally {
            if( log.isTraceEnabled() ) log.trace( "_unexecute() exit " ) ;
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
        return "Cut" ;
    }
   
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer( 512 ) ;
        buffer.append( "\nCutCommand" ) ;
        buffer.append( super.toString() ) ;
        buffer.append( "\narrayIndex: " ).append( arrayIndex ) ;
        if( preserved == null ) {
            buffer.append( "\npreserved: null" ) ;
        }
        else {
            buffer.append( "\npreserved: " ).append( preserved.toString() ) ;
        }
        return buffer.toString() ; 
    }
}