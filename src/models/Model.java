package models;

public interface Model<T> {
	
	public boolean save();
	public boolean delete();
	public boolean hasChange();
	//public PrimaryKey<?>[] getPrimaryKies();
}
