package org.apache.ntis.felix.custom.internal;

import java.util.Hashtable;

import org.apache.felix.gogo.command.Basic;
import org.apache.ntis.felix.custom.Commands;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        Hashtable props = new Hashtable();
        props.put("osgi.command.scope", "custom");
        props.put("osgi.command.function", new String[] { "startpr", "infopr" });
        context.registerService(Commands.class.getName(),
                new Commands(context), props);
    }

    public void stop(BundleContext context) throws Exception {
        // TODO Auto-generated method stub

    }

}
