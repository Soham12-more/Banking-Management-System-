import javax.crypto.spec.PSource;
import java.sql.*;
import java.util.Scanner;

public class BankingApp {

    private static final String url= "jdbc:mysql://localhost:3306/banking_system";
    private static final String user= "root";
    private static final String password="root";



    public static void main(String[] args)throws ClassNotFoundException, SQLException {
        try {
            //load driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connected Succesfully");
        }catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        try{
            Connection con= DriverManager.getConnection(url,user,password);
            con.setAutoCommit(true);
            Scanner scanner= new Scanner(System.in);
            User user=new User(con,scanner);
            Accounts accounts= new Accounts(con,scanner);
            AccountManager accma=new AccountManager(con,scanner);
            String email;
            long account_number;
            while(true){
                System.out.println();
                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit ");
                System.out.println("Enter Your Choice");
                int choice1= scanner.nextInt();
                switch (choice1){
                    case 1:
                        user.register();
                        System.out.println("\003[H\033[2J");
                        System.out.flush();
                        break;
                    case 2:
                        email=user.Login();
                        if(email!=null){
                            System.out.println();
                            System.out.println("User Logged IN!");
                            if(!accounts.account_exits(email)){
                                System.out.println();
                                System.out.println("1.Open a new Bank Account");
                                System.out.println("2. Exit");
                                if(scanner.nextInt() == 1){
                                    account_number=accounts.open_account(email);
                                    System.out.println("Account Created Successfully!!");
                                    System.out.println("Your Account Number is: "+ account_number);
                                }else{
                                    break;
                                }
                            }
                            account_number=accounts.getAccount_number(email);
                            int choice2 =0;
                            while(choice2 != 5){
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2.Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5.Log Out");
                                System.out.println("Enter Ur choice :");
                                choice2= scanner.nextInt();
                                switch (choice2){
                                    case 1:
                                        accma.debit_money(account_number);
                                        break;
                                    case 2:
                                        accma.credit_money(account_number);
                                        break;
                                    case 3:
                                        accma.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accma.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter valid Choice");
                                        break;
                                }

                            }


                    }else{
                            System.out.println("Incorrect email or password");
                        }
                    case 3:
                        System.out.println("THANK YOU FOR USING OUR BANKING SYSTEM!!");
                        System.out.println("Exiting System!!");
                        return;
                    default:
                        System.out.println("Enter Valid choice !!!");
                        break;
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

}
