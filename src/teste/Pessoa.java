package teste;

import java.util.ArrayList;
import java.util.Date;

import models.Model;
import annotation.ManyToMany;
import annotation.OneToMany;
import annotation.PrimaryKey;

public class Pessoa implements Model<Pessoa> {
	
	@PrimaryKey
	private int cpf;
	@PrimaryKey
	private int age;
	
	String name;
	
	@OneToMany(classe= "Carro")
	private ArrayList<Carro> carros;

	Date dataNascimento;
	
	private boolean hasChange;

	public Pessoa(int age, String name, ArrayList<Carro> carros){
		//this(age, name, new OneToMany<>(carros));
		super();
		this.age = age;
		this.name = name;
		this.carros = carros;
		this.hasChange = false;
		this.carros = carros;
	}
	
/*	public Pessoa(int age, String name, OneToMany<Carro> carros) {
		super();
		this.age = age;
		this.name = name;
		this.carros = carros;
		this.hasChange = false;
	}*/
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name+" - "+age+": "+ carros.toString();
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
		return new PrimaryKey<?>[]{cpf};
	}
*/
	
}
