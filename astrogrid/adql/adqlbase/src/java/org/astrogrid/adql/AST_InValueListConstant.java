/* Generated By: AdqlStoX.jjt,v 1.33.2.4 2006-12-19 14&JJTree: Do not edit this line. AST_InValueListConstant.java */

package org.astrogrid.adql;

import org.apache.xmlbeans.XmlObject ;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory ;
import org.astrogrid.adql.v1_0.beans.AtomType ;
import org.astrogrid.adql.v1_0.beans.LiteralType;

public class AST_InValueListConstant extends SimpleNode {
    
    private static Log log = LogFactory.getLog( AST_InValueListConstant.class ) ;
 
    public AST_InValueListConstant(AdqlStoX p, int id) {
        super(p, id);
    }

    public void buildXmlTree( XmlObject xo ) {
        if( log.isTraceEnabled() ) enterTrace( log, "AST_InValueListConstant.buildXmlTree()" ) ;    
        
        AtomType at = AtomType.Factory.newInstance() ;
        children[0].buildXmlTree( at ) ;       
        LiteralType literal = ((AtomType)(children[0].getGeneratedObject())).getLiteral() ;
        xo = xo.set( literal ) ;      
        setGeneratedObject( xo ) ;
        
        super.buildXmlTree( (XmlObject)this.generatedObject ) ;
        if( log.isTraceEnabled() ) exitTrace( log, "AST_InValueListConstant.buildXmlTree()" ) ; 
    }

}