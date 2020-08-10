package edu.unicamp.intrig.p5gex.mapServiceBase.plugins;

public interface MapServicePlugin extends Runnable {
    public boolean isRunning();
    public String getPluginName();
    public String displayInfo();
}