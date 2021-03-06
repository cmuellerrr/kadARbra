package edu.cmu.hcii.peer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.json.JSONException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * Service starts a socket and input stream so it can receive messages from a paired Android device.
 * 
 * @author Gordon
 * 
 */
public class ConnectionService extends Service {
	
	// Connection info
	public String ip;
	private static int port = 5550;
	
	// Socket stuff
	Socket socket;
	private ServerSocket server = null;
	private Thread       receiveThread = null;
	private DataInputStream  streamIn  =  null;
	private Runnable receiveRunnable = null;
		
	// Connection status
		// 0 = receive socket not started
		// 1 = receive socket waiting for connection
		// 2 = receive socket is connected
	private int status = 0; 
	
	// Binder for activities to access functions
	private final IBinder myBinder = new LocalBinder();
	
	// A string used for debugging purposes
	private static String TAG = "ConnectionService";

	// Used as spam detection to detect dc's on client side
	private long lastTime;
	private int spamCount;	
	
	ConnectionService mConnectionService;
	private DataUpdateReceiver dataUpdateReceiver;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return myBinder;
	}
	
	// These functions can be accessed by activities when the service is bound
    public class LocalBinder extends Binder {
        public ConnectionService getService() {
            return ConnectionService.this;
        }
        public void startServer(){
        	ConnectionService.this.startServer();
        
        }
        public void stop(){
        	ConnectionService.this.stop();
        }
        public int getConnectionStatus(){
        	return ConnectionService.this.getConnectionStatus();
        }
    }
    
    // Returns status of connection
    	// 0 = receive socket not started
    	// 1 = receive socket waiting for connection
    	// 2 = receive socket is connected
    public int getConnectionStatus(){
    	return status;
    }
    
    // Called to start the receive socket
    public void startServer(){
        Log.v(TAG, "startServer");
        
    	try {
    		status = 0;
    		if (socket!= null) socket.close();
	    	if (streamIn != null) streamIn.close();
	    	if (server != null) server.close();
	    	
	    } catch (IOException e) {
			e.printStackTrace();
	    }
    	
        try {
			server = new ServerSocket();
        	server.setReuseAddress(true);
        	server.bind(new InetSocketAddress(port));
		} catch (IOException e) {
			e.printStackTrace();
		}  
    	try {
        	socket = new Socket();
			socket.setReuseAddress(true);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	receiveRunnable = new receiveSocket();
    	receiveThread = new Thread(receiveRunnable);
    	receiveThread.start();
    }
    
    // Called on creation of service
    	// Creates the server socket
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
        mConnectionService = this;
        /*
        try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        socket = new Socket();
        */
        
		if (dataUpdateReceiver == null)
			dataUpdateReceiver = new DataUpdateReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("PEER_speech");
		registerReceiver(dataUpdateReceiver, intentFilter);
    }

    // Called on start of service
    @Override
    public void onStart(Intent intent, int startId){
        super.onStart(intent, startId);
    }
    
    // Called when service is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        stop();
        socket = null;
        
		if (dataUpdateReceiver != null)
			unregisterReceiver(dataUpdateReceiver);
    }
    
    // receiveSocket thread handles the input stream
    class receiveSocket implements Runnable {
		@Override
		public void run() {
			try{  
				System.out.println("Waiting for a client ..."); 
	            Log.v("waiting","waiting");
	            status = 1;
	         	socket = server.accept();
	            System.out.println("Client accepted: " + socket);
	            Log.v("waiting to open","open");

	            open();
	            Log.v("waiting to open","opened");
	            boolean done = false;
	            while (!done)
	            {  try
	               {
	            	  status = 2;
		              //Log.v("waiting  for msg",""+spamCount);
		              checkDisconnectSpam();
		              
	            	  String line = streamIn.readLine();
	            	  if (line!=null){ 
	            		  //Log.v(TAG,line);
	            		  MessageHandler.parseMsg(ConnectionService.this, line);
	                      done = line.equals(".bye");
	            	  }
	               }
	               catch(IOException ioe)
	               {  
	            	   done = true;
	            	   status = 0;
	               }
	            }
	            close();
	         }
	         catch(IOException ie)
	         {  
	        	 status = 0;
	        	 System.out.println("Acceptance Error: " + ie);  
	         }
	      }		
	}

	// Closes the socket if a ton of messages are sent
		// this happens when the client unexpectedly closes their end of the socket
	public void checkDisconnectSpam(){
		spamCount++;
		long curTime = System.currentTimeMillis();
		if (curTime - lastTime > 1000){
			if (spamCount > 10000){
				stop();
			}
			spamCount=0;
			lastTime = System.currentTimeMillis();
		}
	}
	
	// opens input stream
	public void open() throws IOException
	{  
		streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
	}
	
	// closes socket and input stream
	public void close() throws IOException
	{ 
		status = 0;
		if (socket != null)    socket.close();
	    if (streamIn != null)  streamIn.close();
	    if (server != null) server.close();
	}

    // Forces the socket to disconnect
    public void stop(){
    	status = 0;

	    try {
	    	if (socket!= null) socket.close();
	    	if (streamIn != null) streamIn.close();
		    if (server != null) server.close();
	    } catch (IOException e) {
			e.printStackTrace();
	    }
    }    
    
	//sends a broadcast message to be read by other classes
	private void sendBroadcastMsg(String msg){
        Intent intent = new Intent("command");
        intent.putExtra("msg", msg);
        sendBroadcast(intent);
	}
	
	/**
	 * Listens to broadcast messages
	 */
	private class DataUpdateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//Log.v(TAG, "on receive");
			Bundle b = intent.getExtras();
						
			if (intent.getAction().equals("PEER_speech")){
				MessageHandler.parseMsg(ConnectionService.this, b.getString("msg"));
			}
		}
	}
}