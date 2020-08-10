package edu.unicamp.intrig.p5gex.topologyMapBase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import edu.unicamp.intrig.p5gex.topologyMapBase.plugins.AltoAggregatorPlugin;
import edu.unicamp.intrig.p5gex.topologyMapBase.util.UtilsFunctions;

import java.util.concurrent.TimeUnit;

public class TopologyMapMain {
    public static void main(String[] args) {

        /*Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println(UtilsFunctions
                        .executeCommand("../util/neo4j-community-3.1.3/bin/neo4j stop"));
            }
        });

        System.out.println("Starting Neo4j");
        System.out
                .println(UtilsFunctions
                        .executeCommand("../util/neo4j-community-3.1.3/bin/neo4j start"));
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        ArrayList<AltoAggregatorPlugin> pluginsList = new ArrayList<AltoAggregatorPlugin>();
        TopologyMapParamsArray params;

        params = new TopologyMapParamsArray(args[0]);
        
        params.initialize();

        // TopologiesDataBase sTop = new TopologiesDataBase();

        // MultiDomainTEDB mdTed = new MDTEDB();
        // sTop.setMdTed(mdTed);

        Lock lock = new ReentrantLock();

        // TMManagementServer TMms=new
        // TMManagementServer(sTop,params,pluginsList);
        // TMms.start();

        (new TopologyMapModuleInitiater(params, lock, pluginsList))
                .intiate();
    }
}
