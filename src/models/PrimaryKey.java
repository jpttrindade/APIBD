package models;

public class PrimaryKey<T> {
	
	T pk;
	
	
	public PrimaryKey(T pk){
		this.pk = pk;
	}


	public T getPk() {
		return pk;
	}

}
