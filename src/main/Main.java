package main;

import java.util.ArrayList;
import teste.Carro;
import teste.Pessoa;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ModelsManagerConcret modelsManager = new ModelsManagerConcret();
		
		modelsManager.init();
	
		ArrayList<Carro> carros = new ArrayList<>();
		
		for(int i = 0; i<10; i++){
			carros.add(new Carro("m"+i+(i+(i*3))+"-","a"+i,1992,"black"));
		}
		Pessoa pedro = new Pessoa(15, "Pedro", carros);
			
		//System.out.println(pedro);
		
//		for(Field f : pedro.getClass().getDeclaredFields()){
//			switch (f.getType().getSimpleName()) {
//			case "ManyToMany":
//				System.out.println("ManyToMany");
//				break;
//			case "OneToMany":
//				System.out.println("OneToMany");
//				break;
//			default:
//				System.out.println(f.getName() + " : "+ f.getType().getSimpleName());
//				break;
//			}
//		}
//			

	}

}
