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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.adql.AdqlCompiler ;
import org.astrogrid.adql.metadata.MetadataQuery;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTransformer;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree.TableData;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;

import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.astrogrid.ColumnBean;

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
    private AdqlCompiler adqlCompiler ;
    private MetadataQuery metadataQuery ;
    private AdqlTransformer transformer ;

    /**
     * 
     */
    public CommandFactory( AdqlTree adqlTree ) {
        this.adqlTree = adqlTree ;
        this.undoManager = new UndoManager() ;
        this.undoManager.setLimit(1) ;
        this.editStore = new EditStore() ; 
        this.transformer = new AdqlTransformer() ;
    }
    
    private CommandFactory() {}
   
    
    public PasteOverCommand newPasteOverCommand( AdqlNode targetOfPasteOver, CopyHolder copy  ) {
        return new PasteOverCommand( adqlTree, undoManager, targetOfPasteOver, copy ) ;
    }
    
    public PasteIntoCommand newPasteIntoCommand( AdqlNode targetOfPasteInto, CopyHolder copy ) {
        //
        // The first part attempts to see whether the system clipboard contains an exact
        // replica of the local CopyHolder based clipboard. OK: this is a cludge to overcome
        // some of the difficulties of in-context menus. (I hope temporarilly).
        // If the two match exactly, then we go ahead with the local clipboard. 
        //
        if( isCopyHolderIdenticalToSystemClipboard( copy ) ) {
            //
            // If we get this far, then we know we can safely use the local copy...
            SchemaType pastableType = AdqlUtils.findSuitablePasteIntoTarget( targetOfPasteInto, copy.getSource() ) ;
            if( pastableType != null ) {
                return new PasteIntoCommand( adqlTree, undoManager, targetOfPasteInto, pastableType, copy ) ;
            }
        }
        return null ;
    }
    
    public PasteNextToCommand newPasteNextToCommand( AdqlNode targetOfNextTo, CopyHolder copy, boolean before ) {
        //
        // The first part attempts to see whether the system clipboard contains an exact
        // replica of the local CopyHolder based clipboard. OK: this is a cludge to overcome
        // some of the difficulties of in-context menus. (I hope temporarilly).
        // If the two match exactly, then we go ahead with the local clipboard. 
        //
        if( isCopyHolderIdenticalToSystemClipboard( copy ) ) {
            //
            // If we get this far, then we know we can safely use the local copy...
            if( AdqlUtils.isSuitablePasteOverTarget( targetOfNextTo, copy.getSource() ) ) {
                return new PasteNextToCommand( adqlTree, undoManager, targetOfNextTo, copy, before ) ;
            }
        }
        return null ;
    }
    
    public CutCommand newCutCommand( AdqlTree adqlTree, UndoManager undoManager, AdqlNode targetOfCut ) {
        return new CutCommand( adqlTree, undoManager, targetOfCut ) ;
    }
    
    public List<StandardInsertCommand> newInsertCommands( AdqlNode parent ) {
        List<StandardInsertCommand> insertCommands = null ;
        // Not sure whether this test is required:
        if( !parent.isHidingRequired() ) {
            // For each distinctive element we build a List
            // corresponding to each type that can be a basis
            // for that element...
            SchemaProperty[] elements = parent.getElementProperties() ;
            SchemaType[] concreteSubtypes ;
            SchemaType childType ;
          
            if( elements != null ) {
                insertCommands = new ArrayList<StandardInsertCommand>( elements.length ) ;
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
                        if( !AdqlUtils.isSupportedTypeWithinParent( concreteSubtypes[j], parent.getSchemaType() ) )
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
    
    public EditTupleTextCommand newEditTupleTextCommand( AdqlTree adqlTree, AdqlNode target, HashMap<String, TableData> fromTables ) {
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
//        return new EnumeratedElementInsertCommand( adqlTree
//                , undoManager
//                , parent
//                , childType
//                , childElement ) ;
        
          return new JoinInsertCommand( adqlTree, undoManager, parent, childType, childElement ) ;
    }
    
   
    public StandardInsertCommand newStandardInsertCommand( AdqlNode parent
            										     , SchemaType childType
            											 , SchemaProperty childElement  ) {
        return new StandardInsertCommand( adqlTree, undoManager, parent, childType, childElement ) ;
    }
    
    
    public EditStore getEditStore() {
        return this.editStore ;
    }
    
    private boolean isCopyHolderIdenticalToSystemClipboard( CopyHolder copy ) {
        //
        // The first part attempts to see whether the system clipboard contains an exact
        // replica of the local CopyHolder based clipboard. OK: this is a cludge to overcome
        // some of the difficulties of in-context menus. (I hope temporarilly).
        // If the two match exactly, then we use the local clipboard copy. This allows
        // us to grey out (or not) the paste action where the underlying type is incorrect.
        // The local clipboard contains enough info to determine type.
        // This is currently impossible to do from the system clipboard because we have no
        // way of testing ADQL type from text present in the system clipboard.
        //
        boolean retValue = false ;
        XmlCursor nodeCursor = null ;
        try {
            XmlObject userObject = copy.getSource() ;       
            userObject = AdqlUtils.modifyQuotedIdentifiers( userObject ) ;
            nodeCursor = userObject.newCursor();
            String text = nodeCursor.xmlText();                    
            userObject = AdqlUtils.unModifyQuotedIdentifiers( userObject ) ;
            String adqls = transformer.transformToAdqls( text, " " ) ; 
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable t = clipboard.getContents( this ) ;
            String contents = ((String)t.getTransferData( DataFlavor.stringFlavor )).trim() ;
            if( contents.equalsIgnoreCase( adqls ) ) {
                retValue = true ;
            }
        } 
        catch ( Exception ex ) {
            ;
        }
        finally {
            if( nodeCursor != null )
                nodeCursor.dispose() ;
        }       
        return retValue ;
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
                return ((MultipleColumnInsertCommand.MCIUndoManager)o).getOwner() ;
            }
            else if( o instanceof JoinInsertCommand.InternalUndoManager ) {
                return ((JoinInsertCommand.InternalUndoManager)o).getOwner() ;
            }
            return (AbstractCommand)o;
        }
        
        public AbstractCommand getCommandToBeUndone() {
            Object o = super.editToBeUndone() ;
            if( o instanceof MultipleColumnInsertCommand.MCIUndoManager ) {              
                return ((MultipleColumnInsertCommand.MCIUndoManager)o).getOwner() ;
            }
            else if( o instanceof JoinInsertCommand.InternalUndoManager ) {
                return ((JoinInsertCommand.InternalUndoManager)o).getOwner() ;
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
        private LinkedHashMap<Integer, AdqlNode> tokenToEntryStore = new LinkedHashMap<Integer, AdqlNode>( 64 ) ;
        private LinkedHashMap<AdqlNode, Integer> entryToTokenStore = new LinkedHashMap<AdqlNode, Integer>( 64 ) ;
        
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
                token = entryToTokenStore.get( entry ) ;
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
            Integer token = entryToTokenStore.get( out ) ;
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
            return tokenToEntryStore.get( token ) ;
        }
        
        public Integer get( AdqlNode node ) {
            return entryToTokenStore.get( node ) ;
        }
        
        protected void logPrintOfEditStore() {
            StringBuffer buffer = new StringBuffer( 512 ) ;
            buffer.append( "\ntokenToEntryStore:" ) ;
            buffer.append( "\nsize: " ).append( tokenToEntryStore.size() ) ;
            Iterator<Integer> it = tokenToEntryStore.keySet().iterator() ;
            while( it.hasNext() ) {
                Integer token = it.next() ;
                buffer
                    .append( "\ntoken: [" )
                    .append( token )
                    .append( "]  Adql did: [")
                    .append( tokenToEntryStore.get( token ).getDid() )
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
            metadataQuery = new MetadataQueryImpl() ;
            adqlCompiler.setMetadataQuery( metadataQuery ) ;
        }
        else {
            adqlCompiler.ReInit( new StringReader( source ) ) ;
        }
        
        return adqlCompiler;
    }
    
    protected class MetadataQueryImpl implements MetadataQuery {

        /* (non-Javadoc)
         * @see org.astrogrid.adql.metadata.MetadataQuery#isColumn(java.lang.String, java.lang.String)
         */
        public boolean isColumn( String tableName, String columnName ) {
            TableBean table = adqlTree.findTableBean( tableName ) ;
            if( table == null )
                return false ;           
            return getColumn( table, columnName ) != null ;
        }

        /* (non-Javadoc)
         * @see org.astrogrid.adql.metadata.MetadataQuery#isTable(java.lang.String)
         */
        public boolean isTable( String tableName ) {
            return adqlTree.findTableBean( tableName ) != null ;
        }
        
        private ColumnBean getColumn( TableBean table, String columnName ) {
            ColumnBean[] columns = table.getColumns() ;
            for( int i=0; i<columns.length; i++ ) {
                if( columns[i].getName().equalsIgnoreCase( columnName ) ) {
                    return columns[i] ; 
                }
            }
            return null ;
        }
        
    }
       
}
