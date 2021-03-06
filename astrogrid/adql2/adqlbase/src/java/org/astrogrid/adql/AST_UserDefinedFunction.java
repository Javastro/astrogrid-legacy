/* Generated By: AdqlStoX.jjt,v 1.34.2.32 2007/05/21 17&JJTree: Do not edit this line. AST_UserDefinedFunction.java */

package org.astrogrid.adql;

import org.apache.xmlbeans.XmlObject;
import org.astrogrid.adql.beans.UserDefinedFunctionType ;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.astrogrid.adql.metadata.*;
import org.astrogrid.adql.AdqlParser.SyntaxOption;

public class AST_UserDefinedFunction extends SimpleNode {
    
    private static Log log = LogFactory.getLog( AST_UserDefinedFunction.class ) ;

    private String name ;
    private int[] cardinality = null ;
    
  public AST_UserDefinedFunction(AdqlStoX p, int id) {
    super(p, id);
    this.schemaType = UserDefinedFunctionType.type ;
  }
  
  public void setFunctionName( String name ) throws ParseException {
      String defaultPrefix = this.adqlstox.parser.getDefaultUserDefinedFunctionPrefix() ;     
      
      //
      // If metadata is available, everything is supplied...
      if( this.adqlstox.parser.isMetadataQueryEnabled() ) {
          MetadataQuery mq = this.adqlstox.parser.getMetadataQuery() ;
          if( mq.isFunction( name ) ) {
              //
              // Cardinality taken from metadata...
              cardinality = mq.getFunctionCardinality( name ) ; 
              this.name = name ;
              return ;
          }
      } 
      //
      // Default prefix could have been set to null deliberately to ignore
      // any use of function prefix.
      // If not null, user functions must begin with whatever the default prefix
      // has been set to...
      if( defaultPrefix != null && name.startsWith( defaultPrefix ) == false ) {
             throw new ParseException( "User Defined Functions must begin with \"" + defaultPrefix + "\" " +
                                       "or be known to the system via a metadata call. " ) ;      
      }
      else {
          //
          // Default allowed cardinality is zero to many... 
         cardinality = new int[] { 0, -1 } ;
      }      
      this.name = name ;
  }
  
public void buildXmlTree( XmlObject xo ) {   
      if( log.isTraceEnabled() ) enterTrace( log, "AST_UserDefinedFunction.buildXmlTree()" ) ; 
      int childCount = jjtGetNumChildren() ;
     
      int expressionCount = 0 ;
      AST_Ownership ownership = null ;
      for( int i=0 ; i<childCount; i++ ) {
          //
          // Value expressions are arguments to the function...
          if( children[i] instanceof AST_ValueExpression ) {
              expressionCount++ ;
          }
          //
          // Anything else implies the function has been
          // qualified (catalogue/schema or some concept of ownership )...
          else if (children[i] instanceof AST_Ownership ){
              ownership = (AST_Ownership)children[i] ;              
          }
      }
      //
      // Cardinality needs to obey some metadata rules...
      if( cardinality[0] > expressionCount 
          ||
          ( cardinality[1] < expressionCount && cardinality[1] != -1 ) ) {
          //
          // Set a cardinality error but allow build to continue...
          this.adqlstox.tracker.setError( "User defined function has incorrect number of parameters " ) ;         
      }
      
      if( name != null ) {
          UserDefinedFunctionType udf = (UserDefinedFunctionType)xo.changeType( UserDefinedFunctionType.type ) ; 
          udf.setName( name ) ;
          if( ownership != null ) {
              if( ownership.getQualifierDepth() == 1 ) {
                  //
                  // Accommodates y.function()
                  udf.setSchema( ownership.getSecondQualifier() ) ;
              }
              else {
                  //
                  // Accommodates x.y.function() or x..function()
                  udf.setCatalog( ownership.getFirstQualifier() ) ;
                  udf.setSchema( ownership.getSecondQualifier() ) ;
              } 
              if( !adqlstox.parser.isSyntaxSet( SyntaxOption.FUNCTION_OWNERSHIP ) ) {
                  this.getTracker().setError( "Reference Implementation: Ownership of user-defined functions not supported" ) ;
              }   
          }
         
          for( int i=0; i<childCount; i++ ) {
              if( children[i] instanceof AST_ValueExpression ) {
                  children[i].buildXmlTree( udf.addNewArg() ) ;
              }
          }
          this.generatedObject = udf ;
      }
      super.buildXmlTree( (XmlObject)this.generatedObject ) ;
      if( log.isTraceEnabled() ) exitTrace( log, "AST_UserDefinedFunction.buildXmlTree()" ) ; 
      
  }

}
