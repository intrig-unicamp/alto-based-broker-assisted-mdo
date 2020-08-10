package edu.unicamp.intrig.p5gex.altoBase.plugins.reader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import javax.ws.rs.core.MediaType;

import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.opendaylight.alto.commons.types.rfc7285.RFC7285Endpoint;
import org.opendaylight.alto.commons.types.rfc7285.RFC7285Endpoint.AddressGroup;
import org.opendaylight.alto.commons.types.rfc7285.RFC7285NetworkMap;
import org.opendaylight.alto.commons.types.rfc7285.RFC7285VersionTag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.unicamp.intrig.p5gex.altoBase.AltoParams;

public class TopologyReaderAggregator extends TopologyReader {
    private boolean isRunning = false;
    private static StatementResult staResult = null;
    private static RFC7285NetworkMap objNetworkMap = null;
    private static final String IPv4_TYPE = "ipv4";
    private static final String IPv6_TYPE = "ipv6";

    public TopologyReaderAggregator(AltoParams params, Lock lock) {
        super(params, lock);
    }

    @Override
    public void run() {
        readTopology();
    }

    @Override
    public void readTopology() {
        lock.lock();
        isRunning = true;

        try {
            System.out.println("\nClean ALTO information");
            clean_DB(params);

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e1) {

                e1.printStackTrace();
            }

            System.out.println("\nCreate ALTO information");

            objNetworkMap = new RFC7285NetworkMap();
            RFC7285NetworkMap.Meta meta = new RFC7285NetworkMap.Meta();
            meta.vtag = new RFC7285VersionTag(params.getAltoPluginNetMapName(),
                    getTag(32));
            objNetworkMap.meta = meta;

            create_VersionTag(params);
            create_AddressType(params);

            create_PID(params);
            create_Prefixes(params);

            create_Relationships_VersionTag_PID(params);
            create_Relationships_Type_EndPoint(params, IPv4_TYPE);
            // create_Relationships_Type_EndPoint(IPv6_TYPE);
            create_Relationships_PID_EndPoint();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void create_Relationships_PID_EndPoint() throws Exception {
        System.out.println("\nCreate Relationship: PID->EndPoint");
        for (Map.Entry<String, AddressGroup> entry : objNetworkMap.map
                .entrySet()) {

            String strQuery = String.format(
                    new StringBuilder().append("MATCH (p:PID {Name: '%s'}),")
                            .append(" (a:EndPointAddress {Prefix: '%s'})")
                            .append(" MERGE (p)-[:Has_EndPoint]->(a)")
                            .toString(), entry.getKey(),
                    entry.getValue().ipv4.get(0));

            REST_Query(
                    strQuery,
                    getNeo4jURL(params.getAltoPluginAltoServerIP(), Integer
                            .toString(params.getAltoPluginAltoServerPort())));
        }

    }

    private void create_Relationships_Type_EndPoint(AltoParams params,
            String strType) throws Exception {

        System.out.println("\nCreate Relationship: Type->EndPoint");
        for (Map.Entry<String, AddressGroup> entry : objNetworkMap.map
                .entrySet()) {

            String strQuery = String.format(
                    new StringBuilder()
                            .append("MATCH (at:AddressType {Type: '%s'}),")
                            .append(" (ep:EndPointAddress {Prefix: '%s'})")
                            .append(" MERGE (at)-[:Type_EndPoint]->(ep)")
                            .toString(), strType, entry.getValue().ipv4.get(0));

            REST_Query(
                    strQuery,
                    getNeo4jURL(params.getAltoPluginAltoServerIP(), Integer
                            .toString(params.getAltoPluginAltoServerPort())));
        }

    }

    private void create_Relationships_VersionTag_PID(AltoParams params)
            throws Exception {

        System.out.println("\nCreate Relationship: VersionTag->PID");
        for (Map.Entry<String, AddressGroup> entry : objNetworkMap.map
                .entrySet()) {

            String strQuery = String
                    .format(new StringBuilder()
                            .append("MATCH (vt:VersionTag {ResourceID: '%s'}),")
                            .append(" (pid:PID {Name: '%s'})")
                            .append(" MERGE (vt)-[:Has_PID]->(pid)").toString(),
                            objNetworkMap.meta.vtag.rid, entry.getKey());

            REST_Query(
                    strQuery,
                    getNeo4jURL(params.getAltoPluginAltoServerIP(), Integer
                            .toString(params.getAltoPluginAltoServerPort())));
        }

    }

    private void create_Prefixes(AltoParams params) throws Exception {

        System.out.println("\nCreate Prefix nodes");
        for (Map.Entry<String, AddressGroup> entry : objNetworkMap.map
                .entrySet()) {

            System.out.println("Create Prefix node with EndPointAddress: "
                    + entry.getValue().ipv4.get(0));

            String strQuery = String.format(
                    new StringBuilder().append(
                            "MERGE (:EndPointAddress { Prefix : '%s' })")
                            .toString(), entry.getValue().ipv4.get(0));

            REST_Query(
                    strQuery,
                    getNeo4jURL(params.getAltoPluginAltoServerIP(), Integer
                            .toString(params.getAltoPluginAltoServerPort())));
        }

    }

    private static void create_PID(AltoParams params) throws Exception {

        System.out.println("\nCreate PID nodes");
        objNetworkMap.map = getMap(params);

        for (Map.Entry<String, AddressGroup> entry : objNetworkMap.map
                .entrySet()) {

            String strName = String.format("%s|%s",
                    entry.getKey().split("\\|")[0],
                    entry.getKey().split("\\|")[1]);
            String strQuery = String.format("MERGE (:PID { Name : '%s' })",
                    strName);

            System.out.println("Create PID node with Name: " + strName);
            REST_Query(
                    strQuery,
                    getNeo4jURL(params.getAltoPluginAltoServerIP(), Integer
                            .toString(params.getAltoPluginAltoServerPort())));
        }
    }

    private static Map<String, AddressGroup> getMap(AltoParams params)
            throws Exception {

        Map<String, RFC7285Endpoint.AddressGroup> objMap = new LinkedHashMap<String, RFC7285Endpoint.AddressGroup>();

        System.out.println("GET AS-level Topology Information");
        String strQuery = String.format(new StringBuilder()
                .append("MATCH (n:AS)").append(" RETURN n.ASN as ASN,")
                .append(" n.unifyslor as unifyslor,")
                .append(" n.IPv4Address as IPv4Address,")
                .append(" n.IPv4Prefix as IPv4Prefix").toString());

        Boolean isCompleted = true;

        do {
            REST_Query(
                    strQuery,
                    getNeo4jURL(params.getAltoPluginAggregatorServerIP(),
                            Integer.toString(params
                                    .getAltoPluginAggregatorServerPort())));

            while (staResult.hasNext()) {

                Record record = staResult.next();

                isCompleted = true;

                if (record.get("ASN").isNull() || record.get("ASN").isEmpty()
                        || record.get("unifyslor").isNull()
                        || record.get("unifyslor").isEmpty()
                        || record.get("IPv4Address").isNull()
                        || record.get("IPv4Address").isEmpty()
                        || record.get("IPv4Prefix").isNull()) {
                    isCompleted = false;
                    break;
                }

                String strPID = record.get("ASN").asString() + "|"
                        + record.get("unifyslor").asString();

                RFC7285Endpoint.AddressGroup ag = new RFC7285Endpoint.AddressGroup();
                List<String> lstIPv4 = new ArrayList<String>();
                lstIPv4.add(record.get("IPv4Address").asString() + "/"
                        + String.valueOf(record.get("IPv4Prefix").asInt()));
                ag.ipv4 = lstIPv4;

                objMap.put(strPID, ag);
            }
        } while (!isCompleted);

        /*
         * Iterator<JsonNode> ite = jsonResult.path("results").findPath("data")
         * .elements();
         * 
         * if (ite.hasNext()) { objMap = new LinkedHashMap<String,
         * RFC7285Endpoint.AddressGroup>(); while (ite.hasNext()) { //JsonNode
         * temp = ite.next().path("row"); Iterator<JsonNode> iteRow =
         * ite.next().path("row").elements();
         * 
         * String strPID = iteRow.next().asText() + "|" +
         * iteRow.next().asText();
         * 
         * RFC7285Endpoint.AddressGroup ag = new RFC7285Endpoint.AddressGroup();
         * List<String> lstIPv4 = new ArrayList<String>();
         * lstIPv4.add(iteRow.next().asText() + "/" + iteRow.next().asText());
         * ag.ipv4 = lstIPv4;
         * 
         * objMap.put(strPID, ag); } }
         */

        return objMap;
    }

    private void create_AddressType(AltoParams params) throws Exception {

        System.out.println("\nCreate AddressType nodes");

        // IPv4
        String strQuery = String.format("MERGE (:AddressType { Type : '%s'})",
                IPv4_TYPE);
        REST_Query(
                strQuery,
                getNeo4jURL(params.getAltoPluginAltoServerIP(), Integer
                        .toString(params.getAltoPluginAggregatorServerPort())));

        // IPv6
        strQuery = String.format("MERGE (:AddressType { Type : '%s'})",
                IPv6_TYPE);
        REST_Query(
                strQuery,
                getNeo4jURL(params.getAltoPluginAltoServerIP(),
                        Integer.toString(params.getAltoPluginAltoServerPort())));

    }

    private void create_VersionTag(AltoParams params) throws Exception {
        String strQuery = String.format(
                "MERGE (:VersionTag { ResourceID : '%s', Tag : '%s' })",
                objNetworkMap.meta.vtag.rid, objNetworkMap.meta.vtag.tag);

        System.out.println("\nCreate VersionTag node");
        // System.out.println("ResourceID:" + objNetworkMap.meta.vtag.rid);
        // System.out.println("Tag:" + objNetworkMap.meta.vtag.tag);

        REST_Query(
                strQuery,
                getNeo4jURL(params.getAltoPluginAltoServerIP(),
                        Integer.toString(params.getAltoPluginAltoServerPort())));

    }

    private static void clean_DB(AltoParams params) throws Exception {
        String strQuery;

        // MATCH(n) WHERE labels(n) in
        // ['AddressType','EndPointAddress','PID','VersionTag'] OPTIONAL MATCH
        // (n)-[r]-() return n,r
        strQuery = String
                .format(new StringBuilder()
                        .append("MATCH (n)")
                        .append(" WHERE labels(n) in ['AddressType','EndPointAddress','PID','VersionTag']")
                        .append(" OPTIONAL MATCH (n)-[r]-()")
                        .append(" DELETE n,r").toString());

        REST_Query(
                strQuery,
                getNeo4jURL(params.getAltoPluginAltoServerIP(),
                        Integer.toString(params.getAltoPluginAltoServerPort())));
    }

    private static String getNeo4jURL(String strAltoServer,
            String strAltoServerPort) {
        // "http://192.168.122.1:7474/db/data/"
        return String.format("%s:%s", strAltoServer, strAltoServerPort);
    }

    private static long REST_Query(String query, String strSERVER) {
        Config config = Config.build()
                .withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig();

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
                // System.out.println("Cypher query: " + query);

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
        // try {
        InetAddress target = InetAddress.getByName(targetIp);
        result = target.isReachable(2000); // timeout 5sec
        // } catch (UnknownHostException ex) {
        // / System.out.println(ex.toString());
        // } catch (IOException ex) {
        // System.out.println(ex.toString());
        // }
        return result;
    }

    public static String getTag(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    @Override
    public boolean isRunning() {
        // TODO Auto-generated method stub
        return isRunning;
    }

    @Override
    public String getPluginName() {
        String str = getPluginName() + "Status: ";
        if (isRunning())
            str += "running";
        else
            str += "stop";
        str += "\nImporter from Aggregator";
        return str;
    }

    @Override
    public String displayInfo() {
        return "Alto-Aggregator importer";
    }
}
