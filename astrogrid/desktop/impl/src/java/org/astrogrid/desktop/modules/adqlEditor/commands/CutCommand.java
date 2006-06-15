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

import javax.swing.tree.DefaultTreeModel;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
// import org.astrogrid.desktop.modules.adqlEditor.AdqlCommand;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode ;
import org.astrogrid.desktop.modules.adqlEditor.nodes.NodeFactory ;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree ;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandExec.Result;
import org.astrogrid.desktop.modules.dialogs.editors.ADQLToolEditorPanel;

/**
 * @author jl99@star.le.ac.uk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CutCommand extends AbstractCommand {
    
    private static final Log log = LogFactory.getLog( CutCommand.class ) ;
    private static final boolean DEBUG_ENABLED = true ;
    private static final boolean TRACE_ENABLED = true ;
    private XmlObject preservedValue ;
    private int arrayIndex = -1 ;

    /**
     * @param target
     * @param source
     */
    public CutCommand( AdqlTree adqlTree, UndoManager undoManager, AdqlNode cutTarget ) {
        super( adqlTree, undoManager, cutTarget ) ;
        preservedValue = getChildObject().copy() ;
        if( isChildHeldInArray() ) {
            AdqlNode parent = getParentEntry() ;
            AdqlNode child = getChildEntry() ;
            arrayIndex = parent.getIndex( child ) ;
        }
    }
    
    
    public Result execute() {      
        Result result = _execute() ;
        if( result != CommandExec.FAILED )
            this.adqlTree.getCommandFactory().getUndoManager().addEdit( this ) ;
        return result ;
    }
   
    private Result _execute() {
        if( TRACE_ENABLED ) log.debug( "CutCommand._execute() entry" ) ;
        // NB: This invalidates the Adql parent/child relationship.
        // So affects somewhat the integrity of the CommandInfo interface.
        Result result = CommandExec.OK ;
        try {  
            AdqlNode parent = getFromEditStore( parentToken ) ;
            AdqlNode child = getFromEditStore( childToken ) ;
            parent.remove( this ) ;
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.debug( exception ) ;
        }
        finally {
            if( TRACE_ENABLED ) log.debug( "CutCommand._execute() exit" ) ;
        }
        return result ;
    }
    
    
    private Result _unexecute() {
        if( TRACE_ENABLED ) log.debug( "CutCommand._unexecute() entry" ) ;
        Result result = CommandExec.OK ;
        try {
            // This restores the integrity of the CommandInfo interface.
            AdqlNode parent = getFromEditStore( parentToken ) ;
            AdqlNode child ;
            if( isChildHeldInArray() ) {
                child = parent.insert( this, preservedValue, arrayIndex ) ;
            }
            else {
                child = parent.insert( this, preservedValue ) ;
            }          
	        exchangeInEditStore( childToken, child ) ;
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.debug( "CutCommand._unexecute", exception ) ;
        }
        finally {
            if( TRACE_ENABLED ) log.debug( "CutCommand._unexecute() exit " ) ;
        }
        return result ;
    }
    
    
    public void die() {
        super.die();
    }
    public void redo() throws CannotRedoException {
        super.redo();
        if( _execute() == CommandExec.FAILED ) {
            throw new CannotRedoException() ;
        }
            
    }
    
    public void undo() throws CannotUndoException {
        super.undo();
        if( _unexecute() == CommandExec.FAILED ) {
            throw new CannotUndoException() ;
        }
    }
    
    
    public String getPresentationName() {
        return "Cut" ;
    }
   
}