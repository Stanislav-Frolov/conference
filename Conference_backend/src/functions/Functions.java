package functions;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Model_package.Conference_model;
import Model_package.Conference_room_model;
import Model_package.Person_model;
import database_package.DB_management;

public class Functions {
	
	public Functions(){
		
	}
	
	public Object json_attribute_toString( String attribute , JSONObject json ){
		
		try {
			return json.get( attribute ) ;
		} catch (JSONException e) {
			System.out.println( "json_attribute_toString error "+e);
		}
		return "";
	}
	
	public JSONObject string_to_json( String string ){
		
		try {
			return new JSONObject( string );
		} catch (JSONException e) {
			System.out.println( "string_to_json error "+e);
		}
		return null;
	}
	
	public int create_conference( DB_management db_class , JSONObject json ){
		
		String conference_name = json_attribute_toString( "conference_name" , json ).toString();
		String conference_date = json_attribute_toString( "conference_date" , json ).toString();
		String conference_time = json_attribute_toString( "conference_time" , json ).toString();
		
		return db_class.create_conference(conference_name, conference_date, conference_time) ;
		
	}
	
	public JSONObject get_all_conferences(  DB_management db_class  ){
		
		List< Conference_model > conferences =  db_class.query_all_conferences();
		JSONArray json_array = new JSONArray();
		for( Conference_model conference : conferences ){
			
			JSONObject sub_json = new JSONObject();
			
			try {
				sub_json.put("conference_primary_key", conference.get_id());
				sub_json.put("conference_name", conference.get_name());
				sub_json.put("conference_date", conference.get_date());
				sub_json.put("conference_time", conference.get_time());
				
			} catch (JSONException e) {
				System.out.println( "get_all_conferences error "+e);
			}
			
			json_array.put( sub_json);
		}
		
		JSONObject json_object = new JSONObject();
		try {
			json_object.put("all_conferences", json_array);
		} catch (JSONException e) {
			System.out.println( "get_all_conferences error "+e);
		}
		
		
		
		return json_object ;
		
	} 
	
	public JSONObject get_conference( DB_management db_class , JSONObject json  ){
		
		String primary_key = json_attribute_toString( "primary_key" , json ).toString();
		Conference_model conference = db_class.get_conference( Integer.parseInt( primary_key ) ) ;
		
		JSONObject conference_json = new JSONObject();
		
		
			try {
				conference_json.put("conference_primary_key", conference.get_id());
				conference_json.put("conference_name", conference.get_name());
				conference_json.put("conference_date", conference.get_date());
				conference_json.put("conference_time", conference.get_time());
				conference_json.put("conference_perticipant_number", conference.get_participants_number() );
			} catch (JSONException e) {
				System.out.println( "get_conference error "+e);
			}
			
			
		return conference_json ;
		
	}
	
	
	public void delete_conference( DB_management db_class , int primary_key  ){
		
		db_class.delete_conference( primary_key );
		
	}
	
	public JSONObject add_person_to_conference( DB_management db_class , JSONObject json  ){
		
		String conference_primary_key = json_attribute_toString( "conference_primary_key" , json ).toString();
		String person_name = json_attribute_toString( "person_name" , json ).toString();
		String person_birth_date = json_attribute_toString( "person_birth_date" , json ).toString();
		
		int person_primary_key = db_class.add_person_to_conference( Integer.parseInt(conference_primary_key) , person_name , person_birth_date  );
		
		JSONObject person_json = new JSONObject();
		
		try {
			person_json.put("conference_primary_key", conference_primary_key );
			person_json.put("person_primary_key", person_primary_key );
			person_json.put("person_name", person_name );
			person_json.put("person_birth_date", person_birth_date );
			
		} catch (JSONException e) {
			System.out.println( "add_person_to_conference error "+e);
		}
		
		return person_json;
	}
	
	public void delete_person ( DB_management db_class , int person_primary_key , int conference_primary_key ){
		
		db_class.delete_person( person_primary_key , conference_primary_key );
		
	}
	
	
	public JSONObject get_person_list( DB_management db_class , JSONObject json   ){
		
		String conference_primary_key = json_attribute_toString( "conference_primary_key" , json ).toString();
		
		List< Person_model > person_list =  db_class.query_person_list( conference_primary_key );
		
		JSONArray json_array = new JSONArray();
		for( Person_model person : person_list ){
			System.out.println( person );
			JSONObject sub_json = new JSONObject();
			
			try {
				sub_json.put("person_primary_key", person.get_id() );
				sub_json.put("person_name", person.get_name() );
				sub_json.put("person_birth_date", person.get_birth_Date() );
				
			} catch (JSONException e) {
				System.out.println( "get_person_list error "+e);
			}
			
			json_array.put( sub_json);
		}
		
		JSONObject json_object = new JSONObject();
		try {
			json_object.put("person_list", json_array);
			return json_object;
		} catch (JSONException e) {
			System.out.println( "get_person_list error "+e);
		}
		
		
		return null;
	}
	
	
	public int create_conference_room( DB_management db_class , JSONObject json ){
		
		String conference_room_name = json_attribute_toString( "conference_room_name" , json ).toString();
		String conference_room_location = json_attribute_toString( "conference_room_location" , json ).toString();
		String conference_room_max_seats = json_attribute_toString( "conference_room_max_seats" , json ).toString();
		
		return db_class.create_conference_room(conference_room_name, conference_room_location, conference_room_max_seats) ;
		
	}
	
	public JSONObject get_conference_room_list(  DB_management db_class , JSONObject json  ){
		
		String conference_primary_key = json_attribute_toString( "conference_primary_key" , json ).toString();
		
		List< Conference_room_model > conference_room_list = db_class.get_conference_room_list(
				Integer.parseInt(conference_primary_key) );
		
		JSONArray json_array = new JSONArray();
		for( Conference_room_model conference_room : conference_room_list ){
			System.out.println( conference_room );
			JSONObject sub_json = new JSONObject();
			
			try {
				sub_json.put("conference_room_name", conference_room.get_name() );
				sub_json.put("conference_room_location", conference_room.get_location() );
				sub_json.put("conference_room_max_seats", conference_room.get_max_seats() );
				
			} catch (JSONException e) {
				System.out.println( "get_conference_room_list error "+e);
			}
			
			json_array.put( sub_json);
		}
		
		JSONObject json_object = new JSONObject();
		try {
			json_object.put("conference_room_list", json_array);
			return json_object;
		} catch (JSONException e) {
			System.out.println( "get_conference_room_list error "+e);
		}
		
		
		return null;
		
	}
	

}
