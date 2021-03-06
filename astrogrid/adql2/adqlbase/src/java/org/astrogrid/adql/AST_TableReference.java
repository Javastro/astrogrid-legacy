/* Generated By: AdqlStoX.jjt,v 1.49.2.2 2008/03/03 13&JJTree: Do not edit this line. AST_TableReference.java */

package org.astrogrid.adql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.adql.AdqlParser.SyntaxOption;
import org.astrogrid.adql.beans.JoinTableType;
import org.astrogrid.adql.beans.TableType;

public class AST_TableReference extends SimpleNode {
 
    private static Log log = LogFactory.getLog( AST_TableReference.class ) ;
    
    public AST_TableReference(AdqlStoX p, int id) {
        super(p, id);
    }

    public void buildXmlTree( XmlObject ftt ) { 
        if( log.isTraceEnabled() ) enterTrace( log, "AST_TableReference.buildXmlTree()" ) ;
        int childCount = jjtGetNumChildren() ;
        if( childCount == 0 ) {
            //
            // This is just a safety first measure.
            // It should fail somewhere else, but without a NullPointerException
            setGeneratedObject( TableType.Factory.newInstance() ) ;
        }
        else if( childCount > 1 ) {
            JoinTableType joinTable = null ;
            if( childCount == 2 ) {         
                joinTable = AST_QualifiedJoin.buildXmlTree( ftt, children[0], (AST_JoinPart)children[1] ) ;
            }
            else {
                if( !this.adqlstox.parser.isSyntaxSet( SyntaxOption.NESTED_JOIN ) ) {
                    this.adqlstox.tracker.setError( "Reference Implementation: Nested Joins without the use of brackets are not supported." ) ;
                }
                joinTable = AST_QualifiedJoin.buildXmlTree( JoinTableType.Factory.newInstance()
                                                          , children[0]
                                                          , (AST_JoinPart)children[1] ) ;
                for( int i=2; i<childCount; i++ ) {
                    if( i == childCount - 1 ) {
                        joinTable = AST_QualifiedJoin.buildXmlTree( ftt
                                                                  , joinTable
                                                                  , (AST_JoinPart)children[i] ) ;
                    }
                    else {
                        joinTable = AST_QualifiedJoin.buildXmlTree( JoinTableType.Factory.newInstance()
                                                                  , joinTable
                                                                  , (AST_JoinPart)children[i] ) ;
                    }                   
                } 
            }      
            setGeneratedObject( joinTable ) ; 
        }
        else if( children[0] instanceof AST_TableReference2 ) {           
            children[0].buildXmlTree( ftt ) ;
            setGeneratedObject( children[0].getGeneratedObject() ) ;    
        }
        else if( children[0] instanceof AST_QualifiedJoin ) {
            children[0].buildXmlTree( ftt ) ;
            setGeneratedObject( children[0].getGeneratedObject() ) ;
        }
        else {
            log.warn( "Unrecognized table type." ) ;
        }
        super.buildXmlTree( (XmlObject)this.generatedObject ) ;
        if( log.isTraceEnabled() ) exitTrace( log, "AST_TableReference.buildXmlTree()" ) ; 
    }

}
