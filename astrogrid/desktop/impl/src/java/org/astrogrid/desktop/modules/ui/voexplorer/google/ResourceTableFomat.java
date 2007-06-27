package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.awt.Color;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.Icon;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.votech.Annotation;
import org.astrogrid.desktop.modules.votech.AnnotationService;
import org.astrogrid.desktop.modules.votech.UserAnnotation;
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
	public ResourceTableFomat(AnnotationService annService,final VoMon vomon,final CapabilityIconFactory capBuilder) {
		super();
		this.vomon = vomon;
		this.annService = annService;
		this.capBuilder = capBuilder;
		this.okLabel = IconHelper.loadIcon("greenled16.png");
		this.downLabel = IconHelper.loadIcon("redled16.png");
		this.unknownLabel = IconHelper.loadIcon("idle16.png");
	}
	private final CapabilityIconFactory capBuilder;
	private final AnnotationService annService;
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

	public  String createTitle(Resource r) {
		if (r == null) {
			return "";
		}
		String title = null;
		int titleLevel = Integer.MAX_VALUE;
		boolean flag = false;
		Color highlight = null;
		int highlightLevel = Integer.MAX_VALUE;
		// check for overrides.
		for (Iterator i = annService.getLocalAnnotations(r); i.hasNext(); ) {
			Annotation a = (Annotation)i.next();
			String t = StringUtils.trimToNull(a.getAlternativeTitle());
			if (t != null && a.getSource().getSortOrder() <= titleLevel) {
				title = t;
				titleLevel = a.getSource().getSortOrder();
			}
			if (a instanceof UserAnnotation) {
				UserAnnotation u = (UserAnnotation)a;
				if (!flag && u.isFlagged()) {
					flag = true;
				}
				if (u.getHighlight() != null && ! u.getHighlight().equals(Color.WHITE) 
						&& u.getSource().getSortOrder() <= highlightLevel) {
					highlight = u.getHighlight();
					highlightLevel = u.getSource().getSortOrder();
				}
			}
		}

		// work out what we've found, and assemble all bits of data into a single formatted string.
		StrBuilder result = new StrBuilder();
		if (flag || title != null|| highlight != null) {
			result.append("<html>");
		}
		
		if (highlight != null) {
			result.append("<body bgcolor='#");
			int i = highlight.getRGB();
			result.append(Integer.toHexString(i).substring(2,8));
			result.append("'>");
		}
		
		if (flag) { // nasty ascii art for now.
			result.append("<b>*</b>");
		}
		
		if (title != null) {
			result.append("<i>").append(title);
		} else {
			result.append(StringUtils.replace(r.getTitle(), "\n", " ") );
		}
		
		return result.toString();
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