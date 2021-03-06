package com.example.noka.a42translate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by noka on 4/18/17.
 */

public class LogInAdmin extends AppCompatActivity implements View.OnClickListener {

    private static final String LOGIN_URL = "http://192.168.1.111/42translate/loginAdmin.php";

    public static final String KEY_USERNAME = "name";
    public static final String KEY_PASSWORD = "password";

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    DatabaseHelper dbhelper;


    private String name;
    private String password;
    public String session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);


        buttonLogin.setOnClickListener(this);
        buttonRegister.setVisibility(View.INVISIBLE);
        dbhelper = new DatabaseHelper(this);
    }


    private void userLogin() {
        name = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {
                            //session = name;
                           dbhelper.insertUser_login(password,name);
                            editTextPassword.setText("");
                            editTextUsername.setText("");
                           // Toast.makeText(LoginActivity.this, session, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), Admin.class);
                            //intent.putExtra("logged_as", session);
                            //intent.putExtra("name", name);

                            startActivity(intent);
                            if(response.trim().equals("failure")){
                                editTextPassword.setText("");
                                editTextUsername.setText("");
                                Toast.makeText(LogInAdmin.this, "You are not an admin!", Toast.LENGTH_LONG).show();


                            }
                            //startActivity(new Intent(this, MainActivity.class))
                        } else {
                            Toast.makeText(LogInAdmin.this, response, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LogInAdmin.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_USERNAME, name);
                map.put(KEY_PASSWORD, password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    @Override
    public void onClick(View v) {
        if (v == buttonLogin) {
            userLogin();
        }

    }
}