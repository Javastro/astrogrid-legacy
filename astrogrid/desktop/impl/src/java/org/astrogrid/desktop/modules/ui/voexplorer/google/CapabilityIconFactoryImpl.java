/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Icon;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.Authority;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Organisation;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.actions.BuildQueryActivity;
import org.astrogrid.desktop.modules.ui.actions.WebInterfaceActivity;
import org.astrogrid.desktop.modules.ui.comp.CombinedIcon;
import org.astrogrid.desktop.modules.ui.scope.ConeProtocol;

/** Nifty little class that looks at a resource, works out what it's capabilties
 * are, and then build a single icon to represent all these.
 * 
 * previously built icons are cached, so they can be reused next time.
 * 
 * also provides functions for creating tooltips describing the icons.
 * 
 * it would be more OO to merge the iconNames, toolTups and tests into a list of
 * 'capabilityDetector' objects, or somesuch. Usually, I'd do this, but suspect
 * that efficiency would suffer, due to the additional indirection to call the test messages.
 * It seems as if this code is in a tight inner loop within the UI display - and so
 * I don't want to slow things down.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 18, 200712:33:42 AM
 */
public class CapabilityIconFactoryImpl implements CapabilityIconFactory {

	public CapabilityIconFactoryImpl() {
		// sanity check..
		if (ICON_NAMES.length != TOOL_TIP_FRAGMENTS.length) {
			throw new RuntimeException("Programming error - icon names and tooltips are of different length");
		}
		icons = new HashMap();
		tips = new HashMap();
		// populate icons map. Careful - icons here must match with order of tests below.
		for (int i = 0; i < ICON_NAMES.length; i++) {
			BitSet key = new BitSet(ICON_NAMES.length);
			key.set(i);
			Icon ic = IconHelper.loadIcon(ICON_NAMES[i]);
			icons.put(key,ic);
			tips.put(ic,TOOL_TIP_FRAGMENTS[i]);
		}
	}
	// take care - these icon names must be in the same order
	// as the toolTipFragments and the tests
	private static final String[] ICON_NAMES = new String[] {
		"cone16.png" // cone
		,"latest16.png"// stap
		,"ssap16.png" // ssap
		,"siap16.png"// siap
		,"filesave16.png"//download
		,"server16.png" // technical system service
		,"unknown_thing16.png" // unknown service
		,"exec16.png" // cea app
		,"db16.png"// query
		,"table16.png" // table.
		,"browser16.png"// browser
		,"organization16.png"// org.
		,"authority16.png"//auth
	};
	
	private static final String[] TOOL_TIP_FRAGMENTS = new String[] {
		 "Catalog cone search service"
		,"Time Series access service (STAP)"
		,"Spectrum access service (SSAP)"
		,"Image access service (SIAP)"
		,"Downloadable Table"
		,"Technical system service"
		,"Unspecified service"
		,"Remote application (CEA)"
		,"Catalog query service (ADQL)"
		,"Table metadata"
		,"Web interface"
		,"Organization"
		,"Authority"
	};
	
	
	private final Map icons;
	private final Map tips;
	
	public Icon buildIcon(Resource r) {
		
		BitSet caps = new BitSet(ICON_NAMES.length);
		int ix = 0;
		// test for each capability type in turn.
		// store each result in a bit set.
		// Carefule - order of tests here must match with order of icons in constructor.
		if (r instanceof Service) {
		caps.set(ix++,r instanceof ConeService || ConeProtocol.isConeSearchableCdsCatalog(r));
		caps.set(ix++,r.getType().indexOf("SimpleTimeAccess") != -1);
		caps.set(ix++,r.getType().indexOf("Spectrum") != -1);		
		caps.set(ix++,r instanceof SiapService);
		caps.set(ix++,ConeProtocol.isCdsCatalog(r));
		caps.set(ix++,SystemFilter.isBoringServiceTitle(r) || SystemFilter.isBoringRelationship(r));
		caps.set(ix++, caps.cardinality() == 0); // a service, but an unrecognized one.
		} else { // just skip these.
			ix+=7;
		}
		caps.set(ix++,r instanceof CeaApplication);
		caps.set(ix++,r instanceof CeaApplication && BuildQueryActivity.hasAdqlParameter((CeaApplication)r));
		caps.set(ix++, hasTabularMetadata(r));
		caps.set(ix++,WebInterfaceActivity.hasWebBrowserInterface(r));
		caps.set(ix++,r instanceof Organisation);
		caps.set(ix++,r instanceof Authority);
		
		//check how many 'capabilities' we've got.
		if (caps.cardinality() ==0) { // nothing.
			return null; 
		} else if (caps.cardinality() == 1) { // single capability - will already be in the map.
			return (Icon)icons.get(caps);
		} else { // a composite of capabilities. may need to build a new icon for this..
			Icon i = (Icon)icons.get(caps);
			if (i == null) { // not there yet.
				// find the individual icons - do this by testing for the same bit set in each of the keys.
				// ignore already existing composite icons.
				Icon[] is = new Icon[caps.cardinality()];
				StringBuffer tip = new StringBuffer();
                tip.append("<html>This resource has multiple capabilities:<br>");
				int j = 0;
				for (Iterator entries = icons.entrySet().iterator(); entries.hasNext(); ) {
					Map.Entry e = (Map.Entry) entries.next();
					BitSet key = (BitSet)e.getKey();
					if (key.cardinality() == 1 && caps.intersects(key)) {
						Icon component = (Icon)e.getValue();
						is[j++] = component;
						tip.append(tips.get(component)).append("<br>");
					}
				}
                tip.append("</html>");
				// merge together what we've got. 
				i = new CombinedIcon(is);
				icons.put(caps,i);
				tips.put(i,tip.toString());
			}
			return i;
		}
	}

    /**
     * @param r
     * @return
     */
    public static boolean hasTabularMetadata(Resource r) {
        return (r instanceof DataCollection  && ((DataCollection)r).getCatalogues().length > 0) 
		        || (r instanceof CatalogService && ((CatalogService)r).getTables().length > 0);
    }

	public String getTooltip(Icon i) {
		return (String)tips.get(i);
	}
}
