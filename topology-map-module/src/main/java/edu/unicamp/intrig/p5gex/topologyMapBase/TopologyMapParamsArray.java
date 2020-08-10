package edu.unicamp.intrig.p5gex.topologyMapBase;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.unicamp.intrig.p5gex.topologyMapBase.util.InterDomainTopo;
import edu.unicamp.intrig.p5gex.topologyMapBase.util.UtilsFunctions;

public class TopologyMapParamsArray {
    private ArrayList<TopologyMapParams> paramList;
    private String confFile;

    public TopologyMapParamsArray(String strConfFile) {
        this.setConfFile(strConfFile);
    }

    public String getConfFile() {
        return confFile;
    }

    public void setConfFile(String confFile) {
        paramList = new ArrayList<TopologyMapParams>();
        this.confFile = confFile;
    }

    public void initialize() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            File confFile = new File(this.confFile);
            System.out.println("Load config from file "
                    + confFile.getAbsolutePath());

            Document doc = builder.parse(confFile);

            NodeList list_nodes_AltoPlugin = doc.getElementsByTagName("InterDomainTopo");
            for (int i = 0; i < list_nodes_AltoPlugin.getLength(); i++) {
                Element nodes_AltoPlugin = (Element) list_nodes_AltoPlugin
                        .item(i);
                TopologyMapParams littleParams = new TopologyMapParams();

                if (nodes_AltoPlugin.getElementsByTagName("server").getLength() > 0) {

                    NodeList tads = nodes_AltoPlugin
                            .getElementsByTagName("server");
                    List<InterDomainTopo> lstInterDomainTopo = new ArrayList<InterDomainTopo>();

                    for (int j = 0; j < tads.getLength(); j++) {
                        InterDomainTopo objInterDomainTopo = new InterDomainTopo(
                                getCharacterDataFromElement(((Element) ((NodeList) ((Element) tads
                                        .item(j))
                                        .getElementsByTagName("serverIP"))
                                        .item(0))),
                                Integer.parseInt(getCharacterDataFromElement(((Element) ((NodeList) ((Element) tads
                                        .item(j))
                                        .getElementsByTagName("serverPort"))
                                        .item(0)))));

                        objInterDomainTopo.setREST(getCharacterDataFromElement(((Element) ((NodeList) ((Element) tads
                                .item(j)).getElementsByTagName("rest")).item(0))));

                        lstInterDomainTopo.add(objInterDomainTopo);

                    }

                    littleParams.setAltoAggregatorPluginTADS(lstInterDomainTopo);

                }

                if (nodes_AltoPlugin.getElementsByTagName("TopologyMapDB")
                        .getLength() > 0) {
                    littleParams
                            .setAltoAggregatorPluginAggregatorServerIP(((getCharacterDataFromElement((Element) ((Element) nodes_AltoPlugin
                                    .getElementsByTagName("TopologyMapDB").item(
                                            0))
                                    .getElementsByTagName("serverIP").item(0)))));

                    littleParams
                            .setAltoAggregatorPluginAggregatorServerPort((Integer
                                    .parseInt(((getCharacterDataFromElement((Element) ((Element) nodes_AltoPlugin
                                            .getElementsByTagName(
                                                    "TopologyMapDB").item(0))
                                            .getElementsByTagName("serverPort")
                                            .item(0)))))));
                }
                littleParams.setAltoAggregatorPlugin(true);
                paramList.add(littleParams);
            }

            // System.out.println(paramList);
        } catch (Exception e) {
            System.out.println(UtilsFunctions.exceptionToString(e));
        }
    }

    public ArrayList<TopologyMapParams> getParamList() {
        return paramList;
    }

    private String getCharacterDataFromElement(Element e) {
        if (e == null) {
            return null;
        }
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        } else {
            return "?";
        }
    }
}
