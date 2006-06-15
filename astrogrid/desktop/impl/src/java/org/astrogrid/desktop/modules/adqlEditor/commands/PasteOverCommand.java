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

import javax.swing.tree.DefaultTreeModel;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.desktop.modules.adqlEditor.nodes.NodeFactory;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode ;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree ;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandExec.Result;

/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PasteOverCommand extends AbstractCommand {
    
    private XmlObject sourceValue ;
    private XmlObject previousValue ;

    /**
     * @param target
     * @param source
     */
    public PasteOverCommand( AdqlTree adqlTree, UndoManager undoManager, AdqlNode target, XmlObject source ) {
        super( adqlTree, undoManager, target ) ;
        this.sourceValue = source ;
        this.previousValue = target.getXmlObject().copy() ;
    }
    
    
    public Result execute() {      
        Result result = _execute( sourceValue ) ;
        if( result != CommandExec.FAILED )
            undoManager.addEdit( this ) ;
        return result ;
    }
    
    private Result _execute( XmlObject srcValue ) {
        Result result = CommandExec.OK ;
        try {     
        	AdqlNode child = getParentEntry().replace( this, srcValue ) ;
        	exchangeInEditStore( childToken, child ) ;
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
        if( _execute( sourceValue ) == CommandExec.FAILED ) {
            throw new CannotRedoException() ;
        }         
    }
    
    public void undo() throws CannotUndoException {
        super.undo();
        if( _execute( previousValue ) == CommandExec.FAILED ) {
            throw new CannotUndoException() ;
        }
    }
     
    public String getPresentationName() {
        return "Paste over" ;
    }
   
}