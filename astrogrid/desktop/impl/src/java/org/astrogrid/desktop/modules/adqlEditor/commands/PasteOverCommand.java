/* PasteOverCommand.java
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

import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PasteOverCommand extends AbstractCommand {
    
    private static final Log log = LogFactory.getLog( PasteOverCommand.class ) ;
    
    private CopyHolder source ;
    private CopyHolder previous ;

    /**
     * @param target
     * @param source
     */
    public PasteOverCommand( AdqlTree adqlTree
                           , UndoManager undoManager
                           , AdqlNode target
                           , CopyHolder source ) {
        super( adqlTree, undoManager, target ) ;
        this.source = source ;
        this.previous = CopyHolder.holderForCopyPurposes( target ) ;
    }
    
    
    @Override
    public Result execute() {      
        Result result = _execute( source ) ;
        if( result != CommandExec.FAILED )
            undoManager.addEdit( this ) ;
        return result ;
    }
    
    private Result _execute( CopyHolder src ) {
        Result result = CommandExec.OK ;
        try {     
        	AdqlNode child = getParentEntry().replace( this, src.getSource() ) ;
            src.openBranchesOn( child ) ;
        	exchangeInEditStore( childToken, child ) ;
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.error( "PasteOverCommand()._execute(): ", exception ) ;
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
        if( _execute( source ) == CommandExec.FAILED ) {
            throw new CannotRedoException() ;
        }         
    }
    
    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        if( _execute( previous ) == CommandExec.FAILED ) {
            throw new CannotUndoException() ;
        }
    }
     
    @Override
    public String getPresentationName() {
        return "Paste over" ;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer( 512 ) ;
        buffer.append( "\nPasteOverCommand" ) ;
        buffer.append( super.toString() ) ;
        if( previous == null ) {
            buffer.append( "\nprevious: null" ) ;
        }
        else {
            buffer.append( "\nprevious: " ).append( previous.toString() ) ;
        }
        if( source == null ) {
            buffer.append( "\nsource: null" ) ;
        }
        else {
            buffer.append( "\nsource: " ).append( source.toString() ) ;
        }
        
        return buffer.toString() ; 
    }
   
}