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
                    System.out.print("\tDo you want to add another account (Y/n)? ");
                    if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                    screen = DASHBOARD;
                    break;
                
                case DEPOSIT_MONEY:
                    String accountID;
                    double accountBalance = 0;
                    double deposit = 0;
                    int idIndex = -1;
                    
                    loopDeposit:
                    do {
                        valid = true;

                        System.out.print("\tEnter Account No. to Deposit: ");
                        accountID = SCANNER.nextLine().strip();

                        if(!notEmpty(accountID) || !validFormat(accountID) || !foundID(accountID, accounts)) {
                            
                            System.out.print("\n\tDo you want to try again (Y/n)? ");
                            if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                valid = false; 
                                continue;
                            }
                            screen = DASHBOARD;
                            break loopDeposit;
                        }

                        idIndex = findIndex(accountID, accounts);
                        accountBalance = Double.valueOf(accounts[idIndex][2]);
                        System.out.printf("\n\tCurrent Balance: Rs. %,.2f\n",accountBalance);

                        boolean repeat;
                        do {
                            repeat = false;
                            System.out.print("\n\tDeposit Amount: ");
                            deposit = SCANNER.nextDouble();
                            SCANNER.nextLine();

                            if(deposit < 500.00) {
                                System.out.printf(ERROR_MSG,"Insufficient Amount");
                                System.out.print("\n\tDo you want to try again (Y/n)? ");
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                    valid = false;
                                    continue loopDeposit;
                                }
                                screen = DASHBOARD;
                                break loopDeposit;
                            }

                        } while (repeat);

                        accountBalance += deposit;

                        System.out.printf("\n\tNew Balance: Rs. %,.2f\n",accountBalance);

                        accounts[idIndex][2] = accountBalance+"";

                    } while (!valid);

                    System.out.print("\n\tDo you want to continue Deposit Money (Y/n)? ");
                    if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                    screen = DASHBOARD;
                    break;
                    
                case WITHDRAW_MONEY:
                    double withdraw;
                    String withdrawID;
                    
                    loopWithdraw:
                    do {
                        valid = true;

                        System.out.print("\tEnter Account No. to Withdraw: ");
                        withdrawID = SCANNER.nextLine().strip();

                        if(!notEmpty(withdrawID) || !validFormat(withdrawID) || !foundID(withdrawID, accounts)) {
                            
                            System.out.print("\n\tDo you want to try again (Y/n)? ");
                            if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                valid = false;
                                continue loopWithdraw;
                            }
                            screen = DASHBOARD;
                            break;
                        }

                        idIndex = findIndex(withdrawID, accounts);
                        accountBalance = Double.valueOf(accounts[idIndex][2]);
                        System.out.printf("\n\tCurrent Balance: Rs. %,.2f\n",accountBalance);

                        boolean repeat;
                        loopWithdrawAmount:
                        do {
                            repeat = false;
                            System.out.print("\n\tWithdraw Amount: ");
                            withdraw = SCANNER.nextDouble();
                            SCANNER.nextLine();

                            if(withdraw < 100.00) {
                                System.out.printf(ERROR_MSG,"Minimum Withdraw amount is Rs. 100.00");
                                System.out.print("\n\tDo you want to try again (Y/n)? ");
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                    repeat = true;
                                    continue;
                                }
                                screen = DASHBOARD;
                                break loopWithdrawAmount;
                            }

                            if((accountBalance - withdraw) < 500.00) {
                                System.out.printf(ERROR_MSG,"Insufficient Account Balance");
                                System.out.print("\n\tDo you want to try again (Y/n)?");
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                                screen = DASHBOARD;
                                break loopWithdrawAmount;
                            }
                            accountBalance -= withdraw;

                            System.out.printf("\n\tNew Balance: Rs. %,.2f\n",accountBalance);

                            accounts[idIndex][2] = accountBalance+"";

                        } while (repeat);

                    } while (!valid);

                    System.out.print("\n\tDo you want to continue Withdraw Money (Y/n)? ");
                    if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                    screen = DASHBOARD;
                    break;

                case TRANSFER_MONEY:
                    double transfer;
                    double fromBalance;
                    double toBalance;
                    String fromAccount;
                    String toAccount;
                    
                    loopTransfer:
                    do {
                        valid = true;

                        boolean repeat;
                        do {
                            repeat = false;
                            System.out.print("\tEnter From Account No.: ");
                            fromAccount = SCANNER.nextLine().strip();

                            if(!notEmpty(fromAccount) || !validFormat(fromAccount) || !foundID(fromAccount, accounts)) {
                                
                                System.out.print("\n\tDo you want to try again (Y/n)?");
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                    repeat = true;
                                    continue;
                                }
                                screen = DASHBOARD;
                                break loopTransfer;
                            }

                        } while (repeat);

                        do {
                            repeat = false;
                            System.out.print("\tEnter To Account No.: ");
                            toAccount = SCANNER.nextLine().strip();

                            if(!notEmpty(toAccount) || !validFormat(toAccount) || !foundID(toAccount, accounts)) {
                                
                                System.out.print("\n\tDo you want to try again (Y/n)?");
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                    repeat = true;
                                    continue;
                                }
                                screen = DASHBOARD;
                                break loopTransfer;
                            }

                        } while (repeat);

                        int fromIdIndex = findIndex(fromAccount, accounts);
                        fromBalance = Double.valueOf(accounts[fromIdIndex][2]);

                        int toIdIndex = findIndex(toAccount, accounts);
                        toBalance = Double.valueOf(accounts[toIdIndex][2]);

                        System.out.printf("\n\tFrom Account Name: %s\n",accounts[fromIdIndex][1]);
                        System.out.printf("\tFrom Account Balance: Rs. %,.2f\n",fromBalance);
                        System.out.printf("\n\tTo Account Name: %s\n",accounts[toIdIndex][1]);
                        System.out.printf("\tTo Account Balance: Rs. %,.2f\n",toBalance);

                        do {
                            repeat = false;
                            System.out.print("\n\tEnter Transfer Amount: ");
                            transfer = SCANNER.nextDouble();
                            SCANNER.nextLine();

                            if(transfer < 100.00) {
                                System.out.printf(ERROR_MSG,"Minimum Transfer amount is Rs. 100.00");
                                System.out.print("\n\tDo you want to try again (Y/n)?");
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                    repeat = true;
                                    continue;
                                }
                                screen = DASHBOARD;
                                break loopTransfer;
                            }

                            if((toBalance - (transfer*1.02)) < 500.00) {
                                System.out.printf(ERROR_MSG,"Insufficient Account Balance");
                                System.out.print("\n\tDo you want to try again (Y/n)?");
                                if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                    repeat = true;
                                    continue;
                                }
                                screen = DASHBOARD;
                                break loopTransfer;
                            }

                        } while (repeat);

                        fromBalance -= (transfer * 1.02);
                        toBalance += transfer;

                        System.out.printf("\n\tNew From Account Balance: Rs. %,.2f\n",fromBalance);
                        System.out.printf("\n\tNew To Account Balance: Rs. %,.2f\n",toBalance);

                        accounts[fromIdIndex][2] = fromBalance+"";
                        accounts[toIdIndex][2] = toBalance+"";

                    } while (!valid);

                    System.out.print("\n\tDo you want to continue Transfer Money (Y/n)?");
                    if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) continue;
                    screen = DASHBOARD;
                    break;

                case ACCOUNT_BALANCE:
                    
                    do {
                        valid = true;

                        System.out.print("\tEnter Account No. to Check Balance: ");
                        accountID = SCANNER.nextLine().strip();

                        if(!notEmpty(accountID) || !validFormat(accountID) || !foundID(accountID, accounts)) {
                            
                            System.out.print("\n\tDo you want to try again (Y/n)?");
                            if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                valid = false;
                                continue;
                            }
                            screen = DASHBOARD;
                            break;
                        }

                        idIndex = findIndex(accountID, accounts);
                        accountBalance = Double.valueOf(accounts[idIndex][2]);

                        System.out.printf("\n\tAccount Name: %s\n",accounts[idIndex][1]);
                        System.out.printf("\tCurrent Account Balance: Rs. %,.2f\n",accountBalance);
                        System.out.printf("\tAvailable Account Balance: Rs. %,.2f\n",(accountBalance-500.00));

                        System.out.print("\n\tDo you want to continue Check Account Balance (Y/n)?");
                        if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                            valid = false;
                            continue;
                        }

                    } while (!valid);
                    screen = DASHBOARD;
                    break;
                
                case DROP_ACCOUNT:
                    
                    do {
                        valid = true;

                        System.out.print("\tEnter Account No. to Delete Account: ");
                        accountID = SCANNER.nextLine().strip();

                        if(!notEmpty(accountID) || !validFormat(accountID) || !foundID(accountID, accounts)) {
                            
                            System.out.print("\n\tDo you want to try again (Y/n)? ");
                            if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                                valid = false;
                                continue;
                            }
                            break;
                        }

                        idIndex = findIndex(accountID, accounts);
                        accountBalance = Double.valueOf(accounts[idIndex][2]);
                        String accountName = accounts[idIndex][1];
                        System.out.printf("\n\tAccount Name: %s\n",accountName);
                        System.out.printf("\tCurrent Account Balance: Rs. %,.2f\n",accountBalance);

                        System.out.print("\n\tAre you sure to delete account (Y/n)?");
                        if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {

                            newAccount = new String[accounts.length-1][3]; 
                    
                            for (int i = 0; i < idIndex; i++) {
                                newAccount[i] = accounts[i];
                            }
                            for (int i = idIndex; i < newAccount.length; i++) {
                                newAccount[i] = accounts[i+1];
                            }

                            accounts = newAccount;

                            System.out.println();
                            System.out.printf(SUCCESS_MSG,String.format("%s: %s has been deleted successfully.",accountID,accountName));
                        }

                        System.out.print("\tDo you want to delete another account (Y/n)?");
                        if (SCANNER.nextLine().strip().toUpperCase().equals("Y")) {
                            valid = false;
                            continue;
                        }

                    } while (!valid);
                    screen = DASHBOARD;
                    break;
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
        System.out.printf(ERROR_MSG,"Not Found");
        return false;
    }

    private static boolean validFormat(String accountId) {

        if((accountId.length() != 9) || (!accountId.startsWith("SDB-"))) {
            System.out.printf(ERROR_MSG,"Invalid Format");
            return false;
        }
        
        for (int i = 4; i < accountId.length(); i++) {
            if (!Character.isDigit(accountId.charAt(i))) {
                System.out.printf(ERROR_MSG,"Invalid Format");
                return false;
            }
        }
        return true;
    }

    private static boolean notEmpty(String accountId) {
        
        if(accountId.isEmpty()) {
            System.out.printf(ERROR_MSG,"Account Number can't be empty");
            return false;
        }
        return true;
    }
}