/* Generated By: AdqlStoX.jjt,v 1.34.2.17 2007/04/24 14&JJTree: Do not edit this line. AST_TableName.java */

package org.astrogrid.adql;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

public class AST_TableName extends SimpleNode {

    private static Log log = LogFactory.getLog( AST_TableName.class ) ;

    public AST_TableName(AdqlStoX p, int id) {
        super(p, id);
    }

    public void jjtClose() { 
        if( log.isTraceEnabled() ) enterTrace( log, "AST_TableName.jjtClose()" ) ;
        if( jjtGetNumChildren() == 1 ) {
            setGeneratedObject( children[0].getGeneratedObject() ) ;
        }
        if( log.isTraceEnabled() ) exitTrace( log, "AST_TableName.jjtClose()" ) ; 
    }

}