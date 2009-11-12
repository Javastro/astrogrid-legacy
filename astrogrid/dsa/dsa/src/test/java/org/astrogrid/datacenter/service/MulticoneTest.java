package org.astrogrid.datacenter.service;

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.jobs.Job;
import org.astrogrid.dataservice.service.TokenQueue;
import org.astrogrid.dataservice.service.multicone.DsaConeSearcher;
import org.astrogrid.dataservice.service.multicone.DsaQuerySequenceFactory;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowListStarTable;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.Tables;
import uk.ac.starlink.ttools.cone.ConeMatcher;
import uk.ac.starlink.ttools.cone.ConeSearcher;
import uk.ac.starlink.ttools.cone.QuerySequenceFactory;
import uk.ac.starlink.ttools.task.TableProducer;

public class MulticoneTest extends TestCase {

    public MulticoneTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
      SimpleConfig.setProperty("datacenter.cache.directory", "target");
      Job.initialize();
      SampleStarsPlugin.initConfig();

        // Suppress STILTS logging, which does not use log4j.
        Logger.getLogger("uk.ac.starlink").setLevel(Level.WARNING);
    }

    public void testMulticone() throws Exception {
        String catalogID = ConfigFactory.getCommonConfig().getString(
              "datacenter.self-test.catalog", null);
        String tableID = ConfigFactory.getCommonConfig().getString(
              "datacenter.self-test.table", null);
        String catalogName = TableMetaDocInterpreter.getCatalogNameForID(
              catalogID);
        String tableName = TableMetaDocInterpreter.getTableNameForID(
              catalogID,tableID);

        // Test direct JDBC-type cone.
        TokenQueue tq = new TokenQueue(1);
        assertEquals(0, tq.getActiveCount());
        TokenQueue.Token token = tq.getToken();
        assertEquals(1, tq.getActiveCount());
        assertNotNull(token);
        
        // Test ADQL-type cone.
        Principal user = new LoginAccount("UnitTester", "test.org");
        doMulticone(new DsaConeSearcher(catalogName, tableName, user, getClass().getName()));

        // KONA Comment:  "Connection is closed" error from HSQLDB - for 
        // reasons discussed below. TOFIX later.
        //doMulticone(DirectConeSearcher.createConeSearcher(token, catalogName, tableName, false));

        assertEquals(1, tq.getActiveCount());
        token.release();
        assertEquals(0, tq.getActiveCount());
    }

    private void doMulticone(ConeSearcher cs) throws Exception {

        // This one uses table column names.
        QuerySequenceFactory qsf = new DsaQuerySequenceFactory("RA", "Dec", 3.);

        // This one uses UCDs.
        QuerySequenceFactory qsf1 = new DsaQuerySequenceFactory(null, null, 3.);

        // This one uses erroneous column names and should fail.
        QuerySequenceFactory qsfBad =
            new DsaQuerySequenceFactory("no_such_column", "nope", 3.);

        // This one has a very small radius.
        QuerySequenceFactory qsfSmall =
            new DsaQuerySequenceFactory(null, null, 1e-4);

        StarTable cone1 = Tables.randomTable(cs.performSearch(10.1, 0.1, 3.));
        assertEquals(9L, cone1.getRowCount());
        int nConeCol = cone1.getColumnCount();
        assertEquals(6, nConeCol);  // I happen to know

        StarTable in = createTestTable();
        long nInRow = in.getRowCount();
        long nInCol = in.getColumnCount();
        assertTrue(nInCol > 0);

        {
            StarTable multi0 = doMulti(cs, qsfSmall, in, true);
            assertEquals(0, multi0.getRowCount());
        }

        {
            StarTable multi1 = doMulti(cs, qsf, in, true);
            assertEquals(nInRow * 1, multi1.getRowCount());
            assertEquals(nInCol + nConeCol + 1, multi1.getColumnCount());

            multi1 = doMulti(cs, qsf1, in, true);
            assertEquals(nInRow * 1, multi1.getRowCount());
            assertEquals(nInCol + nConeCol + 1, multi1.getColumnCount());

            try {
                multi1 = doMulti(cs, qsfBad, in, true);
                fail("Previous call should have thrown an exception");
            }
            catch (IOException e) {
                assertTrue(e.getMessage().indexOf("no_such_column") >= 0);
            }
        }

        {
            StarTable multi2 = doMulti(cs, qsf, in, false);

            // there are three extra stars in this sample
            assertEquals(nInRow * 9 + 3, multi2.getRowCount());
            assertEquals(nInCol + nConeCol + 1, multi2.getColumnCount());
        }

    }

    private static StarTable doMulti(ConeSearcher cs,
                                     QuerySequenceFactory qsf,
                                     final StarTable in,
                                     boolean bestOnly) throws Exception {
        TableProducer inProd = new TableProducer() {
            public StarTable getTable() {
                return in;
            }
        };
        return Tables.randomTable(new ConeMatcher(cs, inProd, qsf, bestOnly)
                                 .getTable());
    }

    private static StarTable createTestTable() {
        ColumnInfo idInfo = new ColumnInfo("ID", Integer.class, null);
        idInfo.setUCD("meta.id");
        ColumnInfo raInfo = new ColumnInfo("RA", Double.class, null);
        raInfo.setUCD("pos.eq.ra;meta.main");
        ColumnInfo decInfo = new ColumnInfo("Dec", Double.class, null);
        decInfo.setUCD("pos.eq.dec;meta.main");
        ColumnInfo[] infos = new ColumnInfo[] {
            idInfo, raInfo, decInfo,
        };
        RowListStarTable table = new RowListStarTable(infos);
        for (int i = 0; i < 8; i++) {
            double ra = 10. * i + 0.1;
            double dec = 0.1;
            table.addRow(new Object[] {new Integer(i),
                                       new Double(ra), new Double(dec)});
        }
        return table;
    }
}
