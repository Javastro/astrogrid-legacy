/*
 * $Id: FileServer.java,v 1.2 2008/10/14 12:28:52 clq2 Exp $
 */
package org.astrogrid.dataservice.service.fileserver;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.metadata.queryable.SearchGroup;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.query.Query;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.webapp.DefaultServlet;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.queriers.DatabaseAccessException;
import org.astrogrid.io.mime.MimeTypes;

/**
 * A very simple streaming file server.  This is implemented to
 * support phased results delivery in TAP. 
 *
 * There are probably lots of other useful things this could do,
 * but right now it just contains the things we need for TAP.
 *
 * @author K Andrews
 */
public class FileServer {
   
	/**
	 * The directory in which the FileServer's files should be kept.
	 * Ideally this would be on a dedicated disk partition.
	 * It's specified in the "datacenter.filestore.location" configuration
	 * property.
	 */
	protected String baseDirectory = "";

	public FileServer() 
	{
		baseDirectory = ConfigFactory.getCommonConfig().getString(
				"datacenter.filestore.location");
		if ((baseDirectory == null) || "".equals(baseDirectory)) {
			// get upset
		}
		// Check if directory actually exists and is writable
		{
			// Create file
			// Write to it
			// Read from it
			// Delete it
			// Check it's gone
		}
	}

   public String getFileLocationFromID(String uniqueID)
   {
      return baseDirectory + uniqueID;
   }
		/*
	public void serveFile(String uniqueID, Writer writer)
	{
		Reader reader = new FileReader(baseDirectory + uniqueID);
		Piper.bufferedPipe(reader,writer);
	}
		*/
/*
	public FileReader getFileReaderFromID(String uniqueID)
	{
	}
	public FileReader getFileWriterFromID(String uniqueID)
	{
	}
	*/
}
