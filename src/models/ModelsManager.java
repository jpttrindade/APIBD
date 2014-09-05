package models;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;


public abstract class ModelsManager {
	public ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

	public static final String Many2Many = "ManyToMany";
	public static final String One2Many = "OneToMany";
	public static final String PK = "PrimaryKey";

	public void init(){
		initializeModels();
	}


	protected abstract void initializeModels();

	protected <T> void addClass(Class<?>... clas) {
		for(Class<?> c : clas){
			classes.add(c);

		}
	}

	public String getSettings(Class<?> c){
		StringBuilder sb = new StringBuilder();
		ArrayList<String> pks = getPrimaryKeys(c.getDeclaredFields());
		sb.append("PRIMARY KEY (");
		for(int i = 0; i<pks.size(); i++){
			sb.append(pks.get(i));
			if(i < pks.size()-1){
				sb.append(", ");
			}

		}
		sb.append(")");

		ArrayList<String> fks = getForeignKeys(c.getDeclaredFields());
		if(fks.size()>0){
			sb.append(", ")
			.append("\n");

			for(int i = 0; i<fks.size(); i++){
				sb.append("FOREIGN KEY (")
				.append(fks.get(i))
				.append(")");

				if(i < fks.size()-1){
					sb.append(", ")
					.append("\n");
				}
			}
		}


		System.out.println(sb.toString());
		return sb.toString();
	}

	private ArrayList<String> getForeignKeys(Field[] fds) {
		ArrayList<String> fks = new ArrayList<>();


		for(Field f : fds){
			if(f.getType().getSimpleName().equals(One2Many)){
				fks.add(f.getName());
			};
		}
		return fks;
	}


	private ArrayList<String> getPrimaryKeys(Field[] fds) {
		ArrayList<String> pks = new ArrayList<>();


		for(Field f : fds){
			if(f.getType().getSimpleName().equals(PK)){
				pks.add(f.getName());
			};
		}
		return pks;
	}


	public String getColumns(Class<?> c){
		StringBuilder columns = new StringBuilder();
		String type = "";

		for(Field f : c.getDeclaredFields()){
			type = f.getType().getSimpleName();
			switch (type) {
			case PK:
				columns.append(f.getName()).append(" ")
				.append(convertType(f))
				.append(" ")
				.append("Primary Key").append(", ");
				break;
			case Many2Many:

				break;

			case One2Many:

				break;
			default:
				columns.append(f.getName()).append(" ").append(convertType(f)).append(", ");
				break;
			}
		}

		return columns.toString();
	}


	private String convertType(Field f) {
		String tp;
		Type type = f.getGenericType();

		if(type instanceof ParameterizedType){
			type = ((ParameterizedType)type).getActualTypeArguments()[0];
		}

		switch (type.toString()) {
		case "int":
			tp = "INTEGER";
			break;
		case "boolean":
			tp = "BOOLEAN";
			break;
		case "class java.util.Date":
			tp = "DATE";
			break;
		default:
			tp = "TEXT";
			break;
		}

		return tp;
	}


}
