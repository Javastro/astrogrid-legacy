/*$Id: ConfigSqlCommands.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 12-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.impl.workflow;

import org.astrogrid.config.SimpleConfig;

/**
 * Implementation of SqlCommands that loads sql commands to execute from configuration 
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Feb-2004
 *
 */
public class ConfigSqlCommands extends DefaultSqlCommands implements SqlCommands {
    public final static String DELETE_SQL = "sql.command.delete";
    public final static String UPDATE_SQL = "sql.command.updatel";
    public final static String RETRIEVE_SQL = "sql.command.retrievel";
    public final static String INSERT_SQL = "sql.command.insert";
    public final static String LIST_SQL="sql.command.list";

    public ConfigSqlCommands() {
        insertSQL = SimpleConfig.getProperty(ConfigSqlCommands.INSERT_SQL,ConfigSqlCommands.INSERT_SQL_DEFAULT);
        retrieveSQL = SimpleConfig.getProperty(ConfigSqlCommands.RETRIEVE_SQL,ConfigSqlCommands.RETRIEVE_SQL_DEFAULT);
        updateSQL = SimpleConfig.getProperty(ConfigSqlCommands.UPDATE_SQL,ConfigSqlCommands.UPDATE_SQL_DEFAULT);
        deleteSQL = SimpleConfig.getProperty(ConfigSqlCommands.DELETE_SQL,ConfigSqlCommands.DELETE_SQL_DEFAULT);
        listSQL = SimpleConfig.getProperty(ConfigSqlCommands.LIST_SQL,ConfigSqlCommands.LIST_SQL_DEFAULT);
    }
}

/* 
$Log: ConfigSqlCommands.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.1.2.1  2004/02/12 12:54:47  nw
worked in inversion of control pattern - basically means that
components have to be assembled, rather than self-configuring
from properties in config files. so easier to test each component in isolation
 
*/