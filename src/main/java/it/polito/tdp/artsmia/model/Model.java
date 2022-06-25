package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private List<Artist> artisti;
	private Graph<Artist, DefaultWeightedEdge> grafo;
	private List<Collegamento> archi;
	private List<Artist> best;
	private int peso;
	
	public int getPeso() {
		return peso;
	}
	
	public List<String> getRoles(){
		this.dao = new ArtsmiaDAO();
		return this.dao.getRoles();
	}
	
	public List<Artist> getArtistsByRole(String role){
		this.dao = new ArtsmiaDAO();
		return artisti = this.dao.getArtistsByRole(role);	
	}
	
	public List<Collegamento> getCollegamenti(String role){
		this.dao = new ArtsmiaDAO();
		return this.dao.getCollegamenti(role);
	}
	
	public String creaGrafo(String role) {
		this.grafo = new SimpleWeightedGraph<Artist, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//INSERISCO I VERTICI
		
		Graphs.addAllVertices(grafo, this.getArtistsByRole(role));
		
		//INSERISCO GLI ARCHI
		
		archi = this.getCollegamenti(role);
		//System.out.println(archi.size());	//DEBUGGING
		
		for(Collegamento c : archi) {
			Graphs.addEdgeWithVertices(grafo, c.getA1(), c.getA2(), c.getWeight());
		}
		
		String result= "Grafo creato\n#VERTICI: " + grafo.vertexSet().size() + "\n#ARCHI: " + grafo.edgeSet().size();
		return result;
	}
	
	public List<Collegamento> getArtistiConnessi(){
		List<Collegamento> result = new ArrayList<Collegamento>(archi);
		Collections.sort(result);
		return result;
	}
	
	public Artist getArtistById(Integer id){
		for(Artist a : artisti) {
			if(a.getArtistId()==id)
				return a;
		}
		return null;
		
	}
	
	public List<Artist> calcolaPercorso(Artist artista){
		best = new ArrayList<Artist>();
		List<Artist> parziale = new ArrayList<Artist>();
		parziale.add(artista);
		
		Set<DefaultWeightedEdge> archiVicini = grafo.outgoingEdgesOf(artista);
		for(DefaultWeightedEdge e : archiVicini) {
			parziale.add(Graphs.getOppositeVertex(grafo, e, artista));
			peso = (int) grafo.getEdgeWeight(e);
			this.cerca(parziale, peso);
		}
		
		return best;
		
	}

	private void cerca(List<Artist> parziale, int peso) {
		
		int flInserimento = 0;
		//CASO NORMALE
		Set<DefaultWeightedEdge> archiVicini = grafo.outgoingEdgesOf(parziale.get(parziale.size()-1));
		for(DefaultWeightedEdge e : archiVicini) {
			Artist inseribile = Graphs.getOppositeVertex(grafo, e, parziale.get(parziale.size()-1));
			if(peso==grafo.getEdgeWeight(e) &&
					!parziale.contains(inseribile)) {
				//STO INSERENDO UN OGGETTO NELLA LISTA PARZIALE
				flInserimento=1;
				parziale.add(inseribile);
				this.cerca(parziale, peso);
				parziale.remove(inseribile);
			}
		}
		
		
		
		if(parziale.size()>best.size() && flInserimento==0) {	//CASO TERMINALE
			best = new ArrayList<Artist>(parziale);
		}
		
		//CASO NORMALE
		
	}

}
