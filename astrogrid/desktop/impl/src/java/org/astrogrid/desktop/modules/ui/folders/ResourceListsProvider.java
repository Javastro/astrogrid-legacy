/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import java.io.File;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;

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
		        new XQueryList("Recent Changes",
		                "let $thresh := current-dateTime() - xs:dayTimeDuration('P30D')\n" // month
		                + "let $dthresh := current-date() - xs:dayTimeDuration('P30D')\n" // month
		                + "for $r in //vor:Resource[not (@status='inactive' or @status='deleted')]\n"
		                + "where  ($r/@updated castable as xs:dateTime and xs:dateTime($r/@updated) > $thresh)\n"
		                + "or ($r/@updated castable as xs:date and xs:date($r/@updated) > $dthresh)\n"
		                + "or ($r/@created castable as xs:dateTime and xs:dateTime($r/@created) > $thresh)\n"
		                + "or ($r/@created castable as xs:date and xs:date($r/@created) > $dthresh)\n"
		                + "return $r"
		        ) //@todo need to find a way to avoid caching - or to control the caching period of this entry.
		        // I suppose default cache is 3 days - that's not too bad.
		        , new StaticList("VO taster list", new String[]{
		                "ivo://irsa.ipac/2MASS-PSC"
		                    ,"ivo://uk.ac.cam.ast/2dFGRS/object-catalogue/ceaApplication"
		                    ,"ivo://nasa.heasarc/skyview/dss2"
		                    ,"ivo://mast.stsci/siap-cutout/goods.hst"
		                    ,"ivo://stecf.euro-vo/SSA/HST/FOS"
		                    ,"http://leda.univ-lyon1.fr" //URRRK?
		                    ,"ivo://org.astrogrid/HyperZ"
		                    ,"ivo://wfau.roe.ac.uk/sdssdr5-dsa/cone"
		                    ,"ivo://wfau.roe.ac.uk/schlegeldustmaps"
		                    ,"ivo://nasa.heasarc/skyview/sdss"
		                    ,"ivo://org.astrogrid/Starburst99"
		                    ,"ivo://wfau.roe.ac.uk/ssa-dsa/ceaApplication"
		                    ,"ivo://nasa.heasarc/rc3"
		                    ,"ivo://wfau.roe.ac.uk/ukidssDR2-dsa/ceaApplication"		                      
		        })
		        
		        // examples by service type
		        , new StaticList("Cone search examples", new String[]{
		                    "ivo://irsa.ipac/2MASS-XSC"
		                    , "ivo://irsa.ipac/2MASS-PSC"
		                    , "ivo://wfau.roe.ac.uk/6df-dsa/cone"
		                    , "ivo://wfau.roe.ac.uk/first-dsa/cone"
		                    , "ivo://nasa.heasarc/iraspsc"
		                    , "ivo://wfau.roe.ac.uk/rosat-dsa/cone"
		                    , "ivo://wfau.roe.ac.uk/sdssdr5-dsa/cone"
		                    , "ivo://wfau.roe.ac.uk/ssa-dsa/cone"
		                    , "ivo://ned.ipac/Basic_Data_Near_Position"
		                    , "ivo://nasa.heasarc/rc3"
		                    , "ivo://fs.usno/cat/usnob"  
		        })
		        , new StaticList("Image access examples",new String[]{
		                "ivo://irsa.ipac/2MASS-ASKYW-AT"
		                    ,"ivo://nasa.heasarc/skyview/dss2"
		                    ,"ivo://nasa.heasarc/skyview/first"
		                    ,"ivo://mast.stsci/siap-cutout/goods.hst"
		                    ,"ivo://nasa.heasarc/skyview/halpha"
		                    ,"ivo://uk.ac.cam.ast/IPHAS/images/SIAP"
		                    ,"ivo://nasa.heasarc/skyview/nvss"
		                    ,"ivo://nasa.heasarc/skyview/rass"
		                    ,"ivo://nasa.heasarc/skyview/sdss"
		        })
		        , new SmartList("Spectrum access examples","type = spectrum")
		        ,new SmartList("Remote applications","type = CeaApplication")
		        ,new StaticList("Queryable database examples",new String[]{
		                "ivo://uk.ac.cam.ast/2dFGRS/object-catalogue/ceaApplication"
		                    ,"ivo://wfau.roe.ac.uk/6df-dsa/ceaApplication"
		                    ,"ivo://dev2.star.le.ac.uk/mysql-first-roe/ceaApplication"
		                    ,"ivo://wfau.roe.ac.uk/iras-dsa/ceaApplication"
		                    ,"ivo://wfau.roe.ac.uk/rosat-dsa/ceaApplication"
		                    ,"ivo://wfau.roe.ac.uk/sdssdr5-dsa/ceaApplication"
		                    ,"ivo://uk.ac.cam.ast/SWIRE/Catalogue/ceaApplication"
		                    ,"ivo://wfau.roe.ac.uk/ssa-dsa/ceaApplication"
		                    ,"ivo://wfau.roe.ac.uk/twomass-dsa/ceaApplication"
		                    ,"ivo://wfau.roe.ac.uk/ukidssDR2-dsa/ceaApplication"		                
		        })
		        
		        // examples by wavelength / field.
		        ,new SmartList("IR redshift","(ucd = REDSHIFT) AND (waveband = Infrared)")
		        ,new SmartList("Solar services","subject=solar")
		        , new StaticList("SWIFT follow up",new String[]{
		                "ivo://sdss.jhu/openskynode/PSCZ"
		                    ,"ivo://nasa.heasarc/rassvars"
		                    ,"ivo://nasa.heasarc/rassbsc"
		                    ,"ivo://nasa.heasarc/rassfsc"
		                    ,"ivo://wfau.roe.ac.uk/sdssdr5-dsa/TDB"
		                    ,"ivo://wfau.roe.ac.uk/ssa-dsa/cone"
		                    ,"ivo://CDS/VizieR/I/267/out"
		                    ,"ivo://ned.ipac/Basic_Data_Near_Position"
		                    ,"ivo://fs.usno/cat/usnob"
		                    ,"ivo://nasa.heasarc/xmmssc"		                
		        })
		        , new SmartList("Radio images","(waveband = Radio) and (type = Image)")
		        ,new SmartList("Vizier AGN tables","(publisher = vizier) and (subject = agn)") 
				,new SmartList("VOEvent services","default=voevent")
		        
		};
		for (int i = 0; i < folders.length; i++) {
			ResourceFolder f = folders[i];
			//f.setFixed(true);
			getList().add( f);
		}
		} catch (InvalidArgumentException e) {
			throw new RuntimeException("Programming Error",e);
		}
	}

}
