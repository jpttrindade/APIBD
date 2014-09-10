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
		primarykeys = new ArrayList<>();
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
		Attribute nAtt;
		for(Attribute att : fk.getPrimarykeys()){
			nAtt = new Attribute(fk.getClasseName().toLowerCase()+"_"+att.getName(), att.getType());
			addColumn(nAtt);
		}
		
	}

	public ArrayList<ForeignKey> getForeignKeys() {
		return foreignKeys;
	}	
	
	public void addForeignKeyPrimaryKey(ForeignKey fk){
		addForeignKey(fk);
		Attribute nAtt;
		for(Attribute at : fk.primarykeys){
			nAtt = new Attribute(fk.classeName.toLowerCase()+"_"+at.getName(), at.getType());
			primarykeys.add(nAtt);
		}
		
	}

	public ForeignKey getForeignKeySettings() {
		return new ForeignKey(this.name, this.primarykeys);
	}
}
