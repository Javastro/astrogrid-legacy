/* Generated By: AdqlStoX.jjt,v 1.49.2.1 2008/02/27 12&JJTree: Do not edit this line. AST_SystemDefinedFunction.java */

package org.astrogrid.adql; 

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.adql.beans.SystemFunctionNameType;
import org.astrogrid.adql.beans.SystemFunctionType;;

public class AST_SystemDefinedFunction extends SimpleNode {
  
    private static Log log = LogFactory.getLog( AST_SystemDefinedFunction.class ) ;

  public AST_SystemDefinedFunction(AdqlStoX p, int id) {
    super(p, id);
  }
  
  public void buildXmlTree( XmlObject xo ) {
      if( log.isTraceEnabled() ) enterTrace( log, "AST_SystemDefinedFunction.buildXmlTree()" ) ; 
      SystemFunctionType sft = (SystemFunctionType)xo.changeType( SystemFunctionType.type ) ;
      sft.setName( SystemFunctionNameType.Enum.forString( firstToken.image.toUpperCase() ) ) ;
      
      int childCount = jjtGetNumChildren() ;
      for( int i=0; i<childCount; i++ ) {            
        children[i].buildXmlTree( sft.addNewArg() ) ;
      }   
     
      setGeneratedObject( sft ) ;
      super.buildXmlTree( sft ) ;
      if( log.isTraceEnabled() ) exitTrace( log, "AST_SystemDefinedFunction.buildXmlTree()" ) ; 
  }

}
