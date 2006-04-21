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
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.desktop.modules.adqlEditor.AdqlEntry;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;

/**
 * @author jl99@star.le.ac.uk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CutCommand extends AbstractCommand {
    
    private static final Log log = LogFactory.getLog( CutCommand.class ) ;
    private static final boolean DEBUG_ENABLED = true ;
    private static final boolean TRACE_ENABLED = false ;
    private XmlObject preservedValue ;
    private int arrayIndex = -1 ;

    /**
     * @param target
     * @param source
     */
    public CutCommand( AdqlTree adqlTree, UndoManager undoManager, AdqlEntry cutTarget ) {
        super( adqlTree, undoManager, cutTarget ) ;
        preservedValue = child.getXmlObject().copy() ;
        if( isChildHeldInArray() ) {
            arrayIndex = AdqlEntry.findChildIndex( parent.getXmlObject(), getChildObject() ) ;
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
            parent = getFromEditStore( parentToken ) ;
            child = getFromEditStore( childToken ) ;
            AdqlEntry.removeInstance( parent, child ) ;
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
            parent = getFromEditStore( parentToken ) ;
	        child = AdqlEntry.newInstance( parent, insertObject().set( preservedValue ) ) ;
	        exchangeInEditStore( childToken, child ) ;
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.debug( exception ) ;
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
    
    private XmlObject insertObject() {
        XmlObject parentObject = parent.getXmlObject() ;
        XmlObject newObject = null ; 
        if( isChildHeldInArray() ) {                
            newObject = AdqlUtils.insertNewInArray( parentObject, getChildElementName(), arrayIndex ) ;
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