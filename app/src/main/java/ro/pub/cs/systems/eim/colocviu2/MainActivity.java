package ro.pub.cs.systems.eim.colocviu2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
{
    private ServerThread serverThread = null;
    private EditText pokemonEditText = null;
    private Button getPokemonButton = null;
    private TextView abilityTextView = null;
    private TextView typeTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pokemonEditText = findViewById(R.id.pokemonName);
        getPokemonButton = findViewById(R.id.getPokemonButton);
        abilityTextView = findViewById(R.id.abilityTextView);
        typeTextView = findViewById(R.id.typeTextView);

        serverThread = new ServerThread();
        serverThread.start();

        getPokemonButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ClientThread clientThread = new ClientThread(pokemonEditText.getText().toString(), abilityTextView, typeTextView);
                clientThread.start();
            }
        });
    }
}