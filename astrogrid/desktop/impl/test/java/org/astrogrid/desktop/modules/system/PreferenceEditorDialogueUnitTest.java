/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**Unit test for the preferenc editor.
 * @author Noel Winstanley
 * @since Jan 12, 20074:39:37 PM
 */
public class PreferenceEditorDialogueUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		preferences = new ArrayList();
		
		
		Preference a= new Preference();
		a.setName("a");
		a.setValue("a");
		a.setModuleName("fred");
		a.setUiName("A");
		a.setDescription("An A");
		preferences.add(a);
		
		a = new Preference();
		a.setName("b");
		a.setValue("b");
		a.setModuleName("fred");
		a.setUiName("B");
		a.setDescription("A B");
		a.setAdvanced(true);;
		preferences.add(a);		

		a = new Preference();
		a.setName("c");
		a.setValue("c");
		a.setModuleName("barney");
		a.setUiName("C");
		a.setDescription("A C");
		preferences.add(a);	
		
		a = new Preference();
		a.setName("d");
		a.setValue("d");
		a.setModuleName("barney");
		a.setUiName("D");
		a.setDescription("A D");
		preferences.add(a);	
		
		a = new Preference();
		a.setName("e");
		a.setValue("e");
		a.setModuleName("wilma");
		a.setUiName("E");
		a.setDescription("An E");
		a.setAdvanced(true);
		preferences.add(a);
		
		aP = new Preference();
		aP.setName("advancedPreference");
		aP.setValue("true");
		aP.setModuleName("view");
		aP.setUiName("Show Advanced Options");
		// no description.
		aP.setAdvanced(false);
		preferences.add(aP);
	}

	List preferences;
	Preference aP;
	protected void tearDown() throws Exception {
		super.tearDown();
		preferences = null;
		aP = null;
	}
	
	// test the processing of the input preferences is correct.
	public void testConstruct() throws Exception {
	
		// override the bit that actuall creates the ui - just check the inputs is correct.
		PreferenceEditorDialogue d = new PreferenceEditorDialogue(preferences,aP,null,null,null) {
			void initUI(List moduleNames, Map basic, Map advanced) {
				assertNotNull(basic);
				assertNotNull(advanced);
				assertNotNull(moduleNames);
				assertNotNull(showAdvancedPreference);
				
				assertEquals(4,moduleNames.size());
				assertEquals(3,basic.size());
				assertEquals(2,advanced.size());
			}
		};
		
	}
	
	public void testMakeForm() throws Exception {
	}
	
	public static void main(String[] args) throws Exception{
		PreferenceEditorDialogueUnitTest t = new PreferenceEditorDialogueUnitTest();
		t.setUp();
		PreferenceEditorDialogue d = new PreferenceEditorDialogue(t.preferences,t.aP,null,null,null);
		d.run();
	}

}
