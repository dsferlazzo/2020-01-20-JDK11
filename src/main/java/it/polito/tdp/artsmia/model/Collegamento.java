package it.polito.tdp.artsmia.model;

public class Collegamento implements Comparable<Collegamento> {

	private Artist a1;
	private Artist a2;
	private int weight;
	public Collegamento(Artist a1, Artist a2, int weight) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.weight = weight;
	}
	public Artist getA1() {
		return a1;
	}
	public Artist getA2() {
		return a2;
	}
	public int getWeight() {
		return weight;
	}
	@Override
	public String toString() {
		return " Artista 1: " + a1 + " Artista 2: " + a2 +" Esposizioni comuni: " + weight ;
	}
	@Override
	public int compareTo(Collegamento o) {
		return this.weight-o.getWeight();
	}
	
	
}
