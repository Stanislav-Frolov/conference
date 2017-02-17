package Model_package;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "conference_person_relation")
public class Conference_person_relation {
	
	
	public Conference_person_relation(){
		
	}
	
	
	@DatabaseField(generatedId  = true)
	private int id  ;
	@DatabaseField(foreign = true)
    private Conference_model conference;
	@DatabaseField(foreign = true)
	private Person_model person;
	
	public int get_conference_id(){
		return conference.get_id() ;
	}
	public int get_person_id(){
		return person.get_id();
	}
	public void set_conference( Conference_model conf ){
		conference = conf;
	}
	public void set_person( Person_model pers){
		person = pers;
	}
	
	public String toString(){
		return id+" "+get_conference_id()+" "+get_person_id();
	}
	

}
