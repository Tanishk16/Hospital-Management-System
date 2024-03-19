package HospitalManagementSystem;

import javax.print.Doc;
import javax.script.ScriptContext;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Tanishkprasad@2004";

    public static void main(String[] args) {
        try{

            // To load our database in the java Class
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add patient: ");
                System.out.println("2. View patient: ");
                System.out.println("3. View Doctors: ");
                System.out.println("4. Book appointment: ");
                System.out.println("5. Exit");
                System.out.println("Enter your choice :");
                int choice  = scanner.nextInt();

                switch (choice){
                    case 1:
                        //Add patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //View patient
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        //View doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        // Book appointment
                        bookAppointment(patient,doctor,connection,scanner);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("THANK YOU FOR USING HOSPITAL MANAGEMENT SYSTEM !");
                        return;

                    default:
                        System.out.println("Enter valid choice !!");
                        break;
                }


            }


        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){

        System.out.println("Enter the patientId :");
        int patientId = scanner.nextInt();
        System.out.println("Enter the DoctorsId :");
        int doctorId = scanner.nextInt();
        System.out.println("Enter your Data(YYYY-MM-DD): ");
        String appointmentDate = scanner.next();

        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailibility(doctorId,appointmentDate,connection)){
                String appointmentQuery = "INSERT INTO appointments (patient_id, doctor_id, appointment_date) VALUES (?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);

                    // This line will show us that any row in the database is affected or not !
                    int rowAffected = preparedStatement.executeUpdate();

                    if(rowAffected>0){
                        System.out.println("Appointment booked!");
                    }
                    else{
                        System.out.println("Doctors is busy at this time !");
                    }

                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Doctors not available on this date !");
            }
        }
        else{
            System.out.println("Either doctor or patient doesn't exist !!! ");
        }

    }
    public static boolean checkDoctorAvailibility(int doctorid, String appointmentDate, Connection connection){

        // (*) This was written because we only want to extract number of row from the database
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorid);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count==0){               // IF no row is entered , means there are no doctor appointed
                    return true;
                }
                else {
                    return false;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
