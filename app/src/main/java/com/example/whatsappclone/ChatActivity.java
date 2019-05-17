package com.example.whatsappclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    SimpleDateFormat simpleDateFormat;
    Firebase firebase1, firebase2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        simpleDateFormat = new SimpleDateFormat("EEE, MMM d 'AT' HH:mm a");
        linearLayout = (LinearLayout) findViewById(R.id.layout2);
//        relativeLayout = (RelativeLayout) findViewById(R.id.layout1);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        messageArea = (EditText) findViewById(R.id.messageArea);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        scrollView.fullScroll(View.FOCUS_DOWN);

        Firebase.setAndroidContext(this);

        firebase1 = new Firebase("https://whatsapp-clone-fddbd.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        firebase2 = new Firebase("https://whatsapp-clone-fddbd.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                if (!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    String currentDateandTime = simpleDateFormat.format(new Date());
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    map.put("time", currentDateandTime);

                    firebase1.push().setValue(map);
                    firebase2.push().setValue(map);

                    messageArea.setText("");


                }
            }
        });

        firebase1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String username = map.get("user").toString();
                String time = map.get("time").toString();

                if (username.equals(UserDetails.username)){
                    addMessageBox("You ", message, time, 1);
                }else {
                    addMessageBox(UserDetails.chatWith, message, time, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String name, String message, String time, int type){

        TextView textmsg = new TextView(ChatActivity.this);
        TextView textname = new TextView(ChatActivity.this);
        TextView texttime = new TextView(ChatActivity.this);

        textname.setText(name);
        textname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        textmsg.setText(message);
        texttime.setText(time);
        texttime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);



        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        lp2.weight = 1.0f;
        if(type == 1) {
            lp1.gravity = Gravity.RIGHT;
            lp2.gravity = Gravity.RIGHT;
            lp3.gravity = Gravity.RIGHT;
            textmsg.setBackgroundResource(R.drawable.text_in);
        }
        else{
            lp1.gravity = Gravity.LEFT;
            lp2.gravity = Gravity.LEFT;
            lp3.gravity = Gravity.LEFT;
            textmsg.setBackgroundResource(R.drawable.text_out);
        }

        textname.setLayoutParams(lp1);
        textmsg.setLayoutParams(lp2);
        texttime.setLayoutParams(lp3);
        linearLayout.addView(textname);
        linearLayout.addView(textmsg);
        linearLayout.addView(texttime);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

 }

