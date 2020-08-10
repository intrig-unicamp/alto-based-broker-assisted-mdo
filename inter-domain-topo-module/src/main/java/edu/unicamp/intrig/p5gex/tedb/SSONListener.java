package edu.unicamp.intrig.p5gex.tedb;

import java.util.LinkedList;

public interface SSONListener extends TEDListener{
	
	public void notifyWavelengthReservationSSON(LinkedList<Object> sourceVertexList,
			LinkedList<Object> targetVertexList, int wavelength, int m);
	
	void notifyWavelengthEndReservationSSON(
			LinkedList<Object> sourceVertexList,
			LinkedList<Object> targetVertexList, int wavelength, int m);

}
