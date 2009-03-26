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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.astrogrid.adql.v1_0.beans.JoinTableType;
import org.astrogrid.adql.v1_0.beans.JointTableQualifierType;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;

/**
 * @author jl99@star.le.ac.uk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EnumeratedElementInsertCommand extends StandardInsertCommand {
   
    private static final Log log = LogFactory.getLog( EnumeratedElementInsertCommand.class ) ;
    
    private String value ;
    
    /**
     * @param target
     * @param 
     */
    public EnumeratedElementInsertCommand( AdqlTree adqlTree
                                  , UndoManager undoManager
                                  , AdqlNode parentTarget
                                  , SchemaType childType 
                                  , SchemaProperty childElement ) {
        super( adqlTree, undoManager, parentTarget, childType, childElement ) ;
    }
    
    public EnumeratedElementInsertCommand( EnumeratedElementInsertCommand eic ) {
        super( eic.adqlTree, eic.undoManager, eic.getParentEntry(), eic.childType, eic.childElement ) ;
    }
    
    protected EnumeratedElementInsertCommand() {}
    
    @Override
    public void setSelectedValue( String value ) {
        this.value = value ;
    }
    
    public String[] getEnumeratedValues() {
        // This is not as straightforward as attribute driven!
        // The typical (only one so far) is JoinTableType, which has three children...
        // (Well, at present - 7th November 2006)
        // (1) Join(t)TableQualifierType
        // (2) ArrayOfFromTableType
        // (3) ComparisonPredType
        // It is (1) that is the enumerated element type that drives the join for
        // JoinTableType. The enumerated elements contain the strings...
        // LEFT_OUTER, RIGHT_OUTER, FULL_OUTER, INNER and CROSS.
        // In order to avoid some display awkwardness and also the difficulty of changing
        // an immutable value, it is better to completely hide the enumerated elements.
        // They should not become nodes in the tree view, which will then expose the
        // elements to editing by the user. It seems a small point, but it is better to
        // edit the value at the parent level, and choose replacement of the whole driven
        // element rather than to try and change a driven element value. 
        // At least, I have found this the easiest way!!!
        return AdqlUtils.getEnumValuesGivenDrivenType( childType ) ;
    }
    
    @Override
    protected Result _execute() {
        Result result = super._execute() ;
        if( result != CommandExec.FAILED ) {
            try {        
//                String drivingTypeLocalName = (String)AdqlData.ENUMERATED_ELEMENTS.get( AdqlUtils.getLocalName( childType ) ) ;
//                SchemaType drivingType = AdqlUtils.getType( childType, drivingTypeLocalName ) ;                
//                XmlObject enumValue = XmlString.Factory.newInstance() ;
//                enumValue = enumValue.changeType( drivingType ) ;  
//                String elementName = "" ;     
//                SchemaProperty[] elements = childType.getElementProperties() ;
//                for( int i=0; i<elements.length; i++ ) {
//                    if( elements[i].getType().isAssignableFrom( enumValue ) ) {
//                        elementName = elements[i].getName().getLocalPart() ;
//                        break ;
//                    }
//                }      
//                if( DEBUG_ENABLED ) {
//                   log.debug( "elementName: " + elementName ) ;
//                }
//                AdqlUtils.set( getChildObject()
//                             , elementName
//                             , enumValue ) ; 
                  // If this works, try something less type specific (see above)...
                  // Yes, it does work.
                  value = AdqlUtils.getMasterEnumSynonym( value ) ;
                  ((JoinTableType)getChildObject()).setQualifier( JointTableQualifierType.Enum.forString(value) );
            }
            catch( Exception exception ) {
                result = CommandExec.FAILED ;
                log.debug( "EnumeratedElementInsertCommand._execute() failed.", exception ) ;
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
        buffer.append( "\nEnumeratedElementInsertCommand" ) ;
        buffer.append( super.toString() ) ;
        buffer.append( "\nvalue: " ).append( value ) ;
        return buffer.toString() ;
    }
}