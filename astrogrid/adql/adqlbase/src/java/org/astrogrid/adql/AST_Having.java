/* Generated By: AdqlStoX.jjt,v 1.33.2.4 2006-12-19 14&JJTree: Do not edit this line. AST_Having.java */

package org.astrogrid.adql;

import org.astrogrid.adql.v1_0.beans.HavingType;
import org.astrogrid.adql.v1_0.beans.SearchType;
import org.apache.xmlbeans.XmlObject ;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

public class AST_Having extends SimpleNode {
    
    private static Log log = LogFactory.getLog( AST_Having.class ) ;
    
    public AST_Having(AdqlStoX p, int id) {
        super(p, id);
    }
    
    public void buildXmlTree( XmlObject xo ) {
        if( log.isTraceEnabled() ) enterTrace( log, "AST_Having.buildXmlTree()" ) ;
        HavingType havingType = (HavingType)xo.changeType( HavingType.type ) ;
        this.generatedObject = havingType ;
        children[0].buildXmlTree( havingType.addNewCondition() ) ;
        super.buildXmlTree( havingType ) ;
        if( log.isTraceEnabled() ) exitTrace( log, "AST_Having.buildXmlTree()" ) ;
    }

}
