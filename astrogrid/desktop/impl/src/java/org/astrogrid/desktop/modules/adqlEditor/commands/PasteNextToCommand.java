/* PasteNextToCommand.java
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
import org.astrogrid.desktop.modules.adqlEditor.AdqlEntry;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;

/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PasteNextToCommand extends AbstractCommand {
    
    private static final Log log = LogFactory.getLog( PasteNextToCommand.class ) ;
    private static final boolean DEBUG_ENABLED = false ;
    private static final boolean TRACE_ENABLED = false ;
    
    private XmlObject sourceValue ;
    private boolean before ;
    private AdqlEntry newInstance ;
    private Integer newInstanceToken ;
    
    /**
     * @param target
     * @param source
     */
    public PasteNextToCommand( AdqlTree adqlTree
                             , UndoManager undoManager
                             , AdqlEntry targetOfNextTo
                             , XmlObject source
                             , boolean before ) {
        super( adqlTree, undoManager, targetOfNextTo ) ;
        this.sourceValue = source ;
        this.before = before ;
    }
    
    public boolean isBefore() {
        return before ;
    }
    
    
    public Result execute() {      
        Result result = _execute() ;
        if( result != CommandExec.FAILED ) {
            newInstanceToken = addToEditStore( newInstance ) ;
            undoManager.addEdit( this ) ;
        }          
        return result ;
    }
   
    private Result _execute() {
        Result result = CommandExec.OK ;
        parent = getFromEditStore( parentToken ) ;
        try {          
            //
	        // ASSUMPTION. The parent is composed solely of an array of elements/types!	        	             
	        if( isChildHeldInArray() ) {   
	            // First identify where in the array the selected entry is....
	            Object selectedObject = child.getXmlObject() ;
	            String childElementName = getChildElementName() ;
	            Object[] objects = AdqlUtils.getArray( parent.getXmlObject(), childElementName ) ;
	            int offset = -1 ;
	            for( int i=0; i<objects.length; i++ ) {
	                if( selectedObject == objects[i] ) {
	                    offset = i ;
	                    break ;
	                }
	            }
	            if( offset != -1 ) {   
	                XmlObject newObject = null ;	
	                if( before ) {
	                    newObject = AdqlUtils.insertNewInArray( parent.getXmlObject(), childElementName, offset ) ;
	                }
	                // If not before, everything else must be after...
	                else {
	                    if( objects.length == offset+1 ) {          
	                        // Simply add to the end of the array...
	                        newObject = AdqlUtils.addNewToEndOfArray( parent.getXmlObject(), childElementName ) ;
	                    }
	                    else {
	                        newObject = AdqlUtils.insertNewInArray( parent.getXmlObject(), childElementName, offset+1 ) ;
	                    }
	                    result = CommandExec.OK ;
	                } 
                    newInstance = AdqlEntry.newInstance( parent, newObject.set( sourceValue ) ) ;
	            }
	            else {
	                result = CommandExec.ERROR ;
	                log.error( "Serious error in array structure with Paste before/after!" ) ;
	            }
            }
	        else {
	            result = CommandExec.ERROR ;
	            log.error( "Paste before/after invoked on an element whose parent is NOT COMPOSED solely of an array of elements/types!" ) ;
	        }	        
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
            log.error( exception ) ;
        }
        return result ;
    }
    
    private Result _unexecute() {
        Result result = CommandExec.OK ;
        try {
            parent = getFromEditStore( parentToken ) ;
            newInstance = getFromEditStore( newInstanceToken ) ;
	        AdqlEntry.removeInstance( parent, newInstance ) ;
        }
        catch( Exception exception ) {
            result = CommandExec.FAILED ;
        }
        return result ;
    }
    
    
    public void die() {
        super.die();
    }
    public void redo() throws CannotRedoException {
        super.redo();
        if( _execute() == CommandExec.FAILED ) {
            throw new CannotRedoException() ;
        }
        exchangeInEditStore( newInstanceToken, newInstance ) ;   
    }
    
    public void undo() throws CannotUndoException {
        super.undo();
        if( _unexecute() == CommandExec.FAILED ) {
            throw new CannotUndoException() ;
        }
    }
     
    public String getPresentationName() {
        return "Paste " + ( before == true ? "before" : "after" ) ;
    }
   
}