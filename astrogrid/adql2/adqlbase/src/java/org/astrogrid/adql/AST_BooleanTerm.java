/* Generated By: AdqlStoX.jjt,v 1.33.2.4 2006-12-19 14&JJTree: Do not edit this line. AST_BooleanTerm.java */

package org.astrogrid.adql;

import org.astrogrid.adql.beans.IntersectionSearchType ;
import org.apache.xmlbeans.XmlObject ;
import org.apache.xmlbeans.SchemaType;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;


public class AST_BooleanTerm extends SimpleNode {
    
    private static Log log = LogFactory.getLog( AST_BooleanTerm.class ) ;
    
    private boolean bAnd = false ;
 
    public AST_BooleanTerm(AdqlStoX p, int id) {
        super(p, id);
    }
    
    public void setAnd() {
        bAnd = true ;
        pushPosition( AdqlParser.CONDITION_ELEMENT, IntersectionSearchType.type ) ;
//        Tracker t = getTracker() ;
//        Tracker.Part p = t.peek() ;
//        //
//        // Adjust the child count. 
//        // As an AND, it already has one child...
//        p.setChildCount( 1 ) ;
//        //
//        // Keep the current element but change its type to AND...
//        t.setType( IntersectionSearchType.type ) ;
    }
    
    public boolean isAnd() {
        return bAnd ;
    }
    
    public void buildXmlTree( XmlObject xo ) {
        if( log.isTraceEnabled() ) enterTrace( log, "AST_BooleanTerm.buildXmlTree()" ) ; 
        if( !bAnd ) {
            children[0].buildXmlTree( xo ) ;
            this.generatedObject = children[0].getGeneratedObject() ;
        }
        else {
            IntersectionSearchType andConstruct = (IntersectionSearchType)xo.changeType( IntersectionSearchType.type) ;
            children[0].buildXmlTree( andConstruct.addNewCondition() ) ;
            children[1].buildXmlTree( andConstruct.addNewCondition() ) ;      
            this.generatedObject = andConstruct ;
            if( log.isDebugEnabled() ) {
                log.debug( "AND written." ) ;
            }
        }
        super.buildXmlTree(xo) ;
        if( log.isTraceEnabled() ) exitTrace( log, "AST_BooleanTerm.buildXmlTree()" ) ; 
    }
    
}