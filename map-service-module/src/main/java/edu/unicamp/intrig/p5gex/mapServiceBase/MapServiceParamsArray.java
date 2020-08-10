package edu.unicamp.intrig.p5gex.mapServiceBase;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.unicamp.intrig.p5gex.mapServiceBase.MapServiceParams;
import edu.unicamp.intrig.p5gex.mapServiceBase.util.UtilsFunctions;

public class MapServiceParamsArray {
    private ArrayList<MapServiceParams> paramList;
    private String confFile;

    public MapServiceParamsArray(String strConfFile) {
        this.setConfFile(strConfFile);
    }

    public String getConfFile() {
        return confFile;
    }

    public void setConfFile(String confFile) {
        paramList = new ArrayList<MapServiceParams>();
        this.confFile = confFile;
    }

    public void initialize() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            File confFile = new File(this.confFile);
            Document doc = builder.parse(confFile);

            NodeList list_nodes_AltoPlugin = doc
                    .getElementsByTagName("MapPlugin");
            for (int i = 0; i < list_nodes_AltoPlugin.getLength(); i++) {
                Element nodes_AltoPlugin = (Element) list_nodes_AltoPlugin
                        .item(i);
                MapServiceParams littleParams = new MapServiceParams();

                if (nodes_AltoPlugin.getElementsByTagName("MapPluginDB").getLength() > 0) {
                    littleParams
                            .setMapPluginServerIP((getCharacterDataFromElement((Element) ((Element) nodes_AltoPlugin
                                    .getElementsByTagName("MapPluginDB").item(0))
                                    .getElementsByTagName("serverIP").item(0))));

                    littleParams
                            .setMapPluginServerPort(Integer
                                    .parseInt((getCharacterDataFromElement((Element) ((Element) nodes_AltoPlugin
                                            .getElementsByTagName("MapPluginDB")
                                            .item(0)).getElementsByTagName(
                                            "serverPort").item(0)))));
                    littleParams
                            .setMapPluginPropMapName((getCharacterDataFromElement((Element) ((Element) ((Element) nodes_AltoPlugin
                                    .getElementsByTagName("MapPluginDB").item(0))
                                    .getElementsByTagName("propertyMap").item(0))
                                    .getElementsByTagName("name").item(0))));

                }

                if (nodes_AltoPlugin.getElementsByTagName("InterDomainResourceDB")
                        .getLength() > 0) {
                    littleParams
                            .setInterDomainResourceServerIP((getCharacterDataFromElement((Element) ((Element) nodes_AltoPlugin
                                    .getElementsByTagName("InterDomainResourceDB").item(
                                            0))
                                    .getElementsByTagName("serverIP").item(0))));

                    littleParams
                            .setInterDomainResourceServerPort(Integer
                                    .parseInt(((getCharacterDataFromElement((Element) ((Element) nodes_AltoPlugin
                                            .getElementsByTagName(
                                                    "InterDomainResourceDB").item(0))
                                            .getElementsByTagName("serverPort")
                                            .item(0))))));
                }
                
                if (nodes_AltoPlugin.getElementsByTagName("InterDomainTopologyDB")
                        .getLength() > 0) {
                    littleParams
                            .setInterDomainTopologyServerIP((getCharacterDataFromElement((Element) ((Element) nodes_AltoPlugin
                                    .getElementsByTagName("InterDomainTopologyDB").item(
                                            0))
                                    .getElementsByTagName("serverIP").item(0))));

                    littleParams
                            .setInterDomainTopologyServerPort(Integer
                                    .parseInt(((getCharacterDataFromElement((Element) ((Element) nodes_AltoPlugin
                                            .getElementsByTagName(
                                                    "InterDomainTopologyDB").item(0))
                                            .getElementsByTagName("serverPort")
                                            .item(0))))));
                }
                
                littleParams.setMapPlugin(true);
                paramList.add(littleParams);
            }

            NodeList list_nodes_AltoServer = doc.getElementsByTagName("Map");
            for (int i = 0; i < list_nodes_AltoServer.getLength(); i++) {
                Element nodes_AltoServer = (Element) list_nodes_AltoServer
                        .item(i);
                MapServiceParams littleParams = new MapServiceParams();

                if (nodes_AltoServer.getElementsByTagName("server").getLength() > 0) {
                    littleParams
                            .setMapServerPort((Integer
                                    .parseInt(((getCharacterDataFromElement((Element) ((Element) nodes_AltoServer
                                            .getElementsByTagName("server")
                                            .item(0)).getElementsByTagName(
                                            "serverPort").item(0)))))));
                }

                if (nodes_AltoServer.getElementsByTagName("MapDB").getLength() > 0) {
                    littleParams
                            .setMapServerDBip((getCharacterDataFromElement((Element) ((Element) nodes_AltoServer
                                    .getElementsByTagName("MapDB").item(0))
                                    .getElementsByTagName("serverIP").item(0))));

                    littleParams
                            .setMapServerDBport(Integer
                                    .parseInt((getCharacterDataFromElement((Element) ((Element) nodes_AltoServer
                                            .getElementsByTagName("MapDB")
                                            .item(0)).getElementsByTagName(
                                            "serverPort").item(0)))));
                }

                littleParams.setMapServer(true);
                paramList.add(littleParams);
            }

            NodeList list_nodes_Unify = doc.getElementsByTagName("UNIFY");
            for (int i = 0; i < list_nodes_Unify.getLength(); i++) {
                Element nodes_Unify = (Element) list_nodes_Unify.item(i);
                MapServiceParams littleParams = new MapServiceParams();

                if (nodes_Unify.getElementsByTagName("Export").getLength() > 0) {
                    littleParams
                            .setUnifyExportPort(Integer
                                    .parseInt(((getCharacterDataFromElement((Element) ((Element) nodes_Unify
                                            .getElementsByTagName("Export")
                                            .item(0)).getElementsByTagName(
                                            "serverPort").item(0))))));
                }

                if (nodes_Unify.getElementsByTagName("AltoServer").getLength() > 0) {
                    littleParams
                            .setUnifyAltoServerIP(((getCharacterDataFromElement((Element) ((Element) nodes_Unify
                                    .getElementsByTagName("AltoServer").item(0))
                                    .getElementsByTagName("serverIP").item(0)))));

                    littleParams
                            .setUnifyAltoServerPort((Integer
                                    .parseInt((getCharacterDataFromElement((Element) ((Element) nodes_Unify
                                            .getElementsByTagName("AltoServer")
                                            .item(0)).getElementsByTagName(
                                            "serverPort").item(0))))));
                }

                if (nodes_Unify.getElementsByTagName("networkMap").getLength() > 0) {
                    littleParams
                            .setUnifyNetworkMapURL(((getCharacterDataFromElement((Element) ((Element) nodes_Unify
                                    .getElementsByTagName("networkMap").item(0))
                                    .getElementsByTagName("url").item(0)))));
                    littleParams
                            .setUnifyNetworkMapContentType(((getCharacterDataFromElement((Element) ((Element) nodes_Unify
                                    .getElementsByTagName("networkMap").item(0))
                                    .getElementsByTagName("contentType")
                                    .item(0)))));
                    littleParams
                            .setUnifyNetworkMapName(((getCharacterDataFromElement((Element) ((Element) nodes_Unify
                                    .getElementsByTagName("networkMap").item(0))
                                    .getElementsByTagName("name").item(0)))));
                }
                littleParams.setUNIFYWriting(true);
                paramList.add(littleParams);
            }

            // System.out.println(paramList);
        } catch (Exception e) {
            System.out.println(UtilsFunctions.exceptionToString(e));
        }
    }

    public ArrayList<MapServiceParams> getParamList() {
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
