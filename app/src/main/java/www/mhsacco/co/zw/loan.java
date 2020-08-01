package www.mhsacco.co.zw;

public class loan {

    private int id;
    private  String reason,amount,paybackperiod,paynumber;

    public loan(int id, String reason,String amount, String paybackperiod ,String paynumber){
        this.id =id;
        this.reason = reason;
        this.amount =amount;
        this.paybackperiod =paybackperiod;
        this.paynumber =paynumber;

    }
    public  int getId(){return id;}
    public  String getReason(){return reason;}
    public  String getAmount(){return amount;}
    public  String getPaybackperiod(){return paybackperiod;}
    public  String getPaynumber(){return paynumber;}
}
