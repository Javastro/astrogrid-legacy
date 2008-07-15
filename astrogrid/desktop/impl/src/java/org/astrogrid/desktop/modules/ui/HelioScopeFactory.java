/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.Factory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.HeadClauseSRQLVisitor;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQLParser;

/** factory that builds instances of helioscope.
 * works by sening a pre-canned subset of resources to astroscoope.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 9, 20073:31:15 PM
 */
public class HelioScopeFactory implements HelioScopeInternal {

    private final Factory factory;
    private List helioscopeResources; // lazily initialized list
    public void show() {
        create();
    }

    public Object create() {
        if (helioscopeResources == null) {
            throw new ApplicationRuntimeException("List of Helioscope Services has not yet been downloaded - please try again in a moment");
        }
            AstroScopeInternal nu = (AstroScopeInternal) factory.create();
            nu.runSubsetAsHelioscope(helioscopeResources);           
            return nu;        
    }

    public HelioScopeFactory(Factory factory,UIContext context,final Registry reg) {
        super();
        this.factory = factory;

        new BackgroundWorker(context,"Listing Helioscope Services") {

            protected Object construct() throws Exception {
                SRQL srql = new SRQLParser(QUERY).parse();
                String xq = new HeadClauseSRQLVisitor().build(srql,null);
                Resource[] resources = reg.xquerySearch(xq);
                return Arrays.asList(resources);
            }
            protected void doFinished(Object result) {
                helioscopeResources= (List)result;                
            }
        }.start();
    }

    public static final String QUERY = "(subject=solar or subject=stp) and (type=Time)";
}
