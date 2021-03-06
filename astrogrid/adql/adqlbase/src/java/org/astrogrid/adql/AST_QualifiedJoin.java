/* Generated By: AdqlStoX.jjt,v 1.33.2.4 2006-12-19 14&JJTree: Do not edit this line. AST_QualifiedJoin.java */

package org.astrogrid.adql;

import org.astrogrid.adql.v1_0.beans.JoinTableType;
import org.astrogrid.adql.v1_0.beans.JointTableQualifierType;
import org.astrogrid.adql.v1_0.beans.ArrayOfFromTableType;
import org.apache.xmlbeans.XmlObject; 
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

public class AST_QualifiedJoin extends SimpleNode {
    
    private static Log log = LogFactory.getLog( AST_QualifiedJoin.class ) ;
 
    public AST_QualifiedJoin(AdqlStoX p, int id) {
        super(p, id);
    }
    
    public void buildXmlTree( XmlObject xo ) {
        if( log.isTraceEnabled() ) enterTrace( log, "AST_QualifiedJoin.buildXmlTree()" ) ;
        JoinTableType jtType = (JoinTableType)xo.changeType( JoinTableType.type ) ;         
        jtType.xsetQualifier( (JointTableQualifierType)children[1].getGeneratedObject() ) ;
        ArrayOfFromTableType tableArray = jtType.addNewTables() ;
        children[0].buildXmlTree( tableArray.addNewFromTableType() ) ;
        children[2].buildXmlTree( tableArray.addNewFromTableType() ) ;   
        if( jjtGetNumChildren() == 4 ) {
            children[3].buildXmlTree( jtType.addNewCondition() ) ;
        }
        setGeneratedObject( jtType ) ;
        super.buildXmlTree( jtType ) ;
        if( log.isTraceEnabled() ) exitTrace( log, "AST_QualifiedJoin.buildXmlTree()" ) ;
    }

}
