/* Generated By: AdqlStoX.jjt,v 1.33.2.4 2006-12-19 14&JJTree: Do not edit this line. AST_DerivedColumn.java */

package org.astrogrid.adql;

import org.astrogrid.adql.beans.AliasSelectionItemType ;
import org.astrogrid.adql.beans.AllSelectionItemType;
import org.astrogrid.adql.beans.SelectionItemType ;
import org.apache.xmlbeans.XmlObject ;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory ;

public class AST_DerivedColumn extends SimpleNode {
    
    private static Log log = LogFactory.getLog( AST_DerivedColumn.class ) ;
    
    private boolean bAliased = false ;

    public AST_DerivedColumn(AdqlStoX p, int id) {
        super(p, id);
    }
    
    public void setAliased( String elementName ) {
        bAliased = true ;
        //pushPosition( elementName, AliasSelectionItemType.type ) ;
        this.schemaType = AliasSelectionItemType.type ;
    }
    
    public void buildXmlTree( XmlObject xo ) {
        if( log.isTraceEnabled() ) enterTrace( log, "AST_DerivedColumn.buildXmlTree()" ) ; 
        SelectionItemType sit = (SelectionItemType)xo ;
        int childCount = jjtGetNumChildren() ;
        if( bAliased ) {
            AliasSelectionItemType asiType = (AliasSelectionItemType)sit.changeType( AliasSelectionItemType.type ) ;
            this.generatedObject = asiType ;
            adqlstox.parser.addAliasSelection( asiType ) ;
            for( int i=0; i<childCount; i++ ) {
                if( children[i] instanceof AST_ColumnName ) {
                    asiType.setAs( (String)children[i].getGeneratedObject() ) ;
                }
                else if( children[i] instanceof AST_ValueExpression ) {
                    AST_ValueExpression ve = (AST_ValueExpression)children[i] ;
                    ve.elementName = AdqlParser.EXPRESSION_ELEMENT ;
                    children[i].buildXmlTree( asiType.addNewExpression() ) ;
                }
            }  
            this.generatedObject = asiType ;
            super.buildXmlTree(xo) ;
        }
        else if( childCount > 0 ){
            children[0].buildXmlTree( sit ) ;
            this.generatedObject = children[0].getGeneratedObject() ;
            super.buildXmlTree(xo) ;
        }
        
        if( log.isTraceEnabled() ) exitTrace( log, "AST_DerivedColumn.buildXmlTree()" ) ; 
        
    }

}