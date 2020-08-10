package edu.unicamp.intrig.p5gex.topologyMapBase.plugins.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.unicamp.intrig.p5gex.topologyMapBase.UnifyTopoModel.model.Link;
import edu.unicamp.intrig.p5gex.topologyMapBase.UnifyTopoModel.model.LinksSchema;
import edu.unicamp.intrig.p5gex.topologyMapBase.UnifyTopoModel.model.Port;
import edu.unicamp.intrig.p5gex.topologyMapBase.UnifyTopoModel.model.PortsSchema;
import edu.unicamp.intrig.p5gex.topologyMapBase.TopologyMapParams;
import edu.unicamp.intrig.p5gex.topologyMapBase.UnifyTopoModel.model.Links;
import edu.unicamp.intrig.p5gex.topologyMapBase.UnifyTopoModel.model.MetadataMetadata;
import edu.unicamp.intrig.p5gex.topologyMapBase.UnifyTopoModel.model.Node;
import edu.unicamp.intrig.p5gex.topologyMapBase.UnifyTopoModel.model.Nodes;
import edu.unicamp.intrig.p5gex.topologyMapBase.UnifyTopoModel.model.Virtualizer;
import edu.unicamp.intrig.p5gex.topologyMapBase.UnifyTopoModel.model.VirtualizerSchema;
import edu.unicamp.intrig.p5gex.topologyMapBase.util.InterDomainTopo;
import edu.unicamp.intrig.p5gex.topologyMapBase.util.DataPathID;

public class TopologyReaderInterTopo extends TopologyReader {
    private boolean isRunning = false;
    private static StatementResult staResult = null;
    private final static String USER_AGENT = "Mozilla/5.0";

    public TopologyReaderInterTopo(TopologyMapParams params, Lock lock) {
        super(params, lock);
        // TODO Auto-generated constructor stub
    }

    public void run() {
        // TODO Auto-generated method stub
        readTopology();
    }

    @Override
    public void readTopology() {
        // TODO Auto-generated method stub
        lock.lock();
        isRunning = true;

        try {
            System.out.println("\nClean AS-level topology information");
            clean_DB(params);
            // try {
            // TimeUnit.SECONDS.sleep(5);
            // } catch (InterruptedException e1) {
            // // TODO Auto-generated catch block
            // e1.printStackTrace();
            // }
            // System.out.println("Create ALTO-AGGREGATION information");

            System.out.println("\nCreate AS-level topology information");
            create_topology(params);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void create_topology(TopologyMapParams params)
            throws Exception {
        for (InterDomainTopo tads : params.getAltoAggregatorPluginTADS()) {

            String response = sendGet("/restconf/data/" + tads.getREST(),
                    tads.getServerIP(), String.valueOf(tads.getServerPort()));

            ObjectMapper mapper = new ObjectMapper();
            VirtualizerSchema objTopology = mapper.readValue(response,
                    VirtualizerSchema.class);

            store_Topology(objTopology, params);
        }
    }

    private static void store_Topology(VirtualizerSchema objTopology,
            TopologyMapParams params) throws Exception {
        String strQuery;

        ObjectMapper mapper = new ObjectMapper();
        Nodes nodes = mapper.convertValue(objTopology.getNodes(), Nodes.class);
        
        Hashtable<Object, Inet4Address> router_id_domain_ed = new Hashtable<Object, Inet4Address>();
        Object router_id_addr = null;
        Object s_router_id_addr = null;
        Object d_router_id_addr = null;

        for (Node objNode : nodes.getNode()) {
            
            String domain_id_str = objNode.getId();
            Inet4Address domain_id = (Inet4Address) Inet4Address
                    .getByName(domain_id_str);                        
            PortsSchema ports = mapper.convertValue(objNode.getPorts(),
                    PortsSchema.class);
            for (Port aPort : ports.getPort()) {
                String router_id = aPort.getId();                
                try { // router_id_addr type: Inet4Address
                    router_id_addr = (Inet4Address) Inet4Address
                            .getByName(router_id);
                } catch (Exception e) { // router_id_addr type: DataPathID
                    router_id_addr = DataPathID.getByName(router_id);
                }
                router_id_domain_ed.put(router_id_addr, domain_id);

            }                                             
            
            strQuery = String.format("MERGE (as:AS { ASN : '%s'})",
                    objNode.getId());

            System.out.println("Create AS node with ASN: " + objNode.getId());

            String unify_slor = null;
            String Ipv4_address = null;
            String Ipv4_prefix = null;
            if (objNode.getMetadata().size() > 0) {
                strQuery += " ON CREATE SET ";
                for (MetadataMetadata meta : objNode.getMetadata()) {

                    if (meta.getKey().equals("unify-slor")) {
                        unify_slor = meta.getValue();
                        strQuery += String.format("as.unifyslor = '%s', ",
                                unify_slor);
                    } else if (meta.getKey()
                            .equals("reachability_ipv4_address")) {
                        Ipv4_address = meta.getValue();
                        strQuery += String.format("as.IPv4Address = '%s', ",
                                Ipv4_address);
                    } else if (meta.getKey().equals("reachability_prefix")) {
                        Ipv4_prefix = meta.getValue();
                        strQuery += String.format("as.IPv4Prefix = %d, ",
                                Integer.parseInt(Ipv4_prefix));
                    }
                }
                strQuery = strQuery.substring(0, strQuery.length() - 2);
            }

            if (unify_slor != null || Ipv4_address != null
                    || Ipv4_prefix != null) {
                strQuery += " ON MATCH SET ";

                if (unify_slor != null)
                    strQuery += String.format("as.unifyslor = '%s', ",
                            unify_slor);
                if (Ipv4_address != null)
                    strQuery += String.format("as.IPv4Address = '%s', ",
                            Ipv4_address);
                if (Ipv4_prefix != null)
                    strQuery += String.format("as.IPv4Prefix = %d, ",
                            Integer.parseInt(Ipv4_prefix));

                strQuery = strQuery.substring(0, strQuery.length() - 2);
            }

            // System.out.println("Cypher query: " + strQuery);

            REST_Query(
                    strQuery,
                    getNeo4jURL(
                            params.getAltoAggregatorPluginAggregatorServerIP(),
                            Integer.toString(params
                                    .getAltoAggregatorPluginAggregatorServerPort())));
        }
        
        for (Link objInterEdge : mapper.convertValue(
                objTopology.getLinks(), LinksSchema.class).getLink()) {

            System.out.println("Looking at edge");
            
            String s_r_id = objInterEdge.getSrc().split("\\[")[2]
                    .split("\\]")[0].split("=")[1];
            System.out.println("Edge Source router_id: " + s_r_id);

            try { // s_router_id_addr type: Inet4Address
                s_router_id_addr = (Inet4Address) Inet4Address
                        .getByName(s_r_id);
            } catch (Exception e) {// s_router_id_addr type: DataPathID
                s_router_id_addr = DataPathID.getByName(s_r_id);
            }
            Inet4Address source_domain_id = router_id_domain_ed
                    .get(s_router_id_addr);
            System.out.println("Edge Source domain_id: " + source_domain_id);

            String d_r_id = objInterEdge.getDst().split("\\[")[2]
                    .split("\\]")[0].split("=")[1];
            System.out.println("Edge Destination router_id: " + d_r_id);
            try { // d_router_id_addr type: Inet4Address
                d_router_id_addr = (Inet4Address) Inet4Address
                        .getByName(d_r_id);
            } catch (Exception e) { // d_router_id_addr type: DataPathID
                d_router_id_addr = DataPathID.getByName(d_r_id);
            }
            Inet4Address dest_domain_id = router_id_domain_ed
                    .get(d_router_id_addr);
            System.out.println("Destination domain_id: " + dest_domain_id);
            
            if (!source_domain_id.equals(dest_domain_id)) {
                System.out.println("Adding interdomain link "
                        + source_domain_id.toString() + "-->"
                        + dest_domain_id.toString());
                
                strQuery = String.format("MATCH (as_src:AS {ASN: '%s'}), (as_dst:AS {ASN: '%s'}) MERGE (as_src)-[:LINK]-(as_dst)",
                        source_domain_id.toString().split("/")[1], dest_domain_id.toString().split("/")[1]);
                
                REST_Query(
                        strQuery,
                        getNeo4jURL(
                                params.getAltoAggregatorPluginAggregatorServerIP(),
                                Integer.toString(params
                                        .getAltoAggregatorPluginAggregatorServerPort())));
            }
        }

    }

    private static String sendGet(String strMethod, String tadsServer,
            String tadsServerPort) {

        String IP = tadsServer.split("//")[1].split(":")[0];
        while (true) {
            try {

                if (!isIpReachable(IP))
                    throw new Exception(String.format(
                            "http://%s is not reachable", IP));
                String url = tadsServer + ":" + tadsServerPort + strMethod;
                // System.out.println("Starting new HTTP connection" + url);

                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj
                        .openConnection();

                // optional default is GET
                con.setRequestMethod("GET");

                // add request header
                con.setRequestProperty("User-Agent", USER_AGENT);

                int responseCode = con.getResponseCode();
                System.out.println("Sending 'GET' request to URL : " + url);
                // System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();

            } catch (Exception e) {
                System.out.println(e.toString());
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /*
     * private static TopNode createTopologyNode(JSONObject jsonNodes) throws
     * Exception { TopNodeMetadata objNodeMet = null; Iterator<?> lstkey =
     * jsonNodes.keys(); String sMetadata = "metadata"; String sID = "id";
     * JSONArray aMetadata; JSONObject jsonMetadata; TopNode objNode = new
     * TopNode(); while (lstkey.hasNext()) { String sKey = (String)
     * lstkey.next(); objNodeMet = new TopNodeMetadata(); if
     * (sKey.equals(sMetadata)) { aMetadata = jsonNodes.getJSONArray(sMetadata);
     * for (int j = 0; j < aMetadata.length(); j++) { jsonMetadata =
     * aMetadata.getJSONObject(j);
     * 
     * System.out.println(jsonMetadata.get("key"));
     * System.out.println(jsonMetadata.get("value"));
     * 
     * if (jsonMetadata.get("key").toString().equals("unify-slor")) {
     * objNodeMet.setEntryPoint5GEx(jsonMetadata.get("value") .toString()); }
     * else if (jsonMetadata.get("key").toString()
     * .equals("reachability_ipv4_address")) {
     * objNodeMet.setIpv4_address(jsonMetadata.get("value") .toString()); } else
     * if (jsonMetadata.get("key").toString() .equals("reachability_prefix")) {
     * objNodeMet.setIpv4_prefix(Integer.parseInt(jsonMetadata
     * .get("value").toString())); } } objNode.setNodeMetadata(objNodeMet); }
     * else if (sKey.equals(sID)) {
     * objNode.setid(jsonNodes.get(sID).toString()); } }
     * 
     * return objNode; }
     */

    private void clean_DB(TopologyMapParams params) throws Exception {
        // TODO Auto-generated method stub
        // MATCH(n) WHERE labels(n) in ['AS'] OPTIONAL MATCH (n)-[r]-() return
        // n,r
        String strQuery = String.format(new StringBuilder().append("MATCH(n)")
                .append(" WHERE labels(n) in ['AS']")
                .append(" OPTIONAL MATCH (n)-[r]-()").append(" DELETE n,r")
                .toString());

        REST_Query(
                strQuery,
                getNeo4jURL(params.getAltoAggregatorPluginAggregatorServerIP(),
                        Integer.toString(params
                                .getAltoAggregatorPluginAggregatorServerPort())));
    }

    private static String getNeo4jURL(String strAltoServer,
            String strAltoServerPort) {
        // "http://192.168.122.1:7474/db/data/"
        return String
                .format("%s:%s/db/data/", strAltoServer, strAltoServerPort);
    }

    private static long REST_Query(String query, String strSERVER) {
        Config config = Config.build()
                .withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig();

        // bolt://172.25.11.4:7687/db/data/
        String IP = strSERVER.split("//")[1].split(":")[0];
        while (true) {
            try {

                if (!isIpReachable(IP))
                    throw new Exception(String.format(
                            "bolt://%s is not reachable", IP));

                Driver driver = GraphDatabase.driver(strSERVER, config);
                Session session = driver.session();

                long startTimeNeo4j = System.currentTimeMillis();

                System.out
                        .println("Neo4j statements executed in Neo4j instance with URL: "
                                + strSERVER);
                //System.out.println("Cypher query: " + query);

                staResult = session.run(query);

                long endTimeNeo4j = System.currentTimeMillis();

                session.close();
                driver.close();

                return endTimeNeo4j - startTimeNeo4j;

            } catch (Exception e) {
                System.out.println(e.toString());
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static boolean isIpReachable(String targetIp) throws IOException {

        boolean result = false;

        InetAddress target = InetAddress.getByName(targetIp);
        result = target.isReachable(0); // timeout 5sec

        return result;
    }

    public boolean isRunning() {
        // TODO Auto-generated method stub
        return isRunning;
    }

    public String getPluginName() {
        // TODO Auto-generated method stub
        String str = getPluginName() + "Status: ";
        if (isRunning())
            str += "running";
        else
            str += "stop";
        str += "\nImporter from TADS";
        return str;
    }

    public String displayInfo() {
        // TODO Auto-generated method stub
        return "TADS importer";
    }
}
