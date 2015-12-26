package org.eclipse.ntis.model.ndl;

import groovy.lang.MissingPropertyException;

class RDFDocument {
	def nodes = []
	def plus(RDFClass attr){
		nodes<< attr
	}
	
	def getAt(position){
		return this.nodes[position]
	}
	
	def putAt(position,attr){
		this.nodes[position] = attr
	}
	
	def Object getProperty(final String property) {
		def value
		
//		println "RDFDocument called with ${property}"
		switch (property){
			case 'nodes':	value = nodes
				break
			case 'Device': 
				value = handleList (Device.class)
				break
			case 'Location':
				value = handleList(Location.class)
				break
			case 'Interface':
				value = handleList(Interface.class)
				break
			default:
				throw new MissingPropertyException("Cannot handle ${property} in RDFDocument");
		}
		value
	}
	
	def handleList(Class c){
		def list = []
		nodes.each {
			if (it.getClass() == c){
				list<<it
			}
		}
		list
	}
	
}
