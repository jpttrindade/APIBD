package models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.omg.CORBA.OMGVMCID;

import table.Attribute;
import table.ForeignKey;
import table.Table;
import annotation.ManyToMany;
import annotation.OneToMany;
import annotation.PrimaryKey;


public abstract class ModelsManager {
	public ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

	
	
	public static final String Many2Many = "ManyToMany";
	public static final String One2Many = "OneToMany";
	public static final String PK = "PrimaryKey";

	private static final String SQLIgnore = "SQLIgnore";
	
	HashMap<Class<?>, Table> tables = new HashMap<>();


	
	
	HashMap<Class<?>, ArrayList<ArrayList<String>>> settingsFK = new HashMap<>();
	HashMap<Class<?>, ArrayList<Field>> settingsPK = new HashMap<>();

	HashMap<Class<?>, ArrayList<String[]>> columns = new HashMap<>();
	
	ArrayList<String> manyToManys = new ArrayList<>();

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
			.append(getPrimaryKeyString(settingsPK.get(c)));



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
		.append(getPrimaryKeyFromString(fk.subList(1, fk.size())))
		.append("\n");

		return sb.toString();
	}


	protected abstract void initializeModels();

	protected <T> void addClass(Class<?>... clas) {
		Table tableA;
		createTables(clas);
		Attribute att;
		String classeBName;
		Class<?> classeB;
		Table tableB;
		Annotation[] annotations;
		
		for(Class<?> c : clas){
			tableA = tables.get(c);			
			
			annotations = null;

			for(Field f : c.getDeclaredFields()){
				annotations = f.getDeclaredAnnotations();
				if(annotations.length > 0){
					for(Annotation a : annotations){
						switch (a.annotationType().getSimpleName()) {
						case One2Many:
							classeBName = ((OneToMany)a).classe();
							classeB = getClassBySimpleName(classeBName);
							tableB = tables.get(classeB);
							addFK(tableB, tableA);
							break;
						case PK:
							//addPK(table, c, f);
							System.out.println("primaryKey ---");
							break;							
						case Many2Many:
							classeBName = ((OneToMany)a).classe();
							classeB = getClassBySimpleName(classeBName);
							tableB = tables.get(classeB);
							addTableManyToMany(tableA, tableB);
							
							break;
						case SQLIgnore:
							break;
						default:
							break;
						}
					}
				}else{
					//addColumn(c, f, null);
					att = new Attribute(f.getName(), convertType(f)); 
					tableA.addColumn(att);
					
				}
			}
		}
	}

	private void createTables(Class<?>[] classes) {
		Table table;
		Attribute att;
		for(Class<?> c : classes){
			this.classes.add(c);
			table = new Table(c.getSimpleName());
			for(Field f: getPrimaryKeys(c)){
				table.addPrimaryKey(new Attribute(f.getName(), convertType(f)));
			}
			tables.put(c, table);
		}
	}


	private void addTableManyToMany(Table tableA, Table tableB) {
		String tableName = tableA.getName()+"_"+tableB.getName();
		Table table = new Table(tableName);	
		table.addForeignKeyPrimaryKey(tableA.getForeignKeySettings());
		table.addForeignKeyPrimaryKey(tableB.getForeignKeySettings());	
		//tables.put(key, value)
	}


//	private void addFK(Table tableA, Class<?> classA, Field f, Annotation a) {
///*		Class<?> classe = getClassBySimpleName(((OneToMany)a).classe());
//
//		ArrayList<Field> pks = getPrimaryKeys(c);
//
//		for(Field fd : pks){
//			addColumn(classe, fd, c);
//		}
//		ArrayList<ArrayList<String>> fks = settingsFK.get(classe);
//		if(fks == null){
//			fks = new ArrayList<>();
//		}
//		fks.add(getForeignKey(c));
//		settingsFK.put(classe, fks);*/
//			
//
//	}

	private void addFK(Table tableB, Table tableA) {
		ForeignKey fk = new ForeignKey(tableA.getName(), tableA.getPrimarykeys());
		tableB.addForeignKey(fk);
	}




	private void addPK(Table table, Class<?> c, Field f) {
//		ArrayList<Field> pks = settingsPK.get(c);
//		if(pks == null){
//			pks = new ArrayList<>();
//		}
//
//		pks.add(f);
//		settingsPK.put(c, pks);
//		addColumn(c, f, null);
		String attName = f.getName();
		String attType = convertType(f);
		Attribute att = new Attribute(attName, attType);
		table.addPrimaryKey(att);
		
	}



	private void addColumn(Class<?> c1, Field f, Class<?> c2){
		ArrayList<String[]> cols = columns.get(c1);

		if(cols == null){
			cols = new ArrayList<>();
		}

		String columnName;
		if(c2 == null){
			columnName = f.getName();
		}else {
			columnName = c2.getSimpleName().toLowerCase()+"_"+f.getName();
		}
		cols.add(new String[]{columnName, convertType(f)});
		columns.put(c1, cols);

	}


	private Class<?> getClassBySimpleName(String simpleName){
		Class<?> classe= null;
		for(Class<?> c : classes){
			if(c.getSimpleName().equals(simpleName)){
				classe  = c;
				break;
			}
		}
		return classe;
	}

	private ArrayList<String> getForeignKey(Class<?> c) {

		ArrayList<String> fks = new ArrayList<String>();
		fks.add(c.getSimpleName());
		for(Field f : getPrimaryKeys(c)){
			fks.add(f.getName());
		}
		return fks;
	}


	private ArrayList<ArrayList<String>> getForeignKeys(Class<?> c) {
		ArrayList<ArrayList<String>> fks = settingsFK.get(c);
		if(fks == null){
			fks = new ArrayList<>();
		}	
		return fks;
	}

	private StringBuilder getPrimaryKeyString(List<Field> pks){
		
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for(int i = 0; i<pks.size(); i++){
			sb.append(((Field)pks.get(i)).getName());
			if(i < pks.size()-1){
				sb.append(", ");
			}

		}
		sb.append(")");
		return sb;
	}

	private StringBuilder getPrimaryKeyFromString(List<String> pks){
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

	private ArrayList<Field> getPrimaryKeys(Class<?> c) {
		ArrayList<Field> pks = new ArrayList<>();

		for(Field f : c.getDeclaredFields()){
			Annotation[] an = f.getDeclaredAnnotations();

			for(Annotation a : an){
				if(a.annotationType().getSimpleName().equals("PrimaryKey")){
					pks.add(f);
				}
			}
		}



		return pks;
	}

	public ArrayList<String[]> getColumns(Class<?> c){
		ArrayList<String[]> cols = columns.get(c);
		if(cols == null){
			cols = new ArrayList<>();
/*
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
			columns.put(c, cols);*/
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

