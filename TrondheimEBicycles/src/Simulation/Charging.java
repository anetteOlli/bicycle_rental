package Simulation;

import Admin_App.DockingStation;

import java.util.Random;


public class Charging extends Thread{
    private int dockingstationID;
    private DockingStation dock;
    private int time;

    public Charging(int dockingstationID){
        this.dockingstationID = dockingstationID;
        time = 0;
        dock = new DockingStation();

    }
    public void run(){
        try {
            while(time <= 80){
                Random random = new Random();
                int powerI = random.nextInt(600);
                double power = (double) powerI;
                dock.setPowerUsage(dockingstationID, power);
                System.out.println("thread with dockingstationID: " + dockingstationID + " er oppdatert med: " + power);
                sleep(10000);
                time++;
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String args[]){
        (new Charging(1)).start();
        (new Charging(2)).start();
        (new Charging(3)).start();
    }
}
