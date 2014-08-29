package models;

import annotation.PrimaryKey;

public class Carro{
	@PrimaryKey(ordem=0)
	String chassi;
	
	@PrimaryKey(ordem=1)
	String modelo;
	int ano;
	String cor;
		
}
