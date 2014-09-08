package models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import annotation.OneToMany;


public abstract class ModelsManager {
	public ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

	public static final String Many2Many = "ManyToMany";
	public static final String One2Many = "OneToMany";
	public static final String PK = "PrimaryKey";

	private static final String SQLIgnore = "SQLIgnore";


	HashMap<String, ArrayList<ArrayList<String>>> settingsFK = new HashMap<>();
	HashMap<String, ArrayList<String>> settingsPK = new HashMap<>();

	HashMap<Class<?>, ArrayList<String[]>> columns = new HashMap<>();

	public void init(){
		initializeModels();
		String script = getScriptCreation();

		System.out.println(script);

	}


	private String getScriptCreation() {
		StringBuilder sb = new StringBuilder();

		for(Class<?> c : classes){
			sb.append("CREATE TABLE ")
			.append(c.getSimpleName())
			.append("(\n");

			for(String[] col : columns.get(c)){
				sb.append(col[0])
				.append(" ")
				.append(col[1])
				.append(",")
				.append("\n");
			}
			sb.append("PRIMARY KEY")
			.append(getPrimaryKeyString(settingsPK.get(c.getCanonicalName())));



			ArrayList<ArrayList<String>> fks = getForeignKeys(c);

			for(ArrayList<String> fk : fks){
				sb.append(getForeignKeyString(fk));
			}
			sb.append(");\n");
		}

		return sb.toString();

	}


	private String getForeignKeyString(ArrayList<String> fk) {
		StringBuilder sb = new StringBuilder();
		sb.append(",\n")
		.append("FOREIGN KEY (");
		// inclui os nomes das foreign keys
		String classe = fk.get(0);

		for(int i = 1; i<fk.size(); i++){
			sb.append(classe.toLowerCase())
			.append("_")
			.append(fk.get(i));
			if(i!=fk.size()-1){
				sb.append(",");
			}

		}
		// -----
		sb.append(") REFERENCES ")
		.append(classe)
		.append(" ")
		.append(getPrimaryKeyString(fk.subList(1, fk.size())))
		.append("\n");

		return sb.toString();
	}


	protected abstract void initializeModels();

	protected <T> void addClass(Class<?>... clas) {
		for(Class<?> c : clas){
			classes.add(c);
			getSettings(c);
			getColumns(c);

		}
	}

	public Class<?> getClassBySimpleName(String simpleName){
		Class<?> classe= null;
		for(Class<?> c : classes){
			if(c.getSimpleName().equals(simpleName)){
				classe  = c;
				break;
			}
		}
		return classe;
	}

	public void getSettings(Class<?> c){
		ArrayList<String> pks = getPrimaryKeys(c.getDeclaredFields());
		settingsPK.put(c.getCanonicalName(), pks);

		///////////////////////////0000000000000000////////////////////

		ArrayList<String> fks = getForeignKey(c);
		ArrayList<Class<?>> classes = getFKClasses(c.getDeclaredFields());
		ArrayList<ArrayList<String>> fkSett = null;

		for(Class<?> cl : classes){
			fkSett = settingsFK.get(cl);
			if(fkSett == null){
				fkSett = new ArrayList<>();
			}

			fkSett.add(fks);
			settingsFK.put(cl.getCanonicalName(), fkSett);
			ArrayList<String[]> cols = getColumns(cl);
			String classe = fks.get(0);
			for(int i = 1; i< fks.size(); i++){
				cols.add(new String[]{classe.toLowerCase()+"_"+fks.get(i), getSQLType(c, fks.get(i))});
			}
		}

	}


	private ArrayList<Class<?>> getFKClasses(Field[] declaredFields) {
		ArrayList<Class<?>> classes = new ArrayList<>();
		Class<?> classe = null;
		for(Field f : declaredFields){
			Annotation[] an = f.getDeclaredAnnotations();
			for(Annotation a : an){
				if(a.annotationType().getSimpleName().equals("OneToMany")){

					classe = getClassBySimpleName(((OneToMany)a).classe());
					classes.add(classe);
				}
			}
		}
		return classes;
	}




	private String getSQLType(Class<?> cl, String fieldName) {
		String type = "TEXT";
		for(Field f : cl.getDeclaredFields()){
			if(f.getName().equals(fieldName)){
				type = convertType(f);
				break;
			}
		}
		return type;
	}


	private ArrayList<String> getForeignKey(Class<?> c) {

		ArrayList<String> fks = new ArrayList<String>();
		fks.add(c.getSimpleName());
		for(String s : getPrimaryKeys(c.getDeclaredFields())){
			fks.add(s);
		}
		return fks;
	}


	private ArrayList<ArrayList<String>> getForeignKeys(Class<?> c) {
		ArrayList<ArrayList<String>> fks = settingsFK.get(c.getCanonicalName());
		if(fks == null){
			fks = new ArrayList<>();
		}	
		return fks;
	}

	private StringBuilder getPrimaryKeyString(List<String> pks){
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for(int i = 0; i<pks.size(); i++){
			sb.append(pks.get(i));
			if(i < pks.size()-1){
				sb.append(", ");
			}

		}
		sb.append(")");
		return sb;
	}

	private ArrayList<String> getPrimaryKeys(Field[] fds) {
		ArrayList<String> pks = new ArrayList<>();

		for(Field f : fds){
			Annotation[] an = f.getDeclaredAnnotations();

			for(Annotation a : an){
				if(a.annotationType().getSimpleName().equals("PrimaryKey")){
					pks.add(f.getName());
				}
			}
		}

		return pks;
	}

	public ArrayList<String[]> getColumns(Class<?> c){
		ArrayList<String[]> cols = columns.get(c);
		if(cols == null){
			cols = new ArrayList<>();

			for(Field f : c.getDeclaredFields()){
				Annotation[] an = f.getDeclaredAnnotations();
				if(an.length > 0){
					for(Annotation a : an){
						if(!a.annotationType().getSimpleName().equals(SQLIgnore)){
							cols.add(new String[]{f.getName(), convertType(f)});
							break;
						}
					}
				}else {
					cols.add(new String[]{f.getName(), convertType(f)});
				}
			}
			columns.put(c, cols);
		}

		return cols;
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
