/*
    Class to hold the contract for the ContractManager program
 */
package contractmanager;

import java.util.regex.Pattern;

/**
 *
 * @author cswan
 */
class Contract
{
    private String name, refNumber, date;
    private int contractPeriod, minutes, data, monthlyCharge;
    private boolean business, internationalCalls;
    private double discount = 0;
    
    boolean setName(String name)
    {
        boolean valid = false;
        if (name.length() <= 25 && name.length() > 0)
        {
            this.name = name;
            valid = true;
        }
        return valid;
    }
    String getName()
    {
        return name;
    }
    
    boolean setRefNumber(String refNumber)
    {
        boolean valid = false;
        if (Pattern.matches("[A-Z][A-Z][0-9][0-9][0-9][B,N]", refNumber))
        {   
            this.refNumber = refNumber;
            business = (this.refNumber.charAt(5) == 'B');
            valid = true;
        }
        return valid;
    }
    String getRefNumber()
    {
        return refNumber;
    }
    
    void setDate(String date)
    {      
        //No built-in date validation as it's very awkward
        //For this program it isn't set by the user so not a worry so far
        this.date = date;
    }
    String getDate()
    {
        return date;
    }
    
    
    boolean setContractPeriod(int contractPeriod)
    {
        boolean valid = false;
        if((contractPeriod == 1 && !business)|| contractPeriod == 12 || contractPeriod == 18 || contractPeriod == 24)
        {
            this.contractPeriod = contractPeriod;
            valid = true;
        }
        return valid;
    }
    int getContractPeriod()
    {
        return contractPeriod;
    }
    
    boolean setMinutes(int minutes)
    {
        boolean valid = false;
        if (minutes == 1 || minutes == 2 || minutes == 3)
        {
            this.minutes = minutes;
            valid = true;
        }          
        return valid;
    }
    int getMinutes()
    {
        return minutes;
    }
    
    boolean setData(int data)
    {
        boolean valid = false;
        if (data == 1 || data == 2 || data == 3 || (data == 4 && minutes == 3))
        {
            this.data = data;
            valid = true;
        }          
        return valid;    
    }                   
    int getData()
    {
        return data;
    }
    
    void setMonthlyCharge(int monthlyCharge)
    {
        this.monthlyCharge = monthlyCharge;
    }
    int getMonthlyCharge()
    {
        return monthlyCharge;
    }
    
    //No set method for business as it is automatic from last digit of refNumber
    boolean getBusiness()
    {
        return business;
    }
    
    void setInternationalCalls(boolean internationalCalls)
    {
        this.internationalCalls = internationalCalls;
    }
    boolean getInternationalCalls()
    {
        return internationalCalls ;
    }
    
    
    void setDiscount(double discount)
    {
        this.discount = discount;
    }
    double getDiscount()
    {
        return discount;
    }
}
