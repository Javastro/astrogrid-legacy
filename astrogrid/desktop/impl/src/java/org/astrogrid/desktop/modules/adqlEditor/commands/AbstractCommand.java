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
import java.util.Enumeration;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;

import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.AdqlEntry;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;

//import com.sun.corba.se.connection.GetEndPointInfoAgainException;


/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractCommand extends AbstractUndoableEdit implements CommandExec, CommandInfo {

    protected AdqlTree adqlTree ;
    protected UndoManager undoManager ;
    protected AdqlEntry parent ;
    protected AdqlEntry child ;
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
    public AbstractCommand(  AdqlTree adqlTree, UndoManager undoManager, AdqlEntry child ) {
        this.adqlTree = adqlTree ;
        this.undoManager = undoManager ;
        this.child = child ;
        this.childToken = addToEditStore( child ) ;
        this.parent = (AdqlEntry)child.getParent() ;
        this.parentToken = addToEditStore( parent ) ;
        this.childType = child.getSchemaType() ;
        getChildIndex() ;
        initializeElementInfo() ;
    }
    
    public AbstractCommand( AdqlTree adqlTree, UndoManager undoManager, AdqlEntry parent, SchemaType childType, SchemaProperty childElement ) {
        this.adqlTree = adqlTree ;
        this.undoManager = undoManager ;
        this.parent = parent ;
        this.parentToken = addToEditStore( parent ) ;
        this.childType = childType ;
        this.childElement = childElement ;
        initializeElementInfo() ;
    }
    
    protected AbstractCommand() { }
    
    private void initializeElementInfo() {
        try {
            if( childElement == null ) {
                if( child != null ) {
                    String elementName = AdqlUtils.extractElementLocalName( getChildObject() ) ;
                    SchemaProperty[] elements = parent.getElementProperties() ;
                    for( int i=0; i<elements.length; i++ ) {
                        if( elements[i].getName().getLocalPart().equals( elementName ) ) {
                            this.childElement = elements[i] ;
                            break ;
                        }
                    }
                }
                else {
                    SchemaProperty[] elements = parent.getElementProperties() ;
                    for( int i=0; i<elements.length; i++ ) {
                        if( elements[i].getType().isAssignableFrom( childType ) ) {
                            this.childElement = elements[i] ;
                            break ;
                        }
                    }
                }
            }
            minOccurs = childElement.getMinOccurs().intValue() ;
            BigInteger biMaxOccurs = childElement.getMaxOccurs() ;
            maxOccurs = ( biMaxOccurs == null ? -1 : biMaxOccurs.intValue() ) ;
        }
        catch( Exception ex ) {
            ;
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
    
    

    public AdqlEntry getChildEntry() {
        return child ;
    }
    public AdqlEntry getParentEntry() {
        return getFromEditStore( parentToken ) ;
    }
    
    public XmlObject getChildObject() {
        return child.getXmlObject() ;
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
        Enumeration e = parent.children() ;
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
        XmlObject o = parent.getXmlObject() ;
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
            // We should report this.
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
        return parent.getDisplayName() ;
    }
    
    public String getParentElementName() {
        String name = null ;
        AdqlEntry grandParent = (AdqlEntry)parent.getParent() ;
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
        XmlObject o = parent.getXmlObject() ;
        String e = getChildElementName() ;
        if( maxOccurs == -1 
                ||
            AdqlUtils.sizeOfArray( o, e ) <= maxOccurs - noElementsToInsert ) {
            return true ;
        }
        return false ;
    }
    
    public boolean isParentSuitablePasteTargetFor( XmlObject clipboardObject ) {
        return AdqlUtils.isSuitablePasteIntoTarget( parent, clipboardObject ) ;
    }
   
    
    public boolean isChildInPatternContext() {
        String elementName = null ;
        if( this.getChildElementName().equals( "Pattern" ) ) 
            return true ;
        if( this.getChildElementName().equals( "Literal" ) ) {
            XmlCursor cursor = parent.getXmlObject().newCursor() ;
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
    
    public Integer addToEditStore ( AdqlEntry entry ) {
        return adqlTree.getCommandFactory().getEditStore().add( entry ) ;
    }
    
    public Integer exchangeInEditStore( AdqlEntry in, AdqlEntry out ) {
        return adqlTree.getCommandFactory().getEditStore().exchange( in, out ) ;
    }
    
    public void exchangeInEditStore( Integer token, AdqlEntry in ) {
        adqlTree.getCommandFactory().getEditStore().exchange( token, in ) ;
    }
    
    public AdqlEntry getFromEditStore( Integer token ) {
        return adqlTree.getCommandFactory().getEditStore().get( token ) ;
    }
}
