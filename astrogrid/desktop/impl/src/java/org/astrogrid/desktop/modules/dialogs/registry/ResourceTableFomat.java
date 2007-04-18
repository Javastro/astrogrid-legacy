package org.astrogrid.desktop.modules.dialogs.registry;

import java.util.Comparator;

import javax.swing.Icon;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.astrogrid.CeaApplication;
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
 * @todo add 'warning' level into this too.
 */

public  class ResourceTableFomat implements AdvancedTableFormat, WritableTableFormat, CheckableTableFormat{
	public ResourceTableFomat(final VoMon vomon, final ListSelection selectedResources, boolean showCheckBox) {
		super();
		this.vomon = vomon;
		this.selectedResources = selectedResources;
		this.showCheckBox = showCheckBox;
		this.okLabel = IconHelper.loadIcon("greenled16.png");
		this.downLabel = IconHelper.loadIcon("redled16.png");
		this.unknownLabel = IconHelper.loadIcon("idle16.png");
	}
	private final Icon okLabel;
	private final Icon downLabel;
	private final Icon unknownLabel;
	private final boolean showCheckBox;
	private static final int COLUMN_COUNT = 4;
	private final ListSelection selectedResources;
	private final VoMon vomon;
	// unsure whether  this is used iin glazed lists.
	public boolean getChecked(Object arg0) {
		return selectedResources.getSelected().contains(arg0);  // suspect this is going to be quite inefficient. crap.
	}

	// makes a checkbox appear in col 1 if parent is application launcher or workflow builder, not registry browser
	public Class getColumnClass(int columnIndex) {
		switch(columnIndex) {
		case 0:
			return showCheckBox ? Boolean.class : Object.class;
			//	!(parent.getClass().equals(RegistryBrowserImpl.class))) ?
			// Boolean.class : Object.class;
		case 1:
			return Icon.class;
		case 2:
		case 3:
			return Object.class;
		default:
			throw new IndexOutOfBoundsException("Oversized column ix:" +columnIndex);			
		}
	}		

	public Comparator getColumnComparator(int arg0) {
		switch (arg0) {
		case 0:
			return GlazedLists.booleanComparator();
		case 1:
			return labelComparator;
		case 2:
			return GlazedLists.caseInsensitiveComparator();
		case 3:
			return GlazedLists.comparableComparator();
		default:
			throw new IndexOutOfBoundsException("Oversized column ix:" +arg0);
		}
	}

	/** compare 2 icons. */
	private final Comparator labelComparator = new Comparator() {
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
			return 0; // don't expect this to happen
		}
	};
	
	public int getColumnCount() {
		return COLUMN_COUNT;
	}
	public String getColumnName(int column) {
		switch(column) {
		case 0: return "Selected";
		case 1 :return "Status";
		case 2: return "Title";
		case 3: return "Date";
		default: 
			throw new IndexOutOfBoundsException("Oversized column ix:" +column);
		}
	}
	public Object getColumnValue(Object arg0, int columnIndex) {
		Resource r = (Resource)arg0;
		switch(columnIndex) {
		case 0:
			return Boolean.valueOf(getChecked(arg0));
		case 1:
			return getAvailability(r);
		case 2:
			return createTitle(r);
		case 3:
			return createDate(r);
		default:
			throw new IndexOutOfBoundsException("Oversized column ix:" +columnIndex);
		}
	}


	public boolean isEditable(Object arg0, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return showCheckBox;  //!(parent.getClass().equals(RegistryBrowserImpl.class)));
		case 1:
		case 2:
		case 3:
			return false;
		default:
			throw new IndexOutOfBoundsException("Oversized column ix:" +columnIndex);

		}
	}
	
	public void setChecked(Object arg0, boolean toCheck) {
		if (toCheck) {
			selectedResources.select(arg0);
		} else {
			selectedResources.getTogglingSelected().remove(arg0);
		}
	}
	public Object setColumnValue(Object obj, Object bVal, int columnIndex) {
		switch (columnIndex) {
		case 0:
			setChecked(obj,((Boolean)bVal).booleanValue());
			return null;
		case 1:
		case 2:
		case 3:
			throw new IllegalArgumentException("This column is not editable");
		default:
			throw new IndexOutOfBoundsException("Oversized column ix:" +columnIndex);
		}
	}
	//@todo find some way to prevent item display from screwing up on fast scroll
	// think this is related to using html. If I used a cell renderer instead, I don't think this would be a problem.
	// preferred - 3rd column, with icon indicating vomon status.
	private String createTitle(Resource r) {
		if (r == null) {
			return "";
		}
		String title = StringUtils.replace(r.getTitle(), "\n", " ") ;

		// default - pass thru
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