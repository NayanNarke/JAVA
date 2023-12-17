package Hospital_Management_System;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.mysql.cj.protocol.Resultset;

public class HospitalManagementSystem {
	private static final String url = "jdbc:mysql://localhost:3306/hospital";
	private static final String username = "root";
	private static final String password ="Nayan@027"; 
	
	
	
	public static void main(String[] args) {
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }

	    try (Scanner sc = new Scanner(System.in);
	         Connection con = DriverManager.getConnection(url, username, password)) {

	        Patient patient = new Patient(con, sc);
	        Doctor doctor = new Doctor(con);

	        while (true) {
	            System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
	            System.out.println("1. Add Patient ");
	            System.out.println("2. View Patient ");
	            System.out.println("3. View Doctor ");
	            System.out.println("4. Book Appointment ");
	            System.out.println("5. View Appointments ");
	            System.out.println("6. Exit \n");
	            System.out.println("Enter Your Choice: ");

	            int choice = sc.nextInt();
	            sc.nextLine(); // consume the newline character

	            switch (choice) {
	                case 1:
	                    // add patient
	                    patient.addPatient();
	                    System.out.println();
	                    break;
	                case 2:
	                    // view patient
	                    patient.viewPatients();
	                    System.out.println();
	                    break;
	                case 3:
	                    // view Doctor
	                    doctor.viewDoctors();
	                    System.out.println();
	                    break;
	                case 4:
	                    // Book Appointment
	                    bookAppointment(patient, doctor, con, sc);
	                    break;
	                case 5:
	                    // View Appointments
	                    viewAppointments(con);
	                    System.out.println();
	                    break;

	                case 6:
	                    System.out.println("Exiting...");
	                    return;
	                default:
	                    System.out.println("Enter Valid Choice !!!");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	public static void viewAppointments(Connection con) {
	    String query = "SELECT * FROM appointments";
	    try (PreparedStatement preparedStatement = con.prepareStatement(query);
	         ResultSet resultSet = preparedStatement.executeQuery()) {

	        System.out.println("Appointments : ");
	        System.out.println("+------------+------------+------------------+------------------------+");
	        System.out.printf("|%-12s|%-12s|%-18s|%-24s|\n", "Appointment ID", "Patient ID", "Doctor ID", "Appointment Date");
	        System.out.println("+------------+------------+------------------+------------------------+");

	        while (resultSet.next()) {
	            int appointmentId = resultSet.getInt("id");
	            int patientId = resultSet.getInt("patient_ID");
	            int doctorId = resultSet.getInt("doctor_ID");
	            String appointmentDate = resultSet.getString("appointment_date");

	            System.out.printf("|%-12s|%-12s|%-18s|%-24s|\n", appointmentId, patientId, doctorId, appointmentDate);
	            System.out.println("+------------+------------+------------------+------------------------+");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}



	
	public static void bookAppointment(Patient patient, Doctor doctor, Connection con , Scanner sc ) {
		System.out.println("Enter Patient ID : ");
		int patientID = sc.nextInt();
		
		System.out.println("Enter Doctor ID : ");
		int doctorID = sc.nextInt();
		
		System.out.println("Enter Appointment Date (YYYY-MM-DD)");
		String appoDate =sc.next();
		
		if (patient.getPatientById(patientID) && doctor.getDoctorById(doctorID)) {
			if(checkDoctorAvailability(doctorID , appoDate, con
	)) {
				String appointmentQuery = "INSERT INTO appointments(patient_ID, doctor_ID, appointment_date) VALUES (?, ?, ?)";
				try {
				    PreparedStatement preparedStatement = con.prepareStatement(appointmentQuery);
				    preparedStatement.setInt(1, patientID);
				    preparedStatement.setInt(2, doctorID);
				    preparedStatement.setString(3, appoDate);

				    int rowsAffected = preparedStatement.executeUpdate();
				    if (rowsAffected > 0) {
				        System.out.println("Appointment Booked \n");
				    } else {
				        System.out.println("Failed to Book Appointment! \n");
				    }
				} catch (SQLException e) {
				    e.printStackTrace();
				}

				
			}else {
				System.out.println("Doctor not available on this date!!");
			}
			
			String appointmentQuery = "INSERT INTO appointments(patient_id , doctor_id, appoinment_data)";
		}else {
			System.out.println("Either doctor or patient doesn't exist!!! ");
		}
	}
	
	public static boolean checkDoctorAvailability(int doctorID ,  String appoDate, Connection con ) {
		String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ? ";
		try {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, doctorID);
			preparedStatement.setString(2, appoDate);
			
			ResultSet resultset = preparedStatement.executeQuery();
			
			if (resultset.next()) {
				int count = resultset.getInt(1);
				if (count == 0) {
					return true ;
				}else {
					return false;
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
			
		}
		return false;
		
	}
}
