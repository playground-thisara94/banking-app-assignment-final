import java.util.Scanner;

public class SmartBankingApp {
    private static final Scanner SCANNER = new Scanner(System.in);

        private static final String CLEAR = "\033[H\033[2J";
        private static final String COLOR_BLUE_BOLD = "\033[34;1m";
        private static final String COLOR_RED_BOLD = "\033[31;1m";
        private static final String COLOR_GREEN_BOLD = "\033[32;1m";
        private static final String RESET = "\033[0m";

        private static final String ERROR_MSG = String.format("\t%s%s%s\n",COLOR_RED_BOLD,"%s",RESET);
        private static final String SUCCESS_MSG = String.format("\t%s%s%s\n", COLOR_GREEN_BOLD,"%s",RESET);

    public static void main(String[] args) {

        final String DASHBOARD = "Welcome to Smart Banking App";
        final String OPEN_ACCOUNT = "Open New Account";
        final String DEPOSIT_MONEY = "Deposit Money";
        final String WITHDRAW_MONEY = "Withdraw Money";
        final String TRANSFER_MONEY = "Transfer Money";
        final String ACCOUNT_BALANCE = "Check Account Balance";
        final String DROP_ACCOUNT = "Drop Existing Account";

        String[][] accounts = new String[0][];

        String screen = DASHBOARD;

        outerloop:
        do {
            final String APP_TITLE = String.format("%s%s%s", COLOR_BLUE_BOLD,screen,RESET);

            System.out.println(CLEAR);
            System.out.println("\t"+APP_TITLE+"\n");

            switch(screen) {
                case DASHBOARD:
                    System.out.println("\t[1]. Open New Account");
                    System.out.println("\t[2]. Deposit Money");
                    System.out.println("\t[3]. Withdraw Money");
                    System.out.println("\t[4]. Transfer Money");
                    System.out.println("\t[5]. Check Account Balance");
                    System.out.println("\t[6]. Drop Existing Account");
                    System.out.println("\t[7]. Exit");   
                    System.out.println();
                    System.out.print("\tEnter an option to continue: ");
                    int option = SCANNER.nextInt();
                    SCANNER.nextLine();

                    switch (option) {
                        case 1: screen = OPEN_ACCOUNT; break;
                        case 2: screen = DEPOSIT_MONEY; break;
                        case 3: screen = WITHDRAW_MONEY; break;
                        case 4: screen = TRANSFER_MONEY; break;
                        case 5: screen = ACCOUNT_BALANCE; break;
                        case 6: screen = DROP_ACCOUNT; break;
                        case 7: System.out.println(CLEAR); System.exit(0);
                        default: continue;
                    }
                    break;

                case OPEN_ACCOUNT:
                    String name;
                    double initialDeposit;
                    boolean valid;

                    int id = accounts.length+1;
                    String newID = String.format("SDB-%05d",id);
                    System.out.printf("\tID : %s\n",newID);

                    do{
                        valid = true;
                        System.out.print("\tName: ");
                        name = SCANNER.nextLine().strip();
                        if (name.isBlank()){
                            System.out.printf(ERROR_MSG,"Name can't be empty");
                            valid = false;
                            continue;
                        }
                        for (int i = 0; i < name.length(); i++) {
                            if(!(Character.isLetter(name.charAt(i)) || Character.isSpaceChar(name.charAt(i)))) {
                                System.out.printf(ERROR_MSG,"Invalid Name");
                                valid = false;
                                break;
                            }
                        }
                    } while (!valid);

                    do{
                        valid = true;
                        System.out.print("\tInitial Deposit: ");
                        initialDeposit = SCANNER.nextDouble();
                        SCANNER.nextLine();
                        if (initialDeposit < 5000.00){
                            System.out.printf(ERROR_MSG,"Insufficient Amount");
                            valid = false;
                            continue;
                        }
                        
                    } while (!valid);
                    
                    String[][] newAccount = new String[accounts.length+1][3]; 
                    

                    for (int i = 0; i < accounts.length; i++) {
                        newAccount[i] = accounts[i];
                    }
                    newAccount[newAccount.length-1][0] = newID;
                    newAccount[newAccount.length-1][1] = name;
                    newAccount[newAccount.length-1][2] = initialDeposit+"";

                    accounts = newAccount;

                    System.out.println();
                    System.out.printf(SUCCESS_MSG,String.format("SDB-%05d: %s has been created successfully.",id,name));
                    System.out.print("\tDo you want to add another account (Y/n)?");
                    if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                    screen = DASHBOARD;
                    break;
                
                case DEPOSIT_MONEY:
                    String accountID;
                    double accountBalance;
                    double deposit;
                    
                    loopDeposit:
                    do {
                        valid = true;

                        System.out.print("Enter Account No.: ");
                        accountID = SCANNER.nextLine().strip();

                        if(accountID.isEmpty()) {
                            System.out.printf(ERROR_MSG,"Account Number can't be empty");
                            System.out.print("\n\tDo you want to try again (Y/n)?");
                            if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                            screen = DASHBOARD;
                            break;
                        }

                        if(!validFormat(accountID)) {
                            System.out.printf(ERROR_MSG,"Invalid Format");
                            System.out.print("\n\tDo you want to try again (Y/n)?");
                            if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                            screen = DASHBOARD;
                            break;
                        }

                        if(!foundID(accountID, accounts)) {
                            System.out.printf(ERROR_MSG,"Not Found");
                            System.out.print("\n\tDo you want to try again (Y/n)?");
                            if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                            screen = DASHBOARD;
                            break;
                        }

                        int idIndex = findIndex(accountID, accounts);
                        accountBalance = Double.valueOf(accounts[idIndex][2]);
                        System.out.printf("\n\tCurrent Balance: Rs. %,.2f\n",accountBalance);

                        boolean repeat;
                        do {
                            repeat = false;
                            System.out.print("\nDeposit Amount: ");
                            deposit = SCANNER.nextDouble();
                            SCANNER.nextLine();

                            if(deposit < 500.00) {
                                System.out.printf(ERROR_MSG,"Insufficient Amount");
                                System.out.print("\n\tDo you want to try again (Y/n)?");
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                                screen = DASHBOARD;
                                break loopDeposit;
                            }

                        } while (repeat);

                        accountBalance += deposit;

                        System.out.printf("\n\tNew Balance: Rs. %,.2f\n",accountBalance);

                        accounts[idIndex][2] = accountBalance+"";

                        System.out.print("\n\tDo you want to continue Deposit Money (Y/n)?");
                        if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                        screen = DASHBOARD;
                        break;

                    } while (!valid);

                case WITHDRAW_MONEY:
                    double withdraw;
                    
                    loopWithdraw:
                    do {
                        valid = true;

                        System.out.print("Enter Account No.: ");
                        accountID = SCANNER.nextLine().strip();

                        if(accountID.isEmpty()) {
                            System.out.printf(ERROR_MSG,"Account Number can't be empty");
                            System.out.print("\n\tDo you want to try again (Y/n)?");
                            if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                            screen = DASHBOARD;
                            break;
                        }

                        if(!validFormat(accountID)) {
                            System.out.printf(ERROR_MSG,"Invalid Format");
                            System.out.print("\n\tDo you want to try again (Y/n)?");
                            if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                            screen = DASHBOARD;
                            break;
                        }

                        if(!foundID(accountID, accounts)) {
                            System.out.printf(ERROR_MSG,"Not Found");
                            System.out.print("\n\tDo you want to try again (Y/n)?");
                            if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                            screen = DASHBOARD;
                            break;
                        }

                        int idIndex = findIndex(accountID, accounts);
                        accountBalance = Double.valueOf(accounts[idIndex][2]);
                        System.out.printf("\n\tCurrent Balance: Rs. %,.2f\n",accountBalance);

                        boolean repeat;
                        do {
                            repeat = false;
                            System.out.print("\nWithdraw Amount: ");
                            withdraw = SCANNER.nextDouble();
                            SCANNER.nextLine();

                            if(withdraw < 100.00) {
                                System.out.printf(ERROR_MSG,"Minimum Withdraw amount is Rs. 100.00");
                                System.out.print("\n\tDo you want to try again (Y/n)?");
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                                screen = DASHBOARD;
                                break loopWithdraw;
                            }

                            if((accountBalance - withdraw) < 500.00) {
                                System.out.printf(ERROR_MSG,"Insufficient Account Balance");
                                System.out.print("\n\tDo you want to try again (Y/n)?");
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                                screen = DASHBOARD;
                                break loopWithdraw;
                            }

                        } while (repeat);

                        accountBalance -= withdraw;

                        System.out.printf("\n\tNew Balance: Rs. %,.2f\n",accountBalance);

                        accounts[idIndex][2] = accountBalance+"";

                        System.out.print("\n\tDo you want to continue Withdraw Money (Y/n)?");
                        if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                        screen = DASHBOARD;
                        break;

                    } while (!valid);

            }
            
        } while (true);

    }

    private static int findIndex(String accountID, String[][] accounts) {

        int index = -1;
        for (int i = 0; i < accounts.length; i++) {
            if (accountID.equals(accounts[i][0])) index = i;
        }
        return index;
    }

    private static boolean foundID(String accountID, String[][] accounts) {

        for (int i = 0; i < accounts.length; i++) {
            if (accountID.equals(accounts[i][0])) return true;
        }
        return false;
    }

    private static boolean validFormat(String accountId) {
        
        for (int i = 0; i < accountId.length(); i++) {
            String digits = accountId.substring(5, 10);
            
            if(accountId.length() != 9) return false;
            else if(!accountId.substring(0, 4).equals("SDB-")) return false;
            else if (!Character.isDigit(digits.charAt(i))) return false;
        }
        return true;
    }

    
}