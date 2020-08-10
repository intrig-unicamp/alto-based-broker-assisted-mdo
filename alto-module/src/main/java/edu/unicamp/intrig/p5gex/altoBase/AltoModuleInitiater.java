package edu.unicamp.intrig.p5gex.altoBase;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import edu.unicamp.intrig.p5gex.altoBase.plugins.AltoPlugin;
import edu.unicamp.intrig.p5gex.altoBase.plugins.reader.TopologyReaderAggregator;
import edu.unicamp.intrig.p5gex.altoBase.plugins.writer.AltoServer;
import edu.unicamp.intrig.p5gex.altoBase.plugins.writer.TopologyServerUnify;

public class AltoModuleInitiater {
    AltoParamsArray params;
    Lock lock;
    ArrayList<AltoPlugin> pluginsList;

    private ScheduledThreadPoolExecutor executor;

    public AltoModuleInitiater(AltoParamsArray params, Lock lock,
            ArrayList<AltoPlugin> pluginsList) {
        this.params = params;
        this.lock = lock;
        this.pluginsList = pluginsList;
    }

    public void intiate() {
        executor = new ScheduledThreadPoolExecutor(20);
        ArrayList<AltoParams> paramList = params.getParamList();
        for (int i = 0; i < paramList.size(); i++) {
            AltoParams actualLittleParams = paramList.get(i);

            if (actualLittleParams.isAltoPlugin()) {
                AltoPlugin p = new TopologyReaderAggregator(actualLittleParams,
                        lock);
                executor.execute(p);
                pluginsList.add(p);
            }

            if (actualLittleParams.isAltoServer()) {
                AltoPlugin pAltoServer = new AltoServer(actualLittleParams,
                        lock);
                executor.execute(pAltoServer);
                pluginsList.add(pAltoServer);
            }

            //if (actualLittleParams.isUNIFYWriting()) {
            //    AltoPlugin pAltoPlugin = new TopologyServerUnify(
            //            actualLittleParams, lock);
            //    executor.execute(pAltoPlugin);
            //    pluginsList.add(pAltoPlugin);
            //}
        }

    }
}
