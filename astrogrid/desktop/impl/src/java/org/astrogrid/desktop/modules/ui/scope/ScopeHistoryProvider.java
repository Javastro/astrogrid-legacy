/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.geom.Dimension2D;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.BitSet;
import java.util.NoSuchElementException;

import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.comp.DoubleDimension;
import org.astrogrid.desktop.modules.ui.folders.Folder;
import org.astrogrid.desktop.modules.ui.folders.FoldersProvider;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.RangeList;
import ca.odell.glazedlists.ThresholdList;
import ca.odell.glazedlists.UniqueList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

/** Provides a persistent model (for multiple views) of the astroscope
 * search history.
 * History only remembers position - not protocols, or the results.
 * @testme
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 2, 20072:07:44 PM
 */
public class ScopeHistoryProvider
	implements ListEventListener,  FoldersProvider, ExceptionListener{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(ScopeHistoryProvider.class);

	protected final UIContext parent;
	protected final RangeList historyList;
	protected final File storage;
	/** number of history items to remeber */
	public static final int HISTORY_SIZE = 20;
	
	public ScopeHistoryProvider(final UIContext parent,Preference workDir) {
		
		this.parent = parent;
		this.storage = new File(new File(workDir.getValue()),"scopeHistory.xml");
		// we use a range list to limit the number of history items stored.
		// and a unique list to remove duplicates
		//using a range list does mean that the in-memory history list
		// continuouysly grows - even though it's not seen or required.
		// probably not an issue though, given the number of searches performed in 
		// an average session..
		this.historyList = new RangeList(new UniqueList(	new BasicEventList()));			
		historyList.setHeadRange(0,HISTORY_SIZE);
		loadHistory(storage);
		historyList.addListEventListener(this);			
}

	public EventList getList() {
		return historyList;
	}
	protected void loadHistory(File f) {
		if (! f.exists()) {
			return;
		}
		logger.info("Loading history from " + f);
		InputStream fis = null;
		XMLDecoder x = null;
		try { 
			fis = new FileInputStream(f);
			x = new XMLDecoder(fis,this,this);
			SearchHistoryItem[] rs = (SearchHistoryItem[])x.readObject();
			if (rs != null) {
				historyList.addAll(Arrays.asList(rs));
				logger.info("Loaded " + rs.length + " items");
			} else {
				logger.info("File is empty");
			}
		} catch (FileNotFoundException ex) {
			logger.error(storage.toString(),ex);
		} catch (ArrayIndexOutOfBoundsException ex) {
			// thrown when the file contains no data - a bit crap - no way to check this.
			// fail gracefully.
			logger.info("File is empty");
		} catch (NoSuchElementException ex) {
			// thrown when the file contains no data - a bit crap - no way to check this.
			// fail gracefully.
			logger.info("File is empty");			
		}finally {
			if (x != null) {
				x.close();
			} 
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException x1) {
					logger.error("IOException",x1);
				}
			}
		}
	}

	protected void saveHistory(final File file) {
		(new BackgroundWorker(parent,"Saving history") {
			protected Object construct() throws Exception {
				logger.info("Saving list to " + file);
				OutputStream fos = null;
				XMLEncoder a = null;
				try {
					fos = new FileOutputStream(file);
					a = new XMLEncoder(fos);
					SearchHistoryItem[] rf = (SearchHistoryItem[])historyList.toArray(new SearchHistoryItem[historyList.size()]);
					a.writeObject(rf);
				} catch (FileNotFoundException x) {
					logger.error(file.toString(),x);
				} finally {
					if (a != null) {
						a.close();
					} 
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException x) {
							logger.error("Failed to save folder list.",x);
						}
					}
				}
				return null;
			}
		}).start();
	}

	/** folder list has changed - write it back to disk */
	public void listChanged(ListEvent arg0) {
		saveHistory(storage);
	}
	// called when a recoverable exception is thrown by the decoder.
	public void exceptionThrown(Exception e) {
		logger.warn("Exception whilst reading file: '" + e.getMessage() + "' - continuing");
		logger.debug(e);
	}
	
	public static class SearchHistoryItem implements Comparable{
		private DoubleDimension radius;
		private SesamePositionBean position;


		public DoubleDimension getRadius() {
			return this.radius;
		}

		public void setRadius(DoubleDimension radius) {
			this.radius = radius;
		}

		public SesamePositionBean getPosition() {
			return this.position;
		}

		public void setPosition(SesamePositionBean position) {
			this.position = position;
		}
		public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((this.position == null) ? 0 : this.position.hashCode());
			result = PRIME * result + ((this.radius == null) ? 0 : this.radius.hashCode());
			return result;
		}
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final SearchHistoryItem other = (SearchHistoryItem) obj;
			if (this.position == null) {
				if (other.position != null)
					return false;
			} else if (!this.position.equals(other.position))
				return false;

			if (this.radius == null) {
				if (other.radius != null)
					return false;
			} else if (!this.radius.equals(other.radius))
				return false;
			return true;
		}
		public int compareTo(Object arg0) {
			SearchHistoryItem other = (SearchHistoryItem)arg0;
			return new CompareToBuilder()
				.append(position.getRa(),other.position.getRa())
				.append(position.getDec(),other.position.getDec())
				.append(radius.getWidth(),other.radius.getWidth())
				.append(radius.getHeight(),other.radius.getHeight())
				.toComparison();
		}
		
	}


}
