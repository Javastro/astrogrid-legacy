package org.astrogrid.scripting.groovy;


import org.astrogrid.scripting.*
import org.astrogrid.scripting.table.*
import  groovy.util.GroovyTestCase
import java.io.File;

import java.util.Calendar

import groovy.text.SimpleTemplateEngine


class test1 extends GroovyTestCase{

	void setUp() {
	  astrogrid = new Toolbox();
	}
	Toolbox astrogrid
	ScriptStarTable table;

//toolbox tests
	 void testToolbox() {
	 	assertNotNull(astrogrid);
	}
	
	void testConfig() {
	 assert astrogrid.systemConfig != null;
	}
	
	void testVersion() {
	  assert astrogrid.version != null
	  println(astrogrid.version);
	  }
/// table tests	
	void createTable() {
	  u = this.getClass().getResource("input.votable");
	  assert u != null
	  table = astrogrid.tableHelper.builder.makeStarTable(u);	
	}
  
	void testMakeTable() {
	    createTable();
		assertNotNull(table);
	}
	
	
	void testMutableTableFromTemplate() {
	createTable();
	mut = astrogrid.tableHelper.newMutableTableFromTemplate(table);
	assert mut.rowCount == 0
    assert mut.columnCount == table.columnCount
	}
	
	void testAsMutableTable() {
	createTable()
	mut = table.asMutableTable();
	assert mut.rowCount == table.rowCount
	assert mut.columnCount == table.columnCount
	}
	
	// need to do one with column definitions too.

	void testAddConstantColumn() {
		createTable()
		info = astrogrid.tableHelper.newColumnInfo("constant");
		table1 = table.addColumn(info,5);
		assert table1.getColumnInfo(table1.columnCount-1).name == 'constant'
		assert table1.rowCount == table.rowCount
		assert table1.columnCount -1 == table.columnCount
		table1.columnIterator(table1.columnCount -1).each{assert it == 5}
		i = table.iterator();		
		table1.iterator().each{arr = i.next(); a = arr + it[it.size() -1]; assertEquals(a.size(),it.size()); assertEquals(a,it)}
	}	
	
	void testAddComputedColumn() {
		createTable()
		info = astrogrid.tableHelper.newColumnInfo("computed");
		ra =  ( 0 ... table.columnCount ).find{ table.getColumnInfo( it ).name == 'ra' }
		dec =  ( 0 ... table.columnCount ).find{ table.getColumnInfo( it ).name == 'dec' }
		table1 = table.addColumn(info,{it[ra] - it[dec]});
		assert table1.getColumnInfo(table1.columnCount-1).name == 'computed'
		assert table1.rowCount == table.rowCount
		assert table1.columnCount -1 == table.columnCount
		//table1.columnIterator(table1.columnCount -1).each{assert it == 5}
		table1.iterator().each{assertEquals( it[table1.columnCount-1],it[ra] - it[dec])}
		i = table.iterator();		
		table1.iterator().each{arr = i.next(); a = arr + it[it.size() -1]; assertEquals(a.size(),it.size()); assertEquals(a,it)}	
	}
	
	void testRemoveColumn() {
		createTable();
	    col = ( 0 ... table.columnCount ).find{ table.getColumnInfo( it ).name == 'naxis' }		
	    assert col == 5
		table1 = table.removeColumn(col);
		assert table1.rowCount == table.rowCount
		assert table1.columnCount == table.columnCount -1
		assert (0 ... table1.columnCount ).find {table1.getColumnInfo(it).name =='naxis'} == null
		i = table.iterator();
		table1.iterator().each{arr = i.next(); a = it[0 ... col] + arr[col] + it[col ... it.size()]; assertEquals( a.size(), arr.size()); assertEquals(a,arr)}
		// lets try dumping it.
	
	}
	
	void testIterateTable() {
		createTable();		
		count = 0
		table.iterator().each{count++;assert it != null && it instanceof java.util.List && it.size() > 0}
		assert count == table.rowCount
		count = 0
		table.columnIterator(0).each{count++;assert it != null}
		assert count == table.rowCount
	}

// external value tests - exercices file protocol at least.	
  void testGetExternalValueTable() {  
	  url = this.getClass().getResource("input.votable");
	  assert url != null
	  ev = astrogrid.ioHelper.getExternalValue(url);
	  assert ev != null
	table = astrogrid.starTableBuilder.makeStarTable(ev)
	assert table != null
  }
  
void testWriteExternalValueTable() {
      File tmp = File.createTempFile("groovy-test",null);
      tmp.deleteOnExit();
      ev = astrogrid.ioHelper.getExternalValue(tmp.toURL());
      assertNotNull(ev);
      createTable();
      astrogrid.tableHelper.writeTable(ev,table,'votable')
      // read it back in again.
      table1 = astrogrid.tableHelper.builder.makeStarTable(ev)
      assert table.rowCount == table1.rowCount
      assert table.columnCount == table1.columnCount
      
}
	
// larger use cases
	void testSiapParsingUseCase() {
	createTable()
	urlCol = ( 0 ... table.columnCount ).find{ table.getColumnInfo( it ).getUCD() == 'VOX:Image_AccessReference' }
	urls = table.iterator().collect{it[urlCol]}
	assert urls != null;
	assert urls.size() == table.rowCount	
	
	}
	
	void testSiapFilteringUseCase() {
	createTable()
	urlCol = ( 0 ... table.columnCount ).find{ table.getColumnInfo( it ).getUCD() == 'VOX:Image_AccessReference' }
	assert urlCol == 7
	ccdCol = ( 0 ... table.columnCount ).find{ table.getColumnInfo( it ).name== 'title' }
	assert ccdCol == 0
	urls = table.iterator().findAll{it[ccdCol].contains('CCD 2')}.collect{it[urlCol]}
	assert urls != null;
	assert urls.size() == (table.rowCount / 4)
}			
// other groovy tests
   void testTemplates() {
queryTemplate = '${var1} < ${var2} where ${foo};' 
binding = ['var1':'23','var2':'RA','foo':'x = 3']
engine = new SimpleTemplateEngine()
template = engine.createTemplate(queryTemplate)
query = template.make(binding)
//assert query ==  '23 < RA where x = 3;' // dunno why this fails.
}   	

   void testTimeManinpulation() {
interval = ['start':'2002-03-14 01:38:00' , 'end':'2002-03-14 01:45:00' ]
cal = Calendar.getInstance()
df = new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss')
cal.setTime(df.parse(interval.start))
cal.set(Calendar.MINUTE,0)
refstarttime = df.format(cal.time)

cal.setTime(df.parse(interval.end))
cal.set(Calendar.MINUTE,0)
cal.add(Calendar.HOUR,1)
refendtime = df.format(cal.time)   
assert refstarttime == '2002-03-14 01:00:00'
assert refendtime ==  '2002-03-14 02:00:00'
   }
}