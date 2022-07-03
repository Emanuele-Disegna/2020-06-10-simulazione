package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private ImdbDAO dao;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private Map<Integer, Actor> idMap;
	
	public Model() {
		dao = new ImdbDAO();
	}
	
	public void creaGrafo(String genere) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		idMap = new HashMap<>();
		dao.getVertici(genere, idMap);
		
		Graphs.addAllVertices(grafo, idMap.values());
		
		for(Adiacenza a : dao.getArchi(genere, idMap)) {
			Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA1(), a.getPeso());
		}
		
		System.out.println(grafo.vertexSet().size());
		System.out.println(grafo.edgeSet().size());
	}
	
	public List<Actor> getSimili(Actor a) {
		ConnectivityInspector<Actor, DefaultWeightedEdge> c = new ConnectivityInspector<>(grafo);
		List<Actor> ret = new ArrayList<>(c.connectedSetOf(a));

		Collections.sort(ret, Comparator.comparing(Actor::getLastName));
		
		return ret;
	}
	
	public List<String> getGeneri(){
		return dao.getGeneri();
	}
	
	public Collection<Actor> getAttori(){
		return idMap.values();
	}
}
