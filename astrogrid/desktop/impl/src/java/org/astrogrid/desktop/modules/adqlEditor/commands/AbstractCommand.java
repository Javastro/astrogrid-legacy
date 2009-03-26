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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
import org.astrogrid.desktop.modules.adqlEditor.nodes.NestingNode;

//import com.sun.corba.se.connection.GetEndPointInfoAgainException;


/**
 * @author Jeff Lusted jl99@star.le.ac.uk
 *
 */
public abstract class AbstractCommand extends AbstractUndoableEdit implements CommandExec, CommandInfo {

    private static final Log log = LogFactory.getLog( AbstractCommand.class ) ;
    
    protected AdqlTree adqlTree ;
    protected UndoManager undoManager ;
    //protected AdqlEntry parent ;
    //protected AdqlEntry child ;
    protected Integer parentToken ;
    protected Integer childToken ;
    protected SchemaProperty childElement ;
    protected SchemaType childType ;
    protected SchemaType parentType ;
    protected int minOccurs ;
    protected int maxOccurs ;
    
    private String[] messages ;
    private boolean initializedStatusGood = false ;
    
    private static int NOT_ALLOWED = 0 ;
    private static int OPTIONAL_SINGLETON = 1 ;
    private static int MANDATORY_SINGLETON = 2 ;
    private static int HELD_IN_ARRAY = 3 ;
    private int heldAsFlag = NOT_ALLOWED ;
    
    
    /**
     * @param undoManager 
     * 
     */
    public AbstractCommand(  AdqlTree adqlTree, UndoManager undoManager, AdqlNode child ) {
        this.adqlTree = adqlTree ;
        this.undoManager = undoManager ;
        this.childToken = addToEditStore( child ) ;
        AdqlNode parent = (AdqlNode)child.getParent() ;
        this.parentToken = addToEditStore( parent ) ;
        this.parentType = parent.getSchemaType() ;
        this.childType = child.getSchemaType() ;
        initializeElementInfo( parent ) ;
    }
    
    public AbstractCommand( AdqlTree adqlTree, UndoManager undoManager, AdqlNode parent, SchemaType childType, SchemaProperty childElement ) {
        this.adqlTree = adqlTree ;
        this.undoManager = undoManager ;
        this.parentToken = addToEditStore( parent ) ;
        this.childType = childType ;
        this.parentType = parent.getSchemaType() ;
        this.childElement = childElement ;
        initializeElementInfo( parent ) ;
    }
    
    protected AbstractCommand() { }
    
    private void initializeElementInfo( AdqlNode parent ) {
        try {
            if( childElement == null ) {
                if( childToken != null ) {
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
            if( parent instanceof NestingNode ) {
                minOccurs = childElement.getMinOccurs().intValue() ;
                maxOccurs = -1 ;
                heldAsFlag = HELD_IN_ARRAY ;
            }
            else if( AdqlUtils.isFunctionType( parentType )
                     &&
                     childElement.getName().getLocalPart().equalsIgnoreCase( "Arg" ) ) {
                Integer[] cardinalities = AdqlUtils.getFunctionCardinalities( parent.getXmlObject() ) ;
                minOccurs = cardinalities[0].intValue() ;
                maxOccurs = cardinalities[1].intValue() ;
                if( minOccurs == 0 & maxOccurs == 0 ) {
                    heldAsFlag = NOT_ALLOWED ;
                }
                else {
                    heldAsFlag = HELD_IN_ARRAY ;
                }               
            }
            else if( AdqlUtils.isCardinalityImposed( childElement ) ) {
                Integer[] cardinalities = AdqlUtils.getImposedCardinality( childElement ) ;
                minOccurs = cardinalities[0].intValue() ;
                maxOccurs = cardinalities[1].intValue() ;
                heldAsFlag = HELD_IN_ARRAY ;
            }
            else {
                minOccurs = childElement.getMinOccurs().intValue() ;
                BigInteger biMaxOccurs = childElement.getMaxOccurs() ;
                maxOccurs = ( biMaxOccurs == null ? -1 : biMaxOccurs.intValue() ) ;
                if( minOccurs == 0 && maxOccurs == 1 ) {
                    heldAsFlag = OPTIONAL_SINGLETON ;
                }
                else if( minOccurs == 1 && maxOccurs == 1 ) {
                    heldAsFlag = MANDATORY_SINGLETON ;
                }
                else {
                    heldAsFlag = HELD_IN_ARRAY ;
                }
            } 
            initializedStatusGood = true ;
        }
        catch( Exception ex ) {
            log.debug( "Failure to initialize element information.", ex );
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
        return messages ;
    }
    
    public void setMessages( String[] messages ) {
        this.messages = messages ;
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
    
//    public int getChildIndex() {          
//        Enumeration e = getParentEntry().children() ;
//        AdqlNode child = getChildEntry() ;
//        int index = 0 ;
//        while( e.hasMoreElements() ) {
//            Object o = e.nextElement() ;
//            if( o == child ) {
//                return index ;
//            }
//        }
//        return -1 ;
//    }
    
    public int getChildIndex() {          
        Enumeration e = getParentEntry().children() ;
        AdqlNode child = getChildEntry() ;
        int retIndex = -1 ;
        int index = -1 ;
        while( e.hasMoreElements() ) {
            Object o = e.nextElement() ;
            index++ ;
            if( o == child ) {
                retIndex = index ;
            }
        }
        if( log.isDebugEnabled() ) {
            int i = getParentEntry().getIndex( getChildEntry() ) ;
            log.debug( "getChildIndex(): " + retIndex + "and getIndex(childNode): " + i ) ;
        }
        return retIndex ;
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
        else if ( isChildDisallowed() ) {
            enabled = false ;
        }
        else {
            log.debug( "Problems with element cardinality in AbstractCommand.isChildEnabled()" ) ;
        }
        return enabled ;
    }
    
    public boolean isChildDisallowed() {
        return heldAsFlag == NOT_ALLOWED ;
    }
    
    public boolean isChildMandatorySingleton() {
        return heldAsFlag == MANDATORY_SINGLETON ;
    }
    public boolean isChildOptionalSingleton() {
        return heldAsFlag == OPTIONAL_SINGLETON ;
    }
    
    public boolean isChildHeldInArray() {
        return heldAsFlag == HELD_IN_ARRAY ;
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
       
    public boolean isChildDrivenByEnumeratedElement() {
        return AdqlUtils.isDrivenByEnumeratedElement( childType ) ;
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

    public boolean isInitializedStatusGood() {
        return initializedStatusGood;
    }
       
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(512) ;
        buffer
            .append( "\nAbstractCommand part"  )
            .append( "\ninitializedStatusGood: " ).append( initializedStatusGood )
            .append( "\nparentToken: " ).append( parentToken ) 
            .append( "\nchildToken: " ).append( childToken ) ;
        if( childElement == null ) {
            buffer.append( "\nchildElement: null" ) ;
        }
        else {
            buffer.append( "\nchildElement: " ).append( childElement.getJavaPropertyName() ) ;
        }
        buffer
             .append( "\nchildType: " ).append( childType.getName().getLocalPart() )
             .append( "\nminOccurs: " ).append( minOccurs )
             .append( "\nmaxOccurs: " ).append( maxOccurs ) ;
        if( messages == null ) {
            buffer.append( "\nmessages: null" ) ;
        }
        else {
            buffer.append( "\nmessages: ");
            for( int i=0; i<messages.length; i++ ) {
                buffer.append(i).append( ' ' ).append( messages[i] ) ;
            }
        }       
        return buffer.toString() ;
    }
}
