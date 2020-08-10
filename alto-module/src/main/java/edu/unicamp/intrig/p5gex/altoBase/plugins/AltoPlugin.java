package edu.unicamp.intrig.p5gex.altoBase.plugins;

public interface AltoPlugin extends Runnable {
    public boolean isRunning();
    public String getPluginName();
    public String displayInfo();
}