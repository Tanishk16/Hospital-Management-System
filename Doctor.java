package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    private Connection connection;
    public Doctor(Connection connection){
        this.connection = connection;
    }


    public void viewDoctors(){
        String query = "SELECT * FROM doctors";
        try{

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Result set: It holds Any table which is coming from the database , and points the table with the help of Next pointer
            // And prints the Next method
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Doctors :");
            System.out.println("+------------+--------------------+----------------------+");
            System.out.println("| Doctors Id | Name               | Specialization       |");
            System.out.println("+------------+--------------------+----------------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                // java local variable             // SQL field name
                String specialization =  resultSet.getString("specialization");
                String name = resultSet.getString("name");
                System.out.printf("| %-10s | %-18s | %-20s |\n",id,name,specialization);
                System.out.println("+------------+--------------------+----------------------+");
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id){
        String query = "SELECT * FROM doctors WHERE id = ?";
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