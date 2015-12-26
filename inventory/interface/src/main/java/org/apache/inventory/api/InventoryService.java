package org.apache.inventory.api;

import org.apache.inventory.model.InventoryIds;
import org.apache.inventory.model.InventoryList;

public interface InventoryService {

    InventoryList getInventory(InventoryIds skuIds);

    String NAMESPACE_URI = "http://www.ntis.org/inventory";
    
    String getServiceName();
}
