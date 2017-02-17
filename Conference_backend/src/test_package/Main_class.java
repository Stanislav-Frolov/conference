package test_package;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import Model_package.Conference_model;
import Model_package.Conference_person_relation;
import Model_package.Conference_room_model;
import Model_package.Person_model;
import database_package.DB_management;

public class Main_class {
	
	
	
	public static void main( String args[] ) {
		
		DB_management db_connection_class = new DB_management();
		
		db_connection_class.connect_to_database();
		
		db_connection_class.create_all_DAO();
		
		
		
		
		db_connection_class.close_database_connection();
		
	}
	
	
	public void temporary(){
		
		
		

		
	}

}
