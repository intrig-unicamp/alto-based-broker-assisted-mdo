package edu.unicamp.intrig.p5gex.interDomainBase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.unicamp.intrig.p5gex.interDomainBase.util.TADS;
import edu.unicamp.intrig.p5gex.interDomainBase.util.UtilsFunctions;
import edu.unicamp.intrig.p5gex.interDomainBase.InterDomainParams;

public class InterDomainParamsArray {
    private ArrayList<InterDomainParams> paramList;
    private String confFile;

    public InterDomainParamsArray(String strConfFile) {
        this.setConfFile(strConfFile);
    }
    
    public InterDomainParamsArray(){
        paramList = new ArrayList<InterDomainParams>();
        setConfFile("TMConfiguration.xml");
    }

    public String getConfFile() {
        return confFile;
    }

    public void setConfFile(String confFile) {
        paramList = new ArrayList<InterDomainParams>();
        this.confFile = confFile;
    }

    public void initialize() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            File confFile = new File(this.confFile);
            Document doc = builder.parse(confFile);           

            NodeList list_nodes_AltoPlugin = doc.getElementsByTagName("tads");
            for (int i = 0; i < list_nodes_AltoPlugin.getLength(); i++) {
                Element nodes_AltoPlugin = (Element) list_nodes_AltoPlugin
                        .item(i);
                InterDomainParams littleParams = new InterDomainParams();

                if (nodes_AltoPlugin.getElementsByTagName("server").getLength() > 0) {

                    NodeList tads = nodes_AltoPlugin
                            .getElementsByTagName("server");
                    List<TADS> lstTADS = new ArrayList<TADS>();

                    for (int j = 0; j < tads.getLength(); j++) {
                        TADS objTADS = new TADS(
                                getCharacterDataFromElement(((Element) ((NodeList) ((Element) tads
                                        .item(j))
                                        .getElementsByTagName("serverIP"))
                                        .item(0))),
                                Integer.parseInt(getCharacterDataFromElement(((Element) ((NodeList) ((Element) tads
                                        .item(j))
                                        .getElementsByTagName("serverPort"))
                                        .item(0)))));

                        objTADS.setREST(getCharacterDataFromElement(((Element) ((NodeList) ((Element) tads
                                .item(j)).getElementsByTagName("rest")).item(0))));

                        lstTADS.add(objTADS);

                    }

                    littleParams.setAltoAggregatorPluginTADS(lstTADS);

                }
                
                littleParams.setTADS(true);
                paramList.add(littleParams);
            }

            NodeList list_nodes_Unify = doc.getElementsByTagName("UNIFY");
            for (int i = 0; i < list_nodes_Unify.getLength(); i++) {
                Element nodes_Unify = (Element) list_nodes_Unify.item(i);
                InterDomainParams littleParams = new InterDomainParams();

                if (nodes_Unify.getElementsByTagName("Export").getLength() > 0) {
                    littleParams
                            .setUnifyExportPort(Integer
                                    .parseInt(((getCharacterDataFromElement((Element) ((Element) nodes_Unify
                                            .getElementsByTagName("Export")
                                            .item(0)).getElementsByTagName(
                                            "serverPort").item(0))))));
                }
                
                littleParams.setUNIFYWriting(true);
                paramList.add(littleParams);
            }

            // System.out.println(paramList);
        } catch (Exception e) {
            System.out.println(UtilsFunctions.exceptionToString(e));
        }
    }

    public ArrayList<InterDomainParams> getParamList() {
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
