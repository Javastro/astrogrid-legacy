package org.astrogrid.portal.cocoon.plugins.readers;

import org.apache.cocoon.reading.AbstractReader;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheValidity;
import org.apache.cocoon.caching.Cacheable;
import org.apache.cocoon.caching.TimeStampCacheValidity;
import org.apache.cocoon.environment.Context;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Response;
import org.apache.cocoon.environment.Source;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.util.HashUtil;
import org.xml.sax.SAXException;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Collection;
import java.util.Enumeration;


import uk.ac.starlink.votable.VOTableBuilder;
import java.net.URL;
import uk.ac.starlink.util.URLDataSource;
import uk.ac.starlink.topcat.TableViewer;
import uk.ac.starlink.table.*;
import uk.ac.starlink.topcat.PlotWindow;
import uk.ac.starlink.table.gui.StarTableColumn;


/*
 * 
 * 

 * @author Kevin Benson
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

public class VotableReader extends AbstractReader {

		/** The  source */
		private Source      inputSource;


		/**
		 * Setup the reader.
		 * The resource is opened to get an <code>InputStream</code>,
		 * the length and the last modification date
		 */
		public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par)
		throws ProcessingException, SAXException, IOException {
			super.setup(resolver, objectModel, src, par);
			this.inputSource = this.resolver.resolve(super.source);
		}

		public void recycle() {
			super.recycle();
			if (this.inputSource != null) {
				this.inputSource.recycle();
				this.inputSource = null;
			}
		}

		/**
		 * Generate the unique key.
		 * This key must be unique inside the space of this component.
		 *
		 * @return The generated key hashes the src
		 */
		public long generateKey() {
			if (this.inputSource.getLastModified() != 0) {
				return HashUtil.hash(this.inputSource.getSystemId());
			}
			return 0;
		}

		/**
		 * Generate the validity object.
		 *
		 * @return The generated validity object or <code>null</code> if the
		 *         component is currently not cacheable.
		 */
		public CacheValidity generateValidity() {
			if (this.inputSource.getLastModified() != 0) {
				return new TimeStampCacheValidity(this.inputSource.getLastModified());
			}
			return null;
		}

		/**
		 * @return the time the read source was last modified or 0 if it is not
		 *         possible to detect
		 */
		public long getLastModified() {
			return this.inputSource.getLastModified();
		}

		/**
		 * Generates the requested resource.
		 */
		public void generate()
		throws IOException, ProcessingException {
			int itemCount = 0;
			String comboBox = null;
			final Response response = ObjectModelHelper.getResponse(this.objectModel);
			Request request = ObjectModelHelper.getRequest(objectModel);
			String url = (String)request.getParameter("url");
         String xaxis = (String)request.getParameter("xaxis");
         String yaxis = (String)request.getParameter("yaxis");
			url = url.trim();
         xaxis = xaxis.trim();
         yaxis = yaxis.trim();
         System.out.println("the xaxlis = " + xaxis + "the yaxis = " + yaxis);
         			
			
			try {
				VOTableBuilder vot = new VOTableBuilder();
            URLDataSource urlds = new URLDataSource(new URL(url));
				StarTable st = vot.makeStarTable(urlds);
			   ColumnRandomWrapperStarTable wst = new ColumnRandomWrapperStarTable(st);
			   TableViewer tv = new TableViewer(wst,null);
			   PlotWindow pw = new PlotWindow(tv);
            ColumnInfo cinfo = null;
            for(int j = 0;j < pw.xColBox.getItemCount();j++) {
               cinfo = ((StarTableColumn)pw.xColBox.getItemAt(j)).getColumnInfo();
               if(xaxis != null && xaxis.equals(cinfo.getName().trim())) {
                        pw.xColBox.setSelectedIndex(j);
               }
               if(yaxis != null && yaxis.equals(cinfo.getName().trim())) {
                        pw.yColBox.setSelectedIndex(j);
               }
               
            }
			   BufferedImage bi = pw.lastPlot.exportImage();
			   javax.imageio.ImageIO.write(bi,"png",out);
			   pw.dispose();
			   tv.dispose();
			   pw = null;
			   tv = null;
			   wst = null;
			   st = null;
			   url = null;
			   vot = null;
			   out.flush();
			}catch(Exception e) {
			   e.printStackTrace();
			   System.out.println(e.toString());
			}
			
		}

		/**
		 * Returns the mime-type of the resource in process.
		 */
		public String getMimeType () {
			Context ctx = ObjectModelHelper.getContext(this.objectModel);

			if (ctx != null) {
			   return ctx.getMimeType(this.source);
			} else {
			   return null;
			}
		}
}