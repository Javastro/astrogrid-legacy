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

import javax.swing.tree.DefaultTreeModel;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
//import org.astrogrid.desktop.modules.adqlEditor.AdqlCommand;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
import org.astrogrid.desktop.modules.adqlEditor.nodes.NodeFactory ;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree ;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandExec.Result;

import sun.rmi.runtime.GetThreadPoolAction;

/**
 * @author jl99@star.le.ac.uk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PasteIntoCommand extends AbstractCommand {
    
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
        return "Paste into" ;
    }
   
}