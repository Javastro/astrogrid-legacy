/* AdqlBaseCommand.java
 * Created on 15-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.adqlEditor.commands;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ListIterator;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
import org.astrogrid.desktop.modules.adqlEditor.nodes.NestingNode;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree ;

//import com.sun.corba.se.connection.GetEndPointInfoAgainException;


/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractCommand extends AbstractUndoableEdit implements CommandExec, CommandInfo {

    private static final Log log = LogFactory.getLog( AbstractCommand.class ) ;
    private static final boolean DEBUG_ENABLED = true ;
    private static final boolean TRACE_ENABLED = true ;
    
    protected AdqlTree adqlTree ;
    protected UndoManager undoManager ;
    //protected AdqlEntry parent ;
    //protected AdqlEntry child ;
    protected Integer parentToken ;
    protected Integer childToken ;
    protected SchemaProperty childElement ;
    protected SchemaType childType ;
    protected int minOccurs ;
    protected int maxOccurs ;
    /**
     * @param undoManager TODO
     * 
     */
    public AbstractCommand(  AdqlTree adqlTree, UndoManager undoManager, AdqlNode child ) {
        this.adqlTree = adqlTree ;
        this.undoManager = undoManager ;
        this.childToken = addToEditStore( child ) ;
        this.parentToken = addToEditStore( (AdqlNode)child.getParent() ) ;
        this.childType = child.getSchemaType() ;
        getChildIndex() ;
        initializeElementInfo() ;
    }
    
    public AbstractCommand( AdqlTree adqlTree, UndoManager undoManager, AdqlNode parent, SchemaType childType, SchemaProperty childElement ) {
        this.adqlTree = adqlTree ;
        this.undoManager = undoManager ;
        this.parentToken = addToEditStore( parent ) ;
        this.childType = childType ;
        this.childElement = childElement ;
        initializeElementInfo() ;
    }
    
    protected AbstractCommand() { }
    
    private void initializeElementInfo() {
        try {
            if( childElement == null ) {
                if( childToken != null ) {
                    String elementName = AdqlUtils.extractElementLocalName( getChildObject() ) ;
                    SchemaProperty[] elements = getParentEntry().getElementProperties() ;
                    for( int i=0; i<elements.length; i++ ) {
                        if( elements[i].getName().getLocalPart().equals( elementName ) ) {
                            this.childElement = elements[i] ;
                            break ;
                        }
                    }
                }
                else {
                    SchemaProperty[] elements = getParentEntry().getElementProperties() ;
                    for( int i=0; i<elements.length; i++ ) {
                        if( elements[i].getType().isAssignableFrom( childType ) ) {
                            this.childElement = elements[i] ;
                            break ;
                        }
                    }
                }
            }
            if( getParentEntry() instanceof NestingNode ) {
                minOccurs = childElement.getMinOccurs().intValue() ;
                maxOccurs = -1 ;
            }
            else {
                minOccurs = childElement.getMinOccurs().intValue() ;
                BigInteger biMaxOccurs = childElement.getMaxOccurs() ;
                maxOccurs = ( biMaxOccurs == null ? -1 : biMaxOccurs.intValue() ) ;
            }          
        }
        catch( Exception ex ) {
            log.debug( ex );
        }
        
    }
      

    public int getChildMaxOccurs() {
        return maxOccurs ;
    }
    public int getChildMinOccurs() {
        return minOccurs ;
    }
    
    
    
    /* (non-Javadoc)
     * @see org.astrogrid.desktop.modules.adqlEditor.Command#execute()
     */
    public Result execute() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.astrogrid.desktop.modules.adqlEditor.Command#getMessages()
     */
    public String[] getMessages() {
        return null;
    }
    
    

    public AdqlNode getChildEntry() {
        return getFromEditStore( childToken ) ;
    }
    public AdqlNode getParentEntry() {
        return getFromEditStore( parentToken ) ;
    }
    
    public XmlObject getParentObject() {
        return getParentEntry().getXmlObject() ;
    }
    
    public SchemaType getParentType() {
        return getParentObject().schemaType() ;
    }
    
    public XmlObject getChildObject() {
        return getChildEntry().getXmlObject() ;
    }
    
    public SchemaProperty getChildElement() {
        return childElement ;
    }
    public String getChildElementName() {
        return childElement.getName().getLocalPart() ;
    }
    
    public SchemaType getChildType() {
        return childType ;
    }
    
    public int getChildIndex() {          
        Enumeration e = getParentEntry().children() ;
        AdqlNode child = getChildEntry() ;
        int index = 0 ;
        while( e.hasMoreElements() ) {
            Object o = e.nextElement() ;
            if( o == child ) {
                return index ;
            }
        }
        return -1 ;
    }
    
    public boolean isChildEnabled() {
        boolean enabled = false ;
        XmlObject o = getParentEntry().getXmlObject() ;
        String e = getChildElementName() ;
        if( isChildOptionalSingleton() ) {
            enabled = !AdqlUtils.isSet( o, e ) ;
        }
        else if( isChildMandatorySingleton() ) {
            enabled = ( AdqlUtils.get( o, e ) == null ) ;
        }
        else if( isChildHeldInArray() ) {
            if( maxOccurs == -1 
                ||
                AdqlUtils.sizeOfArray( o, e ) < maxOccurs )
            	enabled = true ;
        }
        else {
            log.debug( "Problems with cardinality in AbstractCommand.isChildEnabled()" ) ;
        }
        return enabled ;
    }
    
    public boolean isChildMandatorySingleton() {
        if( minOccurs != 1  ||  maxOccurs == -1  ||  maxOccurs != 1 )
            return false ;
        return true ;
    }
    public boolean isChildOptionalSingleton() {
        if( minOccurs > 0  ||  maxOccurs == -1  ||  maxOccurs > 1 )
            return false ;
        return true ;
    }
    
    public boolean isChildHeldInArray() {
        if( maxOccurs == -1  ||  maxOccurs > 1 )
            return true ;
        return false ;
    }
    
     
    public String getChildDisplayName() {
        String name = null ;
        if( childType.isBuiltinType() == false ) {
            if( isChildInPatternContext() ) 
                name = AdqlUtils.extractDisplayName( "Pattern" ) ;
            else
                name = AdqlUtils.extractDisplayName( childType ) ;
        }
        if( name == null || name.length() == 0 ) {
            name = AdqlUtils.extractDisplayName( getChildElementName() ) ;
        }
        return name ;
    }
     
    public String getParentDisplayName() {
        return getParentEntry().getDisplayName() ;
    }
    
    public String getParentElementName() {
        String name = null ;
        AdqlNode parent = getParentEntry() ;
        AdqlNode grandParent = (AdqlNode)parent.getParent() ;
        if( grandParent != null ) {
            SchemaProperty[] elements = grandParent.getElementProperties() ;
            // I shouldn't need this defensive test for null.
            if( elements != null ) {
                for( int i=0; i<elements.length; i++ ) {
                    // JL: Should I be able to use just plain old equals against SchemaType?
                    if( elements[i].getType().getName().equals( parent.getXmlObject().schemaType().getName() ) ) {
                        name = elements[i].getName().getLocalPart() ;
                        break ;
                    }
                }
            }
        }       
        return name ;
    }
    
    public boolean isChildCascadeable() {
        return AdqlUtils.isCascadeable( childType ) ;
    }
    
    public boolean isChildColumnLinked() {
        return AdqlUtils.isColumnLinked( childType ) ;
    }
    
    public boolean isChildDrivenByEnumeratedAttribute() {
        return AdqlUtils.isDrivenByEnumeratedAttribute( childType ) ;
    }
       
    public boolean isChildAnEnumeratedElement() {
        return AdqlUtils.isEnumeratedElement( childType ) ;
    }
    
    
    public boolean isChildSupportedType() {
        boolean retValue = AdqlUtils.isSupportedType( childType ) ;
        if( retValue == true ) {
            try {
                retValue = !AdqlData.UNSUPPORTED_TYPES.containsKey( getChildElementName() ) ;
            }
            catch( Exception ex ) {
                ;
            }
        }
        return retValue ;
    }
    
    public boolean isChildTableLinked() {
        return AdqlUtils.isTableLinked( childType ) ;
    }
    
    public boolean isInsertableIntoArray(int noElementsToInsert) {
        if( isChildHeldInArray() == false )
            return false ;
        XmlObject o = getParentEntry().getXmlObject() ;
        String e = getChildElementName() ;
        if( maxOccurs == -1 
                ||
            AdqlUtils.sizeOfArray( o, e ) <= maxOccurs - noElementsToInsert ) {
            return true ;
        }
        return false ;
    }
    
    public boolean isParentSuitablePasteTargetFor( XmlObject clipboardObject ) {
        return AdqlUtils.isSuitablePasteIntoTarget( getParentEntry(), clipboardObject ) ;
    }
   
    
    public boolean isChildInPatternContext() {
        String elementName = null ;
        if( this.getChildElementName().equals( "Pattern" ) ) 
            return true ;
        if( this.getChildElementName().equals( "Literal" ) ) {
            XmlCursor cursor = getParentEntry().getXmlObject().newCursor() ;
            if( !cursor.currentTokenType().isStart() ) 
                cursor.toFirstChild(); 
            try {
                elementName = cursor.getName().getLocalPart() ;
            }
            catch ( Exception ex ) {
                ;
            }
            cursor.dispose() ;
            if( elementName.equals( "Pattern" ) ) 
                return true ; 
        }
        return false ; 
    }
    
    public Integer addToEditStore ( AdqlNode entry ) {
        return adqlTree.getCommandFactory().getEditStore().add( entry ) ;
    }
    
    public Integer exchangeInEditStore( AdqlNode in, AdqlNode out ) {
        return adqlTree.getCommandFactory().getEditStore().exchange( in, out ) ;
    }
    
    public void exchangeInEditStore( Integer token, AdqlNode in ) {
        adqlTree.getCommandFactory().getEditStore().exchange( token, in ) ;
    }
    
    public AdqlNode getFromEditStore( Integer token ) {
        return adqlTree.getCommandFactory().getEditStore().get( token ) ;
    }
    
    
    public Integer getChildToken() {
        return childToken;
    }
    public Integer getParentToken() {
        return parentToken;
    }
}
