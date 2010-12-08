package org.astrogrid.tableserver.metadata;

import org.astrogrid.config.SimpleConfig;
import org.junit.Test;

/**
 * JUnit-4 tests for {@link org.astrogrid.tableserver.metadata.TableMetaDocRenderer}.
 *
 * @author Guy Rixon
 */
public class MetadocRendererTest {

  @Test
  public void testSampleStars() throws Exception {
    SimpleConfig.setProperty(
            "datacenter.querier.plugin",
            "org.astrogrid.tableserver.test.SampleStarsPlugin");
    String html = new TableMetaDocRenderer().renderMetaDoc();
  }

}
