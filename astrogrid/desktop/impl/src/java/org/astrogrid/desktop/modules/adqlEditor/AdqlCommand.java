/* AdqlCommand.java
 * Created on 25-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.adqlEditor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.ListIterator;

import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;


/**
 * @author jl99
 *
 * @deprecated
 */
public class AdqlCommand {      
    private SchemaProperty element ;
    private AdqlEntry entry ;
    private SchemaType[] concreteTypes ;
    private boolean enabledStatus = false ;
    private BigInteger minOccurs ;
    private BigInteger maxOccurs ;
    
    public static ArrayList buildCommands( AdqlEntry entry ) {
        ArrayList commands = new ArrayList() ;
        if( entry.isChildHidingRequired() == false ) {
            SchemaProperty[] elements = entry.getElementProperties() ;
            if( elements != null ) {
                for( int i=0; i<elements.length; i++ ) {
                    commands.add( new AdqlCommand( entry
                                                 , elements[i] ) ) ;
                }
            }
        }
        return commands ;
    }
    
    
    public static boolean isSuitablePasteTarget( AdqlEntry entry, XmlObject clipboardObject ) {
        return isSuitablePasteTarget( entry, clipboardObject.schemaType() ) ;
    }
    
    public static boolean isSuitablePasteTarget( AdqlEntry entry, SchemaType clipboardType ) {
        boolean result = false ;
        SchemaProperty[] elementProperties = entry.getElementProperties() ;
        for ( int i = 0 ; i < elementProperties.length ; i++ ) {
            if( elementProperties[i].getType().isAssignableFrom( clipboardType ) ) { 
                result = true ;
                break ;
            }
        }
        return result ;
    }
    
    
    public static int[] findSuitableMatch( ArrayList commands, XmlObject pasteObject ) {
        return findSuitableMatch( commands, pasteObject.schemaType() ) ;
    }
    
    public static int[] findSuitableMatch( ArrayList commands, SchemaType pasteType ) {
        int[] match = new int[] { -1, -1 } ;
        SchemaType[] targetTypes = null ;
        ListIterator iterator = commands.listIterator() ;
        checkCommands: while( iterator.hasNext() ) {
            AdqlCommand c = (AdqlCommand)iterator.next() ;
            if( c.isEnabled() == false )
                continue ;
            targetTypes = c.getConcreteTypes() ;
            for( int i=0; i<targetTypes.length; i++ ) {
                // Caution! The test below looks too restrictive.
                // Should it be "isAssignableFrom()" as is used in
                // isSuitablePasteTarget() ?
                if( targetTypes[i].getName().equals( pasteType.getName() ) ) {
                    match[0] = iterator.nextIndex()-1 ;
                    match[1] = i ;
                    break checkCommands ;
                }
            }
        } // end while
        return match ;
    }
    
    
    public AdqlCommand( AdqlEntry entry, SchemaProperty element ) {
        this.entry = entry ;
        this.element = element ;
        this.minOccurs = element.getMinOccurs() ;
        this.maxOccurs = element.getMaxOccurs() ;
        this.concreteTypes = AdqlUtils.getConcreteSubtypes( element.getType() ) ;
        // Experiment. The idea of concreteSubtypes was where an element used abstract
        // types. We searched for the concrete ones that fulfilled its existence.
        // However, elements always have a concrete type. They may be defined directly
        // as of a particular type, which I have not been picking up. So....
        // If there are no concrete subtypes (ie: the element does not depend on abstract
        // types), then we use its basic definition...
        if( concreteTypes == null || concreteTypes.length == 0 ) {
            concreteTypes = new SchemaType[] { element.getType() } ;
        }
        this.extractEnabledStatus() ;
    }
    public String getDisplayName() { return entry.getDisplayName(); }
    public SchemaProperty getElement() { return element; }
    public AdqlEntry getEntry() { return entry; }
    public String getElementName() { return element.getName().getLocalPart() ; }
    public BigInteger getMinOccurs() { return minOccurs ; }
    public BigInteger getMaxOccurs() { return maxOccurs ; }
    public SchemaType[] getConcreteTypes() { return concreteTypes ; }
    public boolean isEnabled() { return this.enabledStatus ; }
    public boolean isSuitablePasteTarget( XmlObject clipboardObject ) {
        return AdqlCommand.isSuitablePasteTarget( this.entry, clipboardObject ) ;
    }
    public boolean isOptionalSingleton() {
        if( minOccurs.intValue() > 0  ||  maxOccurs == null  ||  maxOccurs.intValue() > 1 )
            return false ;
        return true ;
    }
    public boolean isSupportedType( int indexOfType ) {
        boolean retValue = true ;
        // Testing for -1 and allowing it through as supported is balancing on a knife.
        // Good! Bound to find out something.
        if( indexOfType != -1  
            &&
            AdqlData.UNSUPPORTED_TYPES.containsKey( concreteTypes[indexOfType].getName().getLocalPart() ) ) {
            retValue = false ;
        }
        if( AdqlData.UNSUPPORTED_TYPES.containsKey( this.getElementName() ) ) {
            retValue = false ;
        }
        return retValue ;
    }
    public boolean isMandatorySingleton() {
        if( minOccurs.intValue() != 1  ||  maxOccurs == null  ||  maxOccurs.intValue() != 1 )
            return false ;
        return true ;
    }
    public boolean isArray() {
        if( maxOccurs == null  ||  maxOccurs.intValue() > 1 )
            return true ;
        return false ;
    }
    private void extractEnabledStatus() {
        XmlObject o = entry.getXmlObject() ;
        String e = getElementName() ;
        if( isOptionalSingleton() ) {
            boolean isSet = AdqlUtils.isSet( o, e );
            if( !isSet ) 
                this.enabledStatus = true ;
        }
        else if( isMandatorySingleton() ) {
            Object obj = AdqlUtils.get( o, e );
            if( obj == null )
                this.enabledStatus = true ;
        }
        else if( isArray() ) {
            if( this.maxOccurs == null 
                ||
                AdqlUtils.sizeOfArray( o, e ) < this.maxOccurs.intValue() )
            	this.enabledStatus = true ;
        }
        else {
            // We should report this.
        }
    }
    
    public boolean isInsertableIntoArray( int noElementsToInsert ) {
        if( isArray() == false )
            return false ;
        XmlObject o = entry.getXmlObject() ;
        String e = getElementName() ;
        if( this.maxOccurs == null 
                ||
            AdqlUtils.sizeOfArray( o, e ) <= this.maxOccurs.intValue() - noElementsToInsert ) {
            return true ;
        }
        return false ;
    }
    
    public boolean isConcreteTypeCascadeable( int index ) {
        boolean answer = false ;
        String name = null ;
        if( index > -1  && this.concreteTypes.length > 0 ) {
            try {
                name = concreteTypes[index].getName().getLocalPart();
                answer = AdqlData.CASCADEABLE.containsKey( name ) ;
            }
            catch( Exception ex ) {
                ex.printStackTrace() ;
            }                    
        }
        return answer ;
    }
    
    public boolean isConcreteTypeTableLinked( int index ) {
        boolean answer = false ;
        String name = null ;
        if( index > -1  && this.concreteTypes.length > 0 ) {
            try {
                name = concreteTypes[index].getName().getLocalPart();
                answer = AdqlData.METADATA_LINK_TABLE.containsKey( name ) ;
            }
            catch( Exception ex ) {
                ex.printStackTrace() ;
            }             
        }
        return answer ;
    }
    
    public boolean isConcreteTypeColumnLinked( int index ) {
        boolean answer = false ;
        String name = null ;
        if( index > -1  && this.concreteTypes.length > 0 ) {
            try {
                name = concreteTypes[index].getName().getLocalPart();
                answer = AdqlData.METADATA_LINK_COLUMN.containsKey( name ) ;
            }
            catch( Exception ex ) {
                ex.printStackTrace() ;
            }          
        }
        return answer ;
    }
    
    private boolean _isConcreteTypeEnumerable( int index ) {
        boolean answer = false ;
        String name = null ;
        if( index > -1  && this.concreteTypes.length > 0 ) {
            try {
                name = concreteTypes[index].getName().getLocalPart();
                answer = ( AdqlData.ENUMERATED_ATTRIBUTES.containsKey( name ) )
                         ||
                         ( AdqlData.ENUMERATED_ELEMENTS.containsKey( name ) ) ;
            }
            catch( Exception ex ) {
                ex.printStackTrace() ;
            }          
        }
        return answer ;
    }
    
    
    public boolean isConcreteTypeDrivenByEnumeratedAttribute( int index ) {
        boolean answer = false ;
        String name = null ;
        if( index > -1  && this.concreteTypes.length > 0 ) {
            try {
                name = concreteTypes[index].getName().getLocalPart();
                answer = AdqlData.ENUMERATED_ATTRIBUTES.containsKey( name ) ;
            }
            catch( Exception ex ) {
                ex.printStackTrace() ;
            }          
        }
        return answer ;
    }
    
    
    private boolean isConcreteTypeAnEnumeratedElement( int index ) {
        boolean answer = false ;
        String name = null ;
        if( index > -1  && this.concreteTypes.length > 0 ) {
            try {
                name = concreteTypes[index].getName().getLocalPart();
                answer = AdqlData.ENUMERATED_ELEMENTS.containsKey( name ) ;
            }
            catch( Exception ex ) {
                ex.printStackTrace() ;
            }          
        }
        return answer ;
    }
    
    public String extractDisplayNameForType( int indexOfType ) {
        String name = null ;
        if( (concreteTypes.length > 0) 
            &&
            (concreteTypes[ indexOfType ].isBuiltinType() == false) ) {
            if( this.isPatternContext() ) 
                name = AdqlUtils.extractDisplayName( "Pattern" ) ;
            else
                name = AdqlUtils.extractDisplayName( concreteTypes[ indexOfType ] ) ;
        }
        if( name == null || name.length() == 0 ) {
            name = AdqlUtils.extractDisplayName( this.getElementName() ) ;
        }
        return name ;
    }
    
    private boolean isPatternContext() {
        String elementName = null ;
        if( this.getElementName().equals( "Pattern" ) ) 
            return true ;
        if( this.getElementName().equals( "Literal" ) ) {
            XmlCursor cursor = this.entry.getXmlObject().newCursor() ;
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
            
}
