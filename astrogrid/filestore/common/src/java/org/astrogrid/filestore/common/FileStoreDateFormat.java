/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/FileStoreDateFormat.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreDateFormat.java,v $
 *   Revision 1.2  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.2  2005/01/15 08:25:50  dave
 *   Refactored file created and modified date handling ..
 *
 *   Revision 1.1.2.1  2005/01/14 21:01:33  dave
 *   Added FileStoreDateFormat utility to handle dates in ISO format ....
 *   Fixed the file:/// problem on Unix ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common ;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
/**
 * A utility class to handle date formatting.
 *
 */
public class FileStoreDateFormat
	extends SimpleDateFormat
	{
	/**
	 * The default date format.
	 *
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ" ;

	/**
	 * Public constructor.
	 *
	 */
	public FileStoreDateFormat()
		{
		super(
			DATE_FORMAT
			);
		}

	/**
	 * Parse a string into a date.
	 * This just wraps the SimpleDateFormat method to make it capable of handling a null String.
	 * @param string The ISO format string, or null.
	 * @return A new Date generated from the String, or null if the string is null.
	 *
	 */
	public Date parse(String string)
		throws ParseException
		{
		if (null != string)
			{
			return super.parse(
				string
				);
			}
		else {
			return null ;
			}
		}
	}
