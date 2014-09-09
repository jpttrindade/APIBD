package table;

import java.util.ArrayList;

public class Table {
	
	String name;
	ArrayList<Attribute> columns;
	ArrayList<ForeignKey> foreignKeys;
	ArrayList<Attribute> primarykeys;
		

	public Table(String name){
		this.name = name;
		columns = new ArrayList<>();
		foreignKeys = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Attribute> getPrimarykeys() {
		return primarykeys;
	}

	public void addPrimaryKey(Attribute pk){
		primarykeys.add(pk);
		addColumn(pk);
	}

	public ArrayList<Attribute> getColumns() {
		return columns;
	}

	public void addColumn(Attribute att){		
		columns.add(att);
	}
	
	public void addForeignKey(ForeignKey fk){
		foreignKeys.add(fk);
		String attName;
		for(Attribute att : fk.getPrimarykeys()){
			attName = fk.getClasseName()+"_"+att.getName();
			att.setName(attName);
			addColumn(att);
		}
		
	}

	public ArrayList<ForeignKey> getForeignKeys() {
		return foreignKeys;
	}	
	
	public void addForeignKeyPrimaryKey(ForeignKey fk){
		addForeignKey(fk);
		String attName;
		for(Attribute at : fk.primarykeys){
			attName = fk.classeName+"_"+at.getName();
			at.setName(attName);
			primarykeys.add(at);
		}
		
	}

	public ForeignKey getForeignKeySettings() {
		return new ForeignKey(this.name, this.primarykeys);
	}
}
