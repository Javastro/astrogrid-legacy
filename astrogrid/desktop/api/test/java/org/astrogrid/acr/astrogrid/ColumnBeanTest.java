/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import org.astrogrid.acr.ivoa.resource.BaseParamTest;
import org.astrogrid.acr.ivoa.resource.TableDataType;


/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200811:52:22 AM
 */
public class ColumnBeanTest extends BaseParamTest {

    private String name;
    private String description;
    private String ucd;
    private TableDataType datatype;
    private String unit;
    private boolean std;
    private ColumnBean cb;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        name = "a name";
        description = "a descr";
        ucd = "a ucd";
        datatype = new TableDataType();
        unit = "a unit";
        std = true;
        cb = new ColumnBean(name,
                description,
                ucd,
                datatype,
                unit,
                std);
        super.bp = cb;
                
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        cb = null;
        name = null;
        description = null;
        ucd = null;
        datatype = null;
        unit = null;       
    }
    
    public void testColumnDataType() throws Exception {
        assertEquals(datatype,cb.getColumnDataType());
    }
    
    public void testStandard() throws Exception {
        assertTrue(cb.isStd());
    }

}
