/* Generated By: AdqlStoX.jjt,v 1.33.2.4 2006-12-19 14&JJTree: Do not edit this line. AST_InPredicate.java */

package org.astrogrid.adql;

import org.astrogrid.adql.v1_0.beans.InclusionSetType;
import org.astrogrid.adql.v1_0.beans.InclusiveSearchType;
import org.astrogrid.adql.v1_0.beans.ExclusiveSearchType;
import org.astrogrid.adql.v1_0.beans.ScalarExpressionType;
import org.astrogrid.adql.v1_0.beans.SelectType;
import org.astrogrid.adql.v1_0.beans.SearchType;
import org.apache.xmlbeans.XmlObject ;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

public class AST_InPredicate extends SimpleNode {
    
    private static Log log = LogFactory.getLog( AST_Having.class ) ;
 
    private boolean in = true ;

    public AST_InPredicate(AdqlStoX p, int id) {
        super(p, id);
    }

    public void setIn( boolean in ) {
        this.in = in ;
    }
    
    public void buildXmlTree( XmlObject xo ) {
        if( log.isTraceEnabled() ) enterTrace( log, "AST_InPredicate.buildXmlTree()" ) ;
        if( in ) {
            InclusiveSearchType iSearch = (InclusiveSearchType)xo.changeType( InclusiveSearchType.type ) ;
            children[0].buildXmlTree( iSearch.addNewExpression() ) ;
            children[1].buildXmlTree( iSearch.addNewSet() ) ;
            this.generatedObject = iSearch ;
        }
        else {
            ExclusiveSearchType xSearch = (ExclusiveSearchType)xo.changeType( ExclusiveSearchType.type ) ;
            children[0].buildXmlTree( xSearch.addNewExpression() ) ;
            children[1].buildXmlTree( xSearch.addNewSet() ) ;
            this.generatedObject = xSearch ;
        }
        super.buildXmlTree( (XmlObject)this.generatedObject ) ;
        if( log.isTraceEnabled() ) exitTrace( log, "AST_InPredicate.buildXmlTree()" ) ;
    }
  
}