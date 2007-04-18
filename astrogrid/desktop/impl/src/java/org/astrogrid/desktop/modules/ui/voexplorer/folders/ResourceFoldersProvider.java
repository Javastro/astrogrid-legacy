/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.folders;

import java.io.File;

import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;

import ca.odell.glazedlists.event.ListEventListener;

/** takes care of persisting resource folders.
 * Provides a single model to which multople {@link RsourceFoldersView} can attach
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 5, 200712:24:41 PM
 */
public class ResourceFoldersProvider extends AbstractFoldersProvider implements ListEventListener {
	public ResourceFoldersProvider(final UIContext parent, Preference workdirPreference) {
		super(parent,new File(new File(workdirPreference.getValue()),"resourceFolders.xml"));
		logger.info("Reading/Writing resource folders to " + storage);
	}

	//	 create some basic entries in the folder list.
	protected void initializeFolderList() {
		ResourceFolder[] folders = new ResourceFolder[] {
				new FilterResourceFolder("All Resources","world16.png",null)
				, new FilterResourceFolder("Catalogues","search16.png","@xsi:type &= '*ConeSearch' or @xsi:type &= '*TabularSkyService'")  //@todo find a better icon.
				, new FilterResourceFolder("Images","search16.png","@xsi:type &= '*SimpleImageAccess'", 300)
				, new FilterResourceFolder("Spectra","search16.png","@xsi:type &= '*SimpleSpectrumAccess' ", 10)
				, new FilterResourceFolder("Long-running tasks","exec16.png","@xsi:type &= '*CeaApplicationType' or @xsi:type &= '*CeaHttpApplicationType'", 100)
				, new FilterResourceFolder("Databases","db16.png","@xsi:type &= '*TabularDb' or @xsi:type &= '*DataCollection'",100)
				, new FilterResourceFolder("Time Series","latest16.png","@xsi:type &= '*SimpleTimeAccess'",100)
				,new QueryResourceFolder("Recent Changes","foldernew16.png",
						"let $thresh := current-dateTime() - xs:dayTimeDuration('P21D')\n"
						+ "let $dthresh := current-date() - xs:dayTimeDuration('P21D')\n"
						+ "for $r in //vor:Resource[not (@status='inactive' or @status='deleted')]\n"
						+ "where  ($r/@updated castable as xs:dateTime and xs:dateTime($r/@updated) > $thresh)\n"
						+ "or ($r/@updated castable as xs:date and xs:date($r/@updated) > $dthresh)\n"
						+ "or ($r/@created castable as xs:dateTime and xs:dateTime($r/@created) > $thresh)\n"
						+ "or ($r/@created castable as xs:date and xs:date($r/@created) > $dthresh)\n"
						+ "return $r"
					) //@todo need to find a way to avoid caching - or to control the caching period of this entry.
					// I suppose default cache is 3 days - that's not too bad.
				//@future add recently used, most used, tagged.
		};
		for (int i = 0; i < folders.length; i++) {
			ResourceFolder f = folders[i];
			f.setFixed(true);
			folderList.add( f);
		}
	}

}
