/*$Id: Container.java,v 1.2 2008/09/08 15:37:22 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql.metadata;

import java.util.Hashtable;

/**
 * Container
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Jul 29, 2007
 */
public class Container {
    
    private Hashtable htFunctions = new Hashtable( 64 ) ;
    private Hashtable htColumns = new Hashtable( 512 ) ;
    private Hashtable htTables = new Hashtable( 256 ) ;
    private Hashtable htSchemas = new Hashtable( 16 ) ;
    
    public Container() {}
    
    public synchronized void pushFunction( Function f ) {
        this.htFunctions.put( f.getKey(), f ) ;
    }
    
    public Function getFunction( String name ) {
        return (Function)htFunctions.get( name ) ;
    }
    
    public boolean isFunction( String name ) {
        return htFunctions.containsKey( name ) ;
    }
    
    public synchronized void pushColumn( Column c ) {
        this.htColumns.put( c.getKey(), c ) ;
    }
    
    public Column getColumn( String name ) {
        return (Column)htColumns.get( name ) ;
    }
    
    public Column getColumn( String name, String[] qualifiers ) {
        return (Column)htColumns.get( Key.formKeyString( name, qualifiers ) ) ;
    }
    
    public Column getColumn( String name, Table table ) {
        return (Column)htColumns.get( Key.formKeyString( name, table.key.qualifiers ) ) ;
    }
    
    public boolean isColumn( String name ) {
        return htColumns.containsKey( name ) ;
    }
    
    public boolean isColumn( String name, String[] qualifiers ) {
        return htColumns.containsKey( Key.formKeyString( name, qualifiers ) ) ;
    }
    
    public boolean isColumn( String name, Table table ) {
        return htColumns.containsKey( Key.formKeyString( name, table.key.qualifiers ) ) ;
    }
    
    public synchronized void pushTable( Table t ) {
        this.htTables.put( t.getKey(), t ) ;
    }
    
    public Table getTable( String name ) {
        return (Table)htTables.get( name ) ;
    }
    
    public Table getTable( String name, String[] qualifiers ) {
        return (Table)htTables.get( Key.formKeyString( name, qualifiers ) ) ;
    }
    
    public Table getTable( String[] nameAndQualifiers ) {
        return (Table)htTables.get( Key.formKeyString( nameAndQualifiers) ) ;
    }
    
    public boolean isTable( String name ) {
        return htTables.containsKey( name ) ;
    }
    
    public boolean isTable( String name, String[] qualifiers ) {
        return htTables.containsKey( Key.formKeyString( name, qualifiers ) ) ;
    }
    
    public synchronized void pushSchema( Schema s ) {
        this.htSchemas.put( s.getKey(), s ) ;
    }
    
    public Schema getSchema( String name ) {
        return (Schema)htSchemas.get( name ) ;
    }
    
    public Schema getSchema( String name, String catalog ) {
        return (Schema)htSchemas.get( Key.formKeyString( name, new String[] { catalog } ) ) ;
    }
    
    public boolean isSchema( String name ) {
        return htSchemas.containsKey( name ) ;
    }
    
    public boolean isSchema( String name, String catalog ) {
        return htSchemas.containsKey( Key.formKeyString( name, new String[] { catalog } ) ) ;
    }
    
    public boolean isEmpty() {
        if( htColumns.isEmpty() && htFunctions.isEmpty() && htSchemas.isEmpty() && htTables.isEmpty() )
            return true ;
        return false ;
    }

}


/*
$Log: Container.java,v $
Revision 1.2  2008/09/08 15:37:22  jl99
Merge of branch adql_jl_2575_mark2 into HEAD

Revision 1.1.2.1  2008/08/29 14:49:07  jl99
First mass commit for the new project adql2

Revision 1.1.2.1  2008/08/01 18:50:36  jl99
Complete reorg for purposes of setting up a maven build

Revision 1.5.2.1  2008/05/08 16:14:12  jl99
Improvements to semantic and metadata checks

Revision 1.5  2007/08/06 21:29:35  jl99
First attempt at pooling the AdqlCompiler

Revision 1.4  2007/08/02 14:14:13  jl99
Bug fix and code tidy

Revision 1.3  2007/07/29 23:33:59  jl99
tidy

Revision 1.2  2007/07/29 22:51:56  jl99
First commit of meta data low level implementation

Revision 1.1  2007/07/29 11:26:55  jl99
User defined function calls accommodated with metadata

*/