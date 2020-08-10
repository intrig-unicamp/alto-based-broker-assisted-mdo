package edu.unicamp.intrig.p5gex.interDomainBase;

import java.util.List;

import edu.unicamp.intrig.p5gex.interDomainBase.util.TADS;

public class InterDomainParams {     
  //AltoAggregatorPlugin
    private boolean isTADS = false;        
    private List<TADS> AltoAggregatorPluginTADS;    
    public boolean isTADS() {
        return isTADS;
    }
    public void setTADS(boolean isTADS) {
        this.isTADS = isTADS;
    }      
    public List<TADS> getAltoAggregatorPluginTADS() {
        return AltoAggregatorPluginTADS;
    }
    public void setAltoAggregatorPluginTADS(List<TADS> altoAggregatorPluginTADS) {
        AltoAggregatorPluginTADS = altoAggregatorPluginTADS;
    }
    
    //UNIFY
    private boolean isUNIFYWriting = false;
    private int UnifyExportPort;    
        
    public int getUnifyExportPort() {
        return UnifyExportPort;
    }
    public void setUnifyExportPort(int unifyExportPort) {
        UnifyExportPort = unifyExportPort;
    }
    
    public boolean isUNIFYWriting() {
        return isUNIFYWriting;
    }
    public void setUNIFYWriting(boolean isUNIFYWriting) {
        this.isUNIFYWriting = isUNIFYWriting;
    }    
}
