package org.eclipse.ntis.model.ndl;

class Device extends RDFClass{
	Device(){
		super("Device")
		parentClasses = [NetworkElement.class]
	}
	
}
