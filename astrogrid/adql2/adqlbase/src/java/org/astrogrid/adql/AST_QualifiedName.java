/* Generated By: AdqlStoX.jjt,v 1.34.2.16 2007/04/23 12&JJTree: Do not edit this line. AST_QualifiedName.java */

package org.astrogrid.adql;

public class AST_QualifiedName extends SimpleNode {
    
    private boolean bSchemaName = false ;

  public AST_QualifiedName(AdqlStoX p, int id) {
    super(p, id);
  }
  
  public void jjtClose() {        
      if( jjtGetNumChildren() == 2 ) {
          bSchemaName = true ;
      }     
  }
  
  public boolean isSetSchemaName() {
      return bSchemaName ;
  }
  
  public String getIdentifier() {
      return (String)children[0].getGeneratedObject() ;
  }
  
  public String getSchema() {
      return (String)children[1].getGeneratedObject() ;
  }
  
}