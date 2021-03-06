/* Generated By: AdqlStoX.jjt,v 1.33.2.4 2006-12-19 14&JJTree: Do not edit this line. AST_ColumnReference.java */

package org.astrogrid.adql;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.astrogrid.adql.v1_0.beans.ColumnReferenceType;
import org.apache.xmlbeans.XmlObject ;

public class AST_ColumnReference extends SimpleNode {
    
    private static Log log = LogFactory.getLog( AST_ColumnReference.class ) ;
    private ColumnReferenceType columnReference ;
    
    public AST_ColumnReference(AdqlStoX p, int id) {
        super(p, id);
    }

    public void buildXmlTree( XmlObject xo ) {   
      if( log.isTraceEnabled() ) enterTrace( log, "AST_ColumnReference.buildXmlTree()" ) ; 
      columnReference = (ColumnReferenceType)xo.changeType( ColumnReferenceType.type ) ;
      parser.compiler.addColumnReference( this ) ;
      
      int childCount = jjtGetNumChildren() ;
             
      if( log.isDebugEnabled() ) {
          StringBuffer buffer = new StringBuffer() ;
          buffer
              .append( "childCount: " )
              .append( childCount ) ;       
          for( int i=0; i<childCount; i++ ) {
              buffer
                  .append( "\nChild image " )
                  .append( i )
                  .append( ": " )
                  .append( (String)children[i].getGeneratedObject() ) ;
          }
          log.debug( buffer.toString() ) ;
      }
      
      if( childCount > 2 ) {
          columnReference.setArchive( (String)children[0].getGeneratedObject() ) ;
          columnReference.setTable( (String)children[1].getGeneratedObject() ) ;
          columnReference.setName( (String)children[2].getGeneratedObject() ) ;  
      }
      else {
          columnReference.setTable( (String)children[0].getGeneratedObject() ) ;
          columnReference.setName( (String)children[1].getGeneratedObject() ) ;  
      }
                          
      setGeneratedObject( columnReference ) ;
      super.buildXmlTree(xo) ;
      if( log.isTraceEnabled() ) exitTrace( log, "AST_ColumnReference.buildXmlTree()" ) ; 
  }

    /**
     * @return the column
     */
    public ColumnReferenceType getColumnReference() {
        return columnReference;
    }

}
