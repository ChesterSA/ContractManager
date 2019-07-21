/*
    Program for CIS1010-N
    Handles phone contracts for a fictional company
    Can create new contracts, search and edit them
 */
package contractmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

/**
 *
 * @author S6089488 - Chester Swann-Auger
 */
public class ContractManager
{

    public static void main(String[] args)
    {

        Scanner keyboard = new Scanner(System.in);
        int ans;
        do
        {
            System.out.println("~~~Menu~~~");
            System.out.println("1- Create New Contract");
            System.out.println("2- Summary of Contracts");
            System.out.println("3- Summary of Contracts for Selected Month");
            System.out.println("4- Find and Display Contract");
            System.out.println("0- Quit");
            System.out.println();
            System.out.println("Select Option");
            ans = keyboard.nextInt();
            switch (ans)
            {
                case 1:
                    option1();
                    continue;
                case 2:
                    option2();
                    continue;
                case 3:
                    option3();
                    continue;
                case 4:
                    option4();
                    continue;
                case 0:
                    continue;
                default:
                    System.out.println("Invalid value, enter a number from 1-4");
                    break;
            }
        } while (ans != 0);
        System.out.println("Thanks for using this program");
        System.out.println("Program Exiting...");
    }

    private static void option1()
    {
        Scanner keyboard = new Scanner(System.in);
        Contract contract = new Contract();
        int discount = 0;
        boolean valid;

        //contract.setProperty() methods return true if the value fits the spec, and sets the property
        //if the test is false the property isn't set
        System.out.println("Please enter Customer name (1-25 characters)");
        valid = contract.setName(keyboard.nextLine());
        while (!valid)
        {
            System.out.println("Please enter a name with 1-25 characters");
            valid = contract.setName(keyboard.nextLine());
        }

        System.out.println("Please enter Contract ID");
        valid = contract.setRefNumber(keyboard.nextLine().toUpperCase());
        while (!valid)
        {
            System.out.println("Please enter an ID in the form AA123B");
            valid = contract.setRefNumber(keyboard.nextLine().toUpperCase());
        }

        getMinutes(contract);
        getData(contract);
        getDate(contract);
        getContractPeriod(contract);

        System.out.println("Are international calls required as part of your contract");
        contract.setInternationalCalls(okay());

        if (contract.getBusiness() || contract.getContractPeriod() == 24)
            discount = 10; 
        else if (contract.getContractPeriod() == 12 || contract.getContractPeriod() == 18)
            discount = 5;

        contract.setDiscount(discount);
        contract.setMonthlyCharge(getMonthlyCharge(contract));

        output(contract);
        save(contract);

        System.out.println("Press Enter to return to menu");
        keyboard.nextLine();
    }

    private static void output(Contract contract)
    {
        System.out.println(contract.getMinutes());
        //Handles the formatted output of a contract
        //temp1 and temp2 hold the temporary strings associated with a class property
        String temp1, temp2;
        String end = "+--------------------------------------------+";
        String blankLine = "|                                            |";

        System.out.println(end);
        System.out.println(blankLine);

        System.out.printf("| Customer: %-33s|\n", contract.getName());

        System.out.printf("|      Ref: %-14sDate: %-13s|\n", contract.getRefNumber(), contract.getDate());

        switch (contract.getMinutes())
        {
            case 1:
                temp1 = "Small (300)";
                break;
            case 2:
                temp1 = "Medium (600)";
                break;
            default:
                temp1 = "Large (1200)";
                break;
        }

        switch (contract.getData())
        {
            case 1:
                temp2 = "Low (1)";
                break;
            case 2:
                temp2 = "Medium (4)";
                break;
            case 3:
                temp2 = "High (8)";
                break;
            default:
                temp2 = "Unlimited";
                break;
        }

        System.out.printf("|  Package: %-14sData: %-13s|\n", temp1, temp2);

        temp1 = (contract.getBusiness()) ? "Business" : "Non-Business";
        System.out.printf("|   Period: %-14sType: %-13s|\n", contract.getContractPeriod() + " Months", temp1);

        System.out.println(blankLine);

        temp1 = contract.getInternationalCalls() ? "Yes" : "No";
        System.out.printf("| Discount: %-7sIntl. Calls: %-13s|\n", contract.getDiscount() + "%", temp1);

        System.out.println(blankLine);

        //I understand this next section isn't amazingly coded
        //However, center alignment is a pain and this <i>works</i>
        temp1 = "Monthly Charge: " + String.format("£%.2f ", contract.getMonthlyCharge() / 100.0);
        if (contract.getDiscount() > 0)
            temp1 = ("Discounted " + temp1);
        else
            temp1 += "      ";
        System.out.printf("|%44s|\n", temp1 + "     ");

        System.out.println(blankLine);
        System.out.println(end);
    }

    private static void save(Contract contract)
    {
        PrintWriter output = null;

        File contracts = new File("contracts.txt");
        try
        {
            FileWriter fw = new FileWriter(contracts, true);
            output = new PrintWriter(fw);
        } 
        catch (FileNotFoundException e) // problem with file !
        {
            System.out.println("File 'contracts.txt' not found");
        } 
        catch (IOException ex)
        {
            System.out.println("Error - problem creating the file! Program closing");
            System.exit(0);
        }

        assert output != null;
        output.print(contract.getDate() + "\t");
        output.print(contract.getMinutes() + "\t");
        output.print(contract.getData() + "\t");
        output.print(contract.getContractPeriod() + "\t");
        output.print((contract.getInternationalCalls() ? "Y" : "N") + "\t");
        output.print(contract.getRefNumber() + "\t");
        output.print(contract.getMonthlyCharge() + "\t");
        output.print(contract.getName() + "\r\n");

        output.close();
    }

    private static void getDate(Contract contract)
    {
        //Sets the date property of the passed in contract class to the current date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        contract.setDate(sdf.format(cal.getTime()));
    }

    private static void getMinutes(Contract contract)
    {
        //Sets the minutes of the passed in contract class
        //if value estimated is <100 above a class it recommends the lower one
        ///otherwise it rounds up
        int minutes = 0, num;
        Scanner keyboard = new Scanner(System.in);
        while (!contract.setMinutes(minutes))
        {
            System.out.println("Please enter estimated amount of minutes needed");
            System.out.println("  Bundles available are 300, 600, and 1200 minutes");
            num = keyboard.nextInt();
            if (num < 0)
            {
                System.out.println("Please enter a number greater than or equal to 0\n");
            } 
            else if (num < 400)
            {
                System.out.println("300 minutes plan chosen, is this okay?");
                if (okay())
                    minutes = 1;
            } 
            else if (num < 700)
            {
                System.out.println("600 minutes plan chosen, is this okay?");
                if (okay())
                    minutes = 2;
            } 
            else
            {
                System.out.println("1200 minutes plan chosen, is this okay?");
                if (okay())
                    minutes = 3;
            }
        }
        
    }

    private static void getData(Contract contract)
    {
        //Sets the data of the passed in contract class
        //if value estimated is <1000 above a class it recommends the lower one
        ///otherwise it rounds up
        int data = 0, num;
        while (!contract.setData(data))
        {
            Scanner keyboard = new Scanner(System.in);

            System.out.println("Please enter estimated amount of MB needed");
            System.out.print("  Bundles available are 1000, 4000, and 8000");
            if (contract.getMinutes() == 3)
                System.out.print(" or Unlimited (Enter >9000)");
            System.out.println(" MB");
            
            num = keyboard.nextInt();
            
            if (num < 0 )
                System.out.println("Please enter a number greater than or equal to 0"); 
            else if (num < 2000)
            {
                System.out.println("1GB bundle chosen, is this okay?");
                if (okay())
                    data = 1;
            } 
            else if (num < 5000)
            {
                System.out.println("4GB bundle chosen, is this okay?");
                if (okay())
                    data = 2;
            } 
            else if (num >= 9000 && contract.getMinutes() == 3)
            {
                System.out.println("Unlimited bundle chosen, is this okay?");
                if (okay())
                    data = 4;
            } 
            else
            {
                System.out.println("8GB bundle chosen, is this okay?");
                if (okay())
                    data = 3;
            }
        }
    }

    private static void getContractPeriod(Contract contract)
    {
        //Sets the contract period of the passed in contract class
        int contractPeriod = 0;
        Scanner keyboard = new Scanner(System.in);
        System.out.println("What length contract do you need");
        do
        {
            if (contractPeriod == 1 && contract.getBusiness())
            {
                System.out.println("Business contracts cannot have one month deals");
                System.out.println("Please select 12, 18, or 24");
            } 
            else
            {
                System.out.print("Please enter ");
                if (!contract.getBusiness())
                    System.out.print("1, ");
                System.out.println("12, 18, or 24 month");
            }
            contractPeriod = keyboard.nextInt();
        } while (!contract.setContractPeriod(contractPeriod));
    }

    private static int getMonthlyCharge(Contract contract)
    {
        //Sets the monthly charge of the passed in contract class
        int monthlyCharge = 0;
        System.out.println(contract.getMinutes() + "\t" + contract.getData());
        switch (contract.getMinutes())
        {
            case 1:
                monthlyCharge = 500;
                break;
            case 2:
                monthlyCharge = 650;
                break;
            case 3:
                monthlyCharge = 850;
                break;
            default:
                break;
        }
        
        switch (contract.getData())
        {
            case 2:
                monthlyCharge += 200;
                break;
            case 3:
                monthlyCharge += 400;
                break;
            case 4:
                monthlyCharge = 2000;
                break;
            default:
                break;
        }

        //discount is stored as a percentage (e.g. 10% off) so this calculates monthly charge from that
        monthlyCharge = (int) (monthlyCharge * (1 - (contract.getDiscount() / 100)));

        if (contract.getInternationalCalls())
            monthlyCharge *= 1.15;
        
        return monthlyCharge;
    }

    private static boolean okay()
    {
        //All-purpose class for getting a Yes/No response from user
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Please enter (Y/N)");
        String s = keyboard.next().toUpperCase();
        
        while (!s.equals("Y") && !s.equals("N"))
        {
            System.out.println("Please Enter Y or N");
            s = keyboard.next().toUpperCase();
        }
        
        return (s.equals("Y"));
    }

    private static void option2()
    {
        System.out.println("~~~ NOT FULLY IMPLEMENTED ~~~");
        String file;
        int choice;
        int totalContracts = 0;
        int highDataContracts = 0;
        int highDataAverage = 0;
        String currentLine;
        Scanner keyboard = new Scanner(System.in);

        do
        {
            System.out.println("Please select a text file");
            System.out.println("1 - contracts.txt");
            System.out.println("2 - archive.txt");
            choice = keyboard.nextInt();
        } while (choice != 1 && choice != 2);

        System.out.println();

        file = (choice == 1) ? "contracts.txt" : "archive.txt";

        Scanner input = null;
        try
        {
            input = new Scanner(new File(file));
        } 
        catch (FileNotFoundException e)
        {
            System.out.println("File doesn't exist");
            System.exit(1);
        }
        while (input.hasNextLine())
        {
            totalContracts++;
            currentLine = input.nextLine();
            if (isHighData(currentLine))
            {
                highDataContracts++;
                highDataAverage += getChargeFromString(currentLine);
            }
        }
        input.close();

        highDataAverage /= highDataContracts;
        System.out.println("Total Number of Contracts: " + totalContracts);
        System.out.println("Contracts with High or Unlimited Data Bundles: " + highDataContracts);
        System.out.println("Average charge for large packages: " + highDataAverage);
        System.out.println();
        System.out.println("Number of Contracts per Month: ");
        System.out.println();
        System.out.println("Jan\tFeb\tMar\tApr\tMay\tJun\tJul\tAug\tSep\tOct\tNov\tDec");

        for (int i = 0; i < 12; i++)
        {
            System.out.print("val" + "\t");
        }
        System.out.println();
        System.out.println("Press Enter to return to menu");
        keyboard.nextLine();
    }

    private static boolean isHighData(String currentLine)
    {
        String[] data = currentLine.split("\t");
        return (data[2].equals("3"));
    }

    private static int getChargeFromString(String currentLine)
    {
        String[] data = currentLine.split("\t");
        return Integer.parseInt(data[6]);
    }

    private static void option3()
    {
        System.out.println("You have selected Option 3");
        System.out.println("However this hasn't been implemented yet");
        System.out.println("So .. umm .. try Option 1 .. I guess");
    }

    private static void option4()
    {
        System.out.println("You have selected Option 4");
        System.out.println("However this hasn't been implemented yet");
        System.out.println("So .. umm .. try Option 1 .. I guess");
    }
}
