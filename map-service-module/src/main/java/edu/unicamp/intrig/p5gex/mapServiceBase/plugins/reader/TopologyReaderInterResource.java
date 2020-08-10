package edu.unicamp.intrig.p5gex.mapServiceBase.plugins.reader;

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
//import org.opendaylight.alto.commons.types.rfc7285.RFC7285Endpoint;
//import org.opendaylight.alto.commons.types.rfc7285.RFC7285Endpoint.AddressGroup;
//import org.opendaylight.alto.commons.types.rfc7285.RFC7285NetworkMap;
//import org.opendaylight.alto.commons.types.rfc7285.RFC7285VersionTag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.unicamp.intrig.p5gex.mapServiceBase.MapServiceParams;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.Property;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.PropertyMap;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.VersionTag;

public class TopologyReaderInterResource extends TopologyReader {
    private boolean isRunning = false;
    private static StatementResult staResult = null;
    private static PropertyMap objPropertyMap = null;
    private static final String IPv4_TYPE = "ipv4";
    private static final String IPv6_TYPE = "ipv6";

    public TopologyReaderInterResource(MapServiceParams params, Lock lock) {
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
            System.out.println("\nClean MAP Service Information");
            clean_DB(params);

            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e1) {

                e1.printStackTrace();
            }

            System.out.println("\nCreate MAP Service Information");

            createPropertyMap();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void createPropertyMap() throws Exception {

        objPropertyMap = new PropertyMap();
        PropertyMap.Meta meta = new PropertyMap.Meta();
        meta.vtag = new VersionTag(params.getMapPluginPropMapName(), getTag(32));
        objPropertyMap.meta = meta;
        objPropertyMap.map = getMap(params);

        create_VersionTag(params);
        // create_AddressType(params);

        create_PID(params);
        create_Prefixes(params);

        create_Relationships_VersionTag_PID(params);
        // create_Relationships_Type_EndPoint(params, IPv4_TYPE);
        // create_Relationships_Type_EndPoint(IPv6_TYPE);
        create_Relationships_PID_Property();

    }

    private void create_Relationships_PID_Property() throws Exception {
        System.out.println("\nCreate Relationship: PID-> Property");
        for (Map.Entry<String, Property.PropertyGroup> entry : objPropertyMap.map
                .entrySet()) {

            for (String strPort : entry.getValue().port) {

                String strQuery = String.format(
                        new StringBuilder()
                                .append("MATCH (p:PID {Name: '%s'}),")
                                .append(" (pp:PortProperty {Name: '%s'})")
                                .append(" MERGE (p)-[:Has_PortProperty]->(pp)")
                                .toString(), entry.getKey(), strPort);

                REST_Query(
                        strQuery,
                        getNeo4jURL(params.getMapPluginServerIP(), Integer
                                .toString(params.getMapPluginServerPort())));
            }

            for (String strNF : entry.getValue().nf) {

                String strQuery = String.format(
                        new StringBuilder()
                                .append("MATCH (p:PID {Name: '%s'}),")
                                .append(" (nf:NFProperty {Name: '%s'})")
                                .append(" MERGE (p)-[:Has_NFProperty]->(nf)")
                                .toString(), entry.getKey(), strNF);

                REST_Query(
                        strQuery,
                        getNeo4jURL(params.getMapPluginServerIP(), Integer
                                .toString(params.getMapPluginServerPort())));
            }
        }

    }

    /*
     * private void create_Relationships_Type_EndPoint(MapServiceParams params,
     * String strType) throws Exception {
     * 
     * System.out.println("\nCreate Relationship: Type->EndPoint"); for
     * (Map.Entry<String, AddressGroup> entry : objNetworkMap.map .entrySet()) {
     * 
     * String strQuery = String.format( new StringBuilder()
     * .append("MATCH (at:AddressType {Type: '%s'}),")
     * .append(" (ep:EndPointAddress {Prefix: '%s'})")
     * .append(" MERGE (at)-[:Type_EndPoint]->(ep)") .toString(), strType,
     * entry.getValue().ipv4.get(0));
     * 
     * REST_Query( strQuery, getNeo4jURL(params.getAltoPluginAltoServerIP(),
     * Integer .toString(params.getAltoPluginAltoServerPort()))); }
     * 
     * }
     */

    private void create_Relationships_VersionTag_PID(MapServiceParams params)
            throws Exception {

        System.out.println("\nCreate Relationship: VersionTag->PID");
        for (Map.Entry<String, Property.PropertyGroup> entry : objPropertyMap.map
                .entrySet()) {

            String strQuery = String
                    .format(new StringBuilder()
                            .append("MATCH (vt:VersionTag {ResourceID: '%s'}),")
                            .append(" (pid:PID {Name: '%s'})")
                            .append(" MERGE (vt)-[:Has_PID]->(pid)").toString(),
                            objPropertyMap.meta.vtag.rid, entry.getKey());

            REST_Query(
                    strQuery,
                    getNeo4jURL(params.getMapPluginServerIP(),
                            Integer.toString(params.getMapPluginServerPort())));
        }

    }

    private void create_Prefixes(MapServiceParams params) throws Exception {

        System.out.println("\nCreate Prefix nodes");
        for (Map.Entry<String, Property.PropertyGroup> entry : objPropertyMap.map
                .entrySet()) {

            for (String strPort : entry.getValue().port) {
                System.out
                        .println("Create Port property with Name: " + strPort);

                String strQuery = String.format(
                        new StringBuilder().append(
                                "MERGE (:PortProperty { Name : '%s' })")
                                .toString(), strPort);

                REST_Query(
                        strQuery,
                        getNeo4jURL(params.getMapPluginServerIP(), Integer
                                .toString(params.getMapPluginServerPort())));
            }

            for (String strNF : entry.getValue().nf) {
                System.out.println("Create NF property with Type: " + strNF);

                String strQuery = String.format(
                        new StringBuilder().append(
                                "MERGE (:NFProperty { Name : '%s' })")
                                .toString(), strNF);

                REST_Query(
                        strQuery,
                        getNeo4jURL(params.getMapPluginServerIP(), Integer
                                .toString(params.getMapPluginServerPort())));
            }
        }

    }

    private static void create_PID(MapServiceParams params) throws Exception {

        System.out.println("\nCreate PID nodes");

        for (Map.Entry<String, Property.PropertyGroup> entry : objPropertyMap.map
                .entrySet()) {

            String strName = String.format("%s", entry.getKey());
            String strQuery = String
                    .format("MERGE (:PID {Name : '%s', unifyslor: '%s', Mem : %s, CPU : %s, Storage: %s })",
                            strName, entry.getValue().unifyslor,
                            entry.getValue().mem, entry.getValue().cpu,
                            entry.getValue().storage);

            System.out.println("Create PID node with Name: " + strName);
            REST_Query(
                    strQuery,
                    getNeo4jURL(params.getMapPluginServerIP(),
                            Integer.toString(params.getMapPluginServerPort())));
        }
    }

    private static Map<String, Property.PropertyGroup> getMap(
            MapServiceParams params) throws Exception {

        Map<String, Property.PropertyGroup> objMap = new LinkedHashMap<String, Property.PropertyGroup>();

        System.out.println("GET Inter-domain Resource Information");

        String strQuery = String.format(new StringBuilder()
                .append("MATCH (d:DOMAIN)-[:hasNode]->(n:Node)")
                .append(" OPTIONAL MATCH (n)-[:hasPort]->(p:Port)")
                .append(" OPTIONAL MATCH (n)-[:supportedNF]->(nf:NF)")
                .append(" RETURN d.ASN as ASN,").append(" n.cpu AS cpu,")
                .append(" n.mem AS mem,").append(" n.storage AS storage,")
                .append(" collect(DISTINCT p.name) AS SAPs,")
                .append(" collect(DISTINCT nf.type) AS NFs").toString());

        Boolean isCompleted = true;

        do {
            REST_Query(
                    strQuery,
                    getNeo4jURL(params.getInterDomainResourceServerIP(),
                            Integer.toString(params
                                    .getInterDomainResourceServerPort())));
            StatementResult staResultRes = staResult;

            strQuery = String.format(new StringBuilder()
                    .append("MATCH (as:AS)")
                    .append(" RETURN as.ASN AS ASN, as.unifyslor AS unifyslor")
                    .toString());

            REST_Query(
                    strQuery,
                    getNeo4jURL(params.getInterDomainTopologyServerIP(),
                            Integer.toString(params
                                    .getInterDomainTopologyServerPort())));

            List<Record> staResultTopo = staResult.list();

            while (staResultRes.hasNext()) {

                Record record = staResultRes.next();

                isCompleted = true;

                if (record.get("ASN").isNull() || record.get("ASN").isEmpty()
                        || record.get("cpu").isNull()
                        || record.get("mem").isNull()
                        || record.get("storage").isNull()

                ) {
                    isCompleted = false;
                    break;
                }

                String strPID = record.get("ASN").asString();

                Property.PropertyGroup pg = new Property.PropertyGroup();

                for (Record recAux : staResultTopo) {
                    //System.out.println(recAux.get("ASN").asString());
                    if (recAux.get("ASN").asString().equals(strPID)) {
                        pg.unifyslor = recAux.get("unifyslor").asString();
                        break;
                    }
                }

                pg.cpu = record.get("cpu").asString();
                pg.mem = record.get("mem").asString();
                pg.storage = record.get("storage").asString();

                List<String> lstPort = new ArrayList<String>();
                for (Object SAP : record.get("SAPs").asList())
                    lstPort.add(String.valueOf(SAP));
                pg.port = lstPort;

                List<String> lstNF = new ArrayList<String>();
                for (Object NF : record.get("NFs").asList())
                    lstNF.add(String.valueOf(NF));
                pg.nf = lstNF;

                objMap.put(strPID, pg);
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

    private void create_AddressType(MapServiceParams params) throws Exception {

        System.out.println("\nCreate AddressType nodes");

        // IPv4
        String strQuery = String.format("MERGE (:AddressType { Type : '%s'})",
                IPv4_TYPE);
        REST_Query(
                strQuery,
                getNeo4jURL(params.getMapPluginServerIP(),
                        Integer.toString(params.getMapPluginServerPort())));

        // IPv6
        strQuery = String.format("MERGE (:AddressType { Type : '%s'})",
                IPv6_TYPE);
        REST_Query(
                strQuery,
                getNeo4jURL(params.getMapPluginServerIP(),
                        Integer.toString(params.getMapPluginServerPort())));

    }

    private void create_VersionTag(MapServiceParams params) throws Exception {
        String strQuery = String.format(
                "MERGE (:VersionTag { ResourceID : '%s', Tag : '%s' })",
                objPropertyMap.meta.vtag.rid, objPropertyMap.meta.vtag.tag);

        System.out.println("\nCreate VersionTag node");
        // System.out.println("ResourceID:" + objNetworkMap.meta.vtag.rid);
        // System.out.println("Tag:" + objNetworkMap.meta.vtag.tag);

        REST_Query(
                strQuery,
                getNeo4jURL(params.getMapPluginServerIP(),
                        Integer.toString(params.getMapPluginServerPort())));

    }

    private static void clean_DB(MapServiceParams params) throws Exception {
        String strQuery;

        // MATCH(n) WHERE labels(n) in
        // ['AddressType','EndPointAddress','PID','VersionTag'] OPTIONAL MATCH
        // (n)-[r]-() return n,r
        strQuery = String
                .format(new StringBuilder()
                        .append("MATCH (n)")
                        .append(" WHERE labels(n) in ['VersionTag', 'PID', 'PortProperty', 'NFProperty', 'Cost', 'Value']")
                        .append(" OPTIONAL MATCH (n)-[r]-()")
                        .append(" DELETE n,r").toString());

        REST_Query(
                strQuery,
                getNeo4jURL(params.getMapPluginServerIP(),
                        Integer.toString(params.getMapPluginServerPort())));
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
