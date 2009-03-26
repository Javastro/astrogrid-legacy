/* PasteIntoCommand.java
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

import org.apache.xmlbeans.SchemaType;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jl99@star.le.ac.uk
 *
 */
public class PasteIntoCommand extends AbstractCommand {
    
    private static final Log log = LogFactory.getLog( PasteIntoCommand.class ) ;
    
    private CopyHolder source ;
    
    /**
     * @param target
     * @param source
     */
    public PasteIntoCommand( AdqlTree adqlTree
                           , UndoManager undoManager
                           , AdqlNode target
                           , SchemaType childType
                           , CopyHolder source ) {
        super( adqlTree, undoManager, target, childType, null ) ;
        this.source = source ;
    }
    
    
    @Override
    public Result execute() {      
        Result result = _execute() ;
        if( result != CommandExec.FAILED ) {
            undoManager.addEdit( this ) ;
        }         
        return result ;
    }
   
    private Result _execute() {
        Result result = CommandExec.OK ;
        try {    
            AdqlNode parent = getFromEditStore( parentToken ) ;
            AdqlNode child = parent.insert( this, source.getSource() ) ;
            source.openBranchesOn( child ) ;
            if( childToken != null ) 
                exchangeInEditStore( childToken, child ) ;
            else 
                childToken = addToEditStore( child ) ;       	
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.error( "PasteInToCommand()._execute(): ", exception ) ;
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
	        parent.remove( this ) ;
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.error( "PasteInToCommand()._unexecute(): ", exception ) ;
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
        return "Paste into" ;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer( 512 ) ;
        buffer.append( "\nPasteIntoCommand" ) ;
        buffer.append( super.toString() ) ;
        if( source == null ) {
            buffer.append( "\nsource: null" ) ;
        }
        else {
            buffer.append( "\nsource: " ).append( source.toString() ) ;
        }
        return buffer.toString() ; 
    }
   
}