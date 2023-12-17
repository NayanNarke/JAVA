package Hospital_Management_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
private Connection  con;
	
	

	/**
	 * @param con
	 * @param sc
	 */
	public Doctor(Connection con) {
		this.con = con;
		
	}

	
	public void viewDoctors() {
		String query ="select * from doctors";
		try {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("Patient : ");    
			System.out.println("+------------+------------------+--------------------+");
			System.out.println("| Doctor ID  | Name             | Specialization     |");
			System.out.println("+------------+------------------+--------------------+");
			
			while(resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
//				int age = resultSet.getInt("age");
				String specialization = resultSet.getString("specialization");
				
				System.out.printf("|%-12s|%-20s|%-20s|\n", id,name ,specialization);
				System.out.println("+------------+------------------+--------------------+");
			}
		
		}catch(SQLException e) {
			e.printStackTrace();	
		}
			
		}

		public boolean getDoctorById(int id) {
			String query  = "SELECT * FROM doctors WHERE id =?";
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
