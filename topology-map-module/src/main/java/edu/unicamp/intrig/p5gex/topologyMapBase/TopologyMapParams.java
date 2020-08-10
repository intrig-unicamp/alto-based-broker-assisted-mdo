package edu.unicamp.intrig.p5gex.topologyMapBase;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.unicamp.intrig.p5gex.topologyMapBase.util.InterDomainTopo;

public class TopologyMapParams {
    //AltoAggregatorPlugin
    private boolean isInterDomainTopoPlugin = false;        
    private List<InterDomainTopo> InterDomainTopoPluginServer;    
    private String InterDomainTopoPluginTopologyMapServerIP;
    private int InterDomainTopoPluginTopologyMapServerPort;
        
    public boolean isAltoAggregatorPlugin() {
        return isInterDomainTopoPlugin;
    }
    public void setAltoAggregatorPlugin(boolean isAltoAggregatorPlugin) {
        this.isInterDomainTopoPlugin = isAltoAggregatorPlugin;
    }
    
    public String getAltoAggregatorPluginAggregatorServerIP() {
        return InterDomainTopoPluginTopologyMapServerIP;
    }
    public void setAltoAggregatorPluginAggregatorServerIP(
            String altoAggregatorPluginAggregatorServerIP) {
        InterDomainTopoPluginTopologyMapServerIP = altoAggregatorPluginAggregatorServerIP;
    }
    public int getAltoAggregatorPluginAggregatorServerPort() {
        return InterDomainTopoPluginTopologyMapServerPort;
    }
    public void setAltoAggregatorPluginAggregatorServerPort(
            int altoAggregatorPluginAggregatorServerPort) {
        InterDomainTopoPluginTopologyMapServerPort = altoAggregatorPluginAggregatorServerPort;
    }
    public List<InterDomainTopo> getAltoAggregatorPluginTADS() {
        return InterDomainTopoPluginServer;
    }
    public void setAltoAggregatorPluginTADS(List<InterDomainTopo> altoAggregatorPluginTADS) {
        InterDomainTopoPluginServer = altoAggregatorPluginTADS;
    }         
}
