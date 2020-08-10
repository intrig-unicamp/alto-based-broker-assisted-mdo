package edu.unicamp.intrig.p5gex.interDomainBase;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.JsonNode;

import edu.unicamp.intrig.p5gex.interDomainBase.plugins.InterDomainPlugin;
import edu.unicamp.intrig.p5gex.tedb.MDTEDB;
import edu.unicamp.intrig.p5gex.tedb.MultiDomainTEDB;
import edu.unicamp.intrig.p5gex.interDomainBase.InterDomainInitiater;
import edu.unicamp.intrig.p5gex.interDomainBase.InterDomainParamsArray;
import edu.unicamp.intrig.p5gex.interDomainBase.database.TopologiesDataBase;
//import edu.unicamp.intrig.p5gex.interDomainBase.management.TMManagementServer;
import edu.unicamp.intrig.p5gex.interDomainBase.plugins.InterDomainPlugin;

public class InterDomainMain {
    public static void main(String[] args) {
        ArrayList<InterDomainPlugin> pluginsList = new ArrayList<InterDomainPlugin>();
        InterDomainParamsArray params;

        if (args.length >= 1) {
            params = new InterDomainParamsArray(args[0]);
        } else {
            params = new InterDomainParamsArray();
        }
        params.initialize();

        TopologiesDataBase sTop = new TopologiesDataBase();

        // sTop.addTEDB("255.255.255.255", new SimpleTEDB() );
        //
        // ((SimpleTEDB)sTop.getDB()).createGraph();
        //
        MultiDomainTEDB mdTed = new MDTEDB();
        sTop.setMdTed(mdTed);

        // ((SimpleTEDB)sTop.getDB()).createGraph();
        Lock lock = new ReentrantLock();

        // TMManagementServer TMms=new
        // TMManagementServer(sTop,params,pluginsList);
        // TMms.start();

        (new InterDomainInitiater(sTop, params, lock, pluginsList)).intiate();

    }
}
