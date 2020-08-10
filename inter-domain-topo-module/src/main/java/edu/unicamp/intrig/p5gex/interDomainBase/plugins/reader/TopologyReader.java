package edu.unicamp.intrig.p5gex.interDomainBase.plugins.reader;

import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

import edu.unicamp.intrig.p5gex.interDomainBase.plugins.InterDomainPlugin;
import edu.unicamp.intrig.p5gex.interDomainBase.InterDomainParams;
import edu.unicamp.intrig.p5gex.interDomainBase.database.TopologiesDataBase;
import edu.unicamp.intrig.p5gex.interDomainBase.plugins.InterDomainPlugin;


public abstract class TopologyReader implements InterDomainPlugin
{
    /**
     * Logger
     */
    protected Logger log=Logger.getLogger("TMController");
    
    protected TopologiesDataBase ted;
    protected InterDomainParams params;
    protected Lock lock;
    
    public TopologyReader(TopologiesDataBase ted, InterDomainParams params, Lock lock)
    {
        this.ted = ted;
        this.params = params;
        this.lock = lock;
    }
    
    abstract public void readTopology();
    
}
