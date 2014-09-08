package main;

import teste.Carro;
import teste.Pessoa;
import models.ModelsManager;

public class ModelsManagerConcret extends ModelsManager{

	
	@Override
	protected void initializeModels() {
		addClass(Carro.class,
				Pessoa.class);		
	}
	
	 	


	
}
