package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.util.Comparator;

import javax.swing.Icon;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.votech.VoMon;
import org.votech.VoMonBean;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ListSelection;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.CheckableTableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;

/** Glazed Lists format for a table of registry entries
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 07-Sep-2005
 */

public  class ResourceTableFomat implements AdvancedTableFormat, WritableTableFormat{
	public ResourceTableFomat(final VoMon vomon,final CapabilityIconFactory capBuilder) {
		super();
		this.vomon = vomon;
		this.capBuilder = capBuilder;
		this.okLabel = IconHelper.loadIcon("greenled16.png");
		this.downLabel = IconHelper.loadIcon("redled16.png");
		this.unknownLabel = IconHelper.loadIcon("idle16.png");
	}
	private final CapabilityIconFactory capBuilder;
	private final Icon okLabel;
	private final Icon downLabel;
	private final Icon unknownLabel;
	private static final int COLUMN_COUNT = 4;
	private final VoMon vomon;

	public Class getColumnClass(int columnIndex) {
		switch(columnIndex) {
		case 0:
		case 2:
			return Icon.class;
		case 1:
		case 3:
			return Object.class;
		default:
			throw new IndexOutOfBoundsException("Oversized column ix:" +columnIndex);			
		}
	}		

	public Comparator getColumnComparator(int arg0) {
		switch (arg0) {
		case 0:
		case 2:
			return iconComparator;
		case 1:
			return GlazedLists.caseInsensitiveComparator();
		case 3:
			return GlazedLists.comparableComparator();
		default:
			throw new IndexOutOfBoundsException("Oversized column ix:" +arg0);
		}
	}

	/** compare 2 icons. */
	private final Comparator iconComparator = new Comparator() {
		public int compare(Object arg0, Object arg1) {
			Icon a = (Icon)arg0;
			Icon b = (Icon)arg1;
			return map(a) - map(b); 
		}
		
		private int map(Icon a) {
			if (a == null) {
				return 0;
			}
			if (a == okLabel) {
				return 5;
			}
			if (a == unknownLabel) {
				return 4;
			}
			if (a == downLabel) {
				return 1;
			}
			return a.hashCode(); // works for icons in capabilities list.
		}
	};
	
	public int getColumnCount() {
		return COLUMN_COUNT;
	}
	public String getColumnName(int column) {
		switch(column) {
		case 0 :return "Status";
		case 1: return "Title";
		case 2: return "Capability";
		case 3: return "Date";
		default: 
			throw new IndexOutOfBoundsException("Oversized column ix:" +column);
		}
	}
	public Object getColumnValue(Object arg0, int columnIndex) {
		Resource r = (Resource)arg0;
		switch(columnIndex) {
		case 0:
			return getAvailability(r);
		case 1:
			return createTitle(r);
		case 2:
			return capBuilder.buildIcon(r);
		case 3:
			return createDate(r);
		default:
			throw new IndexOutOfBoundsException("Oversized column ix:" +columnIndex);
		}
	}


	public boolean isEditable(Object arg0, int columnIndex) {
		return false;
	}

	public Object setColumnValue(Object obj, Object bVal, int columnIndex) {

			throw new IllegalArgumentException("This column is not editable");
	}

	public static String createTitle(Resource r) {
		if (r == null) {
			return "";
		}
		String title = StringUtils.replace(r.getTitle(), "\n", " ") ;

		return title;
	}		

	private String createDate(Resource r) {
		String date = r.getUpdated();
		if (date == null) {
			date = r.getCreated();
		}
		return date == null ? "" : date.substring(0,10);
	}
	
	private Icon getAvailability(Resource r) {

		if (r instanceof Service) {
			VoMonBean b = vomon.checkAvailability(r.getId());
			if (b == null) {// unknown
				return unknownLabel;
			} else if ( b.getCode() != VoMonBean.UP_CODE) { // service down
				return downLabel;
			} else {
				return okLabel;
			}
		} else if (r instanceof CeaApplication) {
			VoMonBean[] providers = vomon.checkCeaAvailability(r.getId());
			if (providers == null ) { 
				// unknown application.
				return unknownLabel;
			} else {
				for (int i = 0; i < providers.length; i++) {
					if (providers[i].getCode() == VoMonBean.UP_CODE) {
						return okLabel;
					}
				}
				// all servers unavailable.
				return downLabel;
			}

		} else {
			return null;
		}
	}
	
}