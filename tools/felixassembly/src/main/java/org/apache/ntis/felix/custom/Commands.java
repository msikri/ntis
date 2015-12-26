package org.apache.ntis.felix.custom;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class Commands {

    private volatile BundleContext cont;

    public Commands(BundleContext context) {
        cont = context;
    }

    @Descriptor("start bundle using bundle list")
    public void startpr(@Descriptor("target textfile") String url) {
        StringBuffer sb = new StringBuffer();

        String location = url.trim();
        LineNumberReader reader = null;
        Bundle bundle = null;
        try {
            InputStream stream = (new URL(location)).openStream();
            reader = new LineNumberReader(new InputStreamReader(stream));
            String line = reader.readLine();
            while (line != null) {
                String mlocation = line.trim();
                if ((mlocation != null) && (!mlocation.equals(""))) {
                    if (mlocation.startsWith(":")) {
                        mlocation = "wrap:mvn" + mlocation;
                    } else {
                        mlocation = "mvn:" + mlocation;
                    }
                    System.out.println("Starting :" + reader.getLineNumber()
                            + ":" + mlocation);
                    bundle = cont.installBundle(mlocation, null);

                    if (bundle != null) {
                        bundle.start();
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(bundle.getBundleId());
                    }

                }
                line = reader.readLine();
            }
        } catch (MalformedURLException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        } catch (IllegalStateException ex) {
            System.err.println(ex.toString());
        } catch (BundleException ex) {
            if (ex.getNestedException() != null) {
                ex.getNestedException().printStackTrace(System.err);
            } else {
                System.err.println(ex.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }

        if (sb.toString().indexOf(',') > 0) {
            System.out.println("Bundle IDs: " + sb.toString());
        } else if (sb.length() > 0) {
            System.out.println("Bundle ID: " + sb.toString());
        }
    }
    @Descriptor("list property")
    public void infopr(@Descriptor("target textfile") String propertyname) {
        
        System.out.println("PROP:"+propertyname + "="+ cont.getProperty(propertyname));
        
        //
        // StringBuffer sb = new StringBuffer();
        //
        // String location = url.trim();
        // LineNumberReader reader = null;
        // Bundle bundle = null;
        // try {
        // InputStream stream = (new URL(location)).openStream();
        // reader = new LineNumberReader(new InputStreamReader(stream));
        // String line = reader.readLine();
        // while (line != null) {
        // String mlocation = line.trim();
        // if ((mlocation != null) && (!mlocation.equals(""))) {
        // if (mlocation.startsWith(":")) {
        // mlocation = "wrap:mvn" + mlocation;
        // } else {
        // mlocation = "mvn:" + mlocation;
        // }
        // System.out.println("Starting :" + reader.getLineNumber()
        // + ":" + mlocation);
        // bundle = cont.installBundle(mlocation, null);
        //
        // if (bundle != null) {
        // bundle.start();
        // if (sb.length() > 0) {
        // sb.append(", ");
        // }
        // sb.append(bundle.getBundleId());
        // }
        //
        // }
        // line = reader.readLine();
        // }
        // } catch (MalformedURLException e) {
        // System.err.println(e.toString());
        // } catch (IOException e) {
        // System.err.println(e.toString());
        // } catch (IllegalStateException ex) {
        // System.err.println(ex.toString());
        // } catch (BundleException ex) {
        // if (ex.getNestedException() != null) {
        // System.err.println(ex.getNestedException().toString());
        // } else {
        // System.err.println(ex.toString());
        // }
        // } catch (Exception ex) {
        // System.err.println(ex.toString());
        // }
        //
        // if (sb.toString().indexOf(',') > 0) {
        // System.out.println("Bundle IDs: " + sb.toString());
        // } else if (sb.length() > 0) {
        // System.out.println("Bundle ID: " + sb.toString());
        // }
    }
}
