package ro.pub.cs.systems.eim.colocviu2;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SpecialThread extends Thread
{
    private Socket socket = null;
    private TextView abilityTextView = null;

    public SpecialThread(TextView abilityTextView)
    {
        this.abilityTextView = abilityTextView;
    }

    public void run()
    {
        try
        {
            socket = new Socket(Constants.SERVER_ADDRESS, Constants.SERVER_PORT);

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            printWriter.println("SPECIAL");

            String result = "";
            while ((result = bufferedReader.readLine()) != null)
            {
                Log.d(Constants.LOG_TAG, "[CLIENT THREAD] Got from COMMUNICATION THREAD : " + result);
                final String finalizedResults = result;
                abilityTextView.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        abilityTextView.setText(finalizedResults);
                    }
                });
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
