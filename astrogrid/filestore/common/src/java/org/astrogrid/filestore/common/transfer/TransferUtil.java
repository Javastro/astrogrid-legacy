/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/transfer/TransferUtil.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/21 18:11:55 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: TransferUtil.java,v $
 *   Revision 1.2  2004/07/21 18:11:55  dave
 *   Merged development branch, dave-dev-200407201059, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/21 16:28:16  dave
 *   Added content properties and tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.transfer ;

import java.io.InputStream  ;
import java.io.OutputStream ;
import java.io.IOException  ;

/**
 * An toolkit to transfer bytes from one source to another.
 * Initially this runs in-line, later refactoring may extend this to run in a separate thread.
 *
 */
public class TransferUtil
	{

	/**
	 * The size of our copy buffer (8k bytes).
	 *
	 */
	private static final int BUFFER_SIZE = 8 * 1024 ;

	/**
	 * Public constructor.
	 *
	 */
	public TransferUtil(InputStream in, OutputStream out)
		{
		this.in  = in  ;
		this.out = out ;
		}

	/**
	 * Our transfer buffer.
	 *
	 */
	byte[] buffer = new byte[BUFFER_SIZE] ;

	/**
	 * Our input stream.
	 *
	 */
	private InputStream in ;

	/**
	 * Our output stream.
	 *
	 */
	private OutputStream out ;

	/**
	 * The number of bytes transferred so far.
	 *
	 */
	private int total ;

	/**
	 * Access to our total.
	 *
	 */
	public int getTotal()
		{
		return this.total ;
		}

	/**
	 * Transfer data from one stream to another.
	 * @return The number fo bytes transferred.
	 *
	 */
	public int transfer()
		throws IOException
		{
		int count = 0;
		try {
			do {
				this.total += count ;
				this.out.write(this.buffer, 0, count);
				count = this.in.read(this.buffer, 0, this.buffer.length);
				}
			while (count != -1);
			}
		finally {
			if (null != out)
				{
				this.out.close() ;
				}
			if (null != in)
				{
				this.in.close() ;
				}
			}
		return this.total ;
		}
	}

