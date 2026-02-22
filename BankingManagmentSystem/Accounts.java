import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection con;
    private  Scanner scanner;
    public Accounts(Connection con, Scanner scanner) {
        this.scanner=scanner;
        this.con=con;
    }

    public long open_account(String email){
        if(!account_exits(email)){
            String open_account_query="INSERT INTO accounts(account_number,full_name,email,balance,security_pin) VALUES(?,?,?,?,?)";
            scanner.nextLine();
            System.out.println("Full Name: ");
            String full_name=scanner.nextLine();
            System.out.println("Enter Initial_amount: ");
            double balance=scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter Security_pin: ");
            String security_pan=scanner.nextLine();
            try{
                long account_number=generateAccountNumber();
                PreparedStatement pmp=con.prepareStatement(open_account_query);
                pmp.setLong(1,account_number);
                pmp.setString(2,full_name);
                pmp.setString(3,email );
                pmp.setDouble(4,balance);
                pmp.setString(5,security_pan);
                int affectedrows= pmp.executeUpdate();
                if(affectedrows>0){
                    return account_number;
                }else {
                    System.out.println("Account creation Failed!");
                }

        }catch (SQLException e){
                e.printStackTrace();
            }

        }

        throw new RuntimeException("Account Already Exists!!");
    }
    public long getAccount_number(String email){
        String query="SELECT account_number from accounts WHERE email=?";
        try{
            PreparedStatement pmp = con.prepareStatement(query);
            pmp.setString(1,email);
            ResultSet rst=pmp.executeQuery();
            if(rst.next()){
                return rst.getLong("account_number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account number doesn't exist!!");
    }
    public long generateAccountNumber(){
        try{
            Statement stmt=con.createStatement();
            ResultSet rst=stmt.executeQuery("SELECT account_number from accounts order by account_number DESC LIMIT 1");
            if(rst.next()){
                long last_account_number=rst.getLong("account_number");
                return  last_account_number+1;
            }else{
                return 10000100;
            }
    }catch(SQLException e){
            e.printStackTrace();
        }
        return 1000100;
    }
    public boolean account_exits(String email){
        String query="SELECT account_number from accounts WHERE email=?";
        try {
            long account_number = generateAccountNumber();
            PreparedStatement pmp = con.prepareStatement(query);
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
