package edu.unicamp.intrig.p5gex.interDomainBase.plugins.writer;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

//import es.tid.bgp.bgp4Peer.peer.BGPPeer;
import edu.unicamp.intrig.p5gex.tedb.SimpleTEDB;
import edu.unicamp.intrig.p5gex.interDomainBase.InterDomainParams;
import edu.unicamp.intrig.p5gex.interDomainBase.database.TopologiesDataBase;

//import javax.servlet.Servlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class TopologyServerUnify extends TopologyServer {

    public static TopologiesDataBase actualTed;
    public static InterDomainParams topParms;
    private boolean isRunning = false;

    public TopologyServerUnify(TopologiesDataBase ted,
            InterDomainParams params, Lock lock) {
        super(ted, params, lock);
        actualTed = ted;
        topParms = params;
    }

    public static TopologiesDataBase getActualTed() {
        return actualTed;
    }

    public static InterDomainParams getTopologyParams() {
        return topParms;
    }

    @Override
    public void serveTopology() {
        this.run();
    }

    @Override
    public void run() {

        int x;
        do {

            ted = getActualTed();
            x = 0;
            if (ted != null)
                x = ted.getTeds().size();
        } while (!(x == params.getAltoAggregatorPluginTADS().size() + 1));

        log.info("Unify Topology Server");

        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        log.info("Inter-domain Topology Port: " + params.getUnifyExportPort());
        Server jettyServer = new Server(params.getUnifyExportPort());
        jettyServer.setHandler(context);
        ServletHolder jerseyServlet = context.addServlet(
                com.sun.jersey.spi.container.servlet.ServletContainer.class,
                "/*");

        jerseyServlet
                .setInitParameter(
                        "com.sun.jersey.config.property.packages",
                        "io.swagger.jaxrs.json;io.swagger.jaxrs.listing;edu.unicamp.intrig.p5gex.interDomainBase.UnifyTopoModel.api");

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
            log.severe(e.getStackTrace().toString());
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
