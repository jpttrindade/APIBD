package models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

import main.RepositorioGeneric;
import annotation.PrimaryKey;

public abstract class ModelsManager {
	protected ArrayList<Class> classes = new ArrayList<Class>();

	public ArrayList<RepositorioGeneric<? extends Model>> repositorios =new ArrayList<>(); 


	public void init(){
		initializeModels();
		print();
	}

	private void print() {
		for(Class c : classes){
			System.out.println(c.getDeclaredFields().length);
			for(Field f : c.getDeclaredFields()){
				System.out.println(f.getType());
				for(Annotation a : f.getAnnotations()){
					System.out.println(a);
				}
				System.out.println(f.getName());
			}
		}
	}

	protected abstract void initializeModels();

	protected <T> void addClass(Class... clas) {
		for(Class c : clas){
			classes.add(c);
			RepositorioGeneric<T> rep = new RepositorioGeneric<T>();
			repositorios.add((RepositorioGeneric<? extends Model>) rep);

		}
	}


}
