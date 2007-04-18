/* EditEnumeratedElementCommand.java
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
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
/**
 * @author jl99@star.le.ac.uk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EditEnumeratedElementCommand extends AbstractCommand {
    
    private static final Log log = LogFactory.getLog( EditEnumeratedElementCommand.class ) ;
    private static final boolean DEBUG_ENABLED = true ;
    private static final boolean TRACE_ENABLED = true ;
    private String oldValue ;
    private String newValue ;

    /**
     * @param target
     * @param source
     */
    public EditEnumeratedElementCommand( AdqlTree adqlTree, UndoManager undoManager, AdqlNode target ) {
        super( adqlTree, undoManager, target ) ;  
        String[] elementNames = AdqlUtils.getEditableElements( getChildType() ) ;
        XmlObject drivingObject = AdqlUtils.get( getChildObject(), elementNames[0] ) ;
        oldValue = ((SimpleValue)(drivingObject)).getStringValue() ;       
    }
    
    public String[] getEnumeratedValues() {
        return AdqlUtils.getEnumValuesGivenDrivenType( getChildType() ) ;
    }
    
    public void setNewValue( String attributeValue ) {
        String enumValue = AdqlUtils.getMasterEnumSynonym( attributeValue ) ;
        this.newValue = enumValue ;
    }
    
    public void setSelectedValue( String value ) {
        setNewValue( value ) ;
    }
        
    public Result execute() {   
        Result result = _execute( newValue ) ;
        if( result != CommandExec.FAILED )
            undoManager.addEdit( this ) ;
        return result ;
    }
   
    private Result _execute( String srcValue ) {
        Result result = CommandExec.OK ;
        try {     
           String[] elementNames = AdqlUtils.getEditableElements( getChildType() ) ;
           XmlObject drivingObject = AdqlUtils.get( getChildObject(), elementNames[0] ) ;
           String enumValue = AdqlUtils.getMasterEnumSynonym( srcValue ) ;
           ((SimpleValue)drivingObject).setStringValue( enumValue ) ;
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
        if( _execute( newValue ) == CommandExec.FAILED ) {
            throw new CannotRedoException() ;
        }
            
    }
    
    public void undo() throws CannotUndoException {
        super.undo();
        if( _execute( oldValue ) == CommandExec.FAILED ) {
            throw new CannotUndoException() ;
        }
    }
    
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
   
}