package vn.com.it.truongpham.appnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {
    EditText edTitle, edMessage;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=AIzaSyDbOMyDpZAEn_gZQ5hsXASQMho7E-JRUo8";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    private  final String SUBSCRIBE_TO = "userABC";
    ListView listView;
    List<User> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(!task.isSuccessful()){
                    Log.d(TAG, "getInstanceId failed", task.getException());
                    return;
                }
                String android_id = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                String token = task.getResult().getToken();
                FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("User").child(android_id);
                Map<String,String> map=new HashMap<>();
                map.put("token",token);
                map.put("device_id",android_id);
                databaseReference.setValue(map);
            }
        });
        setContentView(R.layout.activity_notification);
        edTitle=findViewById(R.id.edTitle);
        edMessage=findViewById(R.id.edMessage);
        listView=findViewById(R.id.listView);
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list=new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                   User user=snapshot.getValue(User.class);
                   list.add(new User(user.token,user.device_id));
                }

                ArrayAdapter<User> adapter=new ArrayAdapter<User>(NotificationActivity.this,android.R.layout.simple_list_item_1,list);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 pushUser(list.get(i).token);
            }
        });

    }

    private void pushUser(String token){
        NOTIFICATION_TITLE = edTitle.getText().toString();
        NOTIFICATION_MESSAGE = edMessage.getText().toString();

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", token);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendNotification(notification);
    }

    public void onClick(View view) {
        TOPIC = "/topics/userABC";
        NOTIFICATION_TITLE = edTitle.getText().toString();
        NOTIFICATION_MESSAGE = edMessage.getText().toString();

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendNotification(notification);
    }
    private void sendNotification(JSONObject notification) {

        JsonObjectRequest objectRequest=new JsonObjectRequest(FCM_API, notification, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(NotificationActivity.this, "Notification sent", Toast.LENGTH_LONG).show();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NotificationActivity.this, "Notification sent error", Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(objectRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EventBus.getDefault().register(this);
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void getData(RemoteMessage remoteMessage) {
        Log.d("PPPP",remoteMessage.getData().get("title"));
        EventBus.getDefault().removeStickyEvent(remoteMessage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
