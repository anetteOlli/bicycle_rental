package Admin_App;

public class PaymentCard {
    private int cardNumber;
    private int custId;
    private int balance;
    private boolean isActive;

    public PaymentCard(int cardNumber, int custId){
        this.cardNumber = cardNumber;
        this.custId = custId;
        balance = 0;
        isActive = true;
    }

    public int getCardNumber(){
        return cardNumber;
    }

    public int getCustId(){
        return custId;
    }

    public int getBalance(){
        return balance;
    }

    public boolean isActive(){
        return isActive;
    }

    public void setBalance(int newBalance){
        balance = newBalance;
    }

    public void addFunds(int amount){
        balance = balance + amount;
    }

    public boolean deductFunds(int amount){
        int newBalance = balance - amount;
        if(newBalance >= 0){
            balance = newBalance;
            return true;
        }
        else{
            throw new IllegalArgumentException("Insufficient funds");
        }
    }
}
