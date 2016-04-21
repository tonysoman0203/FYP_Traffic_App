package com.example.tonyso.TrafficApp.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.tonyso.TrafficApp.R;

import java.util.HashMap;

/**
 * Created by TonySo on 5/10/2015.
 */
public class ErrorDialog {

    public static ErrorDialog ourInstance = null;
    Context context;

    private ErrorDialog(Context context) {
        this.context = context;
    }

    public static ErrorDialog getInstance(Context context){
        if (ourInstance == null){
            synchronized (ErrorDialog.class){
                if (ourInstance==null){
                    ourInstance = new ErrorDialog(context);
                    return ourInstance;
                }
            }
        }
        return ourInstance;
    }

    public static HashMap<String, String> loadMessageListZH() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Tag.PROCESSING_MSG, Message.Processing_msg_zh);
        //hashMap.put(Tag.PROCESSING_OPTOUT_MSG, Message.Processing_optout_msg_zh);
        hashMap.put(Tag.PROCESSING_SUBMIT_FEEDBACK_MSG, Message.Processing_submit_feedback_msg_zh);
        //hashMap.put(Tag.PROCESSING_VERF_MSG, Message.Processing_verf_msg_zh);
        return hashMap;
    }

    public static HashMap<String, String> loadMessageListEN() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Tag.PROCESSING_MSG, Message.Processing_msg);
        //hashMap.put(Tag.PROCESSING_OPTOUT_MSG, Message.Processing_optout_msg);
        hashMap.put(Tag.PROCESSING_SUBMIT_FEEDBACK_MSG, Message.Processing_submit_feedback_msg);
        //hashMap.put(Tag.PROCESSING_VERF_MSG, Message.Processing_verf_msg);
        return hashMap;
    }

    public void displayAlertDialog(String msg){processErrorMessage(msg);}

    private void processErrorMessage(String errorMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.app_name));
        builder.setMessage(errorMsg);
        builder.setNegativeButton(
                context.getString(R.string.OK_BUTTON),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static class Type {
        public static final int CONNECTION_TIMEOUT = 10001;
    }

    public static String getMessageByTag(String tag, Context context) {
        String msg = (String) ShareStorage.retrieveData(tag,
                ShareStorage.DataType.STRING, ShareStorage.SP.Locale, context).getValue();
        return msg;
    }

    public static class Tag {
        public static final String PROCESSING_MSG = "processing_msg";
        public static final String PROCESSING_VERF_MSG = "processing_verf_msg";
        public static final String PROCESSING_OPTOUT_MSG = "processing_optout_msg";
        public static final String PROCESSING_SUBMIT_FEEDBACK_MSG = "Processing_submit_feedback_msg";
    }

    public static class Message {
        public static final String Processing_msg = "Processing...";
        public static final String Processing_msg_zh = "處理中...";
        public static final String Processing_submit_feedback_msg = "Processing to Submit Your Feedback";
        public static final String Processing_submit_feedback_msg_zh = "正在傳送你的回饋中...";
    }
}
