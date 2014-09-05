package models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import annotation.OneToMany;


public abstract class ModelsManager {
	public ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

	public static final String Many2Many = "ManyToMany";
	public static final String One2Many = "OneToMany";
	public static final String PK = "PrimaryKey";


	HashMap<String, ArrayList<String>> settingsFK = new HashMap<>();
	HashMap<String, StringBuilder> settingsPK = new HashMap<>();

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
			.append(settingsPK.get(c.getCanonicalName()));
			ArrayList<String> fks = getForeignKeys(c);

			for(String fk : fks){
				sb.append(fk);
			}
			sb.append(");\n");
		}

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

	public String getSettings(Class<?> c){
		StringBuilder sb = getPrimaryKeys(c.getDeclaredFields());
		settingsPK.put(c.getCanonicalName(), sb);

		///////////////////////////0000000000000000////////////////////
		sb = new StringBuilder();

		for(Field f : c.getDeclaredFields()){
			Annotation[] an = f.getDeclaredAnnotations();
			for(Annotation a : an){
				if(a.annotationType().getSimpleName().equals("OneToMany")){
					sb.append(",\n")
					.append("FOREIGN KEY (");
					sb.append("id_")
					.append(c.getSimpleName().toLowerCase());
					sb.append(") REFERENCES ")
					.append(c.getSimpleName())
					.append(" ")
					.append(settingsPK.get(c.getCanonicalName()).toString())
					.append("\n");

					Class<?> classe = getClassBySimpleName(((OneToMany)a).classe());

					/*	StringBuilder pktemp = settingsPK.get(classe.getCanonicalName());

					if(pktemp == null){
						pktemp = getPrimaryKeys(classe.getDeclaredFields());
					}
					 */
					ArrayList<String> fkSett = settingsFK.get(classe);
					if(fkSett == null){
						fkSett = new ArrayList<>();
					}
					fkSett.add(sb.toString());
					settingsFK.put(classe.getCanonicalName(), fkSett);
					ArrayList<String[]> cols = getColumns(classe);				
					cols.add(new String[]{"id_"+c.getSimpleName().toLowerCase(), getPrimaryKeysType(c.getDeclaredFields()).toString()});
					columns.put(classe, cols);
					
				}
			}



		}
		return sb.toString();
	}

	

	private ArrayList<String> getForeignKeys(Class<?> c) {
		ArrayList<String> fks = settingsFK.get(c.getCanonicalName());
		if(fks == null){
			fks = new ArrayList<>();
		}	
		return fks;
	}


	private StringBuilder getPrimaryKeys(Field[] fds) {
		ArrayList<String> pks = new ArrayList<>();

		for(Field f : fds){
			Annotation[] an = f.getDeclaredAnnotations();

			for(Annotation a : an){
				if(a.annotationType().getSimpleName().equals("PrimaryKey")){
					pks.add(f.getName());
				}
			}
		}
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
	
	// tem q ver aqui ooohhh
	private String getPrimaryKeysType(Field[] declaredFields) {
		// TODO Auto-generated method stub
		return "TEXT";
	}


	public ArrayList<String[]> getColumns(Class<?> c){
		ArrayList<String[]> cols = columns.get(c);
		if(cols == null){
			cols = new ArrayList<>();

			String type = "";

			for(Field f : c.getDeclaredFields()){
				type = f.getType().getSimpleName();
				switch (type) {
				/*			case PK:
				cols.append(f.getName()).append(" ")
				.append(convertType(f))
				.append(" ")
				.append("Primary Key").append(", ");
				break;*/
				case Many2Many:

					break;

				case One2Many:

					break;
				default:
					cols.add(new String[]{f.getName(), convertType(f)});
					break;
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
