package edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.api.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.json.JSONObject;

import edu.unicamp.intrig.p5gex.mapServiceBase.MapServiceParams;
import edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.api.ConfigApiService;
import edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.api.NotFoundException;
import edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.model.LinksSchema;
import edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.model.Node;
import edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.model.Nodes;
import edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.model.TranslateModel;
import edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.model.Virtualizer;
import edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.model.VirtualizerSchema;
import edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.model.Virtualizers;
import edu.unicamp.intrig.p5gex.mapServiceBase.plugins.writer.TopologyServerUnify;

public class ConfigApiServiceImpl extends ConfigApiService {

    @Override    
    public Response topologyFromALTO(SecurityContext securityContext)
            throws NotFoundException {
        VirtualizerSchema tSchema = new VirtualizerSchema();
        Nodes nodes = new Nodes();
        List<Node> nodelist = new ArrayList<Node>();

        try {

            MapServiceParams params = TopologyServerUnify.getTopologyParams();            

            JSONObject jsonObj = GET_REST_Query(
                    getALTOURL(params.getUnifyAltoServerIP(),
                            Integer.toString(params.getUnifyAltoServerPort()),
                            params.getUnifyNetworkMapURL(),
                            params.getUnifyNetworkMapName()),
                    params.getUnifyNetworkMapContentType());

            String key = "network-map";
            if (jsonObj.get(key) instanceof JSONObject) {
                JSONObject jSon = new JSONObject(jsonObj.get(key).toString());
                Iterator<?> lstkey = jSon.keys();
                while (lstkey.hasNext()) {
                    key = (String) lstkey.next();

                    if (jSon.get(key) instanceof JSONObject) {
                        nodelist.add(TranslateModel.translateTopologyFromALTO(
                                key, jSon.get(key)));
                    }

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        nodes.setNode(nodelist);
        tSchema.setNodes(nodes);
        
        LinksSchema links = new LinksSchema();               
        tSchema.setLinks(links);
        
        tSchema.version("001");
        tSchema.setName("ALTO module example");
        
        //List<VirtualizerSchema> virtualizerList = new ArrayList<VirtualizerSchema>();
        //virtualizerList.add(tSchema);        
        //Virtualizers virtualizers = new Virtualizers();
        //virtualizers.setVirtualizer(virtualizerList);
        //Virtualizer virtualizer = new Virtualizer();
        //virtualizer.setVirtualizer(tSchema);
        //return Response.ok().entity(virtualizer).build();

        return Response.ok().entity(tSchema).build();                
    }

    private JSONObject GET_REST_Query(String strURL, String strContentType) {
        try {

            URL url = new URL(strURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", strContentType);

            // OutputStream os = conn.getOutputStream();
            // if (!strInput.isEmpty())
            // os.write(strInput.getBytes());
            // os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            StringBuilder output = new StringBuilder();
            String readAPIResponse;
            while ((readAPIResponse = br.readLine()) != null)
                output.append(readAPIResponse);

            JSONObject jsonObj = new JSONObject(output.toString());
            conn.disconnect();

            return jsonObj;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;
    }

    private static String getALTOURL(String serverIP, String serverPort,
            String netMapURL, String netMapID) {
        // "http://192.168.122.1:8181/controller/nb/v2/alto/networkmap/my-default-network-map"
        return String.format("%s:%s/controller/nb/v2/alto%s/%s", serverIP,
                serverPort, netMapURL, netMapID);
    }
}
