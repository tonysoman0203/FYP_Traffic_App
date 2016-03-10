package com.example.tonyso.TrafficApp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.tonyso.TrafficApp.baseclass.BaseActivity;
import com.example.tonyso.TrafficApp.utility.LanguageSelector;
import com.example.tonyso.TrafficApp.utility.ShareStorage;
import com.example.tonyso.TrafficApp.utility.StoreObject;

import java.util.Locale;


public class SettingsActivity extends BaseActivity {
    static LanguageSelector languageSelector;
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_preference);
        setupActionBar();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.pref_layout);
        languageSelector = LanguageSelector.getInstance(this);
        fab = (FloatingActionButton) findViewById(R.id.testing_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(fab, "Hello Snack Bar", Snackbar.LENGTH_LONG).setAction("Dismiss", null).show();
            }
        });
        getFragmentManager().beginTransaction().replace(R.id.content_pref, new MyPreferenceFragment()).commit();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.title_activity_settings));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        Resources rs;
        ListPreference listPreference;
        ListPreference distancePref;
        String user_lang_pref;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.mypreference);
            rs = getResources();

            user_lang_pref = languageSelector.getLanguage();
            listPreference = (ListPreference) findPreference("LanguageList");
            distancePref = (ListPreference) findPreference("distance");
            listPreference.setTitle(getString(R.string.pref_title_Language));

            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int valueIndex = Integer.parseInt((String) newValue);
                    changeLanguage(valueIndex, preference);
                    return true;
                }
            });
            setDefaultUserPreference();
            setDefaultNearLocationPreference();
        }

        private void setDefaultNearLocationPreference() {
            int km = ShareStorage.getInteger(MyApplication.KEY_NEAR_IN_KM, ShareStorage.SP.PrivateData,getActivity());
            System.out.println("The km is " + km);
            Log.e("length", "" + distancePref.getEntries().length);

            distancePref.setValueIndex((km - 1));
            distancePref.setSummary(km + " " + getString(R.string.km));

            distancePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.e("object", "Object Value=" + newValue.toString());
                    ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                            new StoreObject<Object>(true, MyApplication.KEY_NEAR_IN_KM,
                                    Integer.parseInt((String) newValue)), ShareStorage.SP.PrivateData, getActivity());
                    MyApplication.KM_IN_NEAR = Integer.valueOf((String) newValue);
                    int newKm = Integer.parseInt(newValue.toString());
                    distancePref.setValueIndex(newKm-1);
                    distancePref.setSummary(newKm + " " + getString(R.string.km));
                    return true;
                }
            });
        }

        private void setDefaultUserPreference() {
            ListPreference listPreference = (ListPreference) findPreference("LanguageList");
            if (languageSelector.isSystemLanguage()) {
                user_lang_pref = languageSelector.getSystemLanguage();
                setSystemLanguage();
            } else {
                user_lang_pref = languageSelector.getUserLanguage();
                listPreference.setDialogTitle(getString(R.string.alert_box_title_language_choice));
                listPreference.setEntries(new CharSequence[]{getString(R.string.English), getString(R.string.TrandionalChinese)});
                listPreference.setEntryValues(new CharSequence[]{"0", "1"});
                if (user_lang_pref.equals(MyApplication.Language.ZH_HANT)) {
                    listPreference.setValueIndex(1);
                    listPreference.setSummary(listPreference.getEntry().toString());
                } else {

                    listPreference.setValueIndex(0);
                    listPreference.setSummary(listPreference.getEntry().toString());
                }
            }
        }

        private void setSystemLanguage() {
            listPreference.setDialogTitle(getString(R.string.alert_box_title_language_choice));
            listPreference.setEntries(new CharSequence[]{getString(R.string.English), getString(R.string.TrandionalChinese)});
            listPreference.setEntryValues(new CharSequence[]{"0", "1"});
            if (!user_lang_pref.equals(MyApplication.Language.ENGLISH)) {
                listPreference.setValueIndex(0);
                listPreference.setSummary(listPreference.getEntry().toString());
            } else {
                listPreference.setValueIndex(1);
                listPreference.setSummary(listPreference.getEntry().toString());
            }
        }

        private void setUserPreferenceLanguage(int valueIndex) {
            Resources res = getResources();
            if (valueIndex == 0) {
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.locale = new Locale(MyApplication.Language.ENGLISH);
                res.updateConfiguration(conf, dm);
            } else {
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.locale = new Locale(MyApplication.Language.ZH_HANT);
                res.updateConfiguration(conf, dm);
            }
        }

        private void changeLanguage(int valueIndex, Preference pref) {
            ListPreference listPreference = (ListPreference) pref;
            CharSequence listDesc = listPreference.getEntry();
            String trim = languageConvertor(listDesc.toString());
            Log.e("Share Pref", user_lang_pref);
            Log.e("Value in List Pref", listDesc.toString());
            Log.e("Trim", trim);

                if (valueIndex == 0) {
                    if (pref instanceof ListPreference){
                        listPreference = (ListPreference) pref;
                        listDesc = listPreference.getEntries()[0];
                        Log.e("Value",listDesc.toString());
                        trim = languageConvertor(listDesc.toString());
                        if (trim.equals(user_lang_pref)) {
                            displaySnackBar(listDesc.toString());
                        }else{
                            this.setUserPreferenceLanguage(valueIndex);
                            if (pref instanceof ListPreference) {
                                if (!TextUtils.isEmpty(listDesc)) {
                                    pref.setSummary(listDesc);
                                }
                                ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                                        new StoreObject<>(false, MyApplication.Language_UserPref, MyApplication.Language.ENGLISH), ShareStorage.SP.PrivateData, this.getActivity());

                                restartActivity();
                            }
                        }
                    }
                } else {
                    if (valueIndex == 1) {
                        if (pref instanceof ListPreference){
                            listPreference = (ListPreference) pref;
                            listDesc = listPreference.getEntries()[1];
                            Log.e("Value",listDesc.toString());
                            trim = languageConvertor(listDesc.toString());
                                if (trim.equals(user_lang_pref)) {
                                    displaySnackBar(listDesc.toString());
                                } else {
                                    this.setUserPreferenceLanguage(valueIndex);
                                    if (pref instanceof ListPreference) {
                                        listPreference = (ListPreference) pref;
                                        listDesc = listPreference.getEntry();
                                        if (!TextUtils.isEmpty(listDesc)) {
                                            pref.setSummary(listDesc);
                                        }
                                        ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                                                new StoreObject<>(false, MyApplication.Language_UserPref, MyApplication.Language.ZH_HANT), ShareStorage.SP.PrivateData, this.getActivity());
                                        restartActivity();
                                    }
                                }
                            }
                    }
                }

        }


        private void displaySnackBar(String s) {
            String format,msg="";
            if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)){
                format = getResources().getString(R.string.language_alert);
                msg = String.format(Locale.ENGLISH,format,s);
            }else{
                format = getResources().getString(R.string.language_alert_zh);
                msg = format;
            }
            Log.e("Here ", msg);
            Snackbar.make(getActivity().findViewById(R.id.pref_layout),
                    msg, Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        }

        private void restartActivity() {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
            getActivity().finish();
        }

        private String languageConvertor(String s) {
            if (s.equals(getString(R.string.English))) {
                return MyApplication.Language.ENGLISH;
            } else {
                return MyApplication.Language.ZH_HANT;
            }
        }
    }
}
