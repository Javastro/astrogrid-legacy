/*$Id: SqlCommandsFromConfig.java,v 1.4 2004/03/15 23:45:07 nw Exp $
 * Created on 12-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component.production;

import org.astrogrid.config.Config;
import org.astrogrid.jes.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.impl.workflow.DefaultSqlCommands;
import org.astrogrid.jes.impl.workflow.SqlCommands;

import junit.framework.Test;

/**
 * Implementation of SqlCommands that loads sql commands to execute from a configuration 
 * Used to configure a {@link org.astrogrid.jes.impl.workflow.DBJobFactoryImpl}
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Feb-2004
 *
 */
 public class SqlCommandsFromConfig extends DefaultSqlCommands implements SqlCommands, ComponentDescriptor {
    public final static String DELETE_SQL = "sql.command.delete";
    public final static String UPDATE_SQL = "sql.command.updatel";
    public final static String RETRIEVE_SQL = "sql.command.retrievel";
    public final static String INSERT_SQL = "sql.command.insert";
    public final static String LIST_SQL="sql.command.list";
/**
 *  Construct a new ConfigSqlCommands
 * @param conf confuguration object to look up sql commands in.
 */
    public SqlCommandsFromConfig(Config conf) {
        insertSQL = conf.getString(SqlCommandsFromConfig.INSERT_SQL,SqlCommandsFromConfig.INSERT_SQL_DEFAULT);
        retrieveSQL = conf.getString(SqlCommandsFromConfig.RETRIEVE_SQL,SqlCommandsFromConfig.RETRIEVE_SQL_DEFAULT);
        updateSQL = conf.getString(SqlCommandsFromConfig.UPDATE_SQL,SqlCommandsFromConfig.UPDATE_SQL_DEFAULT);
        deleteSQL = conf.getString(SqlCommandsFromConfig.DELETE_SQL,SqlCommandsFromConfig.DELETE_SQL_DEFAULT);
        listSQL = conf.getString(SqlCommandsFromConfig.LIST_SQL,SqlCommandsFromConfig.LIST_SQL_DEFAULT);
    }
/**
 * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
 */
public String getName() {
    return "DBJobFactory - sql command configuration";
}
/**
 * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
 */
public String getDescription() {
    return "SQL Commands read from configuration file\n" 
    + LIST_SQL + " : " + getListSQL() + "\n"
    + DELETE_SQL + " : " + getDeleteSQL() + "\n"
    + INSERT_SQL + " : " + getInsertSQL() + "\n"
    + RETRIEVE_SQL + " : " + getRetrieveSQL() + "\n"
    + UPDATE_SQL + " : " + getUpdateSQL() ;
}
/**
 * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
 */
public Test getInstallationTest() {
    return null;
}
}

/* 
$Log: SqlCommandsFromConfig.java,v $
Revision 1.4  2004/03/15 23:45:07  nw
improved javadoc

Revision 1.3  2004/03/15 01:30:06  nw
factored component descriptor out into separate package

Revision 1.2  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.2.1  2004/03/07 20:39:26  nw
added implementation of a self-configuring production set of component

Revision 1.4  2004/03/05 16:16:23  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.3  2004/03/03 01:13:41  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.1.2.1  2004/02/12 12:54:47  nw
worked in inversion of control pattern - basically means that
components have to be assembled, rather than self-configuring
from properties in config files. so easier to test each component in isolation
 
*/