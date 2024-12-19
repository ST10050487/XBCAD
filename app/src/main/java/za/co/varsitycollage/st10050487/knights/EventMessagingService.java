package za.co.varsitycollage.st10050487.knights;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EventMessagingService extends Service {

    private static final String TAG = "EventMessagingService";
    private static final String FCM_API_URL = "https://fcm.googleapis.com/v1/projects/xbcad-ab0f9/messages:send";

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    public void sendMessage(String token, String title, String body) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }

            // Get new FCM registration token
            String userToken = task.getResult();

            if (userToken != null) {
                new SendMessageTask().execute(userToken, title, body);
            } else {
                Log.e(TAG, "User token is null, unable to send notification");
            }
        });
    }

    private String getBearerToken() throws IOException {
        // Path to your Service Account Key JSON file
        FileInputStream serviceAccount = new FileInputStream("path/to/service-account-key.json");

        // Generate Bearer Token
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount)
                .createScoped("https://www.googleapis.com/auth/firebase.messaging");
        credentials.refreshIfExpired();
        return credentials.getAccessToken().getTokenValue();
    }

    private class SendMessageTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String token = params[0];
            String title = params[1];
            String body = params[2];

            try {
                URL url = new URL(FCM_API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                // Set Authorization Header with Bearer Token
                String bearerToken = getBearerToken();
                conn.setRequestProperty("Authorization", "Bearer " + bearerToken);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Payload for the FCM request
                String message = "{"
                        + "\"message\":{"
                        + "\"token\":\"" + token + "\","
                        + "\"notification\":{"
                        + "\"title\":\"" + title + "\","
                        + "\"body\":\"" + body + "\""
                        + "}"
                        + "}"
                        + "}";

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = message.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                String responseMessage = conn.getResponseMessage();
                Log.d(TAG, "FCM Message Response Code: " + responseCode + ", Message: " + responseMessage);
                Log.d(TAG, "FCM Message URL: " + FCM_API_URL);
                Log.d(TAG, "FCM Message Token: " + token);
                Log.d(TAG, "FCM Message Title: " + title);
                Log.d(TAG, "FCM Message Body: " + body);

            } catch (Exception e) {
                Log.e(TAG, "Error sending FCM message", e);
            }
            return null;
        }
    }
}