package Model_package;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "person")
public class Person_model {
	
	
	@DatabaseField(generatedId  = true)
	private int id  ;
	@DatabaseField
    private String name;
	@DatabaseField
	private String birth_date;
	
	public Person_model(){
		
	}
	
	public int get_id(){
		return id;
	}
	
	public String get_name(){
		return name;
	}
	public void set_name( String person_name){
		name = person_name;
	}
	public String get_birth_Date(){
		return birth_date;
	}
	public void set_birth_date( String date ){
		birth_date = date;
	}
	
	public String toString(){
		return id +" "+name+" "+birth_date;
	}

}
