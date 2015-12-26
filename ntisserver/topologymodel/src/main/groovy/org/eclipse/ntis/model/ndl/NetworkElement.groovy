package org.eclipse.ntis.model.ndl;

class NetworkElement extends RDFClass{
	public NetworkElement() {
		super("NetworkElement")
	}

	//A network element. Thus any object
	//that describes an element in a computer network.
	

	//pesudo testing 
	def String elementLocation = "test2"
	
	public String getElementLocation(){
		println "Me called"
		return elementLocation;
	}
}
