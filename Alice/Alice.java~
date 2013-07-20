import java.net.*;
import java.io.*;
import javax.net.ssl.*;

public class Alice {

public static void main(String args[])throws Exception
    {

 // USAGE: java Alice host port pathtofile
// using JSSE java ssl extension api for calls 
//documentation: https://www.ibm.com/developerworks/java/tutorials/j-jsse/section5.html

       InetAddress  hostIA = InetAddress.getByName(args[0]);
       String  host = hostIA.getHostName();
       int port = Integer.parseInt(args[1]);
        File fs= new File(args[2].toString());
  if (args.length<2)
  {
       System.out.println("USAGE: java Alice host port pathtofile");
       System.exit(0);
  }

        

       try {

   

 		//set necessary keystore properties 
                // Alice Key Store is set up at Alice.jks , password psalice
            System.setProperty("javax.net.ssl.keyStore","Alice.jks");
            System.setProperty("javax.net.ssl.keyStorePassword","psalice");
            System.setProperty("javax.net.ssl.keyStoreType", "JKS");       
                        
            //set necessary truststore properties - using JKS
            // certificates in truststore are trusted and we trust only ca certificate
            // preprocessing of ceritificate generation is performed using openssl and keystore available in scripts.txt
            // generate keypair for ca, ALICe, BOB request to sign their public keys using commands provided by openssl library
            System.setProperty("javax.net.ssl.trustStore","truststore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword","catrust");
            // register a https protocol handler  - this may be required for previous JDK versions
            System.setProperty("java.protocol.handler.pkgs","com.sun.net.ssl.internal.www.protocol");
            

           System.out.println("connecting...");
           SSLSocketFactory sslFact =
               (SSLSocketFactory)SSLSocketFactory.getDefault();
           SSLSocket c = (SSLSocket)sslFact.createSocket(host, port);

           System.out.println("Handshaking...");
           c.startHandshake();
         
             System.out.println("Sending File...");
          
 	    FileSender.SendFile(c, fs);
	    c.close();

          System.out.println("done...");
 
        }   catch (IOException e) {
System.out.println("Alice died: " + e.getMessage());
	    e.printStackTrace();
                    }
}
}
