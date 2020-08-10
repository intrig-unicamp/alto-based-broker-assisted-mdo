package edu.unicamp.intrig.p5gex.mapServiceBase.plugins.writer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import edu.unicamp.intrig.p5gex.mapServiceBase.MapServiceParams;

public class MapServiceServer extends TopologyServer {

    public static MapServiceParams topParms;
    private boolean isRunning = false;

    public MapServiceServer(MapServiceParams params, Lock lock) {
        super(params, lock);
        topParms = params;
    }

    public static MapServiceParams getTopologyParams() {
        return topParms;
    }

    @Override
    public void serveTopology() {
        this.run();
    }

    @Override
    public void run() {
        //System.out.println("ALTO Server");

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e1) {

            e1.printStackTrace();
        }
        
        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        System.out.println("Map Server Port: "
                + params.getMapServerPort());
        Server jettyServer = new Server(params.getMapServerPort());
        jettyServer.setHandler(context);
        ServletHolder jerseyServlet = context.addServlet(
                com.sun.jersey.spi.container.servlet.ServletContainer.class,
                "/*");

        jerseyServlet
                .setInitParameter(
                        "com.sun.jersey.config.property.packages",
                        "io.swagger.jaxrs.json;io.swagger.jaxrs.listing;edu.unicamp.intrig.p5gex.mapServiceBase.Server.api");

        jerseyServlet.setInitParameter(
                "com.sun.jersey.spi.container.ContainerRequestFilters",
                "com.sun.jersey.api.container.filter.PostReplaceFilter");

        jerseyServlet.setInitParameter(
                "com.sun.jersey.api.json.POJOMappingFeatures", "true");

        jerseyServlet.setInitOrder(1);

        try {
            isRunning = true;
            jettyServer.start();
            // jettyServer.dumpStdErr();
            jettyServer.join();
        } catch (Exception e) {
            System.out.println(e.getStackTrace().toString());
        } finally {
            jettyServer.destroy();
        }

    }

    @Override
    public boolean isRunning() {
        // TODO Auto-generated method stub
        return isRunning;
    }

    @Override
    public String getPluginName() {
        // TODO Auto-generated method stub
        return "Unify server-exporter";
    }

    @Override
    public String displayInfo() {
        // TODO Auto-generated method stub
        String str = getPluginName() + "\n";
        str += "Status: ";
        if (isRunning())
            str += "running";
        else
            str += "stop";
        str += "\nPort:" + params.getUnifyExportPort();
        return str;
    }
}
