/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/file/FileIdentifier.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/14 13:50:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileIdentifier.java,v $
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/07 14:32:43  dave
 *   Changed DataIdentifier to FileIdentifier
 *
 *   Revision 1.1.2.2  2004/07/07 14:24:14  dave
 *   Changed internal class DataConrainer to FileContainer
 *
 *   Revision 1.1.2.1  2004/07/05 04:50:29  dave
 *   Created initial FileStore components
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.file ;

import org.astrogrid.filestore.common.identifier.UniqueIdentifier ;

/**
 * A unique identifier for files.
 *
 */
public class FileIdentifier
	extends UniqueIdentifier
	{
    /**
     * Create a new identifier.
     *
     */
    public FileIdentifier()
        {
        super() ;
        }

    /**
     * Create an identifier from a string.
     *
     */
    public FileIdentifier(String value)
        {
        super(value) ;
        }
	}

