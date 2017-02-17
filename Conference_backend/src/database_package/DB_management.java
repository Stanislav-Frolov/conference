package database_package;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import Model_package.Conference_model;
import Model_package.Conference_person_relation;
import Model_package.Conference_room_model;
import Model_package.Person_model;

public class DB_management {
	
	public void DB_management(){
		
	}
	
	private ConnectionSource connectionSource = null ;
	private Dao<Conference_model, Integer > Conference_table = null;
    private Dao<Conference_room_model, Integer > Conference_room_table = null;
    private Dao<Person_model, Integer > Person_table = null;
    private Dao<Conference_person_relation, Integer > Conference_person_relation_table = null;
	
	
	
	public void connect_to_database(){
		
	/*	Connection db_connection = null;
	    try {
	      Class.forName("conference.JDBC");
	      db_connection = DriverManager.getConnection("jdbc:sqlite:conference_database.db");
	      return db_connection;
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		return null;*/
		
		String databaseUrl = "jdbc:sqlite:conference_db" ;
		
				try {
					connectionSource = new JdbcConnectionSource(databaseUrl);
					System.out.println(" connect to database ");
				} catch (SQLException e) {
					System.out.println(" database connection error " + e);
				}
	}
	
	 
	public void create_all_DAO (){
		
		// used to query database tables 
        try {
			Conference_table = DaoManager.createDao(connectionSource, Conference_model.class);
			Conference_room_table = DaoManager.createDao(connectionSource, Conference_room_model.class);
			Person_table = DaoManager.createDao(connectionSource, Person_model.class);
			Conference_person_relation_table = DaoManager.createDao(connectionSource, Conference_person_relation.class);
		} catch (SQLException e) {
			System.out.println( "create_all_DAO error "+e );
		}
        // create tables in database
        try {
			TableUtils.createTable(connectionSource, Conference_model.class);
			TableUtils.createTable(connectionSource, Conference_room_model.class);
			TableUtils.createTable(connectionSource, Person_model.class);
			TableUtils.createTable(connectionSource, Conference_person_relation.class);
		} catch (SQLException e) {
			System.out.println( "create_all_DAO error "+e );
		}
		
	}
	
	public Dao<Conference_model, Integer > get_conference_table(){
		return Conference_table;
	}
	public Dao<Conference_room_model, Integer > get_conference_room_table(){
		return Conference_room_table ;
	}
	public Dao<Person_model, Integer > get_person_table(){
		return Person_table;
	}
	public Dao<Conference_person_relation, Integer >  get_conference_person_relation_table(){
		return Conference_person_relation_table;
	}
	
	public int create_conference( String name , String date , String time){
		Conference_model conference = new Conference_model();
		conference.set_name( name );
		conference.set_date( date );
		conference.set_time( time );
		try {
			Conference_table.create(conference);
			System.out.println(" conference created ");
			System.out.println( conference );
			return conference.get_id() ;
		} catch (SQLException e) {
			System.out.println(" conference creation error "+ e);
		}
		
		return -1 ;
	}
	
	public Conference_model get_conference( int primary_key ){
		
	//	conference_model conference = Conference_table.
		QueryBuilder< Conference_model , Integer> conference_builder 
		= Conference_table.queryBuilder();
		try {
			conference_builder.where().eq("id", primary_key );
			
			PreparedQuery< Conference_model > preparedQuery = conference_builder.prepare();
			List < Conference_model > conference = Conference_table.query(preparedQuery);
			return conference.get( 0 );
		} catch (SQLException e) {
			System.out.println(" get_conference error  ");
		}
		return null;
	}
	
	
	public void delete_conference( int primary_key){
		
		try {
			Conference_table.deleteById( primary_key );
			
			QueryBuilder< Conference_person_relation, Integer> conference_person_relation_table_query_builder 
				= Conference_person_relation_table.queryBuilder();
			
			Where<Conference_person_relation, Integer> conference_person_relation_where 
				= conference_person_relation_table_query_builder.where();
			conference_person_relation_where.eq("conference_id", primary_key );
			
			List< Conference_person_relation > Conference_person_relation = 
					conference_person_relation_table_query_builder.query();
			for( Conference_person_relation relation : Conference_person_relation ){
				
				DeleteBuilder< Person_model , Integer> person_delete_builder 
				= Person_table.deleteBuilder();
				person_delete_builder.where().eq("id", relation.get_person_id() );
				person_delete_builder.delete();
			}
			
			DeleteBuilder< Conference_person_relation, Integer> deleteBuilder 
				= Conference_person_relation_table.deleteBuilder();
			deleteBuilder.where().eq("conference_id", primary_key );
			deleteBuilder.delete();
			
			System.out.println(" conference deleted " );
		} catch (SQLException e) {
			System.out.println(" conference delete error "+e);
		}
	}
	
	
	public int add_person_to_conference(int conference_id , String person_name , String person_birth_date ){
		
		Person_model person = new Person_model();
		person.set_name( person_name );
		person.set_birth_date( person_birth_date );
		
		try {
			Person_table.create(person);
			System.out.println(" person created ");
			System.out.println( person );
		} catch (SQLException e) {
			System.out.println(" person create error "+e);
		}
		Conference_person_relation conference_person = new Conference_person_relation();
		Conference_model conference = null ;
		try {
			conference = Conference_table.queryForId( conference_id );
			conference.add_participant();
			Conference_table.update(conference) ;
			conference_person.set_conference( conference  );
			conference_person.set_person( person );
			Conference_person_relation_table.create( conference_person ) ;
			System.out.println(" conference person relation created " );
			System.out.println( conference_person );
			return person.get_id() ;
		} catch (SQLException e) {
			System.out.println(" conference person relation create error "+e);
		}
		return -1;
		
	}
	
	public void delete_person( int person_primary_key , int conference_primary_key ){
		
		try {
			Person_table.deleteById( person_primary_key );
			
			DeleteBuilder< Conference_person_relation , Integer> person_delete_builder 
		= Conference_person_relation_table.deleteBuilder();
		person_delete_builder.where().eq("person_id", person_primary_key  );
		person_delete_builder.delete();
			
		Conference_model conference = Conference_table.queryForId( conference_primary_key );
		conference.remove_participant();
		Conference_table.update( conference ) ;
		
		
		} catch (SQLException e) {
			System.out.println(" delete_person error "+e);
		}
		


		
	}
	
	public List< Conference_model > query_all_conferences(){
		
		QueryBuilder< Conference_model, Integer> conference_query_builder 
		= Conference_table.queryBuilder();
		
		List< Conference_model > conferences = null;
		try {
			 conferences = conference_query_builder.query() ;
		} catch (SQLException e) {
			System.out.println(" conference query all error "+e);
		}
		for( Conference_model conf : conferences ){
			System.out.println( conf );
		}
		return conferences;
		
	}
	
	public int create_conference_room( String conference_room_name, String  conference_room_location, 
			String conference_room_max_seats ){
		Conference_room_model conference_room = new Conference_room_model();
		conference_room.set_name(conference_room_name);
		conference_room.set_location( conference_room_location );
		conference_room.set_max_seats( Integer.parseInt( conference_room_max_seats ) );
		
		try {
			Conference_room_table.create(conference_room);
			System.out.println(" conference room created ");
			System.out.println( conference_room );
			return conference_room.get_id() ;
		} catch (SQLException e) {
			System.out.println(" conference creation error "+ e);
		}
		
		return -1 ;
	}
	
	public List< Conference_room_model > get_conference_room_list ( int conference_primary_key ){
		
		try {
			Conference_model conference = Conference_table.queryForId( conference_primary_key ) ;
			int conference_participants = conference.get_participants_number() ;
			
			QueryBuilder< Conference_room_model , Integer> conference_room_builder 
			= Conference_room_table.queryBuilder();
		
			Where<Conference_room_model, Integer> conference_room_where 
				= conference_room_builder.where();
			conference_room_where.ge( "max_seats", conference_participants  );
	
			List< Conference_room_model > Conference_room_list = conference_room_builder.query();
				
			return Conference_room_list ;
			
		} catch (SQLException e) {
			System.out.println(" get_conference_room_list error "+ e);
		}
		
		
		return null;
	}
	
	
	
	public List< Person_model >  query_person_list( String conference_primary_key ){
		
		try {
			
		List< Person_model > person_list = new ArrayList< Person_model >();
		
		QueryBuilder< Conference_person_relation, Integer> conference_person_relation_table_query_builder 
		= Conference_person_relation_table.queryBuilder();
	
		Where<Conference_person_relation, Integer> conference_person_relation_where 
			= conference_person_relation_table_query_builder.where();
		
			conference_person_relation_where.eq("conference_id", conference_primary_key );
			
		List< Conference_person_relation > Conference_person_relation = 
				conference_person_relation_table_query_builder.query();
		
		for( Conference_person_relation relation : Conference_person_relation ){
			
			Person_model person = Person_table.queryForId( relation.get_person_id() );
			person_list.add( person ) ;
			
		}
		
		return person_list;
		
		} catch (SQLException e) {
			
		}
		
		return null;
	}
	
	
	
	public void close_database_connection (){
		
		try {
			connectionSource.close();
			System.out.println(" databse connection closed");
		} catch (IOException e) {
			System.out.println( " close db connection error "+ e);	
		}
		
	}
	
	

}
