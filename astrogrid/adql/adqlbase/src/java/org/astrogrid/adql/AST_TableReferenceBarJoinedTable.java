/* Generated By: AdqlStoX.jjt,v 1.33.2.4 2006-12-19 14&JJTree: Do not edit this line. AST_TableReferenceBarJoinedTable.java */

package org.astrogrid.adql;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
//import org.astrogrid.adql.v1_0.beans.ArchiveTableType;
import org.astrogrid.adql.v1_0.beans.TableType;
import org.apache.xmlbeans.XmlObject ;

public class AST_TableReferenceBarJoinedTable extends SimpleNode {
    
    private static Log log = LogFactory.getLog( AST_TableReferenceBarJoinedTable.class ) ;
    
    private TableType table ;

    public AST_TableReferenceBarJoinedTable(AdqlStoX p, int id) {
        super(p, id);
    }


    public void buildXmlTree( XmlObject ftt ) { 
        if( log.isTraceEnabled() ) enterTrace( log, "AST_TableReferenceBarJoinedTable.buildXmlTree()" ) ;
        //  joined_table_S()
        //  |
        //  ( table_name_A() [ [ <AS> ] correlation_name_S() ] )
        int childCount = jjtGetNumChildren() ;
        try {
            // This is just a safety first measure.
            // It should fail somewhere else, but without a NullPointerException
            if( childCount == 0 ) {
                setGeneratedObject( TableType.Factory.newInstance() ) ;
            }       
            else {
                // We have a table, maybe with schema and maybe with alias...
                // Complicated possibly by error recovery providing non-standard
                // number of children! ...
                table = (TableType)ftt.changeType( TableType.type ) ;
                parser.compiler.addTableReference( this ) ;
                for( int i=0; i<childCount; i++ ) {
                    if( children[i] instanceof AST_SchemaName ) {
                        table.setArchive( (String)children[0].getGeneratedObject() ) ;
                    }
                    else if( children[i] instanceof AST_TableName ) {
                        table.setName( (String)children[i].getGeneratedObject() ) ;
                    }
                    else if( children[i] instanceof AST_CorrelationName ) {
                        table.setAlias( (String)children[i].getGeneratedObject() ) ;
                    }
                    else {
                        log.warn( "Unrecognized table naming structure." ) ;
                    }                   
                }
                setGeneratedObject( table ) ;
            } 
            super.buildXmlTree( (XmlObject)this.generatedObject ) ;
        }
        finally {
            if( log.isTraceEnabled() ) exitTrace( log, "AST_TableReferenceBarJoinedTable.buildXmlTree()" ) ; 
        }
       
    }
    
    /**
     * @return the table
     */
    public TableType getTable() {
        return table;
    }

}