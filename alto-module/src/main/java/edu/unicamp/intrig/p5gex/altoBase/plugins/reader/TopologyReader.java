package edu.unicamp.intrig.p5gex.altoBase.plugins.reader;

import java.util.concurrent.locks.Lock;

import edu.unicamp.intrig.p5gex.altoBase.AltoParams;
import edu.unicamp.intrig.p5gex.altoBase.plugins.AltoPlugin;


public abstract class TopologyReader implements AltoPlugin {
        
    protected AltoParams params;
    protected Lock lock;
    
    public TopologyReader(AltoParams params, Lock lock)
    {        
        this.params = params;
        this.lock = lock;
    }
    
    abstract public void readTopology();
}
