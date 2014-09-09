package table;

import java.util.ArrayList;

public class ForeignKey {
	
	String classeName;
	ArrayList<Attribute> primarykeys;
	
	public ForeignKey(String classeName, ArrayList<Attribute> pks){
		this.classeName = classeName;
		primarykeys = pks;
		
	}

	public String getClasseName() {
		return classeName;
	}

	public void setClasseName(String classeName) {
		this.classeName = classeName;
	}

	public ArrayList<Attribute> getPrimarykeys() {
		return primarykeys;
	}

	public void setPrimarykeys(ArrayList<Attribute> primarykeys) {
		this.primarykeys = primarykeys;
	}
	

}
