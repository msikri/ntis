package org.eclipse.ntis.server;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


import org.eclipse.ntis.model.ndl.Interface;
import org.eclipse.ntis.model.ndl.Location;
import org.eclipse.ntis.model.ndl.Name;
import org.eclipse.ntis.model.ndl.RDFDocument;
import org.eclipse.ntis.model.ndl.Resource;
import org.apache.ntis.server.datamodel.GetProximityList;
import org.apache.ntis.server.datamodel.GetProximityListResponse;
import org.apache.ntis.server.datamodel.LocationProximityDataType;
import org.apache.ntis.server.datamodel.LocationProximityType;
import org.apache.ntis.server.datamodel.ServiceLocationType;



class NTISAlgo {
	RDFDocument doc;
	ConcurrentHashMap properties = new ConcurrentHashMap<String, Integer>();
	ConcurrentHashMap propertiesVisit = new ConcurrentHashMap<String, Boolean>();
	
	NTISAlgo(){
		ExpandoMetaClass.enableGlobally()
	}
	
	GetProximityListResponse processMessage(GetProximityList list){
		String client = list.getClientlocation();
		ServiceLocationType type = list.getServicelocation();
		List<String>  site = type.getSite();
		doc =  (new CurrentNtisModel()).getRdfDocument()
		assert doc !=null
		
		//		def properties = new ConcurrentHashMap<String, Integer>();
		
		Interface.metaClass.setHops << { int hop ->
			//			println ("SETHOPS"+hop)
			properties[System.identityHashCode(delegate) + "hop"] = hop	
		}
		
		Interface.metaClass.getHops << {
			->
			Integer value = properties[System.identityHashCode(delegate) + "hop"]
			//			println ("GETHOPS"+value)
			if (value==null) {
				value = Integer.MAX_VALUE;
			}
			value
		}
		
		Interface.metaClass.setVisit << { boolean visit->
			println ("SETVISIT"+visit)
			propertiesVisit[System.identityHashCode(delegate) + "visit"] = visit	
		}
		
		Interface.metaClass.getVisit << {
			->
			Boolean value = propertiesVisit[System.identityHashCode(delegate) + "visit"]
			//			println ("GETHOPS"+value)
			if (value==null) {
				value = false;
			}
			value
		}
		
		
		
		String location = client
		info "Location:"+location
		doc.Location.each{
			Location loc = it
			println "NAME"+it.name.text
			Name nameprop  = it.name[0]
			//				info "Looking for Location:"+nameprop.text
			
			if (location.equals(nameprop.text)){
				String about = it.'@rdf:about'.value
				//				info "rdf:about="+about
				info "FOUND Location:"+loc.'@rdf:about'.value
				def devices = []
				
				doc.Device.each {
					def dev = it;
					it.locatedAt.each{					
						//						System.out.println("Values="+it)
						if (about.equals(it.'@rdf:resource'.value)){
							devices<< it.enclosingClass
						}
					}
				}
				
				
				assert devices != null
				
				info "==============Printing devices at"+about
				devices.each{ println it.name[0].text }
				info "END OF Printing devices at"+about
				
				
				devices.each{
					it.hasInterface.each {
						String interfaceresource = it.'@rdf:resource'.value
						println "Interface:"+interfaceresource
						visitInterface(0,interfaceresource)
					}
				}
			}
		}
		
		
		
		
		GetProximityListResponse response = new GetProximityListResponse();
		LocationProximityDataType value = new LocationProximityDataType();
		
		
		println "PRINTING DISTANCE"
		
		site.each{
			location = it
			//			info "Location:"+location
			def interfaces = []
			doc.Location.each{
				Location loc = it
				Name nameprop  = it.name[0]
				//				info "Looking for Location:"+nameprop.text
				
				if (location.equals(nameprop.text)){					
					
					String about = it.'@rdf:about'.value
					doc.Device.each {
						def dev = it;
						it.locatedAt.each{					
							//							System.out.println("Values="+it)
							if (about.equals(it.'@rdf:resource'.value)){
								it.enclosingClass.hasInterface.each{
									String interfaceresource = it.'@rdf:resource'.value
									doc.Interface.each{
										//									println "LOOKING:"+ it.'@rdf:about'.value
										if (it.'@rdf:about'.value.equals(interfaceresource)){
											interfaces << it.getHops()
										}
									}
								}
							}
						}
					}
					
				}
				
			}
			
			int min = minimum(interfaces)
			LocationProximityType v = new LocationProximityType();
			v.setLocation(location);
			v.setProximity(min);
			value.getLocationProximity().add(v);
			println "Location: "+location+ " hops:"+  min
			
		}
		
		
		
		
		
		
		response.setLocationProximityData(value);
		return response
	}
	
	def minimum(List<Integer> interfaces){
		def min = Integer.MAX_VALUE;
		interfaces.each{
			if (min > it){
				min = it
			}
		}
		min
	}
	
	def visitInterface(int currenthops,String interfaceresource){
		//		Interface.metaClass.setHops << { int hop ->
		//			println ("SETHOPS"+hop)
		//			properties[System.identityHashCode(delegate) + "hop"] = hop	
		//		}
		//		
		//		Interface.metaClass.getHops << {
		//			->
		//			Integer value = properties[System.identityHashCode(delegate) + "hop"]
		//			println ("GETHOPS"+value)
		//			if (value==null) {
		//				value = Integer.MAX_VALUE;
		//			}
		//			value
		//		}		
		println "STARTINGVisitInterface" +interfaceresource + " hops:"+ currenthops
		doc.Interface.each{
			println "LOOKING:"+ it.'@rdf:about'.value
			if (it.'@rdf:about'.value.equals(interfaceresource) && (it.visit == false)){
				println "FOUND:"+ it.getHops()
				if (it.getHops() > currenthops){
					println "HOPPING"+ interfaceresource
					it.hops = currenthops
					it.visit = true
					it.connectedTo.each{						
						println "TO VISIT:"+ it.'@rdf:resource'.value
						visitInterface((currenthops+1),it.'@rdf:resource'.value);						
					}
					it.visit = false
				}else{
					println "Already Visited"+ interfaceresource + it.getHops()
				}
			}
		}
		println "RETURNINGVisitInterface" +interfaceresource + " hops:"+ currenthops
	}
	
	def info(String... msg){
		msg.each { 
			System.out.println(it)
		}
	}
}
