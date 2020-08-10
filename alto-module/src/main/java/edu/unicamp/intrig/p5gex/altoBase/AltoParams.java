package edu.unicamp.intrig.p5gex.altoBase;

public class AltoParams {
    //AltoPlugin
    private boolean isAltoPlugin = false;
    private String AltoPluginAltoServerIP;
    private int AltoPluginAltoServerPort;
    private String AltoPluginAggregatorServerIP;
    private int AltoPluginAggregatorServerPort;
    private String AltoPluginNetMapName;
    
    //AltoServer    
    private boolean isAltoServer = false;
    private int AltoServerPort;   
    private String AltoServerDBip;
    private int AltoServerDBport;
    
    //UNIFY
    private boolean isUNIFYWriting = false;
    private int UnifyExportPort;
    private String UnifyAltoServerIP;
    private int UnifyAltoServerPort;
    private String UnifyNetworkMapURL;
    private String UnifyNetworkMapContentType;
    private String UnifyNetworkMapName;
    
    public String getAltoPluginAltoServerIP() {
        return AltoPluginAltoServerIP;
    }
    public void setAltoPluginAltoServerIP(String altoPluginAltoServerIP) {
        AltoPluginAltoServerIP = altoPluginAltoServerIP;
    }
    public int getAltoPluginAltoServerPort() {
        return AltoPluginAltoServerPort;
    }
    public void setAltoPluginAltoServerPort(int altoPluginAltoServerPort) {
        AltoPluginAltoServerPort = altoPluginAltoServerPort;
    }
    public String getAltoPluginAggregatorServerIP() {
        return AltoPluginAggregatorServerIP;
    }
    public void setAltoPluginAggregatorServerIP(
            String altoPluginAggregatorServerIP) {
        AltoPluginAggregatorServerIP = altoPluginAggregatorServerIP;
    }
    public int getAltoPluginAggregatorServerPort() {
        return AltoPluginAggregatorServerPort;
    }
    public void setAltoPluginAggregatorServerPort(
            int altoPluginAggregatorServerPort) {
        AltoPluginAggregatorServerPort = altoPluginAggregatorServerPort;
    }
    public int getAltoServerPort() {
        return AltoServerPort;
    }
    public void setAltoServerPort(int altoServerPort) {
        AltoServerPort = altoServerPort;
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
    public boolean isUNIFYWriting() {
        return isUNIFYWriting;
    }
    public void setUNIFYWriting(boolean isUNIFYWriting) {
        this.isUNIFYWriting = isUNIFYWriting;
    }
    public boolean isAltoServer() {
        return isAltoServer;
    }
    public void setAltoServer(boolean isAltoServer) {
        this.isAltoServer = isAltoServer;
    }
    public boolean isAltoPlugin() {
        return isAltoPlugin;
    }
    public void setAltoPlugin(boolean isAltoPlugin) {
        this.isAltoPlugin = isAltoPlugin;
    }
    public String getAltoServerDBip() {
        return AltoServerDBip;
    }
    public void setAltoServerDBip(String altoServerDBip) {
        AltoServerDBip = altoServerDBip;
    }
    public int getAltoServerDBport() {
        return AltoServerDBport;
    }
    public void setAltoServerDBport(int altoServerDBport) {
        AltoServerDBport = altoServerDBport;
    }
    public String getAltoPluginNetMapName() {
        return AltoPluginNetMapName;
    }
    public void setAltoPluginNetMapName(String altoPluginNetMapName) {
        AltoPluginNetMapName = altoPluginNetMapName;
    }    
}
