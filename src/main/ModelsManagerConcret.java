package main;

import java.util.ArrayList;

import models.Carro;
import models.Model;
import models.ModelsManager;
import models.Pessoa;

public class ModelsManagerConcret extends ModelsManager{

	
	@Override
	protected void initializeModels() {
		addClass(Carro.class,
				Pessoa.class);
			
		
		
			
		
		
	}
	
	 	


	
}
