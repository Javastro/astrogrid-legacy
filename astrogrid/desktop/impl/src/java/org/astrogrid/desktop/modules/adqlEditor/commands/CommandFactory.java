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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.astrogrid.adql.AdqlStoX;
import org.astrogrid.adql.AdqlCompiler ;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;

/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CommandFactory {
    
    private static final Log log = LogFactory.getLog( CommandFactory.class ) ;
    
    private AdqlTree adqlTree ;
    private UndoManager undoManager ;
    private EditStore editStore ;
    //private AdqlStoX adqlCompiler ;
    private AdqlCompiler adqlCompiler ;

    /**
     * 
     */
    public CommandFactory( AdqlTree adqlTree ) {
        this.adqlTree = adqlTree ;
        this.undoManager = new UndoManager() ;
        this.editStore = new EditStore() ;        
    }
    
    private CommandFactory() {}
   
    
    public PasteOverCommand newPasteOverCommand( AdqlNode targetOfPasteOver, CopyHolder copy  ) {
        if( AdqlUtils.isSuitablePasteOverTarget( targetOfPasteOver, copy.getSource() ) ) {
            return new PasteOverCommand( adqlTree, undoManager, targetOfPasteOver, copy ) ;
        }
        return null ;
    }
    
    public PasteIntoCommand newPasteIntoCommand( AdqlNode targetOfPasteInto, CopyHolder copy ) {
        SchemaType pastableType = AdqlUtils.findSuitablePasteIntoTarget( targetOfPasteInto, copy.getSource() ) ;
        if( pastableType != null ) {
            return new PasteIntoCommand( adqlTree, undoManager, targetOfPasteInto, pastableType, copy ) ;
        }
        return null ;
    }
    
    public PasteNextToCommand newPasteNextToCommand( AdqlNode targetOfNextTo, CopyHolder copy, boolean before ) {
        if( AdqlUtils.isSuitablePasteOverTarget( targetOfNextTo, copy.getSource() ) ) {
            return new PasteNextToCommand( adqlTree, undoManager, targetOfNextTo, copy, before ) ;
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
    
    public EditCommand newEditCommand( AdqlTree adqlTree, AdqlNode target, String source ) {
//        source = source.trim() ;
//        if( source.length() == 0 || source.charAt( source.length()-1 ) != ';' ) {
//            source += ';' ;
//        }
        return new EditCommand( adqlTree, undoManager, target, getAdqlCompiler( source ) ) ;
    }
    
    public EditEnumeratedAttributeCommand newEditEnumeratedAttributeCommand( AdqlTree adqlTree, AdqlNode target ) {
        return new EditEnumeratedAttributeCommand( adqlTree, undoManager, target ) ;
    }
   
    public EditEnumeratedElementCommand newEditEnumeratedElementCommand( AdqlTree adqlTree, AdqlNode target ) {
        return new EditEnumeratedElementCommand( adqlTree, undoManager, target ) ;
    }
    
    public EditSingletonTextCommand newEditSingletonTextCommand( AdqlTree adqlTree, AdqlNode target ) {
        return new EditSingletonTextCommand( adqlTree, undoManager, target ) ;
    }
    
    public EditTupleTextCommand newEditTupleTextCommand( AdqlTree adqlTree, AdqlNode target, HashMap fromTables ) {
        return new EditTupleTextCommand( adqlTree, undoManager, target, fromTables ) ;
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
        
        protected void logPrintOfUndoableEdits( AbstractCommand command ) {
            StringBuffer buffer = new StringBuffer( 512 ) ;
            buffer.append( "Log Print of Undoable Edits follows..." ) ;
            Enumeration en = this.edits.elements() ;
            AbstractCommand ac = null ;
            int i = 0 ;
            while( en.hasMoreElements() ) {
                ac = (AbstractCommand)en.nextElement() ;
                if( command != null ) {
                    if( ac == command ) {
                        buffer.append( "Relevant command follows..." ) ;
                    }
                }
                buffer.append( "Edit at index " + i ) ;
                buffer.append( ac.toString() ) ;
                i++ ;
            }
            
            editStore.logPrintOfEditStore() ;
        }
        
    } // end of class UndoManager
    
    public class EditStore {
         
        private Random random = new Random() ;
//        private Hashtable tokenToEntryStore = new Hashtable( 64 ) ;
//        private Hashtable entryToTokenStore = new Hashtable( 64 ) ;
        
        private LinkedHashMap tokenToEntryStore = new LinkedHashMap( 64 ) ;
        private LinkedHashMap entryToTokenStore = new LinkedHashMap( 64 ) ;
        
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
            if( log.isDebugEnabled() ) log.debug("EditStore.add():\n" +
                                          "   tokenToEntryStore.size(): " + tokenToEntryStore.size() +
                                          "   entryToTokenStore.size(): " + entryToTokenStore.size() ) ;
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
            if( log.isDebugEnabled() ) log.debug("EditStore.exchange(AdqlNode,AdqlNode):\n" +
                                          "   tokenToEntryStore.size(): " + tokenToEntryStore.size() +
                                          "   entryToTokenStore.size(): " + entryToTokenStore.size() ) ;
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
            else {
                log.warn( "token not found in EditStore: " + token ) ;
            }
            if( log.isDebugEnabled() ) log.debug("EditStore.exchange(Integer,AdqlNode):\n" +
                                          "   tokenToEntryStore.size(): " + tokenToEntryStore.size() +
                                          "   entryToTokenStore.size(): " + entryToTokenStore.size() ) ;
        }
        
        public AdqlNode get( Integer token ) {
            return (AdqlNode)tokenToEntryStore.get( token ) ;
        }
        
        public Integer get( AdqlNode node ) {
            return (Integer)entryToTokenStore.get( node ) ;
        }
        
        protected void logPrintOfEditStore() {
            StringBuffer buffer = new StringBuffer( 512 ) ;
            buffer.append( "\ntokenToEntryStore:" ) ;
            buffer.append( "\nsize: " ).append( tokenToEntryStore.size() ) ;
            Iterator it = tokenToEntryStore.keySet().iterator() ;
            while( it.hasNext() ) {
                Integer token = (Integer)it.next() ;
                buffer
                    .append( "\ntoken: [" )
                    .append( token )
                    .append( "]  Adql did: [")
                    .append( ((AdqlNode)tokenToEntryStore.get( token )).getDid() )
                    .append( "]" ) ;
            }
        }
        
    } // end of class EditStore 

    /**
     * @return the adqlCompiler
     */
    public AdqlCompiler getAdqlCompiler( String source ) {
        if( adqlCompiler == null ) {
            adqlCompiler = new AdqlCompiler( new StringReader( source ) ) ;
        }
        else {
            adqlCompiler.ReInit( new StringReader( source ) ) ;
        }
        return adqlCompiler;
    }
       
}
