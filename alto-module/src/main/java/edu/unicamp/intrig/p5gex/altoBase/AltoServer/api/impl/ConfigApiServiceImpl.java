package edu.unicamp.intrig.p5gex.altoBase.AltoServer.api.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.opendaylight.alto.commons.types.rfc7285.MediaType;
import org.opendaylight.alto.commons.types.rfc7285.RFC7285Endpoint;
import org.opendaylight.alto.commons.types.rfc7285.RFC7285JSONMapper;
import org.opendaylight.alto.commons.types.rfc7285.RFC7285NetworkMap;
import org.opendaylight.alto.commons.types.rfc7285.RFC7285VersionTag;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.unicamp.intrig.p5gex.altoBase.AltoServer.api.ConfigApiService;
import edu.unicamp.intrig.p5gex.altoBase.AltoServer.api.NotFoundException;
import edu.unicamp.intrig.p5gex.altoBase.AltoParams;
import edu.unicamp.intrig.p5gex.altoBase.plugins.writer.AltoServer;

public class ConfigApiServiceImpl extends ConfigApiService {
    private static final String IPv4 = "ipv4";
    private static final String IPv6 = "ipv6";
    private static StatementResult staResult = null;

    private RFC7285JSONMapper mapper = new RFC7285JSONMapper();

    private Response fail(Response.Status status, Object data) {
        try {
            String output = (data == null ? "" : mapper.asJSON(data));
            return Response.status(status).entity(output)
                    .type(MediaType.ALTO_ERROR).build();
        } catch (Exception e) {
            System.out.println(data.toString());
            return Response.status(status).type(MediaType.ALTO_ERROR).build();
        }
    }

    private Response success(Object data, String mediaType) {
        try {
            
            return Response.ok().entity(data).build();
            
            //String output = mapper.asJSON(data);
            //return Response.ok(output, mediaType).build();
        } catch (Exception e) {
            System.out.println(data.toString());
            return fail(Status.INTERNAL_SERVER_ERROR, null);
        }
    }

    @Override
    public Response retrieveNetworkMap(String id,
            SecurityContext securityContext) throws NotFoundException {

        String strQuery = String
                .format(new StringBuilder()
                        .append("MATCH (v:VersionTag {ResourceID:'%s'})-[r:Has_PID]->(pid)")
                        .append(" OPTIONAL MATCH (pid)-[e:Has_EndPoint]->")
                        .append("(p)<-[r2:Type_EndPoint]-(type)")
                        .append(" WITH v.Tag AS Tag,pid.Name AS Name,")
                        .append(" COLLECT([type.Type,p.Prefix]) AS p")
                        .append(" RETURN Tag, Name,")
                        .append(" [prefix IN FILTER(pAux in p where pAux[0]='%s') | prefix[1]] AS IPv4,")
                        .append(" [prefix in FILTER(pAux IN p where pAux[0]='%s') | prefix[1]] AS IPv6")
                        .append(" ORDER BY Name").toString(), id, IPv4, IPv6);

        AltoParams params = AltoServer.getTopologyParams();
        String neo4jServer = params.getAltoServerDBip();
        String neo4jServerPort = Integer.toString(params.getAltoServerDBport());

        // REST_Query(strQuery, SERVER_ROOT_URI);
        try {
            REST_Query(strQuery, String.format("%s:%s/db/data/", neo4jServer,
                    neo4jServerPort));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Iterator<JsonNode> queryNeo4j = jsonResult.path("results")
        //        .findPath("data").elements();

        RFC7285NetworkMap map = null;
        String tag = "";
        Map<String, RFC7285Endpoint.AddressGroup> AuxMap = new LinkedHashMap<String, RFC7285Endpoint.AddressGroup>();
                
        System.out.println("START conversion: Cypher result values --> ALTO Network Map");
        
        while (staResult.hasNext()) {
            map = new RFC7285NetworkMap();
            
            Record record = staResult.next();
            
            tag = record.get("Tag").asString();                        
            
            RFC7285Endpoint.AddressGroup ag = new RFC7285Endpoint.AddressGroup();
            String strPID = record.get("Name").asString();

            System.out.println("Detected PID: " + strPID);
            
            //String x = record.get("IPv4").toString();
            //List<String> x = (ArrayList<String>)(ArrayList<?>)record.get("IPv4").asList();  
            //System.out.print(x);
            
            List<String> lstIPv4 = new ArrayList<String>();
            lstIPv4.add(record.get("IPv4").asList().get(0).toString());
             //       createIPList(record.get("Name"). lstQuery.next().elements());
            //List<String> lstIPv6 = createIPList(lstQuery.next().elements());
            List<String> lstIPv6 = new ArrayList<String>();
            if (lstIPv4.size() > 0)
            {
                ag.ipv4 = lstIPv4;
                System.out.println(String.format(
                        "Add IPv4 prefixes list to PID: { '%s' }", lstIPv4));
            }
            if (lstIPv6.size() > 0)
            {
                ag.ipv6 = lstIPv6;
                System.out.println(String.format(
                        "Add IPv6 prefixes list to PID: { '%s' }", lstIPv6));
            }

            AuxMap.put(strPID, ag);
        }
        RFC7285NetworkMap.Meta meta = new RFC7285NetworkMap.Meta();
        meta.vtag = new RFC7285VersionTag(id, tag);
        
        map.meta = meta;
        map.map = AuxMap;

        /*
        if (queryNeo4j.hasNext()) {
            map = new RFC7285NetworkMap();

            Iterator<JsonNode> lstQuery = queryNeo4j.next().path("row")
                    .elements();
            RFC7285NetworkMap.Meta meta = new RFC7285NetworkMap.Meta();
            meta.vtag = new RFC7285VersionTag(id, lstQuery.next().asText());

            Map<String, RFC7285Endpoint.AddressGroup> AuxMap = new LinkedHashMap<String, RFC7285Endpoint.AddressGroup>();
            RFC7285Endpoint.AddressGroup ag = new RFC7285Endpoint.AddressGroup();
            String strPID = lstQuery.next().asText();

            List<String> lstIPv4 = createIPList(lstQuery.next().elements());
            List<String> lstIPv6 = createIPList(lstQuery.next().elements());
            if (lstIPv4.size() > 0)
                ag.ipv4 = lstIPv4;
            if (lstIPv6.size() > 0)
                ag.ipv6 = lstIPv6;

            AuxMap.put(strPID, ag);

            while (queryNeo4j.hasNext()) {
                lstQuery = queryNeo4j.next().path("row").elements();

                lstQuery.next();
                ag = new RFC7285Endpoint.AddressGroup();
                strPID = lstQuery.next().asText();

                lstIPv4 = createIPList(lstQuery.next().elements());
                lstIPv6 = createIPList(lstQuery.next().elements());
                if (lstIPv4.size() > 0)
                    ag.ipv4 = lstIPv4;
                if (lstIPv6.size() > 0)
                    ag.ipv6 = lstIPv6;

                AuxMap.put(strPID, ag);
            }

            map.meta = meta;
            map.map = AuxMap;
        }
        */

        System.out.println("END conversion: Cypher result values --> ALTO Network Map");
        
        if (map == null)
            return fail(Status.NOT_FOUND, id);
        return success(map, MediaType.ALTO_NETWORKMAP);
    }

    private List<String> createIPList(Iterator<JsonNode> lstIp) {
        List<String> lstIP = new ArrayList<String>();
        while (lstIp.hasNext())
            lstIP.add(lstIp.next().toString().replaceAll("\"", ""));

        return lstIP;
    }

    private static long REST_Query(String query, String strSERVER)
            throws Exception {

        Driver driver = GraphDatabase.driver(strSERVER);
        Session session = driver.session();

        long startTimeNeo4j = System.currentTimeMillis();

        System.out.println("Neo4j statements executed in Neo4j instance with URL: " + strSERVER);
        //System.out.println("Cypher query: " + query);
        
        staResult = session.run(query);

        long endTimeNeo4j = System.currentTimeMillis();

        session.close();
        driver.close();

        /*
         * // System.out.println("Query " + query);
         * 
         * // START SNIPPET: queryAllNodes final String txUri = strSERVER +
         * "transaction/commit"; WebResource resource =
         * Client.create().resource(txUri);
         * 
         * String payload = "{\"statements\" : [ {\"statement\" : \"" + query +
         * "\"} ]}";
         * 
         * long startTimeNeo4j = System.currentTimeMillis(); ClientResponse
         * response =
         * resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
         * .type(javax.ws.rs.core.MediaType.APPLICATION_JSON).entity(payload)
         * .post(ClientResponse.class); long endTimeNeo4j =
         * System.currentTimeMillis();
         * 
         * jsonResult = new ObjectMapper().readTree(response
         * .getEntity(String.class));
         * 
         * response.close();
         */
        return endTimeNeo4j - startTimeNeo4j;

    }
}
