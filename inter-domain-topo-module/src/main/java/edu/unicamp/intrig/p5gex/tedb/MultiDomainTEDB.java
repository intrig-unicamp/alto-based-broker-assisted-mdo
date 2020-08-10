package edu.unicamp.intrig.p5gex.tedb;

import java.net.Inet4Address;
import java.util.LinkedList;

import org.jgrapht.graph.DirectedWeightedMultigraph;


public interface MultiDomainTEDB extends TEDB {

	public void addInterdomainLink( Object localDomainID, Object localRouterASBR, long localRouterASBRIf, Object remoteDomainID, Object remoteRouterASBR, long remoteRouterASBRIf, TE_Information te_info );
	public void addReachabilityIPv4(Inet4Address domainId,Inet4Address aggregatedIPRange,int prefix);
    public DirectedWeightedMultigraph<Object, InterDomainEdge> getInterDomainLinks2();    
	
}
