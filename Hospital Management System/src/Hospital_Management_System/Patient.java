package Hospital_Management_System;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.xdevapi.Result;

public class Patient {
	private Connection  con;
	
	private Scanner  sc;

	/**
	 * @param con
	 * @param sc
	 */
	public Patient(Connection con, Scanner sc) {
		this.con = con;
		this.sc = sc;
	}
	
	public void addPatient() {
		System.out.println("Enter Patient Name : ");
		String Pname = sc.next();
		
		System.out.println("Enter Patient Age : ");
		int Page = sc.nextInt();
		sc.nextLine();
		
		System.out.println("Enetr Patient Gender : ");
		String Pgender= sc.nextLine();
		
		try {
			
			String query = "INSERT INTO patients(name ,age ,gender ) VALUES(?, ?, ?)";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, Pname);
			preparedStatement.setInt(2, Page);
			preparedStatement.setString(3, Pgender);
			
			int affectedRows = preparedStatement.executeUpdate();
			
			if (affectedRows>0) {
				System.out.println("Patient Added Successfully....");
			}else {
				System.out.println("Failed to add Patient!");
			}
			

			
		}catch (SQLException e ) {
			e.printStackTrace();
		}
	} 
	
	public void viewPatients() {
		String query ="select * from patients";
		try {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("Patients : ");
			System.out.println("+------------+------------------+--------------+---------------+");
			System.out.printf("|%-13s|%-19s|%-14s|%-15s|\n", "Patient ID", "Name", "Age", "Gender");
			System.out.println("+------------+------------------+--------------+---------------+");

			while (resultSet.next()) {
			    int id = resultSet.getInt("id");
			    String name = resultSet.getString("name");
			    int age = resultSet.getInt("age");
			    String gender = resultSet.getString("gender");

			    System.out.printf("|%-13s|%-19s|%-14s|%-15s|\n", id, name, age, gender);
			    System.out.println("+------------+------------------+--------------+---------------+");
			}


		
		}catch(SQLException e) {
			e.printStackTrace();	
		}
			
		}

		public boolean getPatientById(int id) {
			String query  = "SELECT * FROM patients WHERE id =?";
			try {
				PreparedStatement preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, id);
				ResultSet resultSet = preparedStatement.executeQuery();
				if(resultSet.next()) {
					return true ;
				}else {
					return false;
				}

			}catch(SQLException e) {
				e.printStackTrace();
				
			}
			return false; 
		
	}
	
	
}


