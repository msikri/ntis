package org.eclipse.ntis.model.ndl;

class RDFProperty {
	String label
	String text
	def  attributes = []
	RDFClass enclosingClass
	public RDFProperty(String arg) {
		label = arg
	}
	
	
	def plus(Object attr){
		if (attr instanceof RDFAttribute){
			this.attributes << attr
			println "added ${attr} as attribute"
			
		}else{
			throw new Exception("Cannot handle this type:"+attr)
		}
	}
	
	def getProperty(String name){
		debug "property =${name}"
		def value
		if (name.equals("text")){
//			println "TEXT:"+text
			value = text
		}
		if (name.startsWith ('@')){
			return (RDFAttribute) attribute( name.substring(1))
		}
		try{
			value = metaClass.getProperty(this,name)
		}catch(MissingPropertyException e){
			//ignore	
		}catch(MissingMethodException e){
			//ignore
		}
//		println "property =${name} value=${value}"
		value
	}
	
	
	RDFAttribute attribute(String attr){
		debug "attribute =${attr} for ${label}"
		RDFAttribute value
		attributes.each{
			if (it.name.equals(attr)){
				value = it
			}
		}
		debug "attribute =${attr} for ${label} value=${value}"
		value
	}
	
	def debug(message){
		//		println message
	}
}
