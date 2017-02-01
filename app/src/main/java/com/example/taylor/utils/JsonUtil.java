package com.example.taylor.utils;

import com.example.taylor.crypto.Crypto;
import com.example.taylor.crypto.RSA;
import com.example.taylor.desktoptext.Texts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;
import org.spongycastle.operator.GenericKey;
import org.spongycastle.operator.OperatorException;
import org.spongycastle.operator.SymmetricKeyUnwrapper;

import java.security.PublicKey;

public class JsonUtil {
    final static String TAG_CMD = "cmd";
    final static String TAG_TEMP_ID = "tempId";
    final static String TAG_ID = "id";
    final static String TAG_TYPE = "type";
    final static String TAG_DEV_ID = "devid";
    final static String DEVICE_ID = Preferences.getString(Preferences.DEVICE_ID);

    public static String textsJSON(Texts texts) {
        final String TAG_DATA = "data";
        final String TAG_NAME = "name";
        final String TAG_NUMBER = "number";
        final String TAG_DATE = "date";
        final String TAG_MESSAGES = "messages";
        final String TAG_MESSAGE = "message";
        final String TAG_SENT = "sent";

        try {
            JSONObject jsonToSend = new JSONObject();
            JSONArray jsonTexts = new JSONArray();
            JSONObject jsonPerson = new JSONObject();

            for (Texts.Person p : texts.getPersonList()) {
                jsonPerson.put(TAG_NAME, p.getName());
                jsonPerson.put(TAG_NUMBER, p.getNumber());

                JSONArray jsonMessages = new JSONArray();

                for (Texts.Person.Message m : p.getMessagesList()) {
                    JSONObject jsonMessage = new JSONObject();
                    jsonMessage.put(TAG_DATE, m.getDate());
                    jsonMessage.put(TAG_SENT, m.getSent());
                    jsonMessage.put(TAG_MESSAGE, m.getMessage());

                    jsonMessages.put(jsonMessage);
                }

                jsonPerson.put(TAG_MESSAGES, jsonMessages);
                jsonTexts.put(jsonPerson);
            }

            jsonToSend.put(TAG_CMD, "sendMsg");
            jsonToSend.put(TAG_DATA, jsonTexts);

            return jsonTexts.toString();
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String registerJSON(int tempID, String password) {
        JSONObject jsonRegister = new JSONObject();

        try {
            jsonRegister.put(TAG_CMD, "register");
            jsonRegister.put(TAG_ID, password);
            jsonRegister.put(TAG_TYPE, "phone");
            jsonRegister.put(TAG_DEV_ID, DEVICE_ID);
            jsonRegister.put(TAG_TEMP_ID, tempID);

            return jsonRegister.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String initializeJSON(int tempID, String password) {
        final String TAG_PUBLIC_KEY = "publicKey";
        String publicKey = Preferences.getString(Preferences.RSA_PUBLIC_KEY);

        JSONObject jsonInitialize = new JSONObject();

        try {
            jsonInitialize.put(TAG_CMD, "hello");
            jsonInitialize.put(TAG_TEMP_ID, tempID);
            jsonInitialize.put(TAG_PUBLIC_KEY, publicKey);
String test = jsonInitialize.toString();
            return jsonInitialize.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String authJSON(String password) {
        JSONObject jsonAuth = new JSONObject();

        try {
            jsonAuth.put(TAG_CMD, "authenticate");
            jsonAuth.put(TAG_ID, password);
            jsonAuth.put(TAG_DEV_ID, DEVICE_ID);
            jsonAuth.put(TAG_TYPE, "phone");

            return jsonAuth.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String contactsJSON(String password) {
        return null;
    }

    public static void received(String s) {
        String decoded;
        if (Preferences.contains(Preferences.SYMMETRIC_KEY)) {
            try {
                decoded = RSA.decryptWithSecretKey(s);

                JSONObject receivedJSON = new JSONObject(s);

                switch (receivedJSON.getString("cmd")) {
                    case ("serverPubKey"):
                        serverPubKeyJSON(receivedJSON);
                        break;
                    case ("confirm"):
                        break;
                    case ("welcome"):
                        welcomeJSON(receivedJSON);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                JSONObject receivedJSON = new JSONObject(s);

                switch (receivedJSON.getString("cmd")) {
                    case ("serverPubKey"):
                        serverPubKeyJSON(receivedJSON);
                        break;
                    case ("confirm"):
                        break;
                    case ("welcome"):
                        welcomeJSON(receivedJSON);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void serverPubKeyJSON(JSONObject receivedJSON) {
        try {
            PublicKey pubKey = Crypto.getRSAPublicKeyFromString(receivedJSON.getString("publicKey"));
            String symKey = receivedJSON.getString("secret");

            Crypto.writePublicKeyToPreferences(Preferences.RSA_SERVER_PUBLIC_KEY, pubKey.getEncoded());
            Preferences.putString(Preferences.SYMMETRIC_KEY, symKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void welcomeJSON(JSONObject receivedJSON) {
        try {
            String symKey = receivedJSON.getString("secret");
            
            Preferences.putString(Preferences.SYMMETRIC_KEY, symKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
