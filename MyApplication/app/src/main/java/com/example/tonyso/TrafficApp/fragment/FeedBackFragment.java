package com.example.tonyso.TrafficApp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.tonyso.TrafficApp.R;

/**
 * Created by TonySo on 7/1/16.
 */
public class FeedBackFragment extends DialogFragment {

    TextInputLayout userName, email, comment;
    EditText edtUserName, edtEmail, edtComment;
    Button btnSubmit;

    public FeedBackFragment() {

    }

    public static FeedBackFragment newInstance(String title) {
        FeedBackFragment frag = new FeedBackFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NO_TITLE,0);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        initView(view);
        userName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void initView(View view) {

        userName = (TextInputLayout) view.findViewById(R.id.userName);
        edtUserName = (EditText) view.findViewById(R.id.edtUsername);
        email = (TextInputLayout) view.findViewById(R.id.email);
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        comment = (TextInputLayout) view.findViewById(R.id.comment);
        edtComment = (EditText) view.findViewById(R.id.edtComment);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto:" + "tonysoman.develop@gmail.com"));
                //email.putExtra(Intent.EXTRA_EMAIL, new String[]{edtEmail.getText().toString()});
                email.putExtra(Intent.EXTRA_SUBJECT, "Comment to " + getActivity().getResources().getString(R.string.app_name));
                email.putExtra(Intent.EXTRA_TEXT, edtComment.getText().toString());
                //email.setType("message/rfc822");
                startActivity(email);
            }
        });
    }
}
