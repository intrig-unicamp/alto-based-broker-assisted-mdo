package edu.unicamp.intrig.p5gex.topologyMapBase.plugins.reader;

import java.util.concurrent.locks.Lock;

import edu.unicamp.intrig.p5gex.topologyMapBase.TopologyMapParams;
import edu.unicamp.intrig.p5gex.topologyMapBase.plugins.AltoAggregatorPlugin;


public abstract class TopologyReader implements AltoAggregatorPlugin {
        
    protected TopologyMapParams params;
    protected Lock lock;
    
    public TopologyReader(TopologyMapParams params, Lock lock)
    {        
        this.params = params;
        this.lock = lock;
    }
    
    abstract public void readTopology();
}
