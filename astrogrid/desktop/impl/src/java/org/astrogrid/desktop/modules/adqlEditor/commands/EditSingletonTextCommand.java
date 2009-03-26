/* EditSingletonTextCommand.java
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
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AtomNode;
/**
 * @author jl99@star.le.ac.uk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EditSingletonTextCommand extends AbstractCommand {
    
    private static final Log log = LogFactory.getLog( EditSingletonTextCommand.class ) ;
    private String oldValue ;
    private String newValue ;

    /**
     * @param target
     * @param source
     */
    public EditSingletonTextCommand( AdqlTree adqlTree, UndoManager undoManager, AdqlNode target ) {
        super( adqlTree, undoManager, target ) ;  

        // NB: The way I have used target is acceptable ONLY in the constructor.
        // Elsewhere it must be accessed via the command methods!!!
        try {
            if( target instanceof AtomNode ) {
                oldValue = ((AtomNode)target).formatDisplay() ;
            }
            else {
                XmlObject xmlObject = target.getXmlObject() ;
                if( AdqlUtils.isAttributeDriven( xmlObject ) ) {
                    String[] attributeNames = AdqlUtils.getEditableAttributes( xmlObject ) ;
                    XmlObject attribObj = AdqlUtils.get( xmlObject, attributeNames[0] ) ;
                    oldValue = ((SimpleValue)(attribObj)).getStringValue() ;
                }

                else {
                    oldValue = ((SimpleValue)xmlObject).getStringValue() ;
                }
            }
        }
        catch( Exception ex ) {
            oldValue = "" ;
            log.warn( ex ) ;
        }
    }
    
    public String[] getEnumeratedValues() {
        return AdqlUtils.getEnumValuesGivenDrivenType( getChildType() ) ;
    }
    
    public void setNewValue( String value ) {
        this.newValue = value ;
    }
    
    public void setSelectedValue( String value ) {
        setNewValue( value ) ;
    }
        
    @Override
    public Result execute() {   
        Result result = _execute( newValue ) ;
        if( result != CommandExec.FAILED )
            undoManager.addEdit( this ) ;
        return result ;
    }
   
    private Result _execute( String srcValue ) {
        Result result = CommandExec.OK ;
        try {     
            AdqlNode node = getChildEntry() ;
            XmlObject xmlObject = getChildObject() ;
            SchemaType type = getChildType() ;
            SchemaType listType = type.getListItemType() ;
            if( listType != null ) {
                // This surprisingly works for a list...
                ((XmlAnySimpleType)xmlObject).setStringValue( srcValue )  ;
            }
            else if( node instanceof AtomNode ) {
                ((AtomNode)node).setValue( srcValue ) ;
            }
            else if( AdqlUtils.isAttributeDriven( type ) ) {
                String[] attributeNames = AdqlUtils.getEditableAttributes( type ) ;
                XmlObject attr = AdqlUtils.get( xmlObject, attributeNames[0] ) ;
                if( srcValue == null || srcValue.trim().length() == 0 ) {
                   // I cannot see any reason for the attribute be optional
                   // and not existing, but for safety's sake...
                   if( AdqlUtils.isOptionalAttribute( xmlObject, attributeNames[0] ) ) {
                       AdqlUtils.unset( xmlObject, attributeNames[0] ) ;
                   }
                   else {
                       ;  // we do nothing, ignoring any changes.
                   }
                }
                else {
                    ((XmlAnySimpleType)attr).setStringValue( srcValue ) ;
                }                  
            }
            else {
                ((XmlAnySimpleType)xmlObject).setStringValue( srcValue )  ;
            }
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.debug( "EditSingletonTextCommand._unexecute() failed.", exception ) ;
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
        if( _execute( newValue ) == CommandExec.FAILED ) {
            throw new CannotRedoException() ;
        }
            
    }
    
    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        if( _execute( oldValue ) == CommandExec.FAILED ) {
            throw new CannotUndoException() ;
        }
    }
    
    @Override
    public String getPresentationName() {
        return "Edit" ;
    }

    /**
     * @return the oldValue
     */
    public String getOldValue() {
        return oldValue;
    }

    /**
     * @param oldValue the oldValue to set
     */
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    /**
     * @return the newValue
     */
    public String getNewValue() {
        return newValue;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(512) ;
        buffer.append( "\nEditSingletonTextCommand" ) ;
        buffer.append( super.toString() ) ;
        buffer
             .append( "\nnewValue: " ).append( newValue )
             .append( "\noldValue: " ).append( oldValue ) ;
        return buffer.toString() ;
    }
   
}