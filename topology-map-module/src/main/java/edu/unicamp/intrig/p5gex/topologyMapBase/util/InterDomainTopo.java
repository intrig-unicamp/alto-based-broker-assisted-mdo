package edu.unicamp.intrig.p5gex.topologyMapBase.util;

public class InterDomainTopo {
    private String serverIP;
    private int serverPort;
    private String REST;

    public InterDomainTopo(String serverIP, int serverPort)
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }
    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
    public String getREST() {
        return REST;
    }
    public void setREST(String rEST) {
        REST = rEST;
    }
}
