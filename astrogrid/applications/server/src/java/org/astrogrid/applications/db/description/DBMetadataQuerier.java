/*
 * $Id: DBMetadataQuerier.java,v 1.2 2011/09/02 21:55:54 pah Exp $
 * 
 * Created on 7 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.db.description;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.ivoa.resource.dataservice.FKColumn;
import net.ivoa.resource.dataservice.ForeignKey;
import net.ivoa.resource.dataservice.TAPType;
import net.ivoa.resource.dataservice.Table;
import net.ivoa.resource.dataservice.TableParam;
import net.ivoa.resource.dataservice.TableSchema;
import net.ivoa.resource.dataservice.TableSet;

/**
 * Generate IVOA Table description metadata from a database to conform with {@linkplain http://www.ivoa.net/Documents/latest/VODataService.html}.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 8 Jul 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public class DBMetadataQuerier {
    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(DBMetadataQuerier.class);
    
    private DataSource ds;

    public DBMetadataQuerier(DataSource ds) {
        this.ds = ds;
    }

    public List<String> listSchema() throws SQLException
    {
        List<String> retval = new ArrayList<String>();
        DatabaseMetaData jdbcMeta = openMetadataConnection();
        ResultSet rs = jdbcMeta.getSchemas();
        while (rs.next()) {
            retval.add(rs.getString(1));
            logger.debug("schema name="+rs.getString(1));
        }
      
       return retval;
    }
    
    public TableSet readTableSet(String schemaname) throws SQLException{
       
        
        TableSet retval = new TableSet();
        List<TableSchema> schemaList = retval.getSchema();
        TableSchema tableSchema = new TableSchema();
        tableSchema.setName(schemaname);
        schemaList.add(tableSchema);
        List<Table> tables = tableSchema.getTable();
         
        DatabaseMetaData jdbcMeta = openMetadataConnection();
       
        //TODO need some heuristics to determine whether the database had both catalog and/or schema levels
        ResultSet rs = jdbcMeta.getSchemas();
        while (rs.next()) {
            ResultSetMetaData md = rs.getMetaData();
            logger.debug("schema name="+rs.getString(1));
        }
        rs = jdbcMeta.getCatalogs();
        while (rs.next()) {
            ResultSetMetaData md = rs.getMetaData();
            logger.debug("catalog name="+rs.getString(1));
        }
        
        rs = jdbcMeta.getTables(null, schemaname, null, new String[]{"TABLE","VIEW"});
        while (rs.next()) {
            Table table = new Table();
            table.setName(rs.getString("TABLE_NAME"));
            if (rs.getString("REMARKS") != null) {
                table.setDescription(rs.getString("REMARKS"));
            }
            table.setType(rs.getString("TABLE_TYPE"));//FIXME - the types are not as described for DataService types
            fillColumns(jdbcMeta,table, schemaname);
            //Foreign keys
             ResultSet rsfk = jdbcMeta.getImportedKeys(null, schemaname, table.getName());
             List<ForeignKey> fkeys = table.getForeignKey();
             while (rsfk.next()) {//IMPL this does not deal with the case where there might be more than one column in the foreign key - think that the sequence no comes into it.
                ForeignKey fkey = new ForeignKey();
                fkey.setTargetTable(rsfk.getString("PKTABLE_NAME"));
                List<FKColumn> fkcols = fkey.getFkColumn();
                FKColumn fkcol = new FKColumn();
                fkcol.setFromColumn(rsfk.getString("FKCOLUMN_NAME"));
                fkcol.setTargetColumn(rsfk.getString("PKCOLUMN_NAME"));
                fkcols.add(fkcol);
                fkeys.add(fkey);
             }
            tables.add(table);
        }
        return retval ;
    }

    private void fillColumns(DatabaseMetaData jdbcMeta, Table table,
            String schemaname) throws SQLException {
        
        List<String> primkeys = new ArrayList<String>();
        ResultSet rs = jdbcMeta.getPrimaryKeys(null, schemaname, table.getName());
        while (rs.next()) {
            primkeys.add(rs.getString("COLUMN_NAME"));
        }
        
        List<String> indexes = new ArrayList<String>();
        rs = jdbcMeta.getIndexInfo(null, schemaname, table.getName(),false,false);
        while (rs.next()) {
            indexes.add(rs.getString("COLUMN_NAME"));
        }

        rs = jdbcMeta.getColumns(null, schemaname, table.getName(), null);
       List<TableParam> cols = table.getColumn();
       while (rs.next()) {
        TableParam column = new TableParam();
        column.setName(rs.getString("COLUMN_NAME"));
        
        TAPType dt = new TAPType();
        dt.setValue(TAPDataTypes.fromJDBCType(rs.getInt("DATA_TYPE")).toString());
        dt.setSize(BigInteger.valueOf(rs.getInt("COLUMN_SIZE")));
        column.setDataType(dt);
        List<String> flags = column.getFlag();
        if(rs.getInt("NULLABLE") == ResultSetMetaData.columnNullable){
            flags.add("nullable");
        }
        if (primkeys.contains(column.getName())) {
            flags.add("primary");
            
        }
        if (indexes.contains(column.getName())) {
            flags.add("indexed");
            
        }
        
       
        cols.add(column);
    }
    }

    private DatabaseMetaData openMetadataConnection() throws SQLException {
        Connection conn = ds.getConnection();
        
        DatabaseMetaData jdbcMeta = conn.getMetaData();
        logger.info("database product type="+ jdbcMeta.getDatabaseProductName() + " version=" + jdbcMeta.getDatabaseMajorVersion());
        
        logger.info("database schema term ="+jdbcMeta.getSchemaTerm());
        logger.info("database catalog term ="+jdbcMeta.getCatalogTerm());
        return jdbcMeta;
    }
}


/*
 * $Log: DBMetadataQuerier.java,v $
 * Revision 1.2  2011/09/02 21:55:54  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.2  2011/09/02 19:38:48  pah
 * change setup of dynamic description library
 *
 * Revision 1.1.2.1  2009/07/16 19:51:57  pah
 * NEW - bug 2944: add DAL support
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2944
 *
 * initial metadata querier implementation
 *
 */
