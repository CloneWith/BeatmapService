package com.edlplan.beatmapservice;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Util {

    public static byte[] readFullByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        byte[] buf = new byte[256];
        int l;
        while ((l = in.read(buf)) != -1) {
            o.write(buf, 0, l);
        }
        return o.toByteArray();
    }

    public static String readFullString(InputStream in) throws IOException {
        return new String(readFullByteArray(in), "UTF-8");
    }

    public static void toast(Activity activity, String txt) {
        activity.runOnUiThread(() -> Toast.makeText(activity, txt, Toast.LENGTH_SHORT).show());
    }

    public static void asynCall(Runnable runnable) {
        (new Thread(runnable)).start();
    }

    public static void asynLoadString(String urls, RunnableWithParam<String> onLoad, @Nullable RunnableWithParam<Throwable> onErr) {
        asynCall(() -> {
            try {
                URL url = new URL(urls);
                onLoad.run(readFullString(url.openConnection().getInputStream()));
            } catch (Exception e) {
                if (onErr != null) {
                    onErr.run(e);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void checkFile(File file) throws IOException {
        File p = file.getParentFile();
        if (!p.exists()) {
            p.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public interface RunnableWithParam<T> {
        void run(T t);
    }
}
