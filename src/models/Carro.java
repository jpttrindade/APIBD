package models;

import annotation.PrimaryKey;



public class Carro implements Model<Carro>{
	
/*	PrimaryKey<String> chassi;
	PrimaryKey<String> modelo;
*/	
	@PrimaryKey
	String chassi;
	
	@PrimaryKey
	String modelo;
	
	int ano;
	String cor;
	
	private boolean hasChange;


	public Carro(String chassi2, String modelo2, int ano2, String cor2) {
		super();
		//this.chassi = new PrimaryKey<String>(chassi2);
		//this.modelo = new PrimaryKey<String>(modelo2);
		this.chassi = chassi2;
		this.modelo =modelo2;
		this.ano = ano2;
		this.cor = cor2;
		this.hasChange = false;
	}


	public String getChassi() {
		//return chassi.getPk();
		return chassi;
	}


	public String getModelo() {
		//return modelo.getPk();
		return modelo;
	}


	public int getAno() {
		return ano;
	}


	public String getCor() {
		return cor;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Chassi/Modelo: "+ chassi + "/"+modelo + 
				"\nAno:"+ ano+ 
				"\nCor:" +cor;
	}


	@Override
	public boolean save() {
		return false;
	}


	@Override
	public boolean delete() {
		return false;
	}


	@Override
	public boolean hasChange() {
		return hasChange;
	}


/*	@Override
	public PrimaryKey<?>[] getPrimaryKies() {
		return new PrimaryKey<?>[]{chassi, modelo};
	}
	*/
}
