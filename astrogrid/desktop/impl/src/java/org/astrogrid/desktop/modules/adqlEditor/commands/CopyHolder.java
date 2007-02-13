/*$Id: CopyHolder.java,v 1.2 2007/02/13 09:55:47 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.adqlEditor.commands;

import org.astrogrid.desktop.modules.adqlEditor.nodes.*;
import org.apache.xmlbeans.XmlObject;
import java.util.Enumeration;
import java.util.HashMap ;
import javax.swing.tree.TreePath ;

/**
 * CopyHolder
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Dec 25, 2006
 */
public class CopyHolder {
    
    public static CopyHolder holderForCopyPurposes( AdqlNode target ) {
        return new CopyHolder( target, false ) ;
    }
    
    public static CopyHolder holderForEditPurposes( AdqlNode target ) {
        return new CopyHolder( target, true ) ;
    }
    
    private class Position {
        
        String level ;
        int displacement ;
        String relativeContextPath ;
        
        Position( String level, int displacement ) {
            this.level = level ;
            this.displacement = displacement ;
        }
         
        public boolean equals( Object obj ) {
            if( obj instanceof Position == false )
                return false ;
            return toString().equals( obj ) ;
        }

        public int hashCode() {
           return toString().hashCode() ;
        }

        public String toString() {
           return level + ':' + displacement ;
        }
 
        public Position nextLevel() {
            return new Position( level + '.' + displacement, 0 ) ;
        }
        
        public Position increment() {
            return new Position( level, displacement+1 ) ;
        }

        public String getRelativeContextPath() {
            return relativeContextPath;
        }

        public void setRelativeContextPath(String relativeContextPath) {
            this.relativeContextPath = relativeContextPath;
        }
      
    }
    
    private XmlObject source ;
    private HashMap openBranchesByPosition ;
    private HashMap openBranchesByContextPath ;
    private boolean forEditPurposes ;
    
    private CopyHolder() {}
    
    private CopyHolder( AdqlNode target, boolean forEditPurposes ) {       
        this.source = target.getXmlObject().copy() ;
        this.forEditPurposes = forEditPurposes ;
        tallyOpenBranches( new Position( "0", 0 ), target, target ) ;
    }
    
    private void tallyOpenBranches( Position position, AdqlNode ancestor, AdqlNode target ) {
        if( target.isExpanded() ) {
            position.setRelativeContextPath( target.getRelativeElementContextPath( ancestor ) ) ;
            put( position ) ;
        }
        if( target.getChildCount() > 0 ) {
            Position next = position.nextLevel() ;
            Enumeration e = target.children() ;
            while( e.hasMoreElements() ) {
                tallyOpenBranches( next, ancestor, (AdqlNode)e.nextElement() ) ;
                next = next.increment() ;
            }
        }
    }
    
    private void put( Position position ) {
        if( openBranchesByPosition == null ) {
            openBranchesByPosition = new HashMap() ;
        }
        openBranchesByPosition.put( position.toString(), position ) ;
        if( forEditPurposes ) {
            if( openBranchesByContextPath == null ) {
                openBranchesByContextPath = new HashMap() ;
            }
            openBranchesByContextPath.put( position.getRelativeContextPath(), position ) ;
        }
    }
    
    private boolean isOpen( Position position ) {
        if( openBranchesByPosition != null ) {
            Position found = (Position)openBranchesByPosition.get( position.toString() ) ;
            if( found != null ) {
                if( position.getRelativeContextPath().equals( found.getRelativeContextPath() ) ) {
                    return true ;
                }
            }
        }
        if( openBranchesByContextPath != null ) {
            Position found = (Position)openBranchesByContextPath.get( position.getRelativeContextPath() ) ;
            if( found != null ) {
                return true ;
            }
        }
        return false ;
    }
    
    public HashMap getOpenBranchesByContextPath() {
        return openBranchesByContextPath;
    }

    public HashMap getOpenBranchesByPosition() {
        return openBranchesByPosition;
    }

    public XmlObject getSource() {
        return source;
    }
    
    public boolean isForEditPurposes() {
        return forEditPurposes ;
    }
    
    public void openBranchesOn( AdqlNode node ) {
        openBranches( new Position( "0", 0 ), node, node ) ;
    }
    
    private void openBranches( Position position
                             , AdqlNode ancestor
                             , AdqlNode target ) {
        String contextPath = target.getRelativeElementContextPath( ancestor ) ;
        position.setRelativeContextPath( contextPath ) ;
        if( this.isOpen(position) ) {
            TreePath fullPath = new TreePath( target.getPath() ) ;
            target.getNodeFactory().getAdqlTree().expandPath( fullPath ) ;
        }
        if( target.getChildCount() > 0 ) {
            Position next = position.nextLevel() ;
            Enumeration e = target.children() ;
            while( e.hasMoreElements() ) {
                openBranches( next, ancestor, (AdqlNode)e.nextElement() ) ;
                next = next.increment() ;
            }
        }
    }
    


}

/*
$Log: CopyHolder.java,v $
Revision 1.2  2007/02/13 09:55:47  jl99
Merge of branch workbench-jl-2032-a

Revision 1.1.2.1  2007/01/10 14:42:13  jl99
First commit after cvs recovery.

*/