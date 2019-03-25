//Client side of project1 by Kurt Bopst

import java.io.*;
import java.net.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class client_ver4{

    public static ClientThread[] threads;

  public static void main(String[] args) throws IOException{
  
  int threadNum = 1;
  
  if(args.length == 3){
    threadNum = Integer.parseInt(args[2]);
    if(threadNum < 1) //Mimimum amount of threadNum is 1, prevents bad arguements.
      threadNum = 1;
    
  }

  String hostName = args[0]; //192.168.101.130
  int portNumber = Integer.parseInt(args[1]); //6969

      String outputLine;
      boolean online = true;
      Options option = new Options();
      //Display a successful connection
      System.out.println(
       "Host: " + hostName + " Port Number: " + portNumber + " Number of Threads: " + threadNum);


      // Communicating with the server
      while(online){
        option.display();
        outputLine = option.getOpt();

        if(outputLine.equals("Quit")){
          online = false; //Exit online loop

          Socket socket = new Socket(hostName, portNumber); //Tell server to quit
          PrintWriter quit = 
            new PrintWriter(socket.getOutputStream(), true);
          quit.println(outputLine);


          System.out.println("Disconnected from " + hostName);
        }else{

        createthreadNum(threadNum, outputLine, hostName, portNumber);
        runThreads();
        Results(threadNum);

        }
      }
  }

  private static void createthreadNum(int threadNum, String cmd, String Host, int port){
    threads = new ClientThread[threadNum];
    
    for (int x = 0; x < threadNum; x++){
      threads[x] = new ClientThread(cmd, Host, port);
    }
  }

  private static void runThreads() {
      int x;
      boolean ONLINE = true;
      for(x = 0; x < threads.length; x++){
          threads[x].start();
      }
      for(x = 0; x < threads.length; x++){
        try{
        threads[x].join();
        }catch(InterruptedException e){
          
        }
    }
      while (ONLINE){
          ONLINE = false;
          for (x = 0; x < threads.length; x++){
              if(threads[x].isAlive()){
                  ONLINE = true;
              }
          }
      }
  }

  private static void Results(int threadNum){
    double sum = 0;
    System.out.printf("Server response time: ");

    for(ClientThread t : threads){
      System.out.printf("%.2f, ", t.getTotalTime());
      sum += t.getTotalTime();
    }
    System.out.println( 
      "\nLatency (mean server reponse time): " + (sum/((double)threadNum)) + " ms");

  }

}

class Options{
  Scanner keyboard = new Scanner(System.in);
  public Options(){

  }

  public void display(){
    System.out.println("1. Host current Date and Time\n"
                     + "2. Host uptime\n"
                     + "3. Host memory use\n"
                     + "4. Host Netstat\n"
                     + "5. Host current users\n"
                     + "6. Host running processes\n"
                     + "7. Quit");

  }
  public String getOpt(){
    String opt = null;
    String temp = "";
    boolean badIn = false;
 

    System.out.printf("Please choose an option: ");
        temp = keyboard.next(); 

  do {

    switch(temp){
      case "1": //date and time
        opt = "date";
        badIn = false;
        break;
      case "2": //uptime
        opt = "uptime";
        badIn = false;
        break;
      case "3": //memory use
        opt = "free";
        badIn = false;
        break;
      case "4": //netstat
        opt = "netstat";
        badIn = false;
        break;
      case "5": //users
        opt = "users";
        badIn = false;
        break;
      case "6": //processes
        opt = "ps aux";
        badIn = false;
        break;
      case "7": //Quit
        opt = "Quit";
        badIn = false;
        break;
      default: //Input Validation
        badIn = true;
        System.out.println("\nDon't be foolish! Input a real option!");
        this.display();
        System.out.printf("Please choose an option: ");
        temp = keyboard.next();
        break;

    }
    
  } while (badIn);

    return opt;
  }



}


class ClientThread extends Thread {

  private String cmd;
  private double elaspedTime;
  private double totalTime;
  private long startTime;
  private int port;
  private String host;


  public ClientThread(String command, String host, int port){
    this.cmd = command;
    this.elaspedTime = 0;
    this.host = host;
    this.port = port;
  }

  public double getelaspedTime(){
    return this.elaspedTime;
  }

  public double getTotalTime(){
    return this.totalTime;
  }

  public String getCommand(){
    return this.cmd;
  }

  public void run() {
      try{
        String line = null;
        Socket socket = new Socket(this.host, this.port);
        PrintWriter out = 
          new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = 
          new BufferedReader(new InputStreamReader(socket.getInputStream()));

        totalTime = 0;
        

        String split;

        startTime();
        out.println(this.cmd); 

        split = in.readLine().replaceAll("boobs","\n");
        System.out.println(split);

         endTime();
          this.totalTime += this.elaspedTime;
          //System.out.println(this.totalTime);
          socket.close();
        

      }catch(UnknownHostException e){
        e.printStackTrace();
      }catch(IOException e){
        e.printStackTrace();
      }catch(Exception e){
        e.printStackTrace();
      }
        
  }

  private void startTime(){
    this.startTime = System.currentTimeMillis();
  }

  private void endTime(){
    long current = System.currentTimeMillis();
    this.elaspedTime = current - this.startTime;
  }
}