package edu.unicamp.intrig.p5gex.altoBase.plugins.writer;

import java.util.concurrent.locks.Lock;
import edu.unicamp.intrig.p5gex.altoBase.AltoParams;

public class InformationRetriever {    
    AltoParams params;
    Lock lock;
    
    public InformationRetriever(AltoParams params, Lock lock)
    {        
        this.params = params;
        this.lock = lock;
    }      
}
