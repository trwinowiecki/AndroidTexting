package com.example.taylor.desktoptext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.taylor.R;

import java.security.Key;

public class WebSocketsLogin extends AppCompatActivity {

    public String server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_sockets_login2);

        final EditText etIP1 = (EditText) findViewById(R.id.etIP1);
        final EditText etIP2 = (EditText) findViewById(R.id.etIP2);
        final EditText etIP3 = (EditText) findViewById(R.id.etIP3);
        final EditText etIP4 = (EditText) findViewById(R.id.etIP4);
        final EditText etPort = (EditText) findViewById(R.id.etPort);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        etIP1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int val;

                try {
                    val = Integer.parseInt(etIP1.getText().toString());
                } catch (Exception e) {
                    val = 256;
                }

                if (val >= 0 && val <= 255) {
                    if (val == 0)
                        etIP2.requestFocus();
                    else if (etIP1.length() >= 3)
                        etIP2.requestFocus();
                } else {
                    Toast.makeText(WebSocketsLogin.this, "Please enter a number between 0 and 255", Toast.LENGTH_SHORT).show();
                    etIP1.requestFocus();
                }
            }
        });
        etIP2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int val;

                try {
                    val = Integer.parseInt(etIP2.getText().toString());
                } catch (Exception e) {
                    val = 256;
                }

                if (val >= 0 && val <= 255) {
                    if (val == 0)
                        etIP3.requestFocus();
                    else if (etIP2.length() >= 3)
                        etIP3.requestFocus();
                } else {
                    Toast.makeText(WebSocketsLogin.this, "Please enter a number between 0 and 255", Toast.LENGTH_SHORT).show();
                    etIP2.requestFocus();
                }
            }
        });
        etIP3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int val;

                try {
                    val = Integer.parseInt(etIP3.getText().toString());
                } catch (Exception e) {
                    val = 256;
                }

                if (val >= 0 && val <= 255) {
                    if (val == 0)
                        etIP4.requestFocus();
                    else if (etIP3.length() >= 3)
                        etIP4.requestFocus();
                } else {
                    Toast.makeText(WebSocketsLogin.this, "Please enter a number between 0 and 255", Toast.LENGTH_SHORT).show();
                    etIP3.requestFocus();
                }
            }
        });
        etIP4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int val;

                try {
                    val = Integer.parseInt(etIP4.getText().toString());
                } catch (Exception e) {
                    val = 256;
                }

                if (val >= 0 && val <= 255) {
                    if (val == 0)
                        etPort.requestFocus();
                    else if (etIP4.length() >= 3)
                        etPort.requestFocus();
                } else {
                    Toast.makeText(WebSocketsLogin.this, "Please enter a number between 0 and 255", Toast.LENGTH_SHORT).show();
                    etIP4.requestFocus();
                }
            }
        });

        Button btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] text = {etIP1.getText().toString(), etIP2.getText().toString(),
                        etIP3.getText().toString(), etIP4.getText().toString(),
                        etPort.getText().toString(), etPassword.getText().toString()};

                if (!(text[0].equals("") || text[1].equals("") || text[2].equals("") ||
                        text[3].equals("") || text[4].equals("") || text[5].equals(""))) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("1", text[0] + "." + text[1] + "." + text[2] + "." +
                            text[3] + ":" + text[4] + "\n" + text[5]);

                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "Enter login information", Toast.LENGTH_SHORT).show();
            }
        });
    }
}