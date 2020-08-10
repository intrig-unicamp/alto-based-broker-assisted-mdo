package edu.unicamp.intrig.p5gex.tedb;

import java.util.LinkedList;

import edu.unicamp.intrig.p5gex.interDomainBase.UnifyTopoModel.model.VirtualizerSchema;

/**
 * Base Interface for a Generic Traffic Engineering Database
 * @author ogondio
 *
 */
public interface TEDB {

	public void initializeFromFile(String file);

	public void initializeFromFile(String file, String learnFrom);
	
	public void initializeFromTADS(VirtualizerSchema objTopology, String learnFrom);


	public boolean isITtedb(); //FIXME: Remove!
	
	public String printTopology();

	public LinkedList<InterDomainEdge> getInterDomainLinks();

}
