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
import org.astrogrid.desktop.modules.adqlEditor.AdqlEntry;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree ;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandExec.Result;

/**
 * @author jl99@star.le.ac.uk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PasteIntoCommand extends AbstractCommand {
    
    private XmlObject sourceValue ;
 
    /**
     * @param target
     * @param source
     */
    public PasteIntoCommand( AdqlTree adqlTree, UndoManager undoManager, AdqlEntry target, SchemaType childType, XmlObject source ) {
        super( adqlTree, undoManager, target, childType, null ) ;
        this.sourceValue = source ;
    }
    
    
    public Result execute() {      
        Result result = _execute() ;
        if( result != CommandExec.FAILED ) {
            undoManager.addEdit( this ) ;
            childToken = addToEditStore( child ) ;
        }         
        return result ;
    }
   
    private Result _execute() {
        Result result = CommandExec.OK ;
        try {    
            parent = getFromEditStore( parentToken ) ;
            if( childToken != null )
                child = getFromEditStore( childToken ) ;
        	child = AdqlEntry.newInstance( parent, insertObject().set( sourceValue ) ) ;
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
        }
        return result ;
    }
    
    private Result _unexecute() {
        Result result = CommandExec.OK ;
        try {
            parent = getFromEditStore( parentToken ) ;
            child = getFromEditStore( childToken ) ;
	        AdqlEntry.removeInstance( parent, child ) ;
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
        exchangeInEditStore( childToken, child ) ;   
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
    
    private XmlObject insertObject() {
        XmlObject parentObject = parent.getXmlObject() ;
        XmlObject newObject = null ; 
        if( isChildHeldInArray() ) {                
            newObject = AdqlUtils.addNewToEndOfArray( parentObject, getChildElementName() ) ;
            newObject = newObject.changeType( childType ) ;
        }
        else {
            if( childType.isBuiltinType() ) {
                newObject = XmlObject.Factory.newInstance().changeType( childType ) ;
                newObject = AdqlUtils.setDefaultValue( newObject ) ;
            }
            else {            
                newObject = AdqlUtils.addNew( parentObject, getChildElementName() ) ;
                if( newObject != null ) {
                    newObject = newObject.changeType( childType ) ;
                }
                else {
                    newObject = XmlObject.Factory.newInstance().changeType( getChildElement().javaBasedOnType() ) ;
                    newObject = newObject.changeType( childType ) ;
                    newObject = AdqlUtils.setDefaultValue( newObject ) ;
                    AdqlUtils.set( parentObject, getChildElementName(), newObject ) ; 
                }
            }
        } 
        return newObject ;
    }
   
}