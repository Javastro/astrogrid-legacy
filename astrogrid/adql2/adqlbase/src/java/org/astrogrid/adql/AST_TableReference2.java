/* Generated By: AdqlStoX.jjt,v 1.49.2.2 2008/03/03 13&JJTree: Do not edit this line. AST_TableReference2.java */

package org.astrogrid.adql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.adql.beans.DerivedTableType;
import org.astrogrid.adql.beans.TableType;

public class AST_TableReference2 extends SimpleNode {
 
    private static Log log = LogFactory.getLog( AST_TableReference2.class ) ;

    public AST_TableReference2(AdqlStoX p, int id) {
        super(p, id);
    }

    /* (non-Javadoc)
     * @see org.astrogrid.adql.SimpleNode#buildXmlTree(org.apache.xmlbeans.XmlObject)
     */
    public void buildXmlTree( XmlObject ftt ) {
        if( log.isTraceEnabled() ) enterTrace( log, "AST_TableReference2.buildXmlTree()" ) ;
        children[0].buildXmlTree( ftt ) ;
        this.setGeneratedObject( children[0].getGeneratedObject() ) ;
        if( children[0] instanceof AST_DerivedTable ) {
            //
            // For a derived table, alias is mandatory.
            // Alias is a String. It was built during parsing...
            String alias = (String)children[1].getGeneratedObject() ;
            ( (DerivedTableType)children[0].getGeneratedObject() ).setAlias( alias ) ;
        }
        else if( children[0] instanceof AST_TableName ){
            //
            // Record the table reference for later semantic processing...           
            TableType tt = (TableType)children[0].getGeneratedObject() ;
            adqlstox.parser.addTableReference( tt ) ;
            //
            // For a normal table, alias is optional.
            if(jjtGetNumChildren() > 1 ) {
                
                //
                // Alias is a String. It was built during parsing...
                String alias = (String)children[1].getGeneratedObject() ;               
                tt.setAlias( alias ) ;
            }
            
        }  
        super.buildXmlTree( (XmlObject)this.generatedObject );
        if( log.isTraceEnabled() ) exitTrace( log, "AST_TableReference2.buildXmlTree()" ) ;
    }

}
