package models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

import annotation.PrimaryKey;

public abstract class ModelsManager {
	protected ArrayList<Class> classes = new ArrayList<Class>();
	
	public void init(){
		initializeModels();
		print();
	}
	
	private void print() {
		for(Class c : classes){
			System.out.println(c.getDeclaredFields().length);
			for(Field f : c.getDeclaredFields()){
				for(Annotation a : f.getAnnotations()){
					System.out.println(a);
				}
				System.out.println(f.getName());
			}
		}
	}

	protected abstract void initializeModels();
	
	protected void addClass(Class... clas) {
		for(Class c : clas)
			classes.add(c);
		
	}
	
	
}
