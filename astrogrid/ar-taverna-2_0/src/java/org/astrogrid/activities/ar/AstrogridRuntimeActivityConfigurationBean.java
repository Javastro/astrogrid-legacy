package org.astrogrid.activities.ar;

import org.astrogrid.acr.builtin.ModuleDescriptor;

public class AstrogridRuntimeActivityConfigurationBean {

	private ModuleDescriptor md = null;
	
	public void setModuleDescriptor(ModuleDescriptor md) {
		this.md = md;
		
	}
	
	public ModuleDescriptor getModuleDescriptor() {
		return this.md;
	}
	
}
