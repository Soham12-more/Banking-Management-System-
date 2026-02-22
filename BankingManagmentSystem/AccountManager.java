import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection con;
    private  Scanner scanner;
    public AccountManager(Connection con, Scanner scanner) {
        this.scanner=scanner;
        this.con=con;
    }
    public void credit_money(long account_number)throws SQLException{
        scanner.nextLine();
        System.out.println("Enter amount: ");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter security pin: ");
        String pin=scanner.nextLine();

        try{
            con.setAutoCommit(false);
            if(account_number != 0){
                PreparedStatement pmp = con.prepareStatement("SELECT * from accounts WHERE account_number=? AND security_pin=?");
                pmp.setDouble(1,account_number);
                pmp.setString(2,pin);
                ResultSet rst=pmp.executeQuery();
                if(rst.next()){
                    String credit_query="UPDATE accounts SET balance= balance + ? WHERE account_number=?";
                    PreparedStatement pcp=con.prepareStatement(credit_query);
                    pcp.setDouble(1,amount);
                    pcp.setLong(2,account_number);
                    int affectedrows= pcp.executeUpdate();
                    if(affectedrows>0){
                        System.out.println("Rs"+ amount + "credited Succesfully");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
                    }else {
                        System.out.println("Transaction Failed!!");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                }else{
                    System.out.println("invalid Security pin!!");
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }
    public void debit_money(long account_number)throws SQLException{
        scanner.nextLine();
        System.out.println("Enter amount: ");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter security pin: ");
        String pin=scanner.nextLine();

        try{
            con.setAutoCommit(false);
            if(account_number != 0){
                PreparedStatement pmp = con.prepareStatement("SELECT * from accounts WHERE account_number=? AND security_pin=?");
                pmp.setDouble(1,account_number);
                pmp.setString(2,pin);
                ResultSet rst=pmp.executeQuery();
                if(rst.next()){
                    double current_balance=rst.getDouble("balance");
                    if (amount<=current_balance){
                        String debit_query="UPDATE accounts SET balance= balance - ? WHERE account_number=?";
                        PreparedStatement pdp=con.prepareStatement(debit_query);
                        pdp.setDouble(1,amount);
                        pdp.setLong(2,account_number);
                        int affectedrows= pdp.executeUpdate();
                        if(affectedrows>0){
                            System.out.println("Rs"+ amount + "debited Succesfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        }else {
                            System.out.println("Transaction Failed!!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("Insufficient balance!!");
                    }
                }else{
                    System.out.println("invalid Security pin!!");
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        con.setAutoCommit(true);

    }
    public void transfer_money(long sender_account_number)throws SQLException{
        scanner.nextLine();
        System.out.println("Enter Receiver account number: ");
        long receiver_account_number=scanner.nextLong();
        System.out.println("Enter amount: ");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter security pin: ");
        String pin=scanner.nextLine();
        try{
            con.setAutoCommit(false);
            if(sender_account_number!= 0 && receiver_account_number!=0){
                PreparedStatement pmp = con.prepareStatement("SELECT * from accounts WHERE account_number=? AND security_pin=?");
                pmp.setDouble(1,sender_account_number);
                pmp.setString(2,pin);
                ResultSet rst=pmp.executeQuery();
                if(rst.next()){
                    double current_balance= rst.getDouble("balance");
                    if(amount<=current_balance){
                        String credit_query="UPDATE accounts SET balance= balance + ? WHERE account_number=?";
                        String debit_query="UPDATE accounts SET balance= balance - ? WHERE account_number=?";
                        PreparedStatement pcp=con.prepareStatement(credit_query);
                        PreparedStatement pdp=con.prepareStatement(debit_query);
                        pcp.setDouble(1,amount);
                        pcp.setLong(2,receiver_account_number);
                        pdp.setDouble(1,amount);
                        pdp.setLong(2,sender_account_number);
                        int affectedrows1= pcp.executeUpdate();
                        int affectedrows2= pdp.executeUpdate();
                        if(affectedrows1 >0 && affectedrows2>0){
                            System.out.println("Transaction Successfull!");
                            System.out.println("Rs"+ amount + "Transfered Succesfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        }else {
                            System.out.println("Transaction Failed!!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }

                    }else{
                        System.out.println("Insufficient Balance!");
                    }
                }else{
                    System.out.println("invalid Security pin!!");
                }
            }else{
                System.out.println("Invalid account_number");
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
        con.setAutoCommit(true);

    }
    public void getBalance(long account_number){
        scanner.nextLine();
        System.out.println("Enter security pin: ");
        String pin=scanner.nextLine();
        try{
            PreparedStatement pp = con.prepareStatement("SELECT balance from accounts WHERE account_number=? AND security_pin=?");
            pp.setLong(1,account_number);
            pp.setString(2,pin);
            ResultSet rst=pp.executeQuery();
            if(rst.next()){
                double balance=rst.getDouble("balance");
                System.out.println("Balance: "+ balance);
            }else{
                System.out.println("Invalid Security pin");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
