/* EnumeratedInsertCommand.java
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

import javax.swing.undo.UndoManager;

import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jl99@star.le.ac.uk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EnumeratedInsertCommand extends StandardInsertCommand {
    
    private static final Log log = LogFactory.getLog( EnumeratedInsertCommand.class ) ;
    
    private SchemaType attrType ;
    private String attrName ;
    private String attrValue ;
   
    /**
     * @param target
     * @param 
     */
    public EnumeratedInsertCommand( AdqlTree adqlTree
                                  , UndoManager undoManager
                                  , AdqlNode parentTarget
                                  , SchemaType childType 
                                  , SchemaProperty childElement
                                  , SchemaType attrType
                                  , String attrName ) {
        super( adqlTree, undoManager, parentTarget, childType, childElement ) ;
        this.attrType = attrType ;	
        this.attrName = attrName ;
    }
    
    public EnumeratedInsertCommand( EnumeratedInsertCommand eic ) {
        super( eic.adqlTree, eic.undoManager, eic.getParentEntry(), eic.childType, eic.childElement ) ;
        this.attrType = eic.attrType ;
        this.attrName = eic.attrName ;
        this.attrValue = eic.attrValue ;
}
    
    
    
    public String getAttributeValue() {
        return attrValue ;
    }
    
    public void setAttributeValue( String attributeValue ) {
        this.attrValue = attributeValue ;
    }
    
    @Override
    public void setSelectedValue( String value ) {
        setAttributeValue( value ) ;
    }
    
    public String[] getEnumeratedValues() {
        return AdqlUtils.getEnumValuesGivenDrivingType( attrType ) ;
    }
    
    @Override
    protected Result _execute() {
        Result result = super._execute() ;
        if( result != CommandExec.FAILED ) {
            try {          
                attrValue = AdqlUtils.getMasterEnumSynonym( attrValue ) ;
                XmlObject enumValue = XmlString.Factory.newInstance() ;
                enumValue = enumValue.set(  XmlString.Factory.newValue( attrValue ) ) ;
                enumValue = enumValue.changeType( attrType ) ;       
                AdqlUtils.set( getChildObject()
                             , attrName
                             , enumValue ) ; 
            }
            catch( Exception exception ) {
                result = CommandExec.FAILED ;
                log.debug( "EnumeratedInsertCommand._execute() failed.", exception ) ;
            }
        }       
        return result ;
    }
    
    @Override
    protected Result _unexecute() {
        Result result = super._unexecute() ;
        return result ;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(512) ;
        buffer.append( "\nEnumeratedInsertCommand" ) ;
        buffer.append( super.toString() ) ;
        buffer.append( "\nattrName: " ).append( attrName ) ;
        buffer.append( "\nattrValue: " ).append( attrValue ) ;   
        if( attrType == null ) {
            buffer.append( "\nattrType: null" ) ;
        }
        else {
            buffer.append( "\nattrType: " ).append( attrType.getName().getLocalPart() ) ;
        }
        return buffer.toString() ;
    }
           
}