import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import javax.net.*;
import javax.net.ssl.*;

public class Alice {

private KeyStore AliceKeyStore;
  
  /**
   * KeyStore for storing the CA's public key
   */
  private KeyStore CAStore;
static private SecureRandom secureRandom;
private SSLContext sslContext;


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
            System.setProperty("javax.net.ssl.trustStorePassword","catrust");   //password for truststore which has ca certificate
            // register a https protocol handler  - this may be required for previous JDK versions
            System.setProperty("java.protocol.handler.pkgs","com.sun.net.ssl.internal.www.protocol");
            

           System.out.println("connecting...");



       Alice nalice= new Alice();

      SSLSocket c = nalice.connect( host, port );
     


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



  private void setupAliceKeyStore() throws GeneralSecurityException, IOException {
    AliceKeyStore = KeyStore.getInstance( "JKS" );
    AliceKeyStore.load( new FileInputStream( "Alice.jks" ),
                       "psalice".toCharArray());
  }

 private void setupTruststore() throws GeneralSecurityException, IOException {
    CAStore = KeyStore.getInstance( "JKS" );
    CAStore.load( new FileInputStream( "truststore.jks" ), 
                        "catrust".toCharArray());


  }


 private void setupSSLContext() throws GeneralSecurityException, IOException {
    TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
    tmf.init( CAStore );

    KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
    kmf.init( AliceKeyStore, "psalice".toCharArray() );

    sslContext = SSLContext.getInstance( "TLS" );
    sslContext.init( kmf.getKeyManagers(),
                     tmf.getTrustManagers(),
                     secureRandom );
  }


private SSLSocket connect( String host, int port ) {
	    try {
	      setupTruststore();
	      setupAliceKeyStore();
	      setupSSLContext();

	      SSLSocketFactory sf = sslContext.getSocketFactory();
	      SSLSocket socket = (SSLSocket)sf.createSocket( host, port );
	       return socket;
	    }
	    catch( GeneralSecurityException e ) {
	        e.printStackTrace();
	      } catch( IOException e ) {
	        e.printStackTrace();
	      }
	    return null;
	    
	    
 }


}



