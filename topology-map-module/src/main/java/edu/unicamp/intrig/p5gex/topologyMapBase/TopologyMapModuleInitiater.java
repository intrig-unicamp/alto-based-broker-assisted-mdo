package edu.unicamp.intrig.p5gex.topologyMapBase;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import edu.unicamp.intrig.p5gex.topologyMapBase.plugins.AltoAggregatorPlugin;
import edu.unicamp.intrig.p5gex.topologyMapBase.plugins.reader.TopologyReaderInterTopo;


public class TopologyMapModuleInitiater {
    TopologyMapParamsArray params;
    Lock lock;
    ArrayList<AltoAggregatorPlugin> pluginsList;

    private ScheduledThreadPoolExecutor executor;
    
    public TopologyMapModuleInitiater(TopologyMapParamsArray params, Lock lock, ArrayList<AltoAggregatorPlugin> pluginsList)
    {        
        this.params = params;
        this.lock = lock;
        this.pluginsList=pluginsList;
    }
    public void intiate()
    {
        executor = new ScheduledThreadPoolExecutor(20);
        ArrayList<TopologyMapParams> paramList = params.getParamList();
        for (int i = 0; i < paramList.size(); i++)
        {
            TopologyMapParams actualLittleParams = paramList.get(i);
            
            if(actualLittleParams.isAltoAggregatorPlugin())
            {
                AltoAggregatorPlugin p = new TopologyReaderInterTopo(actualLittleParams,lock);               
                executor.execute(p);
                pluginsList.add(p);
            }
            
            //if(actualLittleParams.isAltoServer())
            //{
                //AltoAggregatorPlugin pAltoServer = new AltoServer( actualLittleParams,lock);
                //executor.execute(pAltoServer);
                //pluginsList.add(pAltoServer);
            //}
            
            //if (actualLittleParams.isUNIFYWriting())
            //{
                //AltoAggregatorPlugin pAltoPlugin = new TopologyServerUnify( actualLittleParams,lock);
                //executor.execute(pAltoPlugin);
                //pluginsList.add(pAltoPlugin);
            //}                      
        }
        
        
        
    }
}
