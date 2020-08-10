package edu.unicamp.intrig.p5gex.mapServiceBase;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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

import edu.unicamp.intrig.p5gex.mapServiceBase.plugins.MapServicePlugin;

public class MapServiceMain {
    public static void  main(String []args)
    {                        
        ArrayList<MapServicePlugin> pluginsList = new ArrayList<MapServicePlugin>();
        MapServiceParamsArray params;
                
        params=new MapServiceParamsArray(args[0]);
        
        params.initialize();    
        
        
        //TopologiesDataBase sTop = new TopologiesDataBase();
        
        //MultiDomainTEDB mdTed = new MDTEDB();
        //sTop.setMdTed(mdTed);
                
        Lock lock = new ReentrantLock();        
        
        //TMManagementServer TMms=new TMManagementServer(sTop,params,pluginsList);
        //TMms.start();
        
        (new MapServiceInitiater(params, lock, pluginsList)).intiate();
        
    }        
}
