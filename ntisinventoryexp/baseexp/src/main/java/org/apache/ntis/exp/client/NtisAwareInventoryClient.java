package org.apache.ntis.exp.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.inventory.model.InventoryIds;
import org.apache.inventory.model.InventoryList;
import org.apache.ntis.ntisclient.NtisClient;
import org.apache.ntis.server.datamodel.GetProximityList;
import org.apache.ntis.server.datamodel.GetProximityListResponse;
import org.apache.ntis.server.datamodel.LocationProximityType;
import org.apache.ntis.server.datamodel.ServiceLocationType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.service.inventory.client.InventoryClient;

@Component
@Lazy
@ConfigurationProperties
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class NtisAwareInventoryClient implements
		org.apache.inventory.api.InventoryService, InitializingBean {

	@Autowired
	private InventoryClient delegate;

	@Value("${ntisserver.address}")
	private String ntisAddress;

	@Value("${inventoryclient.location}")
	private String inventoryClientLocation;

	@Value("${inventoryserver.locations}")
	private String inventoryServerLocationConfig;

	@Value("${inventoryserver.address}")
	private String inventoryServerAddressConfig;

	private String[] inventoryServerLocations;

	private String[] inventoryServerAddress;

	private List<String> markedDownLocations = new ArrayList<String>();

	private String currentAddress;
	private String currentLocation;

	@Autowired
	private NtisClient ntisClient;

	@Value("${inventoryclient.name}")
	private String clientName;

	public InventoryList getInventory(InventoryIds skuIds) {
		try {
			return delegate.getInventory(skuIds);
		} catch (org.springframework.ws.client.WebServiceIOException io) {
			markedDownLocations.add(currentLocation);
			throw io;
		}
	}

	public void afterPropertiesSet() throws Exception {

		populateConfig();

		discoverInventoryAddress();
	}

	public void discoverInventoryAddress() throws Exception {
		GetProximityList list = new GetProximityList();
		list.setClientlocation(inventoryClientLocation);
		ServiceLocationType type = new ServiceLocationType();
		list.setServicelocation(type);
		type.getSite().addAll(Arrays.asList(inventoryServerLocations));

		GetProximityListResponse response = ntisClient.callService(list,
				ntisAddress);
		List<LocationProximityType> proxiType = response
				.getLocationProximityData().getLocationProximity();

		String location = null;
		if (proxiType.size() > 0) {
			
			for (LocationProximityType proxy : proxiType) {
				if (location == null) {
					location = proxy.getLocation();
					if (markedDownLocations.contains(location)) {
						location = null;
					}
				}
			}

			if (location == null) {
				// all locations markdown.
				// clear marked down and try again
				markedDownLocations.clear();
				discoverInventoryAddress();
				return;
			}

		} else {
			throw new Exception("Cannot find any inventoryservice");
		}

		currentLocation = location;
		String address = null;
		for (int i = 0; i < inventoryServerLocations.length
				&& (address == null); i++) {
			if (inventoryServerLocations[i].equals(currentLocation)) {
				address = inventoryServerAddress[i];
			}
		}

		currentAddress = address;
		delegate.setAddress(currentAddress, currentLocation);

	}

	private void populateConfig() {
		inventoryServerLocations = StringUtils.split(
				inventoryServerLocationConfig, ",");

		inventoryServerAddress = StringUtils.split(
				inventoryServerAddressConfig, ",");

	}

	public String getServiceName() {
		return clientName;
	}

}
