/* Generated By: AdqlStoX.jjt,v 1.33.2.4 2006-12-19 14&JJTree: Do not edit this line. AST_GroupingColumnReferenceList.java */

package org.astrogrid.adql;

import org.astrogrid.adql.v1_0.beans.ColumnReferenceType;
import org.astrogrid.adql.v1_0.beans.GroupByType;
import org.apache.xmlbeans.XmlObject ;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

public class AST_GroupingColumnReferenceList extends SimpleNode {
    
    private static Log log = LogFactory.getLog( AST_GroupingColumnReferenceList.class ) ;
   
    public AST_GroupingColumnReferenceList(AdqlStoX p, int id) {
        super(p, id);
    }

    // May have problems with comments here.
    public void jjtClose() {
//        int childCount = jjtGetNumChildren() ;
//        ColumnReferenceType[] crtArray = new ColumnReferenceType[ childCount ] ;
//        for( int i=0; i<childCount; i++ ) {
//            crtArray[i] = ( (ColumnReferenceType)children[i].getGeneratedObject() ) ;
//        }
//        this.setGeneratedObject( crtArray ) ;
    }
    
    public void buildXmlTree( XmlObject xo ) {
        if( log.isTraceEnabled() ) enterTrace( log, "AST_GroupingColumnReferenceList.buildXmlTree()" ) ;
        GroupByType gbt = (GroupByType)xo.changeType( GroupByType.type ) ;
        this.generatedObject = gbt  ;
        int childCount = jjtGetNumChildren() ;
        for( int i=0; i<childCount; i++ ) {
            children[i].buildXmlTree( gbt.addNewColumn() ) ;
        }
        super.buildXmlTree(xo) ;
        if( log.isTraceEnabled() ) exitTrace( log, "AST_GroupingColumnReferenceList.buildXmlTree()" ) ;
    }
    
}
