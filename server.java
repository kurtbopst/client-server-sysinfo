import java.net.*;
import java.io.*;
import java.lang.Process;


public class server{


  static boolean ONLINE = true;
  public static void main(String[] args) throws IOException{

    ServerSocket server = null;

  if (args.length != 1){
    System.err.println("Usage: java EchoServer <port number>");
    System.exit(1);
  }
  int portNumber = Integer.parseInt(args[0]); //6969

  System.out.println("Server is listening on port " + portNumber);

  try { //Listen and connect to a client on a portnumber
    server = new ServerSocket(portNumber, 100);

  } catch (IOException e){
      System.out.println(e);
  }

  try{
      while (ONLINE) {
        Socket client = server.accept();

        System.out.println("A new client entered the pain train");

        new ServerThread(client).start();
      }

    }catch(IOException e){
        System.out.println("Error : " + e);
    }
  }

}


class ServerThread extends Thread {

  private Socket socket;

  public ServerThread(Socket socket){
    this.socket = socket;
  }

  public void run() {

    try{
    String inputLine;
    boolean online = true;

    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    BufferedReader in = new BufferedReader(
      new InputStreamReader(socket.getInputStream()));

    inputLine = in.readLine();
    

      System.out.println("A client requested " + inputLine);

      if(inputLine.equals("Quit")){
        System.out.println("Quiting upon client request..");
        online = false;
        socket.close();
      } else {
        try {
            String line = "";
            String value = "";
            Process p = Runtime.getRuntime().exec(inputLine);
            BufferedReader input =
              new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
               value += line + "boobs";
          }
          out.println(value);

          input.close();
        }catch (Exception e) {
         e.printStackTrace();
        }

      }
    
   
  }catch (Exception e) {
    e.printStackTrace();
  }

  } 

}
