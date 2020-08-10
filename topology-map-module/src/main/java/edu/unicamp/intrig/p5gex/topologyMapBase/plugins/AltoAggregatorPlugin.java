package edu.unicamp.intrig.p5gex.topologyMapBase.plugins;

public interface AltoAggregatorPlugin extends Runnable {
    public boolean isRunning();
    public String getPluginName();
    public String displayInfo();
}