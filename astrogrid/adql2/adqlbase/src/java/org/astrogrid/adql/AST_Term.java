/* Generated By: AdqlStoX.jjt,v 1.33.2.4 2006-12-19 14&JJTree: Do not edit this line. AST_Term.java */

package org.astrogrid.adql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.adql.beans.BinaryExprType;
import org.astrogrid.adql.beans.BinaryOperatorType;
import org.astrogrid.adql.beans.UnaryExprType;

public class AST_Term extends SimpleNode {
    
    private static Log log = LogFactory.getLog( AST_Term.class ) ;
    
//    String sBinaryOp = null ; 
    Token binaryOpToken = null ;

    public AST_Term(AdqlStoX p, int id) {
        super(p, id);
    }
 
//    public void setMultiply( String elementName ) {
//        this.sBinaryOp = "*" ;
//        pushPosition( elementName, BinaryExprType.type ) ;
//    }
//    
//    public void setDivide( String elementName ) {
//        this.sBinaryOp = "/" ;
//        pushPosition( elementName, BinaryExprType.type ) ;
//    }
//    
    public void setOperator( Token binaryOpToken ) {       
        this.binaryOpToken = binaryOpToken ;      
        //
        // Adjust the child count. 
        // As a binary operation, it already has one child...
        Tracker t = getTracker() ;
//        Tracker.Part p = t.peek() ;
//        p.setChildCount( 1 ) ;  
        //
        // Keep the current element but change its type to operator...
        t.setType( BinaryExprType.type ) ;
    }
    
    public boolean isSetOperator() {
        return binaryOpToken != null ;
    }

    public void buildXmlTree( XmlObject xo ) {
        if( log.isTraceEnabled() ) enterTrace( log, "AST_Term.buildXmlTree()" ) ; 
        int childCount = jjtGetNumChildren() ;
        StringBuffer buffer = null ;
        if( log.isDebugEnabled() ) {
            buffer = new StringBuffer() ;
            buffer
                .append( "childCount: ") 
                .append( childCount ) ;         
        }
        if( isSetOperator() == false ) {
            children[0].buildXmlTree( xo ) ;
            this.generatedObject = children[0].getGeneratedObject() ;
            if( log.isDebugEnabled() ) {
                buffer
                    .append( "\n setOperator() is false. " )
                    .append( "\n generatedObject set to one of " + this.generatedObject.getClass() )
                    .append( "\n generatedObjec.toString(): " + this.generatedObject.toString() );
            }
        }
        else {
            BinaryExprType beType = (BinaryExprType)xo.changeType( BinaryExprType.type ) ;
            beType.setOper( BinaryOperatorType.Enum.forString( this.binaryOpToken.image ) ) ;
            if( log.isDebugEnabled() ) {
                buffer
                    .append( "\nbinaryOpToken: " )
                    .append( this.binaryOpToken.image ) ;
            }
            for( int i=0; i<childCount; i++ ) {
                children[i].buildXmlTree( beType.addNewArg() ) ;
                if( log.isDebugEnabled() ) {
                    buffer
                        .append( "\narg" ) 
                        .append( i )
                        .append( ": \n" )
                        .append( beType.getArgArray(i) ) ;
                }
            }
            setGeneratedObject( beType ) ;
        }
        
        if( log.isDebugEnabled() ) {
            log.debug( buffer.toString() ) ;
        }
        super.buildXmlTree( (XmlObject)this.generatedObject ) ;
        if( log.isTraceEnabled() ) exitTrace( log, "AST_Term.buildXmlTree()" ) ; 
    }

}