package models;

import annotation.PrimaryKey;

public class Carro extends Model<Carro>{
	@PrimaryKey
	String chassi;
	
	@PrimaryKey
	String modelo;
	
	int ano;
	String cor;
	
	ManyToMany<Pessoa> donos;
	

	public Carro(String chassi, String modelo, int ano, String cor) {
		super();
		this.chassi = chassi;
		this.modelo = modelo;
		this.ano = ano;
		this.cor = cor;
	}


	public String getChassi() {
		return chassi;
	}


	public String getModelo() {
		return modelo;
	}


	public int getAno() {
		return ano;
	}


	public String getCor() {
		return cor;
	}


	public ManyToMany<Pessoa> getDonos() {
		return donos;
	}

	

	
}
