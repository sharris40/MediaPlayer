package edu.uco.map2016.mediaplayer.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;

public class HttpJsonUtility {
    private static final String LOG_TAG = "HttpJsonUtility";

    private HttpURLConnection mConnection;
    private JSONObject mJson;
    private Context mContext;

    public HttpJsonUtility(@NonNull Context context, @NonNull HttpURLConnection connection) {
        mConnection = connection;
        mContext = context;
    }

    private JSONObject parseResponse(InputStream input, String charset, boolean isValid)
            throws IOException {
        if (isValid) {
            if (charset == null || charset.isEmpty())
                charset = "utf-8";
            Reader reader = new InputStreamReader(input, charset);
            char[] buffer = new char[4096];
            int count = reader.read(buffer);
            StringBuilder builder = new StringBuilder();
            while (count > 0) {
                builder.append(buffer, 0, count);
                count = reader.read(buffer);
            }
            try {
                return new JSONObject(builder.toString());
            } catch (JSONException jex) {
                return null;
            }
        } else {
            return null;
        }
    }

    private boolean checkMimeType(String contentType) {
        String value = contentType.toLowerCase();
        if (!value.startsWith("application/json"))
            return false;
        for (int i = 16; i < value.length(); ++i) {
            char current = value.charAt(i);
            if (current == ';')
                return true;
            if (current != ' ' && current != '\t')
                return false;
        }
        return true;
    }

    private String getCharset(String contentType) {
        String[] parts = contentType.toLowerCase().split(";");
        for (int i = 1; i < parts.length; ++i) {
            String charsetPart = null;
            if (parts[i].startsWith("charset")) {
                charsetPart = parts[i];
            } else {
                for (int c = 0; c < parts[i].length(); ++c) {
                    char current = parts[i].charAt(c);
                    if (current != ' ' && current != '\t') {
                        charsetPart = parts[i].substring(c);
                        if (!charsetPart.startsWith("charset"))
                            charsetPart = null;
                        break;
                    }
                }
            }
            if (charsetPart != null && charsetPart.charAt(7) == '=') {
                String charset = charsetPart.substring(8);
                if (charset.charAt(0) == '"') {
                    if (charset.charAt(charset.length() - 1) == '"') {
                        return charset.substring(1, charset.length() - 1);
                    }
                } else {
                    return charset;
                }
            }
        }
        return null;
    }

    public int connect() {
        InputStream input = null;
        try {
            mConnection.connect();
            input = mConnection.getInputStream();
            String contentType = mConnection.getContentType();
            mJson = parseResponse(input, getCharset(contentType),
                    checkMimeType(contentType));
            return mConnection.getResponseCode();
        } catch (Exception ex) {
            return 0;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException iex) {
                    Log.w(LOG_TAG, Log.getStackTraceString(iex));
                }
            }
        }

    }

    public JSONObject getJson() {
        return mJson;
    }
}
