/* PasteNextToCommand.java
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
 * @author jl99
 *
 */
public class PasteNextToCommand extends AbstractCommand {
    
    private static final Log log = LogFactory.getLog( PasteNextToCommand.class ) ;
    
    private CopyHolder source ;
    private boolean before ;
    private AdqlNode newInstance ;
    private Integer newInstanceToken ;
    
    /**
     * @param target
     * @param source
     */
    public PasteNextToCommand( AdqlTree adqlTree
                             , UndoManager undoManager
                             , AdqlNode targetOfNextTo
                             , CopyHolder source
                             , boolean before ) {
        super( adqlTree, undoManager, targetOfNextTo ) ;
        this.source = source ;
        this.before = before ;
    }
    
    public boolean isBefore() {
        return before ;
    }
    
    
    @Override
    public Result execute() {    
        Result result = _execute() ;
        if( result != CommandExec.FAILED ) {
            newInstanceToken = addToEditStore( newInstance ) ;
            source.openBranchesOn( newInstance ) ;
            undoManager.addEdit( this ) ;
        }          
        return result ;
    }
    
    private Result _execute() {
        Result result = CommandExec.OK ;
        try {          
           newInstance = getParentEntry().insert( this, source.getSource(), before ) ;
//           source.openBranchesOn( newInstance ) ;
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.error( "PasteNextToCommand()._execute(): ", exception ) ;
            if( log.isDebugEnabled() ) {
                log.debug(  this.toString() ) ;
            }
        }
        return result ;
    }
    
    private Result _unexecute() {
        Result result = CommandExec.OK ;
        try {
            AdqlNode parent = getFromEditStore( parentToken ) ;
            newInstance = getFromEditStore( newInstanceToken ) ;
	        parent.removeNode( newInstance ) ;
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.error( "PasteNextToCommand()._unexecute(): ", exception ) ;
            if( log.isDebugEnabled() ) {
                log.debug(  this.toString() ) ;
            }
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
        // Not sure why I need this...
        // But it seems to work for normal nodes but not for NestedNodes.
        exchangeInEditStore( newInstanceToken, newInstance ) ;  
        source.openBranchesOn( newInstance ) ;
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
        return "Paste " + ( before == true ? "before" : "after" ) ;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer( 512 ) ;
        buffer.append( "\nPasteNextToCommand" ) ;
        buffer.append( super.toString() ) ;
        if( source == null ) {
            buffer.append( "\nsource: null" ) ;
        }
        else {
            buffer.append( "\nsource: " ).append( source.toString() ) ;
        }
        buffer.append( "\nnewInstanceToken: " ).append( newInstanceToken ) ;
        if( newInstance == null ) {
            buffer.append( "\nnewInstance: null" ) ;
        }
        else {
            try {
                buffer.append( "\nnewInstance: \n" ).append( newInstance.toString() ) ;
            }
            catch( Throwable th ) {
                buffer
                    .append( "\nnewInstance toString() produced exception: " )
                    .append(  th.getClass() ) ;
            }
        }
        return buffer.toString() ; 
    }
   
}