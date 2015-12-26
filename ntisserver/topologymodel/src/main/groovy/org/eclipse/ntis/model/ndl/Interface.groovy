package org.eclipse.ntis.model.ndl;

class Interface extends RDFClass{
	Interface(){
		super("Interface")
	}
	Class[]  parentClasses = [NetworkTransportElement.class,ConnectionPoint.class]
}
