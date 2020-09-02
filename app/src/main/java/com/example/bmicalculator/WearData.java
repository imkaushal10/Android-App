package com.example.bmicalculator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


public class WearData extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    GoogleApiClient googleApiClient = null;
    public static final String TAG = "MyDataMap.....";
    public static final String WEARABLE_DATA_PATH = "/wearable/data/path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_data);

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(Wearable.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        googleApiClient = builder.build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if(googleApiClient!=null && googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }

        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        sendMessage();
    }

    public void sendMessage(){
        if(googleApiClient.isConnected()){
            String message = ((TextView) findViewById(R.id.text)).getText().toString();
            if(message== null || message.equalsIgnoreCase("")){
                message = "Hello World";
            }
            new SendMessageToDataLayer(WEARABLE_DATA_PATH, message).start();
        }
        else{
            Toast.makeText(this, "API not connected", Toast.LENGTH_LONG).show();

        }
    }


    public void sendMessageOnClick(View view){
        sendMessage();
    }

    public class SendMessageToDataLayer extends Thread{
        String path;
        String message;

        public SendMessageToDataLayer(String path,String message){
            this.path = path;
            this.message = message;
        }

        @Override
        public void run() {
            NodeApi.GetConnectedNodesResult nodesList = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
            for(Node node: nodesList.getNodes()){
                MessageApi.SendMessageResult messageResult = Wearable.MessageApi.sendMessage(googleApiClient,node.getId(),path,message.getBytes()).await();
                if(messageResult.getStatus().isSuccess()){
                    //print success log
                    Log.v(TAG,"Message: successfully sent to "+node.getDisplayName());
                    Log.v(TAG,"Message: Node Id is "+node.getId());
                    Log.v(TAG,"Message: Node size is "+nodesList.getNodes().size());
                }else{
                    //print failure log
                    Log.v(TAG,"Error while sending Message");
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}