package edu.unicamp.intrig.p5gex.mapServiceBase.plugins.writer;

import java.util.concurrent.locks.Lock;

import edu.unicamp.intrig.p5gex.mapServiceBase.MapServiceParams;

public class InformationRetriever {    
    MapServiceParams params;
    Lock lock;
    
    public InformationRetriever(MapServiceParams params, Lock lock)
    {        
        this.params = params;
        this.lock = lock;
    }      
}
