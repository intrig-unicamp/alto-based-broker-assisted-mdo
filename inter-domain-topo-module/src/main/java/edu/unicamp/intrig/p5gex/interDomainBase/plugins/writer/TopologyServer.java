package edu.unicamp.intrig.p5gex.interDomainBase.plugins.writer;

import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

import edu.unicamp.intrig.p5gex.interDomainBase.InterDomainParams;
import edu.unicamp.intrig.p5gex.interDomainBase.database.TopologiesDataBase;
import edu.unicamp.intrig.p5gex.interDomainBase.plugins.InterDomainPlugin;
/**
 * 
 * @author jaume
 *
 */
public abstract class TopologyServer implements InterDomainPlugin 
{
	/**
	 * Logger
	 */
	protected static Logger log=Logger.getLogger("TMController");
	
	protected TopologiesDataBase ted;
	protected InterDomainParams params;
	protected Lock lock;
	protected InformationRetriever infRetriever;
	
	
	public TopologyServer(TopologiesDataBase ted, InterDomainParams params, Lock lock)
	{
		this.ted = ted;
		this.params = params;
		this.lock = lock;
		infRetriever = new InformationRetriever(ted, params, lock);
	}
	
	abstract void serveTopology();
}
