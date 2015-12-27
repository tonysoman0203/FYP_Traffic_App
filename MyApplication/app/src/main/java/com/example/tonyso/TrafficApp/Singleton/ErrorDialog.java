package com.example.tonyso.TrafficApp.Singleton;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.tonyso.TrafficApp.R;

/**
 * Created by TonySo on 5/10/2015.
 */
public class ErrorDialog {

    public static class Type{
        public static final int CONNECTION_TIMEOUT = 10001;
    }

    Context context;
    public static ErrorDialog ourInstance = null;

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

    private ErrorDialog(Context context){
        this.context = context;
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

}
