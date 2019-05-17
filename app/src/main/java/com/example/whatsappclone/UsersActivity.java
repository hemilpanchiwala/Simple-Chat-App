package com.example.whatsappclone;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class UsersActivity extends AppCompatActivity {

    ListView usersList;
    TextView noUsersList;
    ProgressDialog progressDialog;
    int totalUsers = 0;
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersList = (ListView) findViewById(R.id.usersList);
        noUsersList = (TextView) findViewById(R.id.noUsersFound);
        progressDialog = new ProgressDialog(UsersActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "https://whatsapp-clone-fddbd.firebaseio.com/users.json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                doOnSuccess(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UsersActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(UsersActivity.this);
        requestQueue.add(stringRequest);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = list.get(position);
                startActivity(new Intent(UsersActivity.this, ChatActivity.class));
            }
        });
    }

    public void doOnSuccess(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);
            Iterator i = jsonObject.keys();
            String key = "";

            while (i.hasNext()){

                key = i.next().toString();
                if (!key.equals(UserDetails.username)){
                    list.add(key);
                }

                totalUsers++;
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        if (totalUsers <= 1){
            noUsersList.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }else {
            noUsersList.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
        }

        progressDialog.dismiss();
    }
}
