/* EditCommand.java
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
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.adql.AdqlCompiler;
import org.astrogrid.adql.AdqlException;
import org.astrogrid.adql.SimpleNode;
import org.astrogrid.adql.v1_0.beans.SelectDocument;
import org.astrogrid.adql.v1_0.beans.SelectType;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
/**
 * @author jl99@star.le.ac.uk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EditCommand extends AbstractCommand {
    
    private static final Log log = LogFactory.getLog( EditCommand.class ) ;
    private XmlObject newValue ;
    private CopyHolder preserved ;
    private AdqlCompiler compiler ;

    /**
     * @param target
     * @param source
     */
    public EditCommand( AdqlTree adqlTree, UndoManager undoManager, AdqlNode target, AdqlCompiler compiler ) {
        super( adqlTree, undoManager, target ) ;
        preserved = CopyHolder.holderForEditPurposes( target ) ;
        this.compiler = compiler ;
    }
        
    @Override
    public Result execute() {   
        if( log.isTraceEnabled() ) log.trace( "EditCommand.execute() entry" ) ;
        Result result = CommandExec.FAILED ;
        try {  
            String elementContextPath = getFromEditStore( childToken ).getElementContextPath() ;
            log.debug( "child.getElementContextPath(): " + elementContextPath ) ;
            XmlObject pxo = this.getParentObject() ;
//            elementContextPath.equalsIgnoreCase( "/Select[@type='selectType']"
            if( pxo.schemaType() == SelectDocument.type ) {
                SelectType sto = (SelectType)getChildObject() ;
                //
                // We preserve a start comment to prevent the absence of
                // a comment removing an existing comment...
                String preservedComment = null ;
                if( sto.isSetStartComment() ) {
                    preservedComment = sto.getStartComment() ;
                }
                String[] comments = new String[2] ;
                newValue = compiler.execFragment( elementContextPath, comments ) ; 
                sto = (SelectType)newValue ;
                adqlTree.getNodeFactory().setForcedNotToUseModel( true ) ;
                //
                // If there is a genuine start comment,
                // We replace it in the query
                // Otherwise we use the preserved comment if there is one...
                if( comments[0] != null ) {
                    if( comments[0].length() > 0 ) {
                        sto.setStartComment( SimpleNode.prepareComment( comments[0] ) ) ;
                    }
                    else if( preservedComment != null ) {
                        sto.setStartComment( preservedComment ) ;
                    }                   
                }
                else if( preservedComment != null ) {
                    sto.setStartComment( preservedComment ) ;
                }   
                exchangeInEditStore( childToken, getParentEntry().replace( this, newValue ) ) ; 
            }
            else {
                newValue = compiler.compileFragmentToXmlBean( elementContextPath ) ;
                adqlTree.getNodeFactory().setForcedNotToUseModel( true ) ;
                exchangeInEditStore( childToken, getParentEntry().replace( this, newValue ) ) ;
            }
            adqlTree.getNodeFactory().setForcedNotToUseModel( false ) ;
            
            //
            // this requires adjusting...
//            ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( getChildEntry() ) ;
//            preserved.openBranchesOn( getChildEntry() ) ;
            
            result = CommandExec.OK ;
            undoManager.addEdit( this ) ;
        }
        catch( AdqlException adqlex ) {
            String[] messages = adqlex.getMessages() ;
            if( messages != null && messages.length > 0) {
                this.setMessages( messages ) ;
            }
            else {
                this.setMessages ( new String[] { "Internal compiler error. See log."} ) ;
                log.debug( "Internal compiler error. See log.", adqlex ) ;
            }              
        }
        catch( Exception ex ) {
            this.setMessages ( new String[] { "Internal compiler error. See log."} ) ;
            log.debug( "Internal compiler error. See log.", ex ) ;
            if( log.isDebugEnabled() ) {
                log.debug(  this.toString() ) ;
            }
        }
        finally {
            if( log.isTraceEnabled() ) log.trace( "EditCommand.execute() exit" ) ;
        }
        return result ;
    }
   
    private Result _execute( XmlObject srcValue ) {
        Result result = CommandExec.OK ;
        try {    
            adqlTree.getNodeFactory().setForcedNotToUseModel( true ) ;
            exchangeInEditStore( childToken, getParentEntry().replace( this, srcValue ) ) ;
            adqlTree.getNodeFactory().setForcedNotToUseModel( false ) ;
//            ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( getChildEntry() ) ;
//            preserved.openBranchesOn( getChildEntry() ) ;
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.error( "EditCommand._execute() failed: ", exception ) ;
        }
        return result ;
    }
     
    @Override
    public void die() {
        super.die();
    }
    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        if( _execute( newValue ) == CommandExec.FAILED ) {
            if( log.isDebugEnabled() ) {
                log.debug(  this.toString() ) ;
            }
            throw new CannotRedoException() ;
        }
            
    }
    
    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        if( _execute( preserved.getSource() ) == CommandExec.FAILED ) {
            if( log.isDebugEnabled() ) {
                log.debug(  this.toString() ) ;
            }
            throw new CannotUndoException() ;
        }
    }
    
    @Override
    public String getPresentationName() {
        return "Edit" ;
    }
    
    public void adjustBranches() {
        preserved.openBranchesOn( getParentEntry() ) ;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer( 512 ) ;
        buffer.append( "\nEditCommand" ) ;
        buffer.append( super.toString() ) ;
        if( preserved == null ) {
            buffer.append( "\npreserved: null" ) ;
        }
        else {
            buffer.append( "\npreserved: " ).append( preserved.toString() ) ;
        }
        if( newValue == null ) {
            buffer.append( "\nnewValue: null" ) ;
        }
        else {
            try {
                buffer.append( "\nnewValue: \n" ).append( newValue.toString() ) ;
            }
            catch( Throwable th ) {
                buffer
                    .append( "\nnewValue toString() produced exception: " )
                    .append(  th.getClass() ) ;
            }
        }
        return buffer.toString() ; 
    }
   
}