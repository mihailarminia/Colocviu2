package ro.pub.cs.systems.eim.colocviu2;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread
{
    private ServerSocket serverSocket = null;
    private boolean isRunning = false;

    public ServerThread()
    {
        try
        {
            serverSocket = new ServerSocket(Constants.SERVER_PORT);
            isRunning = true;
            Log.d(Constants.LOG_TAG, "[SERVER THREAD] SERVER LISTENING ON PORT : " + Constants.SERVER_PORT);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        while(isRunning)
        {
            try
            {
                Socket clientSocket = serverSocket.accept();
                Log.d(Constants.LOG_TAG, "[SERVER THREAD] New connection");

                CommunicationThread communicationThread = new CommunicationThread(clientSocket);
                communicationThread.start();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
