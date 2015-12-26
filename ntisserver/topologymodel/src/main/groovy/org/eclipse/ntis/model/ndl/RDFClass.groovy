package org.eclipse.ntis.model.ndl;
import groovy.lang.MissingMethodException;


import groovy.lang.MissingPropertyException;



import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class RDFClass {
	
	public RDFClass(String arg){
		label = arg		
	}
	
	String label
	org.eclipse.ntis.model.ndl.Name name
	Class[] parentClasses = null
	def RDFClass[] parentObject
	def  properties = []
	def  attributes = []
	
	def plus(Object attr){
		if (attr instanceof RDFAttribute){
			this.attributes << attr
			
			println "added ${attr} as attribute"
			
		}
		else if (attr instanceof RDFProperty){
			this.properties << attr
			attr.enclosingClass = this
			println "added ${attr} as property"
		}else{
			throw new Exception("Cannot handle this type:"+attr)
		}
	}
	
	def getAt(position){
		return this.properties[position]
	}
	
	def putAt(position,attr){
		this.properties[position] = attr
	}
	
	
	def invokeMethod(String method, Object params) {
		def value
		debug "${label}: invoke ${method}"
		//invoke local method first.
		MetaMethod invokableMethod = findMethod(method)
		if (invokableMethod!=null){
			debug "${method} found in class ${label}"
			params.each { debug 'params '+it } 
			return invokableMethod.invoke(this, params)
		}else{
			debug "${method} not found in class ${label}"
		}
		if (parentClasses!=null){
			def position = 0
			debug "Looking in parent"
			if (parentObject == null){
				debug "parent Object is null"
				parentObject = new Object[parentClasses.length]
				parentClasses.each {
					parentObject[position] = parentClasses[position].newInstance();
					position++	
				}
				
			}
			parentObject.each {
				invokableMethod =  it.findMethod(method)
				if (invokableMethod !=null){
					debug "found ${method} on ${it}"					
					value = invokableMethod.invoke(it, params)
					debug 'value:'+ value					
				}else{
					debug "NOT found ${method} on ${it}"	
				}
				
			}
		}
		value
	}
	
	
	def getProperty(String name){
		debug "property =${name}"
		def value
		
//		if (name.contains ('=')){
//			value = attribute( name.substring(1))
//		}
		
		
		if (name.startsWith ('@')){
			value = attribute( name.substring(1))
		}
		try{
			value = this.getMetaClass().getProperty(this,name)
		}catch(MissingPropertyException e){
			//ignore	
		}catch(MissingMethodException e){
			//ignore
		}
		if (value == null){
			def method = "get"+ capitalize(name)
			value = invokeMethod (method, null)
		}
		if (value==null){
			//time for looking at childerns
			value = children(name)
		}
		
		
		value
	}
	
	def children(String name){
		debug "children= ${name}"
		def value = []
		
		properties.each {
			if (it.label.equals(name)){
				value<<it
			}			
		}
		
		value
	}
	
	def attribute(String attr){
		debug "attribute =${attr}"
		def value
		attributes.each{
			if (it.name.equals(attr)){
				value = it
			}
		}
		value
	}
	
	def capitalize(String name){
		return  name[0].toUpperCase() + name.substring( 1)
	}
	
	def Map<String,Method> methods;
	def findMethod(methodName){
		debug "find ${methodName} in ${label}"
		if (methods==null){
			methods = new HashMap<String, Method>();
			List<MetaMethod> mlist = this.metaClass.getMethods()
			mlist.each {
				methods.put it.getName(), it
			}
		}
		
		return methods.get(methodName)
	}
	
	def debug(message){
		//		println message
	}
	
}
