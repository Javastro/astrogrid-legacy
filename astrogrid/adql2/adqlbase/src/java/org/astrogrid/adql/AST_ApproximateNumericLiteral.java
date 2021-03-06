/* Generated By: AdqlStoX.jjt,v 1.33.2.4 2006-12-19 14&JJTree: Do not edit this line. AST_ApproximateNumericLiteral.java */

package org.astrogrid.adql;

import org.astrogrid.adql.beans.AtomType ;
import org.astrogrid.adql.beans.RealType ;
import org.apache.xmlbeans.XmlObject;

public class AST_ApproximateNumericLiteral extends SimpleNode {
    
    private int sign = +1 ;

    public AST_ApproximateNumericLiteral(AdqlStoX p, int id) {
        super(p, id);
    }
    
    public void setSign( int signIndication ) {
        if( signIndication >= 0 ) {
            this.sign = +1 ;
        }
        else {
           this.sign = -1 ;
        }
    }

    public void buildXmlTree( XmlObject xo ) {
        AtomType atomType = (AtomType)xo.changeType( AtomType.type ) ; 
        RealType realType = RealType.Factory.newInstance() ;
        realType.setValue( new Double( getFirstToken().image ).doubleValue() * sign ) ;
        atomType.setLiteral( realType ) ;
        this.generatedObject = atomType ;
        super.buildXmlTree( atomType ) ;
    }
  
}
