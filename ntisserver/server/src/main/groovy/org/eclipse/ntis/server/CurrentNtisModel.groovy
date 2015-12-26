package org.eclipse.ntis.server;

import org.eclipse.ntis.model.ndl.RDFDocument;


import org.codehaus.groovy.ast.stmt.SwitchStatement;



import groovy.xml.QName;

import java.io.StringWriter; 

import org.eclipse.ntis.model.ndl.ConnectedTo;
import org.eclipse.ntis.model.ndl.Device;
import org.eclipse.ntis.model.ndl.HasInterface;
import org.eclipse.ntis.model.ndl.Interface;
import org.eclipse.ntis.model.ndl.LocatedAt;
import org.eclipse.ntis.model.ndl.Location;
import org.eclipse.ntis.model.ndl.Name;
import org.eclipse.ntis.model.ndl.RDFAbout;
import org.eclipse.ntis.model.ndl.RDFAttribute;
import org.eclipse.ntis.model.ndl.RDFClass;
import org.eclipse.ntis.model.ndl.RDFDocument;
import org.eclipse.ntis.model.ndl.RDFProperty;
import org.eclipse.ntis.model.ndl.Resource;

class CurrentNtisModel {
	
	
	def writer = new StringWriter()
	def builder = new groovy.xml.MarkupBuilder(writer)
	
	def ntisXmlModel(){
		builder.'rdf:RDF'("xmlns:rdf":"http://www.w3.org/1999/02/22-rdf-syntax-ns#","xmlns":"http://www.science.uva.nl/research/air/ndl#" ) {
			Location('rdf:about':"#HQ") { name("HeadQuarters") }
			Location('rdf:about':"#DataCenter1") { name("DataCenter1") }
			Location('rdf:about':"#DataCenter2") { name("DataCenter2") }
			Location('rdf:about':"#DataCenter3") { name("DataCenter3") }
			
			Device('rdf:about':"#RouterHQ"){
				name("RouterHQ")
				locatedAt('rdf:resource':"#HQ")
				hasInterface('rdf:resource':"#RouterHQ:port")
			}
			
			Device('rdf:about':"#RouterD1"){
				name("RouterD1")
				locatedAt('rdf:resource':"#DataCenter1")
				hasInterface('rdf:resource':"#RouterD1:port")
			}
			
			Device('rdf:about':"#RouterD2"){
				name("RouterD2")
				locatedAt('rdf:resource':"#DataCenter2")
				hasInterface('rdf:resource':"#RouterD2:port")
			}
			
			Device('rdf:about':"#RouterD3"){
				name("RouterD3")
				locatedAt('rdf:resource':"#DataCenter3")
				hasInterface('rdf:resource':"#RouterD3:port")
			}
			
			Interface('rdf:about':"#RouterHQ:port"){
				name("RouterHQ:port")
				connectedTo('rdf:resource':"#RouterD1:port")
				connectedTo('rdf:resource':"#RouterD2:port")
			}
			
			Interface('rdf:about':"#RouterD1:port"){
				name("RouterD1:port")
				connectedTo('rdf:resource':"#RouterHQ:port")		
			}
			
			Interface('rdf:about':"#RouterD2:port"){
				name("RouterD2:port")
				connectedTo('rdf:resource':"#RouterHQ:port")
				connectedTo('rdf:resource':"#RouterD3:port")
			}
			
			Interface('rdf:about':"#RouterD3:port"){
				name("RouterD3:port")
				connectedTo('rdf:resource':"#RouterD2:port")
			}
		}
		println writer.toString()
		return writer.toString();
	}
	
	def RDFDocument getRdfDocument(){
		def ndl = new XmlParser().parseText (ntisXmlModel())
		def rdf = new RDFDocument()
		
		
		
		ndl.Device.each{
			def dev = new Device()
			it.attributes().each {key,value->
				handleAttributes(dev, key, value)
			}
			it.children().each { handleProperties(dev,it) }
			rdf + dev
		}
		
		
		ndl.Location.each { 
			def l = new Location()
			it.attributes().each {key,value->
				handleAttributes(l, key, value)
			}
			it.children().each { handleProperties(l,it) }  
			rdf+l
		}
		
		ndl.Interface.each {
			def l = new Interface()
			it.attributes().each {key,value->
				handleAttributes(l, key, value)
			}
			it.children().each { handleProperties(l,it) }  
			rdf+l
		}
		
		assert rdf.Device.size() == 4
		assert rdf.Interface.size() == 4
		assert rdf.Location.size() == 4
		return rdf
	}
	
	def handleProperties(RDFClass c,Node e){
		def RDFProperty prop
		switch(e.name().getLocalPart()){
			case 'name': prop = new Name();	
				break
			case 'locatedAt' : prop = new LocatedAt()
				break
			case 'hasInterface' : prop = new HasInterface()
				break
			case 'connectedTo' : prop = new ConnectedTo()
				break
			default:
				println "Cannot handle Property"+e
		}
		if (prop!=null){
			e.attributes().each{key,value->
				handleAttributes(prop, key, value)
			}
			prop.text = e.value()[0]
			println prop.text
			c+prop
		}
	}
	
	def handleAttributes(c, QName key,String attrvalue){
		def RDFAttribute attr
		switch(key.getLocalPart()){
			case 'about': attr = new RDFAbout()
				break
			case 'resource': attr = new Resource()
				break
			default:
				println "Cannot handle Attr"+key
		}
		
		if (attr!=null){
			attr.value = attrvalue
			println "ATTR"+attr.value
			c + attr
		}
		
	}
	
}
