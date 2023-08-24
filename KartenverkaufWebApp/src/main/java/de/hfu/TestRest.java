package de.hfu;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Singleton
@Path("/test-rest")
public class TestRest {
	
	@Path("/random-numbers")
	@GET
	@Produces({ APPLICATION_JSON })
	public double [] randomNumbers() {
		double[] x = new double [25];
		for (int i=0; i<x.length; i++)
			x[i] = Math.random();
		return x;
	}

}
