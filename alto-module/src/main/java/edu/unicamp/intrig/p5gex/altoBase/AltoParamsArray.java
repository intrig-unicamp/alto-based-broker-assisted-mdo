package edu.unicamp.intrig.p5gex.altoBase;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.unicamp.intrig.p5gex.altoBase.AltoParams;
import edu.unicamp.intrig.p5gex.altoBase.util.UtilsFunctions;

public class AltoParamsArray {
    private ArrayList<AltoParams> paramList;
    private String confFile;

    public AltoParamsArray(String strConfFile) {
        this.setConfFile(strConfFile);
    }

    public String getConfFile() {
        return confFile;
    }

    public void setConfFile(String confFile) {
        paramList = new ArrayList<AltoParams>();
        this.confFile = confFile;
    }

    public void initialize() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            File confFile = new File(this.confFile);
            Document doc = builder.parse(confFile);

            NodeList list_nodes_AltoPlugin = doc
                    .getElementsByTagName("AltoPlugin");
            for (int i = 0; i < list_nodes_AltoPlugin.getLength(); i++) {
                Element nodes_AltoPlugin = (Element) list_nodes_AltoPlugin
                        .item(i);
                AltoParams littleParams = new AltoParams();

                if (nodes_AltoPlugin.getElementsByTagName("AltoDB").getLength() > 0) {
                    littleParams
                            .setAltoPluginAltoServerIP((getCharacterDataFromElement((Element) ((Element) nodes_AltoPlugin
                                    .getElementsByTagName("AltoDB").item(0))
                                    .getElementsByTagName("serverIP").item(0))));

                    littleParams
                            .setAltoPluginAltoServerPort(Integer
                                    .parseInt((getCharacterDataFromElement((Element) ((Element) nodes_AltoPlugin
                                            .getElementsByTagName("AltoDB")
                                            .item(0)).getElementsByTagName(
                                            "serverPort").item(0)))));
                    littleParams
                            .setAltoPluginNetMapName((getCharacterDataFromElement((Element) ((Element) ((Element) nodes_AltoPlugin
                                    .getElementsByTagName("AltoDB").item(0))
                                    .getElementsByTagName("networkMap").item(0))
                                    .getElementsByTagName("name").item(0))));

                }

                if (nodes_AltoPlugin.getElementsByTagName("AggregatorDB")
                        .getLength() > 0) {
                    littleParams
                            .setAltoPluginAggregatorServerIP((getCharacterDataFromElement((Element) ((Element) nodes_AltoPlugin
                                    .getElementsByTagName("AggregatorDB").item(
                                            0))
                                    .getElementsByTagName("serverIP").item(0))));

                    littleParams
                            .setAltoPluginAggregatorServerPort(Integer
                                    .parseInt(((getCharacterDataFromElement((Element) ((Element) nodes_AltoPlugin
                                            .getElementsByTagName(
                                                    "AggregatorDB").item(0))
                                            .getElementsByTagName("serverPort")
                                            .item(0))))));
                }
                littleParams.setAltoPlugin(true);
                paramList.add(littleParams);
            }

            NodeList list_nodes_AltoServer = doc.getElementsByTagName("ALTO");
            for (int i = 0; i < list_nodes_AltoServer.getLength(); i++) {
                Element nodes_AltoServer = (Element) list_nodes_AltoServer
                        .item(i);
                AltoParams littleParams = new AltoParams();

                if (nodes_AltoServer.getElementsByTagName("server").getLength() > 0) {
                    littleParams
                            .setAltoServerPort((Integer
                                    .parseInt(((getCharacterDataFromElement((Element) ((Element) nodes_AltoServer
                                            .getElementsByTagName("server")
                                            .item(0)).getElementsByTagName(
                                            "serverPort").item(0)))))));
                }

                if (nodes_AltoServer.getElementsByTagName("AltoDB").getLength() > 0) {
                    littleParams
                            .setAltoServerDBip((getCharacterDataFromElement((Element) ((Element) nodes_AltoServer
                                    .getElementsByTagName("AltoDB").item(0))
                                    .getElementsByTagName("serverIP").item(0))));

                    littleParams
                            .setAltoServerDBport(Integer
                                    .parseInt((getCharacterDataFromElement((Element) ((Element) nodes_AltoServer
                                            .getElementsByTagName("AltoDB")
                                            .item(0)).getElementsByTagName(
                                            "serverPort").item(0)))));
                }

                littleParams.setAltoServer(true);
                paramList.add(littleParams);
            }

            NodeList list_nodes_Unify = doc.getElementsByTagName("UNIFY");
            for (int i = 0; i < list_nodes_Unify.getLength(); i++) {
                Element nodes_Unify = (Element) list_nodes_Unify.item(i);
                AltoParams littleParams = new AltoParams();

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

    public ArrayList<AltoParams> getParamList() {
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
