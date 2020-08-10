package edu.unicamp.intrig.p5gex.topologyMapBase.UnifyTopoModel.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

//import es.tid.tedb.elements.Link;

public class TranslateModel {



    public static Node translateTopologyFromALTO(String strDomain,
            Object JSONdomain) {
        Node node = new Node();
        node.setId(strDomain.split("\\|")[0]);
        
        String EntryPoint5GEx = strDomain.split("\\|")[1];
        
        // Translating nodes from TEDB to Unify Model
        PortsSchema ports = new PortsSchema();
        List<Port> portlist = new ArrayList<Port>();

        // node.setLinks(translateIntraDomainLinks(domainID, ted));
        
        MetadataMetadata e = new MetadataMetadata();
        e.setKey("unify-slor");
        e.setValue(EntryPoint5GEx);        
        node.metadata.add(e);
        
        /*
        try {
            JSONObject jSonAux = new JSONObject(JSONdomain.toString());
            Iterator<?> lstkeyF = jSonAux.keys();
            while (lstkeyF.hasNext()) {
                String key = (String) lstkeyF.next();
                
                e = new MetadataMetadata();
                e.setKey(key);
                e.setValue(jSonAux.get(key).toString());
                // metadata.getMetadata().add(e);
                node.metadata.add(e);                               
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }        
         */
        
        //ports.setPort(portlist);
        //node.setPorts(ports);

        return node;
    }

    
    public static Node translateTopologyFromALTO(String strDomain) {
        Node node = new Node();
        node.setId(strDomain.split("\\|")[0]);
        
        String EntryPoint5GEx = strDomain.split("\\|")[1];
        
        // Translating nodes from TEDB to Unify Model
        PortsSchema ports = new PortsSchema();
        List<Port> portlist = new ArrayList<Port>();

        // node.setLinks(translateIntraDomainLinks(domainID, ted));
        
        MetadataMetadata e = new MetadataMetadata();
        e.setKey("EntryPoint5GEx");
        e.setValue(EntryPoint5GEx);        
        node.metadata.add(e);                  

        ports.setPort(portlist);
        node.setPorts(ports);

        return node;
    }

}
