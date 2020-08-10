package edu.unicamp.intrig.p5gex.mapServiceBase;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import edu.unicamp.intrig.p5gex.mapServiceBase.plugins.MapServicePlugin;
import edu.unicamp.intrig.p5gex.mapServiceBase.plugins.reader.TopologyReaderInterResource;
import edu.unicamp.intrig.p5gex.mapServiceBase.plugins.writer.MapServiceServer;
import edu.unicamp.intrig.p5gex.mapServiceBase.plugins.writer.TopologyServerUnify;

public class MapServiceInitiater {
    MapServiceParamsArray params;
    Lock lock;
    ArrayList<MapServicePlugin> pluginsList;

    private ScheduledThreadPoolExecutor executor;

    public MapServiceInitiater(MapServiceParamsArray params, Lock lock,
            ArrayList<MapServicePlugin> pluginsList) {
        this.params = params;
        this.lock = lock;
        this.pluginsList = pluginsList;
    }

    public void intiate() {
        executor = new ScheduledThreadPoolExecutor(20);
        ArrayList<MapServiceParams> paramList = params.getParamList();
        for (int i = 0; i < paramList.size(); i++) {
            MapServiceParams actualLittleParams = paramList.get(i);

            if (actualLittleParams.isMapPlugin()) {
                MapServicePlugin p = new TopologyReaderInterResource(actualLittleParams,
                        lock);
                executor.execute(p);
                pluginsList.add(p);
            }

            if (actualLittleParams.isMapServer()) {
                MapServicePlugin pAltoServer = new MapServiceServer(actualLittleParams,
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
