package org.apache.ntis.inventory.service;

import java.sql.Date;

import javax.persistence.Basic;

import org.apache.inventory.api.InventoryService;
import org.apache.inventory.model.InventoryIds;
import org.apache.inventory.model.InventoryList;
import org.apache.inventory.model.InventoryStatusType;
import org.apache.inventory.model.InventoryType;
import org.apache.inventory.model.ObjectFactory;
import org.apache.ntis.common.LoggerFactory;
import org.apache.ntis.log.time.BasicTimedInterceptor;
import org.apache.ntis.logging.LogLevel;
import org.apache.ntis.logging.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.service.inventory.dao.IDataService;
import com.service.inventory.dao.Product;

@Endpoint
public class InventoryServiceImpl implements InventoryService, InitializingBean {

	@Value("${spring.application.name}")
	private String serviceName;
	
	
	@Autowired
    private IDataService daoManager;
	
	@Autowired
	private BasicTimedInterceptor timedInterceptor;

    @Override
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "InventoryIds")
    @ResponsePayload
    public InventoryList getInventory(@RequestPayload InventoryIds ids) {
    	timedInterceptor.startMethod();
        try {
        	Thread.sleep(100);
            ObjectFactory factory = new ObjectFactory();
            InventoryList responses = factory.createInventoryList();
            for (Long skuid : ids.getSkuids()) {
                Product p = daoManager.findProductBySkid(skuid);
                if (p != null) {
                    InventoryType resp = factory.createInventoryType();
                    resp.setName(p.getName());
                    resp.setPrice(p.getPrice());
                    resp.setStatus(p.getStatus());
                    responses.getInventory().add(resp);
                }
            }
//            for (String name : appcontext.getBeanDefinitionNames()) {
//                Object b = appcontext.getBean(name);
//                System.err.println("Bean=" + name + " Type=" + b.toString());
//            }
            return responses;
        } catch (Exception e) {
            e.printStackTrace();
		} finally {
			timedInterceptor.endMethod("InventoryServiceImpl");
		}
        return null;
    }

    // @PostConstruct
    public void init() {
        Logger log = LoggerFactory.getInstance().getLogger();
        log.message(LogLevel.INFO, "Intializing Inventory Service...");
        log.message(LogLevel.INFO, "Adding Data to Product Table.............");
        // (1, '2012-03-25', '2012-03-25', 'Des for Product1', 'Product1',
        // 10, 0, 0),
        // (2, '2012-03-25', '2012-03-25', 'Des for Product2', 'Product2',
        // 20, 0, 0);
        Product p = new Product();
        p.setSkuid(1L);
        p.setDateCreated(Date.valueOf("2012-03-25"));
        p.setDateCreated(Date.valueOf("2012-03-25"));
        p.setDescription("Des for Product1");
        p.setName("Product1");
        p.setPrice(10);
        p.setQuantity(1);
        p.setStatus(InventoryStatusType.IN_STOCK);
        daoManager.saveProduct(p);

        Product p1 = new Product();
        p1.setSkuid(1234L);
        p1.setDateCreated(Date.valueOf("2012-03-25"));
        p1.setDateCreated(Date.valueOf("2012-03-25"));
        p1.setDescription("Des for Product2");
        p1.setName("Product2");
        p1.setPrice(20);
        p1.setQuantity(0);
        p1.setStatus(InventoryStatusType.BACK_ORDERABLE);
        daoManager.saveProduct(p1);
        log.message(LogLevel.INFO, "Intialized Inventory Service successfully");

        /**
        System.err.println("BUNDLE CONTEXT=" + bundlecontext);
        if (bundlecontext != null) {

            try {
                ServiceReference sRef = bundlecontext
                        .getServiceReference(ExtHttpService.class.getName());

                System.err.println("ServiceReference sref=" + sRef);
                if (sRef != null) {

                    log.message(LogLevel.ERROR, "About to call schemahandler");
                    // Class.forName("org.springframework.context.config.ContextNamespaceHandler").newInstance();

                    ExtHttpService service = (ExtHttpService) bundlecontext
                            .getService(sRef);
                    System.err.println("Activator HttpService" + service);
                    HttpContext hcont = service.createDefaultHttpContext();
                    Properties initparams = new Properties();
                    initparams.put("transformWsdlLocations", "true");
                    // initparams.put("contextClass",
                    // OsgiBundleXmlWebApplicationContext.class);
                    initparams.put("contextConfigLocation", "");
                    // "spring-ws-servlet.xml");

//                    Class.forName("com.sun.xml.internal.messaging.saaj.soap.SAAJMetaFactoryImpl");

                    System.err.println("Activator HttpService 1");

                    MessageDispatcherServlet servlet = appcontext.getBean(
                            "dispatcher", MessageDispatcherServlet.class);

                    OsgiServletFilter filter = appcontext.getBean("osgifilter",
                            OsgiServletFilter.class);

                    //Current approach of filter matching uses Pattern.Compile ;-)
                    //org.apache.felix.http.base.internal.handler.FilterHandler
                    service.registerFilter(filter, "/inventory/.*", null, 1, hcont);

                    service.registerServlet("/inventory", new MessageDispatcherServlet(), initparams,
                            hcont);
                    System.err.println("Activator HttpService 2");
                    log.message(LogLevel.INFO,
                            "successfully registered Inventory Service Server");
                } else {
                    log.message(LogLevel.ERROR, "Cannot find http service");
                    throw new Exception("Cannot find http service");

                }
            } catch (Throwable e) {
                e.printStackTrace();
                log.message(LogLevel.ERROR,
                        "Error in starting Inventory Service Server", e);
                throw new RuntimeException(e);
            }
        } else {
            log.message(LogLevel.ERROR, "BUNDLE CONTEXT IS NULL");
        }
        **/

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

	public String getServiceName() {
		return serviceName;
	}
}
