package Model_package;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "conference_room")
public class Conference_room_model {
	
	
	@DatabaseField(generatedId  = true)
	private int id  ;
	@DatabaseField
    private String name;
	@DatabaseField
	private String location;
	@DatabaseField
	private int max_seats;
	
	public Conference_room_model(){
		
	}
	public int get_id(){
		return id;
	}
	
	public String get_name(){
		return name;
	}
	public void set_name( String room_name){
		name = room_name;
	}
	public String get_location(){
		return location;
	}
	public void set_location( String room_name){
		location = room_name;
	}
	public int get_max_seats(){
		return max_seats;
	}
	public void set_max_seats( int seats){
		max_seats = seats ;
	}
	
	public String toString(){
		return id+" "+name+" "+location+" "+max_seats;
	}

}
