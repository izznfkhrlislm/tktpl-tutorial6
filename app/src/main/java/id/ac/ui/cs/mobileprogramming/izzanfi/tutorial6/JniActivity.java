package id.ac.ui.cs.mobileprogramming.izzanfi.tutorial6;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class JniActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);

        final TextView nameTextView = findViewById(R.id.output_name);
        final EditText nameEditText = findViewById(R.id.editTextName);
        Button submitBtn = findViewById(R.id.enterTextBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getName = nameEditText.getText().toString();
                String res = showNameFromEditText(getName);
                nameTextView.setText("Hi, " + res + "!");

                Toast.makeText(JniActivity.this, "name changed!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public native String stringFromJNI();
    public native String showNameFromEditText(String name);
}
