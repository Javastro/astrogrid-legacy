/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import java.io.File;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.TermSRQL;

import ca.odell.glazedlists.event.ListEventListener;

/** takes care of persisting resource folders.
 * Provides a single model to which multople views can attach
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 5, 200712:24:41 PM
 */
public class ResourceListsProvider extends AbstractListProvider implements ListEventListener {
	public ResourceListsProvider(final UIContext parent, Preference workdirPreference, XmlPersist xml) {
		super(parent,new File(new File(workdirPreference.getValue()),"resourceLists.xml"), xml);
		logger.info("Reading/Writing resource folders to " + getStorageLocation());
	}

	//	 create some basic entries in the folder list.
	protected void initializeFolderList() {
		try {		   
		ResourceFolder[] folders = new ResourceFolder[] {

				//new SmartList("Abell clusters","abell")
				new SmartList("Radio & X-ray","(waveband = Radio) AND (waveband = X-ray)")
				,new SmartList("IR Redshift","(ucd = REDSHIFT) AND (waveband = Infrared)")
				/*
				new SmartList("All Resources","world16.png",null)
				, new SmartList("Catalogues","search16.png","@xsi:type &= '*ConeSearch' or @xsi:type &= '*TabularSkyService'")  //@todo find a better icon.
				, new SmartList("Images","search16.png","@xsi:type &= '*SimpleImageAccess'", 300)
				, new SmartList("Spectra","search16.png","@xsi:type &= '*SimpleSpectrumAccess' ", 10)
				, new SmartList("Long-running tasks","exec16.png","@xsi:type &= '*CeaApplicationType' or @xsi:type &= '*CeaHttpApplicationType'", 100)
				, new SmartList("Databases","db16.png","@xsi:type &= '*TabularDb' or @xsi:type &= '*DataCollection'",100)
				, new SmartList("Time Series","latest16.png","@xsi:type &= '*SimpleTimeAccess'",100)
				*/,new XQueryList("Recent Changes",
						"let $thresh := current-dateTime() - xs:dayTimeDuration('P60D')\n"
						+ "let $dthresh := current-date() - xs:dayTimeDuration('P60D')\n"
						+ "for $r in //vor:Resource[not (@status='inactive' or @status='deleted')]\n"
						+ "where  ($r/@updated castable as xs:dateTime and xs:dateTime($r/@updated) > $thresh)\n"
						+ "or ($r/@updated castable as xs:date and xs:date($r/@updated) > $dthresh)\n"
						+ "or ($r/@created castable as xs:dateTime and xs:dateTime($r/@created) > $thresh)\n"
						+ "or ($r/@created castable as xs:date and xs:date($r/@created) > $dthresh)\n"
						+ "return $r"
					) //@todo need to find a way to avoid caching - or to control the caching period of this entry.
					// I suppose default cache is 3 days - that's not too bad.
				//@future add recently used, most used, tagged.
				,new SmartList("Solar","subject=solar")
				,new SmartList("VOEvent","default=voevent")
				,new SmartList("ROE Holdings","id=roe.ac.uk and type=(not service)")
		};
		for (int i = 0; i < folders.length; i++) {
			ResourceFolder f = folders[i];
			f.setFixed(true);
			getList().add( f);
		}
		} catch (InvalidArgumentException e) {
			throw new RuntimeException("Programming Error",e);
		}
	}

}
