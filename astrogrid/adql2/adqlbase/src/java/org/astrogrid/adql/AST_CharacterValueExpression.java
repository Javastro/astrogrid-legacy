/* Generated By: AdqlStoX.jjt,v 1.19 2007/07/25 13&JJTree: Do not edit this line. AST_CharacterValueExpression.java */

package org.astrogrid.adql;

import org.apache.xmlbeans.XmlObject;
import org.astrogrid.adql.beans.CharacterValueExpressionType;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

public class AST_CharacterValueExpression extends SimpleNode {
    
    private static Log log = LogFactory.getLog( AST_CharacterValueExpression.class ) ;

    public AST_CharacterValueExpression(AdqlStoX p, int id) {
        super(p, id);
        setPositionType( CharacterValueExpressionType.type ) ;
    }

    public void buildXmlTree( XmlObject xo ) {
        if( log.isTraceEnabled() ) enterTrace( log, "AST_CharacterValueExpression.buildXmlTree()" ) ; 
        //
        // "Create" the appropriate type type using XmlBeans magic.
        CharacterValueExpressionType cvet = (CharacterValueExpressionType)xo.changeType( CharacterValueExpressionType.type ) ;
        
        int childCount = jjtGetNumChildren() ;
        for( int i=0; i<childCount; i++ ) {
            children[i].buildXmlTree( cvet.addNewCharacterFactor() ) ;
        }       
        this.generatedObject = cvet ;
        super.buildXmlTree( cvet ) ;
        if( log.isTraceEnabled() ) exitTrace( log, "AST_CharacterValueExpression.buildXmlTree()" ) ; 
    }

}
