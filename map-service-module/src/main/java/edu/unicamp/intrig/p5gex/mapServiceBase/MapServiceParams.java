package edu.unicamp.intrig.p5gex.mapServiceBase;

public class MapServiceParams {
    //MapPlugin
    private boolean isMapPlugin = false;
    private String MapPluginServerIP;
    private int MapPluginServerPort;
    private String MapPluginPropMapName;
    
    private String InterDomainResourceServerIP;
    private int InterDomainResourceServerPort;
    private String InterDomainTopologyServerIP;
    private int InterDomainTopologyServerPort;

    
    
    //Map    
    private boolean isMapServer = false;
    private int MapServerPort;   
    private String MapServerDBip;
    private int MapServerDBport;
    
    //UNIFY
    private boolean isUNIFYWriting = false;
    private int UnifyExportPort;
    private String UnifyAltoServerIP;
    private int UnifyAltoServerPort;
    private String UnifyNetworkMapURL;
    private String UnifyNetworkMapContentType;
    private String UnifyNetworkMapName;
    
    public boolean isMapPlugin() {
        return isMapPlugin;
    }
    public void setMapPlugin(boolean isMapPlugin) {
        this.isMapPlugin = isMapPlugin;
    }
    public String getMapPluginServerIP() {
        return MapPluginServerIP;
    }
    public void setMapPluginServerIP(String mapPluginServerIP) {
        MapPluginServerIP = mapPluginServerIP;
    }
    public int getMapPluginServerPort() {
        return MapPluginServerPort;
    }
    public void setMapPluginServerPort(int mapPluginServerPort) {
        MapPluginServerPort = mapPluginServerPort;
    }
    public String getMapPluginPropMapName() {
        return MapPluginPropMapName;
    }
    public void setMapPluginPropMapName(String mapPluginPropMapName) {
        MapPluginPropMapName = mapPluginPropMapName;
    }
    public String getInterDomainResourceServerIP() {
        return InterDomainResourceServerIP;
    }
    public void setInterDomainResourceServerIP(
            String interDomainResourceServerIP) {
        InterDomainResourceServerIP = interDomainResourceServerIP;
    }
    public int getInterDomainResourceServerPort() {
        return InterDomainResourceServerPort;
    }
    public void setInterDomainResourceServerPort(
            int interDomainResourceServerPort) {
        InterDomainResourceServerPort = interDomainResourceServerPort;
    }
    public boolean isMapServer() {
        return isMapServer;
    }
    public void setMapServer(boolean isMapServer) {
        this.isMapServer = isMapServer;
    }
    public int getMapServerPort() {
        return MapServerPort;
    }
    public void setMapServerPort(int mapServerPort) {
        MapServerPort = mapServerPort;
    }
    public String getMapServerDBip() {
        return MapServerDBip;
    }
    public void setMapServerDBip(String mapServerDBip) {
        MapServerDBip = mapServerDBip;
    }
    public int getMapServerDBport() {
        return MapServerDBport;
    }
    public void setMapServerDBport(int mapServerDBport) {
        MapServerDBport = mapServerDBport;
    }
    public boolean isUNIFYWriting() {
        return isUNIFYWriting;
    }
    public void setUNIFYWriting(boolean isUNIFYWriting) {
        this.isUNIFYWriting = isUNIFYWriting;
    }
    public int getUnifyExportPort() {
        return UnifyExportPort;
    }
    public void setUnifyExportPort(int unifyExportPort) {
        UnifyExportPort = unifyExportPort;
    }
    public String getUnifyAltoServerIP() {
        return UnifyAltoServerIP;
    }
    public void setUnifyAltoServerIP(String unifyAltoServerIP) {
        UnifyAltoServerIP = unifyAltoServerIP;
    }
    public int getUnifyAltoServerPort() {
        return UnifyAltoServerPort;
    }
    public void setUnifyAltoServerPort(int unifyAltoServerPort) {
        UnifyAltoServerPort = unifyAltoServerPort;
    }
    public String getUnifyNetworkMapURL() {
        return UnifyNetworkMapURL;
    }
    public void setUnifyNetworkMapURL(String unifyNetworkMapURL) {
        UnifyNetworkMapURL = unifyNetworkMapURL;
    }
    public String getUnifyNetworkMapContentType() {
        return UnifyNetworkMapContentType;
    }
    public void setUnifyNetworkMapContentType(String unifyNetworkMapContentType) {
        UnifyNetworkMapContentType = unifyNetworkMapContentType;
    }
    public String getUnifyNetworkMapName() {
        return UnifyNetworkMapName;
    }
    public void setUnifyNetworkMapName(String unifyNetworkMapName) {
        UnifyNetworkMapName = unifyNetworkMapName;
    }
    public String getInterDomainTopologyServerIP() {
        return InterDomainTopologyServerIP;
    }
    public void setInterDomainTopologyServerIP(
            String interDomainTopologyServerIP) {
        InterDomainTopologyServerIP = interDomainTopologyServerIP;
    }
    public int getInterDomainTopologyServerPort() {
        return InterDomainTopologyServerPort;
    }
    public void setInterDomainTopologyServerPort(
            int interDomainTopologyServerPort) {
        InterDomainTopologyServerPort = interDomainTopologyServerPort;
    }          
}
