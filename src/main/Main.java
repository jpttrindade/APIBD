package main;

import models.Carro;
import models.Model;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ModelsManagerConcret modelsManager = new ModelsManagerConcret();
		
		modelsManager.init();
		
		Carro carro = new Carro("123-123","ix35", 2014, "vermelha");
		carro.setRepositorio((RepositorioGeneric<Carro>) modelsManager.repositorios.get(0));
		for(int i = 0; i < 15; i++){
			carro = new Carro("123-"+i,"ix35", 2014, "vermelha");
			carro.save();
		}
		
		for(Model c : modelsManager.repositorios.get(0).lista){
			
			System.out.println(((Carro)c).getChassi() + " " +((Carro)c).getModelo());
			
			
		}
			

	}

}
