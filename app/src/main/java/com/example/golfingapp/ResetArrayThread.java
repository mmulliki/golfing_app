package com.example.golfingapp;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class ResetArrayThread extends HandlerThread {
    private Handler requestHandler;
    private final Handler responseHandler;
    private final int RESET_ARRAY = 1;
    private ArrayList<Integer> arrayList;
    private MutableLiveData<ArrayList<Integer>> roundScores = new MutableLiveData<>();

    public ResetArrayThread(ArrayList<Integer> arrayList,
                            MutableLiveData<ArrayList<Integer>> roundScores,
                            Handler responseHandler) {
        super("ResetArrayThread");
        this.responseHandler = responseHandler;
        this.arrayList = arrayList;
        this.roundScores = roundScores;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        requestHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == RESET_ARRAY) {
                    handleResponse();
                }
            }
        };
    }

    public void queueResetHT() {
        requestHandler.obtainMessage(RESET_ARRAY).sendToTarget();
    }

    public Handler getRequestHandler() {
        return requestHandler;
    }

    public void handleResponse() {
        responseHandler.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 21; i++) {
                    Log.d("TestHT", "Entered handlerResponse");
                    arrayList.add(0);
                }
                roundScores.setValue(arrayList);
                Log.d("TestHT", "ArrayList count: " + arrayList.size());
            }
        });
    }
}
