/* Generated By: AdqlStoX.jjt,v 1.33.2.4 2006-12-19 14&JJTree: Do not edit this line. AST_TableSubQuery.java */

package org.astrogrid.adql;

import org.astrogrid.adql.beans.SubQuerySet;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.apache.xmlbeans.XmlObject ;

public class AST_TableSubQuery extends SimpleNode {
    
    private static Log log = LogFactory.getLog( AST_TableSubQuery.class ) ;

    public AST_TableSubQuery(AdqlStoX p, int id) {
        super(p, id);
    }

    public void buildXmlTree( XmlObject xo ) {
        if( log.isTraceEnabled() ) enterTrace( log, "AST_TableSubQuery.buildXmlTree()" ) ;
        SubQuerySet sqs = (SubQuerySet)xo.changeType( SubQuerySet.type ) ;
        children[0].buildXmlTree( sqs.addNewSelection() ) ;
        setGeneratedObject( children[0].getGeneratedObject() ) ;
        super.buildXmlTree( (XmlObject)this.generatedObject ) ;
        if( log.isTraceEnabled() ) exitTrace( log, "AST_TableSubQuery.buildXmlTree()" ) ;
    }
}