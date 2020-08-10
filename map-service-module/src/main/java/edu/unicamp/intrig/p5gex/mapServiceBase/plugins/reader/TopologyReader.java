package edu.unicamp.intrig.p5gex.mapServiceBase.plugins.reader;

import java.util.concurrent.locks.Lock;

import edu.unicamp.intrig.p5gex.mapServiceBase.MapServiceParams;
import edu.unicamp.intrig.p5gex.mapServiceBase.plugins.MapServicePlugin;


public abstract class TopologyReader implements MapServicePlugin {
        
    protected MapServiceParams params;
    protected Lock lock;
    
    public TopologyReader(MapServiceParams params, Lock lock)
    {        
        this.params = params;
        this.lock = lock;
    }
    
    abstract public void readTopology();
}
