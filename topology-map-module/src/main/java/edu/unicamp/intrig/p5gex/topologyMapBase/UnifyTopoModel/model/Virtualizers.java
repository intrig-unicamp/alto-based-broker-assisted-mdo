package edu.unicamp.intrig.p5gex.topologyMapBase.UnifyTopoModel.model;

import java.util.ArrayList;
import java.util.List;

public class Virtualizers {
    private List<VirtualizerSchema> virtualizer = new ArrayList<VirtualizerSchema>();

    public List<VirtualizerSchema> getVirtualizer() {
        return virtualizer;
    }

    public void setVirtualizer(List<VirtualizerSchema> virtualizer) {
        this.virtualizer = virtualizer;
    }
}
