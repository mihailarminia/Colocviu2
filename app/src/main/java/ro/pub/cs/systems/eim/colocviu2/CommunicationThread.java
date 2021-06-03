package ro.pub.cs.systems.eim.colocviu2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.zip.CheckedOutputStream;

public class CommunicationThread extends Thread
{
    private Socket socket = null;

    public CommunicationThread(Socket socket)
    {
        this.socket = socket;
    }

    public void run()
    {
        try
        {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            String pokemonName = bufferedReader.readLine();
            Log.d(Constants.LOG_TAG, "[COMMUNICATION THREAD] Got : " + pokemonName);

            String stringUrl = Constants.HTTP_SERVER_API + pokemonName + "/";
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            BufferedReader httpBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = httpBufferedReader.readLine()) != null)
            {
                buffer.append(line);
            }

            Log.d(Constants.LOG_TAG, "[COMMUNICATION THREAD] Got from REMOTE API : " + buffer);
            try
            {
                JSONObject jsonResponse = new JSONObject(buffer.toString());
                JSONArray abilitiesArray = jsonResponse.getJSONArray(Constants.JSON_ABILITIES_TAG);
                JSONArray typesArray = jsonResponse.getJSONArray(Constants.JSON_TYPES_TAG);

                StringBuilder result = new StringBuilder();
                for (int i = 0; i < abilitiesArray.length(); i++)
                {
                    JSONObject currentAbility = abilitiesArray.getJSONObject(i);
                    String abilityName = currentAbility.getJSONObject(Constants.JSON_ABILITY_TAG).getString(Constants.JSON_ABILITY_NAME_TAG);
                    result.append(abilityName).append(", ");
                }

                result.append(Constants.SEPARATOR);

                for (int i = 0; i < typesArray.length(); i++)
                {
                    JSONObject currentType = typesArray.getJSONObject(i);
                    String typeName = currentType.getJSONObject(Constants.JSON_TYPE_TAG).getString(Constants.JSON_TYPE_NAME_TAG);
                    result.append(typeName).append(", ");
                }

                printWriter.println(result.toString());
                printWriter.flush();
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
