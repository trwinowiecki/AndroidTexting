package com.example.taylor.desktoptext;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.taylor.R;
import com.example.taylor.crypto.RSA;
import com.example.taylor.crypto.SignatureUtils;
import com.example.taylor.utils.JsonUtil;
import com.example.taylor.utils.Preferences;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static MainActivity inst;
    TelephonyManager tm;
    String server, password, currPhoneNumber;
    ArrayList<String> smsMessagesList = new ArrayList<String>();
    ListView smsListView;
    ArrayAdapter arrayAdapter;
    WebSocketClient wss;
    final int GET_INFO = 1;
    Texts texts = new Texts();
    List<PhoneNumber> numbers = new ArrayList<>();
    int tempID;

    static {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public static MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        inst = this;
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.taylor.desktoptext/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Preferences.init(this);
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewMessage.class);
                startActivity(intent);
            }
        });

        smsListView = (ListView) findViewById(R.id.lvSMSList);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        smsListView.setAdapter(arrayAdapter);
        smsListView.setOnItemClickListener(this);
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        currPhoneNumber = tm.getLine1Number();
        Preferences.clear();
        if (!Preferences.contains(Preferences.RSA_PUBLIC_KEY)) {
            RSA.generate();
        }

        if (!Preferences.contains(Preferences.DEVICE_ID)) {
            Preferences.putString(Preferences.DEVICE_ID, android_id);
        }

        getInfo();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsCursor = contentResolver.query(Uri.parse("content://sms/"), null, null, null, null);
        int indexBody = smsCursor.getColumnIndex("body");
        int indexAddress = smsCursor.getColumnIndex("address");
        int indexProtocol = smsCursor.getColumnIndex("protocol");
        boolean found;
        if (indexBody < 0 || !smsCursor.moveToFirst()) return;

        numbers.clear();
        arrayAdapter.clear();

        do {
            String str;
            found = false;

            if (smsCursor.getString(indexProtocol) == null) {
                str = "Sent to: " + smsCursor.getString(indexAddress) +
                        "\n" + smsCursor.getString(indexBody) + "\n";
            } else {
                str = "SMS From: " + smsCursor.getString(indexAddress) +
                        "\n" + smsCursor.getString(indexBody) + "\n";
            }
            arrayAdapter.add(str);

            if (numbers.size() == 0) {
                PhoneNumber pn = new PhoneNumber(smsCursor.getString(indexAddress), 1);

                Texts.Person person = new Texts.Person("none", smsCursor.getString(indexAddress));
                Texts.Person.Message message = new Texts.Person
                        .Message((smsCursor.getString(indexProtocol) == null) ? "1" : "0",
                        "None", smsCursor.getString(indexBody));

                person.getMessagesList().add(message);
                texts.getPersonList().add(person);
            }

            for (PhoneNumber pn : numbers) {
                if (pn.getNumber() == smsCursor.getString(indexAddress)) {
                    if (pn.getCount() <= 500) {
                        pn.setCount(pn.getCount() + 1);

                        for (Texts.Person p : texts.getPersonList()) {
                            if (p.getNumber() == smsCursor.getString(indexAddress)) {
                                Texts.Person.Message message = new Texts.Person
                                        .Message((smsCursor.getString(indexProtocol) == null) ? "1" : "0",
                                        "None", smsCursor.getString(indexBody));

                                p.getMessagesList().add(message);
                                found = true;
                            }
                        }
                    }
                }
            }

            if (found == false) {
                PhoneNumber pn = new PhoneNumber(smsCursor.getString(indexAddress), 1);

                Texts.Person person = new Texts.Person("none", smsCursor.getString(indexAddress));
                Texts.Person.Message message = new Texts.Person
                        .Message((smsCursor.getString(indexProtocol) == null) ? "1" : "0",
                        "None", smsCursor.getString(indexBody));

                person.getMessagesList().add(message);
                texts.getPersonList().add(person);
            }
        } while (smsCursor.moveToNext());

        //sendTexts();
    }

    public void updateList(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }

    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        try {
            String[] smsMessages = smsMessagesList.get(pos).split("\n");
            String address = smsMessages[0];
            String smsMessage = "";
            for (int i = 1; i < smsMessages.length; ++i) {
                smsMessage += smsMessages[i];
            }

            String smsMessageStr = address + "\n";
            smsMessageStr += smsMessage;
            Toast.makeText(this, smsMessageStr, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.logOut) {
            wss.send("CLOSED");
            wss.close();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getInfo() {
        Intent intent = new Intent(MainActivity.this, WebSocketsLogin.class);
        startActivityForResult(intent, GET_INFO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (GET_INFO): {
                if (resultCode == Activity.RESULT_OK) {
                    String[] newText = data.getStringExtra("1").split("\n");
                    server = newText[0];
                    password = newText[1];

                    Preferences.remove(Preferences.CURRENT_SERVER);
                    Preferences.putString(Preferences.CURRENT_SERVER, server);
                    Preferences.setCurrentServer();

                    if (!Preferences.contains(Preferences.CURRENT_SERVER_CONNECTIONS)) {
                        Preferences.putInteger(Preferences.CURRENT_SERVER_CONNECTIONS, 0);
                        Preferences.putInteger(Preferences.CURRENT_SERVER_MESSAGES, 0);
                    }

                    connectWebSocket();
                }
                break;
            }
        }
    }

    private void connectWebSocket() {
        URI uri;

        try {
            uri = new URI("ws://" + Preferences.getString(Preferences.CURRENT_SERVER));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        wss = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
            }

            @Override
            public void onMessage(String s) {
                final String message = s;

                if (message == "testing") {
                    return;
                }

                /*if (Preferences.getInteger(Preferences.CURRENT_SERVER_MESSAGES) <= 1) {
                    int hold = Preferences.getInteger(Preferences.CURRENT_SERVER_MESSAGES);
                    Preferences.remove(Preferences.CURRENT_SERVER_MESSAGES);
                    Preferences.putInteger(Preferences.CURRENT_SERVER_MESSAGES, hold++);

                    try {
                        JsonUtil.received(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    wss.send(RSA.encryptWithSecretKey(JsonUtil.registerJSON(tempID, password)));
                } else {
                    int hold = Preferences.getInteger(Preferences.CURRENT_SERVER_MESSAGES);
                    Preferences.remove(Preferences.CURRENT_SERVER_MESSAGES);
                    Preferences.putInteger(Preferences.CURRENT_SERVER_MESSAGES, hold++);

                    try {
                        JsonUtil.received(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
                getInfo();
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };

        wss.connect();
        refreshSmsInbox();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                connected();
            }
        }, 3000);
    }

    private void connected() {
        Random r = new Random();

        if (Preferences.getInteger(Preferences.CURRENT_SERVER_CONNECTIONS) > 0) {
            int hold = Preferences.getInteger(Preferences.CURRENT_SERVER_CONNECTIONS);
            Preferences.remove(Preferences.CURRENT_SERVER_CONNECTIONS);
            Preferences.putInteger(Preferences.CURRENT_SERVER_CONNECTIONS, hold++);

            wss.send(SignatureUtils.genSignature(JsonUtil.authJSON(password)));
        } else {
            tempID = r.nextInt(999);
            Preferences.putInteger(Preferences.CURRENT_SERVER_CONNECTIONS, 1);

            wss.send(JsonUtil.initializeJSON(tempID, password).getBytes());
        }
    }

    private void sendTexts() {
        for (Texts.Person p : texts.getPersonList()) {
            wss.send(RSA.encryptWithStoredKey(JsonUtil.textsJSON(texts)));
        }
    }

    @Retention(value = RetentionPolicy.RUNTIME)
    @Target(value = ElementType.METHOD)
    public @interface OnMessage{}

    @OnMessage
    public void processMessage(String s) {
        wss.onMessage(s);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.taylor.desktoptext/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
