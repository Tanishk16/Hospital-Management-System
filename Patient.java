package HospitalManagementSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {

    private Connection connection;

    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient(){
        System.out.print("Enter patient name: ");
        String name = scanner.next();

        System.out.print("Enter patient age: ");
        int age = scanner.nextInt();

        System.out.print("Enter patient gender: ");
        String gender = scanner.next();

        try {

            String query = "INSERT INTO patients(name,age,gender) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);

            //This will return An integer value , and shows the if any row is affected by this particular query statement
            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows > 0){
                System.out.println("Patient added Successfully !");
            }
            else{
                System.out.println("Failed to add patient !");
            }


        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void viewPatient(){
        String query = "SELECT * FROM patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Result set: It holds Any table which is coming from the database , and points the table with the help of Next pointer
            // And prints the Next method
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Patients :");
            System.out.println("+------------+--------------------+-----------+--------------+");
            System.out.println("| Patient Id | Name               | Age       | Gender       |");
            System.out.println("+------------+--------------------+-----------+--------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                // java local variable             // SQL field name

                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12s|%-20s|%-11s|%-14s|\n",id,name,age,gender);
                System.out.println("+------------+--------------------+-----------+--------------+");
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
        String query = "SELECT * FROM patients WHERE id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return true;
            }
            else {
                return false;
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }



}
