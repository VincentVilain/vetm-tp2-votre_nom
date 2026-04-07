package service;

import documents.Document;
import personnel.Member;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BorrowDocument {

    private Member member;
    private Date borrowingDate;
    private Date returnDate;
    public static final String R = "\u001B[31m";
    public static final String RES = "\u001B[0m";
    public static final String G = "\u001B[32m";

    public BorrowDocument(){

    }

    public BorrowDocument(Member adherent, Date borrowingDate, Date returnDate) {
        this.member = adherent;
        this.borrowingDate = borrowingDate;
        this.returnDate = returnDate;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Date getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(Date borrowingDate) {
        this.borrowingDate = borrowingDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
    
    public void displayBorrow(Document d){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String memberName = getMember().getFirstName() + " " + getMember().getName();
        System.out.printf("|%9s", d.getClass().getSimpleName());
        System.out.printf("|%40s", d.getTitle());
        System.out.printf("|%40s", memberName);
        System.out.printf("|%13s", getMember().getType());
        System.out.printf("|%13s", sdf.format(getBorrowingDate()));
        if (getReturnDate().before(date)){
//            in red
            System.out.print("|"+R);
            System.out.printf("%13s", sdf.format(getReturnDate()));
            System.out.print(RES);
        }else {
//            in green
            System.out.print("|"+G);
            System.out.printf("%13s", sdf.format(getReturnDate()));
            System.out.print(RES);
        }

        System.out.println("|");
    }

    public void calculateReturnDate(){
        Calendar c = Calendar.getInstance();
        c.setTime(getBorrowingDate());
        c.add(Calendar.DATE, getMember().getMaxBorrowDuration());
        setReturnDate(c.getTime());
    }
}
