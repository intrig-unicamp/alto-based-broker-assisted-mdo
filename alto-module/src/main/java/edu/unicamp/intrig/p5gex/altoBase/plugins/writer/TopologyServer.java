package edu.unicamp.intrig.p5gex.altoBase.plugins.writer;

import java.util.concurrent.locks.Lock;

import edu.unicamp.intrig.p5gex.altoBase.AltoParams;
import edu.unicamp.intrig.p5gex.altoBase.plugins.AltoPlugin;

public abstract class TopologyServer implements AltoPlugin {
    protected AltoParams params;
    protected Lock lock;
    protected InformationRetriever infRetriever;
    
    
    public TopologyServer(AltoParams params, Lock lock)
    {        
        this.params = params;
        this.lock = lock;
        infRetriever = new InformationRetriever(params, lock);
    }
    
    abstract void serveTopology();
}
