/* Generated By: AdqlStoX.jjt,v 1.5 2007/07/13 08&JJTree: Do not edit this line. AST_JoinCondition.java */

package org.astrogrid.adql;

import org.astrogrid.adql.beans.JoinConditionType;
import org.apache.xmlbeans.XmlObject; 
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

public class AST_JoinCondition extends SimpleNode {
  
    private static Log log = LogFactory.getLog( AST_JoinCondition.class ) ;

    public AST_JoinCondition(AdqlStoX p, int id) {
        super(p, id);
    }

    public void buildXmlTree( XmlObject xo ) {
        if( log.isTraceEnabled() ) enterTrace( log, "AST_JoinCondition.buildXmlTree()" ) ;
        JoinConditionType jctType = (JoinConditionType)xo.changeType( JoinConditionType.type ) ; 
        children[0].buildXmlTree( jctType.addNewCondition() ) ;
        setGeneratedObject( jctType ) ;
        super.buildXmlTree( jctType ) ;
        if( log.isTraceEnabled() ) exitTrace( log, "AST_JoinCondition.buildXmlTree()" ) ;
    }

}