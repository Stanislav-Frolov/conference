package servlet_package;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.sun.org.apache.xpath.internal.functions.Function;

import database_package.DB_management;

import org.json.JSONException;

import functions.Functions;


@WebServlet("/Main_servlet")
public class Main_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Functions function_class = null ; 
	DB_management db_connection_class;
	
    public Main_servlet() {
        super();
        
        db_connection_class = new DB_management();
		
		db_connection_class.connect_to_database();
		
		db_connection_class.create_all_DAO();
       
    }

    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	//	this is wrong
		response.setHeader("Access-Control-Allow-Origin", "*");
		    System.out.println("work ");
			response.getWriter().write("i hear ya");
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 function_class = new Functions() ;
		 
		response.setHeader("Access-Control-Allow-Origin", "*");
		  System.out.println( "hello" );
		  
		  String JSON_string = null;
		    StringBuilder stringBuilder = new StringBuilder();
		    BufferedReader bufferedReader = null;

		    
		        InputStream inputStream = request.getInputStream();
		        if (inputStream != null) {
		            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		            char[] charBuffer = new char[128];
		            int bytesRead = -1;
		            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
		                stringBuilder.append(charBuffer, 0, bytesRead);
		            }
		        } else {
		            stringBuilder.append("");
		        }
		        JSON_string = stringBuilder.toString();
		        System.out.println( JSON_string );
		  
		  
		  
		  JSONObject json = function_class.string_to_json( JSON_string );
		  
		
		  String command = function_class.json_attribute_toString( "command" , json ).toString() ;
		  System.out.println( command );
		  
		  
		  response.setContentType("application/json");
		  PrintWriter out = response.getWriter();
		  
		  if( command.equals("create_conference") ){
			  System.out.println( "create_conference" );
			  int conference_primary_key = function_class.create_conference( db_connection_class , json ) ;
			  out.println("{\"result\":\"success\",\"conference_primary_key\":\""+conference_primary_key+"\"}");
			  out.flush();
		  } else if( command.equals("get_all_conferences") ){
			  System.out.println( "get_all_conferences" );
			  JSONObject conferences_json = function_class.get_all_conferences( db_connection_class ) ;
			  out.println("{\"result\":\"success\",\"conferences\":"+conferences_json.toString()+"}");
			  out.flush();
		  } else if( command.equals("conference_info") ){
			  System.out.println( "conference_info" );
			  JSONObject conference_json = function_class.get_conference( db_connection_class , json ) ;
			  out.println("{\"result\":\"success\",\"conference\":"+conference_json.toString()+"}");
			  out.flush();
		  } else if( command.equals("delete_conference") ){
			  System.out.println( "delete_conference" );
			  String primary_key = function_class.json_attribute_toString( "primary_key" , json ).toString() ;
			  function_class.delete_conference( db_connection_class , Integer.parseInt( primary_key) ) ;
			  out.println("{\"result\":\"success\"}");
			  out.flush();
		  } else if( command.equals("add_person_to_conference") ) {
			  System.out.println( "add_person_to_conference" );
			  JSONObject person =  function_class.add_person_to_conference( db_connection_class , json ) ;
			  out.println("{\"result\":\"success\",\"person\":"+person.toString()+"}");
			  out.flush();
			  
		  } else if( command.equals("delete_person") ){
			  
			  String person_primary_key = function_class.json_attribute_toString( "person_primary_key" , json ).toString() ;
			  String conference_primary_key = function_class.json_attribute_toString( "conference_primary_key" , json ).toString() ;
					  
			  function_class.delete_person( db_connection_class , Integer.parseInt( person_primary_key ) ,
					  Integer.parseInt(conference_primary_key) ) ;
			  out.println("{\"result\":\"success\"}");
			  out.flush();
			  
		  } else if( command.equals( "get_person_list" ) ){
			  System.out.println( "get_person_list" );
			  JSONObject person_list_json = function_class.get_person_list( db_connection_class , json ) ;
			  out.println("{\"result\":\"success\",\"person_list\":"+person_list_json.toString()+"}");
			  out.flush();
			  
		  } else if( command.equals("create_conference_room")  ){
			  System.out.println( "create_conference_room" );
			  int conference_room_primary_key = function_class.create_conference_room( db_connection_class , json );
			  out.println("{\"result\":\"success\",\"conference_room_primary_key\":\""+conference_room_primary_key+"\"}");
			  out.flush();
		  } else if( command.equals( "conference_room_list" ) ){
			  System.out.println( "conference_room_list" );
			  JSONObject conference_room_list = function_class.get_conference_room_list(
					  db_connection_class , json ) ;
			  
			  out.println("{\"result\":\"success\",\"conference_room_list\":"+conference_room_list.toString()+"}");
			  out.flush();
			  
		  }
		  
		  
		
		
		
	}

}
