package edu.unicamp.intrig.p5gex.mapServiceBase.Server.api.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;

//import org.opendaylight.alto.commons.types.rfc7285.RFC7285Endpoint;
//import org.opendaylight.alto.commons.types.rfc7285.RFC7285JSONMapper;
//import org.opendaylight.alto.commons.types.rfc7285.RFC7285NetworkMap;
//import org.opendaylight.alto.commons.types.rfc7285.RFC7285VersionTag;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableList;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.unicamp.intrig.p5gex.mapServiceBase.MapServiceParams;
import edu.unicamp.intrig.p5gex.mapServiceBase.plugins.writer.MapServiceServer;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostMap;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostType;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.Endpoint;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.MediaType;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.Property;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.PropertyMap;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.QueryPairs;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.RFC7285JSONMapper;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.VersionTag;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostSG.CostSGRequest;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostSG.EdgeReq;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostSG.NF;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostSG.NextHop;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostSG.SAP;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostSG.SG;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.MultiCost.data.MulticostRequest;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.exception.AltoBasicException;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.exception.AltoErrorInvalidFieldValue;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.api.ConfigApiService;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.api.NotFoundException;

//"or-constraints" : [ ["[0] ge 5", "[0] le 10"],
//["[1] eq 0"] ]

public class ConfigApiServiceImpl extends ConfigApiService {
    private static StatementResult staResult = null;

    private RFC7285JSONMapper mapper = new RFC7285JSONMapper();

    public static final String FIELD_PIDS = "pids";
    public static final String FIELD_PID_SOURCE = "srcs";
    public static final String FIELD_PID_DESTINSTION = "dsts";

    public static final String FIELD_COST_TYPE = "cost-type";
    public static final String FIELD_COST_MODE = "cost-mode";
    public static final String FIELD_COST_METRIC = "cost-metric";

    public static final String FIELD_CONSTRAINT = "constraints";

    public static final String FIELD_MULTI_COST_TYPE = "multi-cost-types";

    public static final String FIELD_TESTABLE = "testable-cost-types";

    public static final String FIELD_ORCONSTRAINT = "or-constraints";

    public static final String FIELD_ENDPOINTS = "endpoints";

    public static final String FIELD_SG = "sg";
    public static final String FIELD_SG_PARAMETERS = "parameters";
    public static final String FIELD_SG_NODES_NFS = "node_nfs";
    public static final String FIELD_SG_NODES_SAP = "node_saps";
    public static final String FIELD_SG_NEXT_HOPS = "edge_sg_nexthops";
    public static final String FIELD_SG_REQS = "edge_reqs";

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

            // String output = mapper.asJSON(data);
            // return Response.ok(output, mediaType).build();
        } catch (Exception e) {
            System.out.println(data.toString());
            return fail(Status.INTERNAL_SERVER_ERROR, null);
        }
    }

    protected List<String> arrayNode2List(String field, ArrayNode node) {
        HashSet<String> retval = new HashSet<String>();

        for (Iterator<JsonNode> itr = node.elements(); itr.hasNext();) {
            JsonNode data = itr.next();

            retval.add(data.asText());
        }
        return new LinkedList<String>(retval);
    }

    @Override
    public Response retrieveCostMapSG(String id, String filterJSON,
            SecurityContext securityContext) throws NotFoundException {

        MapServiceParams params = MapServiceServer.getTopologyParams();
        String neo4jServer = params.getMapServerDBip();
        String neo4jServerPort = Integer.toString(params.getMapServerDBport());

        CostMap map = new CostMap();
        Map<String, Map<String, Object>> AuxMap = new LinkedHashMap<String, Map<String, Object>>();

        try {
            CostSGRequest request = getFilterJsonCostMapSG(new ObjectMapper()
                    .readTree(filterJSON));

            for (EdgeReq auxReq : request.sg.reqs) {

                String Aux4 = "WITH DISTINCT FILTER(x IN nodes(p) WHERE 'Node' IN labels(x)) AS nodes1";
                String Aux41 = "";
                String Aux42 = "";
                String Aux5 = "WITH collect(nodes1) AS rels";
                String Aux7 = "WITH extract(rel IN x | rel) as x1, extract(rel IN x | rel.id) AS relsID";
                String Aux71 = "";
                String Aux8 = "WITH x1[LENGTH(x1)-1] AS xx1, x1, relsID";
                String Aux81 = "";
                String strReturn = "";
                String strWithAuxRel = "";
                String strWithAuxDom = "";
                String strWith1 = "";
                String strWith2 = "";
                String strStartBis1 = "startBis:Node";
                String strStartBis2 = "startBis";

                String strQuery = "";

                for (String SGid : auxReq.sg_path) {
                    NextHop nh = request.sg.nexthops.get(SGid);
                    String srcProperty = "name";
                    String srcPropertyval = nh.src_node;
                    String srcLink = "hasPort";
                    String dstProperty = "name";
                    String dstPropertyVal = nh.dst_node;
                    String dstLink = "hasPort";

                    if (nh.src_type.equals("NF")) {
                        srcProperty = "type";
                        srcPropertyval = nh.src_node_nf;
                        srcLink = "supportedNF";
                    }
                    if (nh.dst_type.equals("NF")) {
                        dstProperty = "type";
                        dstPropertyVal = nh.dst_node_nf;
                        dstLink = "supportedNF";
                    }

                    strQuery += String
                            .format(new StringBuilder()
                                    .append("MATCH (%s)-[:%s]->(:%s {%s:'%s'}),")
                                    .append(" (endBis:Node)-[:%s]->(:%s {%s:'%s'})")
                                    .append(" MATCH p=(%s)-[:hasPort*0..]-(endBis)")
                                    .append(" WHERE length(nodes(p)) =1 or (length(nodes(p))>1 and nodes(p)[length(nodes(p))-1]<>%s)")
                                    // .append(" WHERE NOT ANY (ne in nodes(p) where ne.id = %s.id  ) or length(nodes(p)) =1")
                                    .append(" %s%s")
                                    .append(" %s")
                                    .append(" UNWIND rels AS x")
                                    .append(" %s%s%s")
                                    .append(" %s%s%s")
                                    .append(" UNWIND x1 AS y")
                                    .append(" MATCH (startDomain:DOMAIN)-[:hasNode]->(y) ")
                                    .toString(), strStartBis1, srcLink,
                                    nh.src_type, srcProperty, srcPropertyval,
                                    dstLink, nh.dst_type, dstProperty,
                                    dstPropertyVal, strStartBis2, strStartBis2,
                                    Aux4, Aux41, Aux5, Aux7, SGid, Aux71, Aux8,
                                    SGid, Aux81);

                    strStartBis1 = "xx1";
                    strStartBis2 = "xx1";

                    Aux4 = Aux4 + ", relsID" + SGid;

                    Aux41 = ", collect(startDomain.ASN) AS d" + SGid + Aux42;

                    Aux42 = Aux42 + ", d" + SGid;

                    Aux5 = Aux5 + ", relsID" + SGid + ", d" + SGid;

                    Aux71 = Aux71 + ", relsID" + SGid + ", d" + SGid;

                    Aux81 = Aux81 + ", relsID" + SGid + ", d" + SGid;

                    strWithAuxRel = strWithAuxRel + "relsID" + SGid + "+";
                    strWithAuxDom = strWithAuxDom + "d" + SGid + "+";

                    strWith1 = strWith1 + "relsID" + SGid + ", ";

                    // System.out.println(strQuery);
                }

                strWith1 = strWith1.substring(0, strWith1.length() - 2);
                strWith1 += Aux41;
                strWith1 = " WITH " + strWith1;
                strQuery += strWith1;

                strWithAuxRel = strWithAuxRel.substring(0,
                        strWithAuxRel.length() - 1);
                strWithAuxDom = strWithAuxDom.substring(0,
                        strWithAuxDom.length() - 1);

                List<String> lstShortAux = Arrays.asList(Aux42.split(","));

                String strSAux = "";
                String strS = "";
                String strOrderByAux = "";
                String strOrderBy = "";
                for (String str : lstShortAux) {
                    if (!str.isEmpty()) {
                        strSAux = "LENGTH(" + str.trim() + ") AS l"
                                + str.trim() + ", ";
                        strS += strSAux;
                        strOrderByAux = "l" + str.trim() + ", ";
                        strOrderBy += strOrderByAux;
                    }
                }
                strS = strS.substring(0, strS.length() - 2);

                strWith2 = String.format(
                        new StringBuilder().append(
                                " WITH %s AS allRel, %s AS allDom%s,%s")
                                .toString(), strWithAuxRel, strWithAuxDom,
                        Aux71, strS);
                strQuery += strWith2;

                if (request.constraints != null
                        && !request.constraints.get(0).equals("shortest")) {

                    String strConAux = "";
                    String strCon = "";
                    for (String con : request.constraints) {
                        strConAux = "LENGTH(allRel)" + con + " AND ";
                        strCon += strConAux;
                    }

                    strCon = strCon.substring(0, strCon.length() - 5);
                    strCon = " WHERE " + strCon;

                    strQuery += strCon;
                }

                strReturn = "allRel, allDom" + Aux71;
                strQuery += " RETURN " + strReturn;

                if (request.constraints != null
                        && request.constraints.size() == 1
                        && request.constraints.get(0).equals("shortest")) {
                    strOrderBy = strOrderBy.substring(0,
                            strOrderBy.length() - 2);
                    strQuery += " ORDER BY " + strOrderBy + " ASC LIMIT 1";
                }

                try {
                    REST_Query(strQuery,
                            getNeo4jURL(neo4jServer, neo4jServerPort));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Map<String, Object> mapDST = new LinkedHashMap<String, Object>();

                Map<String, Object> SGPathSRC = new LinkedHashMap<String, Object>();

                List<Record> lstResult = staResult.list();

                for (String SGid : auxReq.sg_path) {

                    NextHop nh = request.sg.nexthops.get(SGid);

                    Map<String, Object> SGPathDST = new LinkedHashMap<String, Object>();

                    List<List<Object>> lstCost = new ArrayList<List<Object>>();

                    for (Record record : lstResult) {

                        // Record record = staResult.next();

                        List<Object> lstMdO = new ArrayList<Object>();
                        for (Object mdo : record.get("d" + SGid).asList())
                            lstMdO.add(String.valueOf(mdo));

                        if (!lstCost.contains(lstMdO))
                            lstCost.add(lstMdO);
                    }

                    String srcProperty = "name";
                    String srcPropertyval = nh.src_node;
                    String srcLink = "hasPort";
                    String dstProperty = "name";
                    String dstPropertyVal = nh.dst_node;
                    String dstLink = "hasPort";

                    if (nh.src_type.equals("NF")) {
                        srcProperty = "type";
                        srcPropertyval = nh.src_node_nf;
                        srcLink = "supportedNF";
                    }
                    if (nh.dst_type.equals("NF")) {
                        dstProperty = "type";
                        dstPropertyVal = nh.dst_node_nf;
                        dstLink = "supportedNF";
                    }

                    // createCostMapPathVector(id, request.costType.mode,
                    // request.costType.metric, nh.src_type,
                    // srcPropertyval, nh.dst_type, dstPropertyVal,
                    // lstCost);

                    SGPathDST.put(nh.dst_node, lstCost);

                    SGPathSRC.put(nh.src_node, SGPathDST);
                }

                mapDST.put(auxReq.dst_node, SGPathSRC);

                AuxMap.put(auxReq.src_node, mapDST);

            }

            // map.meta = meta;
            map.map = AuxMap;

        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return success(map, MediaType.ALTO_COSTMAP);
    }

    private void createCostMapPathVector(String id, String mode, String metric,
            String src_type, String srcPropertyval, String dst_type,
            String dstPropertyVal, List<List<Object>> lstCost) {

        MapServiceParams params = MapServiceServer.getTopologyParams();
        String neo4jServer = params.getMapServerDBip();
        String neo4jServerPort = Integer.toString(params.getMapServerDBport());

        String strQuery = String
                .format(new StringBuilder()
                        .append("MATCH (:VersionTag {ResourceID:'%s'})-[:Has_PID]->(:PID)")
                        .append("-[:Has_%sProperty]->(start:%sProperty {Name:'%s'}),")
                        .append(" (:VersionTag {ResourceID:'%s'})-[:Has_PID]->(:PID)")
                        .append("-[:Has_%sProperty]->(end:%sProperty {Name:'%s'})")
                        .append(" MERGE (start)-[:Cost]->(c:Cost)<-[:Cost]-(end)")
                        .append(" ON CREATE SET c.cost_mode = '%s', c.cost_metric = '%s'")
                        .append(" ON MATCH SET c.cost_mode = '%s', c.cost_metric = '%s'")
                        .toString(), id, src_type, src_type, srcPropertyval,
                        id, dst_type, dst_type, dstPropertyVal, mode, metric,
                        mode, metric);

        try {
            REST_Query(strQuery, getNeo4jURL(neo4jServer, neo4jServerPort));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (int ic = 0; ic < lstCost.size(); ic++) {
            String strCost = "";
            for (int i = 0; i < lstCost.get(ic).size(); i++) {
                if (i == lstCost.get(ic).size() - 1)
                    strCost += lstCost.get(ic).get(i);
                else
                    strCost += lstCost.get(ic).get(i) + ",";
            }

            strQuery = String
                    .format(new StringBuilder()
                            .append("MATCH (:VersionTag {ResourceID:'%s'})-[:Has_PID]->(:PID)")
                            .append("-[:Has_%sProperty]->(start:%sProperty {Name:'%s'})")
                            .append("-[:Cost]->(c:Cost {cost_mode: '%s', cost_metric: '%s' })")
                            .append("<-[:Cost]-(end:%sProperty {Name:'%s'})")
                            .append("<-[:Has_%sProperty]-(:PID)<-[:Has_PID]")
                            .append("-(:VersionTag {ResourceID:'%s'})")
                            .append(" MERGE (c)-[:List {id:%d}]->(v:Value)")
                            .append(" ON CREATE SET v.value = '%s'")
                            .append(" ON MATCH SET v.value = '%s'").toString(),
                            id, src_type, src_type, srcPropertyval, mode,
                            metric, dst_type, dstPropertyVal, dst_type, id, ic,
                            strCost, strCost);

            try {
                REST_Query(strQuery, getNeo4jURL(neo4jServer, neo4jServerPort));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    private CostSGRequest getFilterJsonCostMapSG(JsonNode filterNode) {
        CostSGRequest request = new CostSGRequest();
        String cost_mode = null;
        String cost_metric = null;

        JsonNode _cost_type = filterNode.get(FIELD_COST_TYPE);
        cost_mode = _cost_type.get(FIELD_COST_MODE).asText();
        cost_metric = _cost_type.get(FIELD_COST_METRIC).asText();

        CostType c1 = new CostType(cost_mode, cost_metric);
        request.costType = c1;

        JsonNode _constrains = (ArrayNode) filterNode.get(FIELD_CONSTRAINT);
        if (_constrains != null) {
            List<String> lstCon = arrayNode2List(FIELD_CONSTRAINT,
                    (ArrayNode) _constrains);
            request.constraints = lstCon;
        }

        // request.constraints =

        // AltoBasicException e;
        // e = new
        // AltoErrorInvalidFieldValue(MulticostRequest.FIELD_ORCONSTRAINT);
        // request.orConstraints = request.orConstraintsRepr
        // .stream()
        // .map(l -> l.stream()
        // .map(repr -> Condition.compile(repr, request.testableTypes, e))
        // .collect(Collectors.toList()))
        // .collect(Collectors.toList());

        SG sg = new SG();

        JsonNode _sg_parameters = filterNode.get(FIELD_SG).get(
                FIELD_SG_PARAMETERS);
        sg.id = _sg_parameters.get("id").asText();
        sg.name = _sg_parameters.get("name").asText();

        List<NF> lstNF = new ArrayList<NF>();
        JsonNode _nodes_nfs = (ArrayNode) filterNode.get(FIELD_SG).get(
                FIELD_SG_NODES_NFS);
        for (JsonNode nfAux : _nodes_nfs) {
            NF nf = new NF();
            nf.id = nfAux.get("id").asText();
            nf.name = nfAux.get("name").asText();
            nf.functional_type = nfAux.get("functional_type").asText();
            lstNF.add(nf);
        }
        sg.nfs = lstNF;

        List<SAP> lstSAP = new ArrayList<SAP>();
        JsonNode _nodes_saps = (ArrayNode) filterNode.get(FIELD_SG).get(
                FIELD_SG_NODES_SAP);
        for (JsonNode sapAux : _nodes_saps) {
            SAP sap = new SAP();
            sap.id = sapAux.get("id").asText();
            sap.name = sapAux.get("name").asText();
            lstSAP.add(sap);
        }
        sg.saps = lstSAP;

        Map<String, NextHop> lstHop = new LinkedHashMap<String, NextHop>();
        JsonNode _nodes_hops = (ArrayNode) filterNode.get(FIELD_SG).get(
                FIELD_SG_NEXT_HOPS);
        for (JsonNode hopAux : _nodes_hops) {
            NextHop hop = new NextHop();
            hop.src_node = hopAux.get("src_node").asText();
            hop.src_type = getNodeType(hop.src_node, lstSAP, lstNF);
            if (hop.src_type.equals("NF"))
                hop.src_node_nf = getNFtype(hop.src_node, lstNF);
            hop.dst_node = hopAux.get("dst_node").asText();
            hop.dst_type = getNodeType(hop.dst_node, lstSAP, lstNF);
            if (hop.dst_type.equals("NF"))
                hop.dst_node_nf = getNFtype(hop.dst_node, lstNF);
            lstHop.put(hopAux.get("id").asText(), hop);
        }
        sg.nexthops = lstHop;

        List<EdgeReq> lstReq = new ArrayList<EdgeReq>();
        JsonNode _nodes_reqs = (ArrayNode) filterNode.get(FIELD_SG).get(
                FIELD_SG_REQS);
        for (JsonNode reqAux : _nodes_reqs) {
            EdgeReq req = new EdgeReq();
            req.id = reqAux.get("id").asText();
            req.src_node = reqAux.get("src_node").asText();
            req.dst_node = reqAux.get("dst_node").asText();

            List<String> lstSGpath = new ArrayList<String>();
            JsonNode _sg_path = (ArrayNode) reqAux.get("sg_path");
            for (JsonNode sgPath : _sg_path)
                lstSGpath.add(sgPath.toString());
            req.sg_path = lstSGpath;

            lstReq.add(req);
        }
        sg.reqs = lstReq;

        request.sg = sg;

        return request;
    }

    private String getNFtype(String strNode, List<NF> lstNF) {
        for (NF auxNF : lstNF) {
            if (auxNF.id.equals(strNode))
                return auxNF.functional_type;
        }
        // TODO Auto-generated method stub
        return null;
    }

    private String getNodeType(String strNode, List<SAP> lstSAP, List<NF> lstNF) {

        for (SAP auxSAP : lstSAP) {
            if (auxSAP.id.equals(strNode))
                return "Port";
        }
        for (NF auxNF : lstNF) {
            if (auxNF.id.equals(strNode))
                return "NF";
        }
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response retrieveMultiCostMapFilter(String id, String filterJSON,
            SecurityContext securityContext) throws NotFoundException {

        MulticostRequest request = new MulticostRequest();
        // request.costType = c1;
        // request.multicostTypes = clist;
        // MulticostService service = new MulticostService(clist, true);

        Response error;
        String cost_mode = null;
        String cost_metric = null;
        List<String> pid_source = null;
        List<String> pid_destination = null;

        List<String> endpoint_source = null;
        List<String> endpoint_destination = null;

        ObjectMapper mapper1 = new ObjectMapper();

        try {
            JsonNode filterNode = mapper1.readTree(filterJSON);

            JsonNode _pids = filterNode.get(FIELD_PIDS);
            JsonNode _pid_source = _pids.get(FIELD_PID_SOURCE);
            pid_source = arrayNode2List(FIELD_PID_SOURCE,
                    (ArrayNode) _pid_source);
            JsonNode _pid_destination = _pids.get(FIELD_PID_DESTINSTION);
            pid_destination = arrayNode2List(FIELD_PID_DESTINSTION,
                    (ArrayNode) _pid_destination);
            QueryPairs qPIDs = new QueryPairs();
            qPIDs.src = pid_source;
            qPIDs.dst = pid_destination;
            request.pids = qPIDs;

            JsonNode _cost_type = filterNode.get(FIELD_COST_TYPE);
            if (_cost_type == null) {
                error = null;
                return error;
            } else {
                cost_mode = _cost_type.get(FIELD_COST_MODE).asText();
                cost_metric = _cost_type.get(FIELD_COST_METRIC).asText();
            }
            CostType c1 = new CostType(cost_mode, cost_metric);
            request.costType = c1;

            List<CostType> clist = new ArrayList<CostType>();

            JsonNode _multi_cost_types = (ArrayNode) filterNode
                    .get(FIELD_MULTI_COST_TYPE);
            for (JsonNode ctAux : _multi_cost_types) {
                String multi_cost_mode = ctAux.get(FIELD_COST_MODE).asText();
                String multi_cost_metric = ctAux.get(FIELD_COST_METRIC)
                        .asText();
                c1 = new CostType(multi_cost_mode, multi_cost_metric);
                clist.add(c1);
            }
            request.multicostTypes = clist;

            clist = new ArrayList<CostType>();
            JsonNode _testable_cost_types = (ArrayNode) filterNode
                    .get(FIELD_TESTABLE);
            for (JsonNode ctAux : _testable_cost_types) {
                String multi_cost_mode = ctAux.get(FIELD_COST_MODE).asText();
                String multi_cost_metric = ctAux.get(FIELD_COST_METRIC)
                        .asText();
                c1 = new CostType(multi_cost_mode, multi_cost_metric);
                clist.add(c1);
            }
            request.testableTypes = clist;

            List<String> lstCon = new ArrayList<String>();
            List<List<String>> lstConFinal = new ArrayList<List<String>>();
            JsonNode _constraints = (ArrayNode) filterNode
                    .get(FIELD_ORCONSTRAINT);
            for (JsonNode conAux : _constraints) {
                lstCon = arrayNode2List(FIELD_ORCONSTRAINT, (ArrayNode) conAux);
                lstConFinal.add(lstCon);
            }
            request.orConstraintsRepr = lstConFinal;

            RFC7285JSONMapper mapper = new RFC7285JSONMapper();

            Endpoint.CostRequest req = new Endpoint.CostRequest();
            req.costType = new CostType(cost_mode, cost_metric);
            req.endpoints = new QueryPairs();
            JsonNode _endpoint = filterNode.get(FIELD_ENDPOINTS);
            JsonNode _endpoint_source = _endpoint.get(FIELD_PID_SOURCE);
            endpoint_source = arrayNode2List(FIELD_PID_SOURCE,
                    (ArrayNode) _endpoint_source);
            JsonNode _endpoint_destination = _endpoint
                    .get(FIELD_PID_DESTINSTION);
            endpoint_destination = arrayNode2List(FIELD_PID_DESTINSTION,
                    (ArrayNode) _endpoint_destination);
            req.endpoints.src = endpoint_source;
            req.endpoints.dst = endpoint_destination;

        } catch (JsonProcessingException e) {
            try {
                throw e;
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } catch (Exception e) {
            return Response.status(500).build();
        }

        return null;
    }

    @Override
    public Response retrieveCostMap(String id, String filterJSON,
            SecurityContext securityContext) throws NotFoundException {

        MapServiceParams params = MapServiceServer.getTopologyParams();
        String neo4jServer = params.getMapServerDBip();
        String neo4jServerPort = Integer.toString(params.getMapServerDBport());

        CostMap.Filter filter = null;
        try {
            filter = mapper.asCostMapFilter(filterJSON);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String strMode = filter.costType.mode;
        String strMetric = filter.costType.metric;

        CostMap map;
        String tag = "";

        map = new CostMap();

        CostMap.Meta meta = new CostMap.Meta();
        meta.costType = new CostType(strMode, strMetric);

        List<VersionTag> netmap_tags = new LinkedList<VersionTag>();
        // tag = "n3EPIZNVJSEywwhXSSKhAqzQ5ROreBeK";
        netmap_tags.add(new VersionTag(id, tag));
        meta.netmap_tags = netmap_tags;

        Map<String, Map<String, Object>> AuxMap = new LinkedHashMap<String, Map<String, Object>>();

        // for (String strSRC : filter.pids.src) {
        // Map<String, Object> mapDST = new LinkedHashMap<String, Object>();
        // for (String strDST : filter.pids.dst) {
        // mapDST.put(strDST, new Random().nextInt(100));
        // }
        // AuxMap.put(strSRC, mapDST);
        // }

        for (String strSRC : filter.pids.src) {
            Map<String, Object> mapDST = new LinkedHashMap<String, Object>();
            for (String strDST : filter.pids.dst) {

                String strQuery = String
                        .format(new StringBuilder()
                                .append("MATCH p=(startAS:AS {ASN:'%s'})")
                                .append("-[r:LINK*]-(endAS:AS {ASN:'%s'})")
                                .append(" WHERE NONE (n IN nodes(p)")
                                .append(" WHERE size(filter(x IN nodes(p)")
                                .append(" WHERE x = endAS))> 1)")
                                .append(" WITH extract(n in nodes(p) | n.ASN) as path,")
                                .append(" reduce(s=0, rel IN r | s+1) as depth")
                                .toString(), strSRC, strDST);

                if (filter.constraints != null
                        && !filter.constraints.get(0).equals("shortest")) {

                    String strConAux = "";
                    String strCon = "";
                    for (String con : filter.constraints) {
                        strConAux = "depth " + con + " AND ";
                        strCon += strConAux;
                    }

                    strCon = strCon.substring(0, strCon.length() - 5);
                    strCon = " WHERE " + strCon;

                    strQuery += strCon;
                }

                strQuery += " RETURN path, depth";

                if (filter.constraints != null
                        && filter.constraints.size() == 1
                        && filter.constraints.get(0).equals("shortest")) {
                    // String strOrderBy = "";
                    // strOrderBy = strOrderBy.substring(0,
                    // strOrderBy.length() - 2);
                    strQuery += " ORDER BY depth ASC LIMIT 1";
                }

                try {
                    REST_Query(strQuery,
                            getNeo4jURL(neo4jServer, neo4jServerPort));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                List<List<Object>> lstCost = new ArrayList<List<Object>>();

                while (staResult.hasNext()) {
                    Record record = staResult.next();

                    List<Object> lstAux = new ArrayList<Object>();
                    for (Object AS : record.get("path").asList())
                        lstAux.add(String.valueOf(AS));

                    lstCost.add(lstAux);
                }

                mapDST.put(strDST, lstCost);
            }
            AuxMap.put(strSRC, mapDST);
        }

        map.meta = meta;
        map.map = AuxMap;

        return success(map, MediaType.ALTO_COSTMAP);

    }

    @Override
    public Response retrieveFilteredPropertyMap(String id, String filterJSON,
            SecurityContext securityContext) throws NotFoundException {

        MapServiceParams params = MapServiceServer.getTopologyParams();
        String neo4jServer = params.getMapServerDBip();
        String neo4jServerPort = Integer.toString(params.getMapServerDBport());

        PropertyMap.Filter filter = null;
        try {
            filter = mapper.asPropertyMapFilter(filterJSON);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String lstPID = "";
        for (String sAux : filter.pids)
            lstPID = lstPID + "'" + sAux + "',";
        lstPID = lstPID.substring(0, lstPID.length() - 1);

        String strQuery = String
                .format(new StringBuilder()
                        .append("MATCH (v:VersionTag {ResourceID:'%s'})-[r:Has_PID]->(pid)")
                        .append(" WHERE pid.Name IN [%s]")
                        .append(" OPTIONAL MATCH (pid)-[:Has_PortProperty]->(p)")
                        .append(" OPTIONAL MATCH (pid)-[:Has_NFProperty]->(nf)")
                        .append(" RETURN v.Tag AS Tag, pid.Name AS Name, pid.unifyslor AS unifyslor,")
                        .append(" pid.CPU AS cpu,pid.Mem AS mem,pid.Storage AS storage,")
                        .append(" collect(DISTINCT p.Name) AS SAPs,")
                        .append(" collect(DISTINCT nf.Name) AS NFs")
                        .append(" ORDER BY Name").toString(), id, lstPID);

        try {
            REST_Query(strQuery, getNeo4jURL(neo4jServer, neo4jServerPort));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        PropertyMap map = CreatePropertyMap(id);

        if (map == null)
            return fail(Status.NOT_FOUND, id);
        return success(map, MediaType.ALTO_PROPERTYMAP);

    }

    @Override
    public Response retrievePropertyMap(String id,
            SecurityContext securityContext) throws NotFoundException {

        String strQuery = String
                .format(new StringBuilder()
                        .append("MATCH (v:VersionTag {ResourceID:'%s'})-[r:Has_PID]->(pid)")
                        .append(" OPTIONAL MATCH (pid)-[:Has_PortProperty]->(p)")
                        .append(" OPTIONAL MATCH (pid)-[:Has_NFProperty]->(nf)")
                        .append(" RETURN v.Tag AS Tag, pid.Name AS Name, pid.unifyslor AS unifyslor,")
                        .append(" pid.CPU AS cpu,pid.Mem AS mem,pid.Storage AS storage,")
                        .append(" collect(DISTINCT p.Name) AS SAPs,")
                        .append(" collect(DISTINCT nf.Name) AS NFs")
                        .append(" ORDER BY Name").toString(), id);

        MapServiceParams params = MapServiceServer.getTopologyParams();
        String neo4jServer = params.getMapServerDBip();
        String neo4jServerPort = Integer.toString(params.getMapServerDBport());

        try {
            REST_Query(strQuery, getNeo4jURL(neo4jServer, neo4jServerPort));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        PropertyMap map = CreatePropertyMap(id);

        /*
         * System.out
         * .println("START conversion: Cypher result values --> Property Map");
         * 
         * 
         * PropertyMap map = null; Map<String, Property.PropertyGroup> AuxMap =
         * new LinkedHashMap<String, Property.PropertyGroup>(); String tag = "";
         * PropertyMap.Meta meta = new PropertyMap.Meta();
         * 
         * while (staResult.hasNext()) { map = new PropertyMap();
         * 
         * Record record = staResult.next();
         * 
         * tag = record.get("Tag").asString(); meta.vtag = new VersionTag(id,
         * tag);
         * 
         * String strPID = record.get("Name").asString();
         * System.out.println("Detected PID: " + strPID);
         * 
         * Property.PropertyGroup pg = new Property.PropertyGroup();
         * pg.unifyslor = record.get("unifyslor").asString();
         * System.out.println(String.format("Add unifyslor to PID: { '%s' }",
         * pg.unifyslor)); pg.cpu = Float.toString(record.get("cpu").asFloat());
         * System.out.println(String .format("Add cpu to PID: { '%s' }",
         * pg.cpu)); pg.mem = Float.toString(record.get("mem").asFloat());
         * System.out.println(String .format("Add mem to PID: { '%s' }",
         * pg.mem)); pg.storage =
         * Float.toString(record.get("storage").asFloat());
         * System.out.println(String.format("Add storage to PID: { '%s' }",
         * pg.storage));
         * 
         * List<String> lstPort = new ArrayList<String>(); for (Object SAP :
         * record.get("SAPs").asList()) lstPort.add(String.valueOf(SAP));
         * pg.port = lstPort;
         * System.out.println(String.format("Add ports list to PID: { '%s' }",
         * lstPort));
         * 
         * List<String> lstNF = new ArrayList<String>(); for (Object NF :
         * record.get("NFs").asList()) lstNF.add(String.valueOf(NF)); pg.nf =
         * lstNF;
         * System.out.println(String.format("Add NFs list to PID: { '%s' }",
         * lstNF));
         * 
         * AuxMap.put(strPID, pg); }
         * 
         * map.meta = meta; map.map = AuxMap;
         * 
         * System.out
         * .println("END conversion: Cypher result values --> ALTO Network Map"
         * );
         */

        if (map == null)
            return fail(Status.NOT_FOUND, id);
        return success(map, MediaType.ALTO_PROPERTYMAP);
    }

    private PropertyMap CreatePropertyMap(String id) {
        System.out
                .println("START conversion: Cypher result values --> Property Map");

        PropertyMap map = null;
        Map<String, Property.PropertyGroup> AuxMap = new LinkedHashMap<String, Property.PropertyGroup>();
        String tag = "";
        PropertyMap.Meta meta = new PropertyMap.Meta();

        while (staResult.hasNext()) {
            map = new PropertyMap();

            Record record = staResult.next();

            tag = record.get("Tag").asString();
            meta.vtag = new VersionTag(id, tag);

            String strPID = record.get("Name").asString();
            System.out.println("Detected PID: " + strPID);

            Property.PropertyGroup pg = new Property.PropertyGroup();
            pg.unifyslor = record.get("unifyslor").asString();
            System.out.println(String.format("Add unifyslor to PID: { '%s' }",
                    pg.unifyslor));
            pg.cpu = Float.toString(record.get("cpu").asFloat());
            System.out.println(String
                    .format("Add cpu to PID: { '%s' }", pg.cpu));
            pg.mem = Float.toString(record.get("mem").asFloat());
            System.out.println(String
                    .format("Add mem to PID: { '%s' }", pg.mem));
            pg.storage = Float.toString(record.get("storage").asFloat());
            System.out.println(String.format("Add storage to PID: { '%s' }",
                    pg.storage));

            List<String> lstPort = new ArrayList<String>();
            for (Object SAP : record.get("SAPs").asList())
                lstPort.add(String.valueOf(SAP));
            pg.port = lstPort;
            System.out.println(String.format("Add ports list to PID: { '%s' }",
                    lstPort));

            List<String> lstNF = new ArrayList<String>();
            for (Object NF : record.get("NFs").asList())
                lstNF.add(String.valueOf(NF));
            pg.nf = lstNF;
            System.out.println(String.format("Add NFs list to PID: { '%s' }",
                    lstNF));

            AuxMap.put(strPID, pg);
        }

        map.meta = meta;
        map.map = AuxMap;

        System.out
                .println("END conversion: Cypher result values --> ALTO Network Map");

        return map;
    }

    @Override
    public Response retrieveNetworkMap(String id,
            SecurityContext securityContext) throws NotFoundException {
        /*
         * String strQuery = String .format(new StringBuilder()
         * .append("MATCH (v:VersionTag {ResourceID:'%s'})-[r:Has_PID]->(pid)")
         * .append(" OPTIONAL MATCH (pid)-[e:Has_EndPoint]->")
         * .append("(p)<-[r2:Type_EndPoint]-(type)")
         * .append(" WITH v.Tag AS Tag,pid.Name AS Name,")
         * .append(" COLLECT([type.Type,p.Prefix]) AS p")
         * .append(" RETURN Tag, Name,") .append(
         * " [prefix IN FILTER(pAux in p where pAux[0]='%s') | prefix[1]] AS IPv4,"
         * ) .append(
         * " [prefix in FILTER(pAux IN p where pAux[0]='%s') | prefix[1]] AS IPv6"
         * ) .append(" ORDER BY Name").toString(), id, IPv4, IPv6);
         * 
         * AltoParams params = AltoServer.getTopologyParams(); String
         * neo4jServer = params.getAltoServerDBip(); String neo4jServerPort =
         * Integer.toString(params.getAltoServerDBport());
         * 
         * // REST_Query(strQuery, SERVER_ROOT_URI); try { REST_Query(strQuery,
         * String.format("%s:%s/db/data/", neo4jServer, neo4jServerPort)); }
         * catch (Exception e) { // TODO Auto-generated catch block
         * e.printStackTrace(); }
         * 
         * // Iterator<JsonNode> queryNeo4j = jsonResult.path("results") //
         * .findPath("data").elements();
         * 
         * RFC7285NetworkMap map = null; String tag = ""; Map<String,
         * RFC7285Endpoint.AddressGroup> AuxMap = new LinkedHashMap<String,
         * RFC7285Endpoint.AddressGroup>();
         * 
         * System.out
         * .println("START conversion: Cypher result values --> ALTO Network Map"
         * );
         * 
         * while (staResult.hasNext()) { map = new RFC7285NetworkMap();
         * 
         * Record record = staResult.next();
         * 
         * tag = record.get("Tag").asString();
         * 
         * RFC7285Endpoint.AddressGroup ag = new RFC7285Endpoint.AddressGroup();
         * String strPID = record.get("Name").asString();
         * 
         * System.out.println("Detected PID: " + strPID);
         * 
         * // String x = record.get("IPv4").toString(); // List<String> x = //
         * (ArrayList<String>)(ArrayList<?>)record.get("IPv4").asList(); //
         * System.out.print(x);
         * 
         * List<String> lstIPv4 = new ArrayList<String>();
         * lstIPv4.add(record.get("IPv4").asList().get(0).toString()); //
         * createIPList(record.get("Name"). lstQuery.next().elements()); //
         * List<String> lstIPv6 = createIPList(lstQuery.next().elements());
         * List<String> lstIPv6 = new ArrayList<String>(); if (lstIPv4.size() >
         * 0) { ag.ipv4 = lstIPv4; System.out.println(String.format(
         * "Add IPv4 prefixes list to PID: { '%s' }", lstIPv4)); } if
         * (lstIPv6.size() > 0) { ag.ipv6 = lstIPv6;
         * System.out.println(String.format(
         * "Add IPv6 prefixes list to PID: { '%s' }", lstIPv6)); }
         * 
         * AuxMap.put(strPID, ag); } RFC7285NetworkMap.Meta meta = new
         * RFC7285NetworkMap.Meta(); meta.vtag = new RFC7285VersionTag(id, tag);
         * 
         * map.meta = meta; map.map = AuxMap;
         * 
         * System.out
         * .println("END conversion: Cypher result values --> ALTO Network Map"
         * );
         * 
         * if (map == null) return fail(Status.NOT_FOUND, id); return
         * success(map, MediaType.ALTO_NETWORKMAP);
         */
        return null;
    }

    private List<String> createIPList(Iterator<JsonNode> lstIp) {
        List<String> lstIP = new ArrayList<String>();
        while (lstIp.hasNext())
            lstIP.add(lstIp.next().toString().replaceAll("\"", ""));

        return lstIP;
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
}
