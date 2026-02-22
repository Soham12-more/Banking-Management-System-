import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection con;
    private  Scanner scanner;
    public User(Connection con, Scanner scanner) {
        this.scanner=scanner;
        this.con=con;
    }
    public void register(){
        scanner.nextLine();
        System.out.println("Full Name: ");
        String name=scanner.nextLine();
        System.out.println("Email: ");
        String email=scanner.nextLine();
        System.out.println("Password: ");
        String password=scanner.nextLine();
        if(user_exits(email)){
            System.out.println("User already exists for this Email Addresss!!");
            return;
        }
        String register_query="INSERT INTO user(full_name,email,password) VALUES(?,?,?)";
        try{
            PreparedStatement pmp=con.prepareStatement(register_query);
            pmp.setString(1,name);
            pmp.setString(2,email);
            pmp.setString(3,password);
            int affectedrows= pmp.executeUpdate();
            if(affectedrows>0){
                System.out.println("registered Successfully!!");
            }else {
                System.out.println("Registeration Failed!");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public String Login(){
        scanner.nextLine();
        System.out.println("Email: ");
        String email=scanner.nextLine();
        System.out.println("Password: ");
        String password=scanner.nextLine();
        String login_query="SELECT * from user WHERE email=? AND password=? ";
        try{
            PreparedStatement pmp=con.prepareStatement(login_query);
            pmp.setString(1,email);
            pmp.setString(2,password);
            ResultSet rst=pmp.executeQuery();
            if(rst.next()){
                return email;
            }else{
                return null;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }


        return null;
    }
    public boolean user_exits(String email){
        String query="SELECT * from user WHERE email=?";
        try{
            PreparedStatement pmp=con.prepareStatement(query);
            pmp.setString(1,email);
            ResultSet rst=pmp.executeQuery();
            if(rst.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }



}
