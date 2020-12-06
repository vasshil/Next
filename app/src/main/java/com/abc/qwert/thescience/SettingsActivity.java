package com.abc.qwert.thescience;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity {//AppCompatActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
/*

        getLayoutInflater().inflate(R.layout.toolbar, findViewById(android.R.id.content));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
*/

        addPreferencesFromResource(R.xml.pref);

        Preference mail = findPreference("mail");
        mail.setOnPreferenceClickListener(preference -> {
            /*final Intent intent = new Intent(android.content.Intent.ACTION_SEND)
                    //.setType("plain/text").setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail")
                    .putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.pref_mail_summary)})
                    .putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.pref_mail_title))
                    .putExtra(Intent.EXTRA_TEXT, "");
            startActivity(intent);*/





            ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("", getResources().getString(R.string.pref_mail_summary)));
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.it_copy_to_clipboard), Toast.LENGTH_SHORT).show();
            return true;
        });

        Preference privacy = findPreference("privacy");
        privacy.setOnPreferenceClickListener(preference -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tarashoh.github.io/nextPrivacy/nextPrivacyPolicy.html"));
            startActivity(browserIntent);
            return true;
        });

        SwitchPreference g = (SwitchPreference) findPreference("gravity");
        g.setTitle("g = " + (pref.getBoolean("gravity", true)?"9.81":"10"));
        g.setOnPreferenceChangeListener((preference, o) -> {
            boolean gb = !pref.getBoolean("gravity", true);
            g.setTitle("g = " + (gb?"9.81":"10"));
            Consts.g = (gb?9.81:10);
            return true;
        });

        CheckBoxPreference physHelp = (CheckBoxPreference) findPreference("phys_help");
        physHelp.setOnPreferenceClickListener((preference -> {
            onBackPressed();
            return true;
        }));

        CheckBoxPreference calcHelp = (CheckBoxPreference) findPreference("calc_help");
        calcHelp.setOnPreferenceClickListener((preference -> {
            onBackPressed();
            return true;
        }));

        CheckBoxPreference itHelp = (CheckBoxPreference) findPreference("it_help");
        itHelp.setOnPreferenceClickListener((preference -> {
            onBackPressed();
            return true;
        }));


    }

























/*

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.pref);
        }
    }
*/

}

