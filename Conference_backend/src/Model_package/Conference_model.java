package Model_package;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "conference")
public class Conference_model {
	
	@DatabaseField(generatedId  = true)
	private int id  ;
	@DatabaseField
    private String name;
	@DatabaseField(defaultValue = "0")
	private int participants_number;
	@DatabaseField
	private String date;
	@DatabaseField
	private String time;
	
	public Conference_model(){
		
	}
	public int get_id(){
		return id;
	}
	
	public String get_name(){
		return name;
	}
	public void set_name( String conf_name){
		name = conf_name;
	}
	public int get_participants_number(){
		return participants_number;
	}
	public void add_participant(){
		participants_number++;
	}
	public void remove_participant(){
		participants_number--;
	}
	public String get_date(){
		return date;
	}
	public void set_date( String conf_date){
		date = conf_date;
	}
	public String get_time(){
		return time;
	}
	public void set_time( String conf_time){
		time = conf_time;
	}
	
	public String toString(){
		return id + " " + name + " "+participants_number+" "+date+" "+time;
	}

}
