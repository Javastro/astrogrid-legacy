/* Generated By: AdqlStoX.jjt,v 1.33.2.4 2006-12-19 14&JJTree: Do not edit this line. AST_SetFunctionSpecification.java */

package org.astrogrid.adql;

import org.astrogrid.adql.v1_0.beans.AggregateFunctionType ;
import org.astrogrid.adql.v1_0.beans.AggregateFunctionNameType ;
import org.astrogrid.adql.v1_0.beans.SelectionItemType ;
import org.astrogrid.adql.v1_0.beans.AllSelectionItemType; 
import org.apache.xmlbeans.XmlObject; 
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

public class AST_SetFunctionSpecification extends SimpleNode {
    
    private static Log log = LogFactory.getLog( AST_SetFunctionSpecification.class ) ;

    public AST_SetFunctionSpecification(AdqlStoX p, int id) {
        super(p, id);
    }

    public void buildXmlTree( XmlObject xo ) {
        if( log.isTraceEnabled() ) enterTrace( log, "AST_SetFunctionSpecification.buildXmlTree()" ) ; 
        if( jjtGetNumChildren() == 1 ) {
            children[0].buildXmlTree( xo ) ;
            setGeneratedObject( children[0].getGeneratedObject() ) ;
        }
        else {
            AggregateFunctionType afType = (AggregateFunctionType)xo.changeType( AggregateFunctionType.type ) ;
            afType.setName( AggregateFunctionNameType.COUNT ) ;
            SelectionItemType[] 
               argArray = new SelectionItemType[] { AllSelectionItemType.Factory.newInstance() } ;
            afType.setArgArray( argArray ) ;
            setGeneratedObject( afType ) ;
        } 
        super.buildXmlTree( (XmlObject)this.generatedObject ) ;
        if( log.isTraceEnabled() ) exitTrace( log, "AST_SetFunctionSpecification.buildXmlTree()" ) ; 
    }
}
