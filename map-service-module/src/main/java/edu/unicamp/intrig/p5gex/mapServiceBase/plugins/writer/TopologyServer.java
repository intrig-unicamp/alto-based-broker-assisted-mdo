package edu.unicamp.intrig.p5gex.mapServiceBase.plugins.writer;

import java.util.concurrent.locks.Lock;

import edu.unicamp.intrig.p5gex.mapServiceBase.MapServiceParams;
import edu.unicamp.intrig.p5gex.mapServiceBase.plugins.MapServicePlugin;

public abstract class TopologyServer implements MapServicePlugin {
    protected MapServiceParams params;
    protected Lock lock;
    protected InformationRetriever infRetriever;
    
    
    public TopologyServer(MapServiceParams params, Lock lock)
    {        
        this.params = params;
        this.lock = lock;
        infRetriever = new InformationRetriever(params, lock);
    }
    
    abstract void serveTopology();
}
