/* CommandFactory.java
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

import java.util.Iterator;
import java.util.ListIterator;
import java.util.List ;
import java.util.ArrayList ;
import java.util.Enumeration;
import java.util.Random;
import javax.swing.JMenuItem;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import org.apache.xmlbeans.SchemaStringEnumEntry;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.DatabaseBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.desktop.modules.adqlEditor.* ;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode ;

import java.util.Hashtable ;
/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CommandFactory {
    
    private AdqlTree adqlTree ;
    private UndoManager undoManager ;
    private EditStore editStore ;

    /**
     * 
     */
    public CommandFactory( AdqlTree adqlTree ) {
        this.adqlTree = adqlTree ;
        this.undoManager = new UndoManager() ;
        this.editStore = new EditStore() ;        
    }
    
    private CommandFactory() {}
   
    
    public PasteOverCommand newPasteOverCommand( AdqlNode targetOfPasteOver, XmlObject pasteObject  ) {
        if( AdqlUtils.isSuitablePasteOverTarget( targetOfPasteOver, pasteObject ) ) {
            return new PasteOverCommand( adqlTree, undoManager, targetOfPasteOver, pasteObject ) ;
        }
        return null ;
    }
    
    public PasteIntoCommand newPasteIntoCommand( AdqlNode targetOfPasteInto, XmlObject pasteObject ) {
        SchemaType pastableType = AdqlUtils.findSuitablePasteIntoTarget( targetOfPasteInto, pasteObject ) ;
        if( pastableType != null ) {
            return new PasteIntoCommand( adqlTree, undoManager, targetOfPasteInto, pastableType, pasteObject ) ;
        }
        return null ;
    }
    
    public PasteNextToCommand newPasteNextToCommand( AdqlNode targetOfNextTo, XmlObject pasteObject, boolean before ) {
        if( AdqlUtils.isSuitablePasteOverTarget( targetOfNextTo, pasteObject ) ) {
            return new PasteNextToCommand( adqlTree, undoManager, targetOfNextTo, pasteObject, before ) ;
        }
        return null ;
    }
    
    public CutCommand newCutCommand( AdqlTree adqlTree, UndoManager undoManager, AdqlNode targetOfCut ) {
        return new CutCommand( adqlTree, undoManager, targetOfCut ) ;
    }
    
    public List newInsertCommands( AdqlNode parent ) {
        List insertCommands = null ;
        // Not sure whether this test is required:
        if( !parent.isHidingRequired() ) {
            // For each distinctive element we build a List
            // corresponding to each type that can be a basis
            // for that element...
            SchemaProperty[] elements = parent.getElementProperties() ;
            SchemaType[] concreteSubtypes ;
            SchemaType childType ;
          
            if( elements != null ) {
                insertCommands = new ArrayList( elements.length ) ;
                for( int i=0; i<elements.length; i++ ) {
                    childType = elements[i].getType() ;
                    if( !AdqlUtils.isSupportedType( childType ) )
                        continue ;
                    if( AdqlUtils.isEnumeratedElement( childType ) )
                        continue ;
                    concreteSubtypes = AdqlUtils.getConcreteSubtypes( childType ) ;
                    // If no concrete subtypes, treat the child as the concrete type...
                    if( concreteSubtypes.length == 0 ) {
                        concreteSubtypes = new SchemaType[] { childType };
                    }
                               
                    for( int j=0; j<concreteSubtypes.length; j++ ) {
                        if( !AdqlUtils.isSupportedType( concreteSubtypes[j] ) )
                            continue ;
                        if( AdqlUtils.isEnumeratedElement( concreteSubtypes[j] ) )
                            continue ;
                        if( AdqlUtils.isColumnLinked( concreteSubtypes[j] ) ) {
                            insertCommands.add( newColumnInsertCommand( parent, concreteSubtypes[j], elements[i] ) ) ;
                        }
                        else if( AdqlUtils.isTableLinked( concreteSubtypes[j] ) ) {
                            insertCommands.add( newTableInsertCommand( parent, concreteSubtypes[j], elements[i] ) ) ;
                        }
                        else if( AdqlUtils.isDrivenByEnumeratedAttribute( concreteSubtypes[j] ) ) {
                            insertCommands.add( newEnumeratedInsertCommand( parent, concreteSubtypes[j], elements[i] ) ) ;
                        }
                        else if( AdqlUtils.isDrivenByEnumeratedElement( concreteSubtypes[j] ) ) {
                            insertCommands.add( newEnumeratedElementInsertCommand( parent, concreteSubtypes[j], elements[i] ) ) ;
                        }
                        else {
                            insertCommands.add( newStandardInsertCommand( parent, concreteSubtypes[j], elements[i] ) ) ;
                        }               
                    } // end for
                       
                } // end for 
                
            }
            
        }
        return insertCommands ;
    }
    
    
    public MultipleColumnInsertCommand newMultipleColumnInsertCommand( AdqlNode parent
                                                                     , SchemaType type ) {
        SchemaType colRefType = AdqlUtils.getType( type, AdqlData.COLUMN_REFERENCE_TYPE ) ;
        if( AdqlUtils.areTypesEqual( type, colRefType )
            &&    
            AdqlUtils.isSuitablePasteIntoTarget( parent, colRefType ) ) {        
            return new MultipleColumnInsertCommand( adqlTree
                                                  , undoManager
                                                  , parent
                                                  , type ) ;
        } 
        return null ;
    }
    
    
    public ColumnInsertCommand newColumnInsertCommand( AdqlNode parent, SchemaType childType, SchemaProperty childElement ) { 
        return new ColumnInsertCommand( adqlTree, undoManager, parent, childType, childElement ) ;
    }
    
    
    public TableInsertCommand newTableInsertCommand( AdqlNode parent, SchemaType childType, SchemaProperty childElement ) {
        return new TableInsertCommand( adqlTree, undoManager, parent, childType, childElement ) ;        
    }
    
    public EnumeratedInsertCommand newEnumeratedInsertCommand( AdqlNode parent
            											     , SchemaType childType
            											     , SchemaProperty childElement ) { 
        String childTypeName = AdqlUtils.getLocalName( childType );
        String attributeTypeName = (String)AdqlData.ENUMERATED_ATTRIBUTES.get( childTypeName ) ;
        SchemaType attributeSchemaType = AdqlUtils.getAttributeType( attributeTypeName, childType.getTypeSystem() ) ;
        String attributeName = AdqlUtils.getAttributeName( childType, attributeSchemaType ) ;
        return new EnumeratedInsertCommand( adqlTree
                                          , undoManager
							              , parent
							              , childType
							              , childElement
							              , attributeSchemaType
							              , attributeName ) ;
    }
    
    
    public EnumeratedElementInsertCommand newEnumeratedElementInsertCommand( AdqlNode parent
                                                                           , SchemaType childType
                                                                           , SchemaProperty childElement ) { 
        return new EnumeratedElementInsertCommand( adqlTree
                , undoManager
                , parent
                , childType
                , childElement ) ;
    }
    
   
    public StandardInsertCommand newStandardInsertCommand( AdqlNode parent
            										     , SchemaType childType
            											 , SchemaProperty childElement  ) {
        return new StandardInsertCommand( adqlTree, undoManager, parent, childType, childElement ) ;
    }
    
    
    public EditStore getEditStore() {
        return this.editStore ;
    }

    /**
     * @return Returns the undoManager.
     */
    public UndoManager getUndoManager() {
        return undoManager ;
    }
    
    
    public boolean retireOutstandingMultipleInsertCommands() {
        return undoManager.killMultipleInserts() ;
    }
    
    
    public class UndoManager extends javax.swing.undo.UndoManager {
        
        public AbstractCommand getCommandToBeRedone() {
            Object o = super.editToBeRedone() ;
            if( o instanceof MultipleColumnInsertCommand.MCIUndoManager ) {              
                return (AbstractCommand)((MultipleColumnInsertCommand.MCIUndoManager)o).getOwner() ;
            }
            return (AbstractCommand)o;
        }
        
        public AbstractCommand getCommandToBeUndone() {
            Object o = super.editToBeUndone() ;
            if( o instanceof MultipleColumnInsertCommand.MCIUndoManager ) {              
                return (AbstractCommand)((MultipleColumnInsertCommand.MCIUndoManager)o).getOwner() ;
            }
            return (AbstractCommand)o;
        }
        
        protected boolean killMultipleInserts() {
            boolean killed = false ;
            Enumeration e = edits.elements() ;  
            while( e.hasMoreElements() ) {
                Object o = e.nextElement() ;
                if( o instanceof MultipleColumnInsertCommand.MCIUndoManager ) {
                    ((MultipleColumnInsertCommand.MCIUndoManager)o).die() ;
                    killed = true ;
                }
            }
            return killed ;
        }
        
    } // end of class UndoManager
    
    public class EditStore {
         
        private Random random = new Random() ;
        private Hashtable tokenToEntryStore = new Hashtable() ;
        private Hashtable entryToTokenStore = new Hashtable() ;
        
        private Integer newToken() {
            Integer token ;
            do {
                token = new Integer( random.nextInt() ) ;
            } while ( tokenToEntryStore.containsKey( token ) ) ;
            return token ;
        }
        
        public synchronized Integer add( AdqlNode entry ) {
            Integer token = null ;
            if( entryToTokenStore.containsKey( entry ) ) {
                token = (Integer)entryToTokenStore.get( entry ) ;
            }
            else {
                token = newToken() ;
                tokenToEntryStore.put( token, entry ) ;
                entryToTokenStore.put( entry, token ) ;
            }           
            return token ;
        }
        
        public synchronized Integer exchange( AdqlNode in, AdqlNode out ) {
            Integer token = (Integer)entryToTokenStore.get( out ) ;
            if( token == null ) {
                token = add( in ) ;
            }
            else {
                entryToTokenStore.remove( out ) ;
                entryToTokenStore.put( in, token ) ;
                tokenToEntryStore.remove( token ) ;
                tokenToEntryStore.put( token, in ) ;
            }
            return token ;
        }
        
        public synchronized void exchange( Integer token, AdqlNode in ) {
            if( tokenToEntryStore.containsKey( token ) ) {
                Object out = tokenToEntryStore.get( token ) ;
                entryToTokenStore.remove( out ) ;
                entryToTokenStore.put( in, token ) ;
                tokenToEntryStore.remove( token ) ;
                tokenToEntryStore.put( token, in ) ;
            }
        }
        
        public AdqlNode get( Integer token ) {
            return (AdqlNode)tokenToEntryStore.get( token ) ;
        }
        
    } // end of class EditStore 
       
}
