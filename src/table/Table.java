package table;

import java.util.ArrayList;

public class Table {
	
	String name;
	ArrayList<Attribute> columns;
	ArrayList<ForeignKey> foreignKeys;
		
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

	public ArrayList<Attribute> getColumns() {
		return columns;
	}

	public void addColumn(Attribute att){		
		columns.add(att);
	}
	
	public void addForeignKey(ForeignKey fk){
		foreignKeys.add(fk);
	}

	public ArrayList<ForeignKey> getForeignKeys() {
		return foreignKeys;
	}	
}
