/*$Id: Column.java,v 1.2 2008/09/08 15:37:22 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql.metadata;

/**
 * Column
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Jul 29, 2007
 */
public class Column extends MetaData {
    
    private String type ;
    private String unit ;
    private String ucd ;
    private String description ;

    public Column( String name, String[] qualifiers, String type, String unit, String ucd, String description ) {
        super( name, qualifiers ) ;
        initDetails( type, unit, ucd, description ) ;
    } 
    
    public Column( String name, Table table, String type, String unit, String ucd, String description ) {
        super( name, table ) ;
        initDetails( type, unit, ucd, description ) ;
    } 
    
    public Column( String name ) {
        super( name ) ;
        this.ucd = "" ;
        this.type = "" ;
        this.unit = "" ;
        this.description = "" ;
    }
    
    public Column( String name, Table table, String[] details ) {
        super( name, table ) ;
        initDetails( details ) ;
    }
    
    public Column( String name, String[] qualifiers, String[] details ) {
        super( name, qualifiers ) ;
        initDetails( details ) ;
    }
    
    private void initDetails( String[] details ) {
        if( details == null ) {
            this.type = "" ;
            this.unit = "" ;
            this.ucd = "" ;          
            this.description = "" ;
        }
        else {
            if( details[0] == null )
                this.type = "" ;
            else
                this.type = details[0] ;
            if( details[0] == null )
                this.unit = "" ;
            else
                this.unit = details[0] ;
            if( details[1] == null )
                this.ucd = "" ;
            else
                this.ucd = details[1] ;
            if( details[2] == null ) 
                this.description = "" ;
            else
                this.description = details[2] ;
        }
    }
    
    private void initDetails( String type, String unit, String ucd, String description ) {
        if( unit == null )
            this.unit = "" ;
        else
            this.unit = unit ;
        if( ucd == null )
            this.ucd = "" ;
        else
            this.ucd = ucd ;
        if( type == null )
            this.type = "" ;
        else
            this.type = type ;
        if( description == null ) 
            this.description = "" ;
        else
            this.description = description ;
    }
    
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the ucd
     */
    public String getUcd() {
        return ucd;
    }
    
    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

}


/*
$Log: Column.java,v $
Revision 1.2  2008/09/08 15:37:22  jl99
Merge of branch adql_jl_2575_mark2 into HEAD

Revision 1.1.2.1  2008/08/29 14:49:07  jl99
First mass commit for the new project adql2

Revision 1.1.2.1  2008/08/01 18:50:36  jl99
Complete reorg for purposes of setting up a maven build

Revision 1.2  2007/08/02 14:14:13  jl99
Bug fix and code tidy

Revision 1.1  2007/07/29 22:51:57  jl99
First commit of meta data low level implementation

*/