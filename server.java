import java.net.*;
import java.io.*;
import java.lang.Process;


public class server{


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

    System.out.println("Successfully created a socket");

    // Socket client = server.accept();
    // PrintWriter out =
    //   new PrintWriter(client.getOutputStream(), true);
    // BufferedReader in = new BufferedReader(
    //   new InputStreamReader(client.getInputStream()));
  } catch (IOException e){
      System.out.println(e);
  }

  try{
      String inputLine;

      boolean online = true;
      




      while (online) {
        Socket client = server.accept();
        PrintWriter out =
            new PrintWriter(client.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(client.getInputStream()));


        while(!client.isClosed() && ((inputLine = in.readLine()) != null)){
          System.out.println("The client requested " + inputLine);


          if(inputLine.equals("Quit")){
            System.out.println("Quiting upon client request..");
            online = false;
            client.close();
            break;
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
          } catch (Exception e) {
            e.printStackTrace();
          }

        }
      }

      }

    }catch(IOException e){
        System.out.println("Error : " + e);
    }
    //   } catch(IOException e){
    //     System.out.println("Exception caught when trying to listen on port " +
    //     portNumber + " or listening fo a connection\n");
    //     System.out.println(e.getMessage());
    //   }



  }
}
