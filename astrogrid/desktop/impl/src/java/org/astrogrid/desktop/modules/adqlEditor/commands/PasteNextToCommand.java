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

import javax.swing.tree.DefaultTreeModel;
import javax.swing.undo.CannotRedoException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode ;
import org.astrogrid.desktop.modules.adqlEditor.nodes.NodeFactory ;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandExec.Result;
import org.astrogrid.desktop.modules.dialogs.editors.ADQLToolEditorPanel;

/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PasteNextToCommand extends AbstractCommand {
    
    private static final Log log = LogFactory.getLog( PasteNextToCommand.class ) ;
    private static final boolean DEBUG_ENABLED = false ;
    private static final boolean TRACE_ENABLED = false ;
    
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
    
    
    public Result execute() {      
        Result result = _execute() ;
        if( result != CommandExec.FAILED ) {
            newInstanceToken = addToEditStore( newInstance ) ;
            undoManager.addEdit( this ) ;
        }          
        return result ;
    }
    
    private Result _execute() {
        Result result = CommandExec.OK ;
        try {          
           newInstance = getParentEntry().insert( this, source.getSource(), before ) ;
           source.openBranchesOn( newInstance ) ;
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.error( "PasteNextToCommand():", exception ) ;
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
        exchangeInEditStore( newInstanceToken, newInstance ) ;   
    }
    
    public void undo() throws CannotUndoException {
        super.undo();
        if( _unexecute() == CommandExec.FAILED ) {
            throw new CannotUndoException() ;
        }
    }
     
    public String getPresentationName() {
        return "Paste " + ( before == true ? "before" : "after" ) ;
    }
   
}