package Recyclerview;

public class ListItem {
   // private  String reason,amount,paybackperiod,paynumber;
    private String date;
    private String head;
    private  String  desc;
    private String amount;


    public ListItem(String head, String desc,String amount,String date) {
        this.head = head;
        this.desc = desc;
        this.amount = amount;
        this.date =date;
        //this.reason = reason;
        //this.amount =amount;
    }



    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
//public String getReason() {
     //   return reason;
    //}

    //public int getid() {
       // return id;
    //}


  //  public String getImageurl() {
    //    return imageurl;
  //  }
}
