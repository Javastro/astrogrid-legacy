/* Generated By: AdqlStoX.jjt,v 1.33.2.4 2006-12-19 14&JJTree: Do not edit this line. AST_BooleanTerm.java */

package org.astrogrid.adql;

import org.astrogrid.adql.v1_0.beans.SearchType ;
import org.astrogrid.adql.v1_0.beans.IntersectionSearchType ;
import org.apache.xmlbeans.XmlObject ;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;


public class AST_BooleanTerm extends SimpleNode {
    
    private static Log log = LogFactory.getLog( AST_BooleanTerm.class ) ;
 
    public AST_BooleanTerm(AdqlStoX p, int id) {
        super(p, id);
    }

    public void jjtClose() {
//        if( jjtGetNumChildren() == 1 ) {
//            setGeneratedObject( children[0].getGeneratedObject() ) ;
//        }
//        else {
//            SearchType[] conditionArray = new SearchType[2] ;
//            IntersectionSearchType andConstruct = IntersectionSearchType.Factory.newInstance() ;
//            conditionArray[0] = (SearchType)children[0].getGeneratedObject() ;
//            conditionArray[1] = (SearchType)children[1].getGeneratedObject() ;
//            andConstruct.setConditionArray( conditionArray ) ;
//            conditionArray = andConstruct.getConditionArray() ;
//            children[0].exchangeGeneratedObject( conditionArray[0] ) ;
//            children[1].exchangeGeneratedObject( conditionArray[1] ) ;
//            setGeneratedObject( andConstruct ) ;
//            if( log.isDebugEnabled() ) {
//                log.debug( "AND written." ) ;
//            }
//        }
//        
    }
    
    public void buildXmlTree( XmlObject xo ) {
        if( log.isTraceEnabled() ) enterTrace( log, "AST_BooleanTerm.buildXmlTree()" ) ; 
        if( jjtGetNumChildren() == 1 ) {
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
