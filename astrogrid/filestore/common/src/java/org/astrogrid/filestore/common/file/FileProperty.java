/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/file/FileProperty.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:44:01 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileProperty.java,v $
 *   Revision 1.3  2005/01/28 10:44:01  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.2.104.1  2005/01/18 14:54:49  dave
 *   Refactored properties ..
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/13 16:37:29  dave
 *   Refactored common and client to use an array of FileProperties (more SOAP friendly)
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.file ;

import java.util.Map.Entry ;

/**
 * A single property value.
 *
 */
public class FileProperty
    {
    /**
     * Public constructor.
     *
     */
    public FileProperty()
        {
        }

    /**
     * Public constructor from a name and value.
     * @param name The property name.
     * @param value The property value.
     *
     */
    public FileProperty(String name, String value)
        {
        this.name  = name ;
        this.value = value ;
        }

    /**
     * Public constructor from a Map entry.
     * @param name The property name.
     * @param value The property value.
     *
     */
    public FileProperty(Entry entry)
        {
        this.name  = (String) entry.getKey() ;
        this.value = (String) entry.getValue() ;
        }

    /**
     * The property name.
     *
     */
    private String name ;

    /**
     * Access to the property name.
     *
     */
    public String getName()
        {
        return this.name ;
        }

    /**
     * Access to the property name.
     *
     */
    public void setName(String name)
        {
        this.name = name ;
        }

    /**
     * The property value.
     *
     */
    private String value ;

    /**
     * Access to the property value.
     *
     */
    public String getValue()
        {
        return this.value ;
        }

    /**
     * Access to the property value.
     *
     */
    public void setValue(String value)
        {
        this.value = value ;
        }

    }

