package edu.unicamp.intrig.p5gex.interDomainBase.plugins;

public interface InterDomainPlugin extends Runnable {
    public boolean isRunning();
    public String getPluginName();
    public String displayInfo();
}