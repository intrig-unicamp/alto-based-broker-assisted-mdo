package edu.unicamp.intrig.p5gex.interDomainBase;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

import edu.unicamp.intrig.p5gex.interDomainBase.plugins.InterDomainPlugin;
import edu.unicamp.intrig.p5gex.interDomainBase.plugins.reader.TopologyReaderTADS;
import edu.unicamp.intrig.p5gex.interDomainBase.plugins.InterDomainPlugin;
import edu.unicamp.intrig.p5gex.interDomainBase.plugins.writer.TopologyServerUnify;
import edu.unicamp.intrig.p5gex.interDomainBase.plugins.reader.TopologyReaderTADS;
import edu.unicamp.intrig.p5gex.interDomainBase.InterDomainParamsArray;
import edu.unicamp.intrig.p5gex.interDomainBase.database.TopologiesDataBase;
import edu.unicamp.intrig.p5gex.interDomainBase.plugins.InterDomainPlugin;

public class InterDomainInitiater {
    Logger log = Logger.getLogger("TMController");

    TopologiesDataBase ted;
    InterDomainParamsArray params;
    Lock lock;
    ArrayList<InterDomainPlugin> pluginsList;

    private ScheduledThreadPoolExecutor executor;

    public InterDomainInitiater(TopologiesDataBase ted,
            InterDomainParamsArray params, Lock lock,
            ArrayList<InterDomainPlugin> pluginsList) {
        this.ted = ted;
        this.params = params;
        this.lock = lock;
        this.pluginsList = pluginsList;
    }

    public void intiate() {
        executor = new ScheduledThreadPoolExecutor(20);
        ArrayList<InterDomainParams> paramList = params.getParamList();
        for (int i = 0; i < paramList.size(); i++) {
            InterDomainParams actualLittleParams = paramList.get(i);

            if (actualLittleParams.isTADS()) {
                InterDomainPlugin p = new TopologyReaderTADS(ted,
                        actualLittleParams, lock);
                executor.execute(p);
                pluginsList.add(p);
            }

            if (actualLittleParams.isUNIFYWriting()) {

                actualLittleParams.setAltoAggregatorPluginTADS(paramList.get(0)
                        .getAltoAggregatorPluginTADS());
                actualLittleParams.setTADS(paramList.get(0).isTADS());

                InterDomainPlugin p = new TopologyServerUnify(ted,
                        actualLittleParams, lock);
                executor.execute(p);
                pluginsList.add(p);
                // }
                // } while (!actualLittleParams.isTADS());
            }

            // if (actualLittleParams.isUNIFYWriting()) {
            // AltoPlugin pAltoPlugin = new TopologyServerUnify(
            // actualLittleParams, lock);
            // executor.execute(pAltoPlugin);
            // pluginsList.add(pAltoPlugin);
            // }
        }

    }
}
