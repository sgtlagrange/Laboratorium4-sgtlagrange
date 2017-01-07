package pl.edu.pwr.wiz.wzorlaboratorium4;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.List;


public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * Listener oczekujący na zmianę ustawienia. Po jej wykonaniu aktualizuje odpowiednio Summary
     * dla danego ustawienia. Używany tylko przy listach rozwijanych i polach typu tekstowego
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // Dla listy, wyszukujemy odpowiednią wartość we wszystkich wpisach
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Ustawiamy summary
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof EditTextPreference) {
                EditTextPreference editTextPreference = (EditTextPreference) preference;
                EditText editText = editTextPreference.getEditText();

                if( editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) ) {
                    /* Zastępujemy string przez gwiazdki */
                    stringValue = stringValue.replaceAll(".", "*");
                }

                preference.setSummary(stringValue);
            }
            else {
                // Dla pozostalych opcji wyświetlamy tekst
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Funkcja pomocnicza sprawdzająca czy mamy doczynienia z tabletem o rozmiarze XLarge
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Funkcja pomocnicza do podpięcia listenera sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Ustaw listener
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Uruchom go pierwszy raz, aby ustawić odpowiednie summary początkowe
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Podpięcie pod ActionBar
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        /* Zaczytujemy nagłówki z pliku XML pref_headers */
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * Funkcja zapobiegająca wstrzyknięciu fragmentu z innej aplikacji
     * Powinna zwrócić true, jeśli mamy doczynienia z fragmentem należącym do naszych ustawień
     */
    protected boolean isValidFragment(String fragmentName) {

        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || TwitterPreferenceFragment.class.getName().equals(fragmentName)
                || FacebookPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * Fragment wyświetlający główne ustawienia
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Podpinamy EditTextPreference oraz ListPreference pod nasz listener
            bindPreferenceSummaryToValue(findPreference("name"));
            bindPreferenceSummaryToValue(findPreference("friends_list"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Fragment wyświetlający ustawienia Facebooka
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class FacebookPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_facebook);
            setHasOptionsMenu(true);

            // Podpinamy EditTextPreference pod nasz listener
            bindPreferenceSummaryToValue(findPreference("facebook_login"));
            bindPreferenceSummaryToValue(findPreference("facebook_pass"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /* Fragment wyświetlający ustawienia Twittera
    */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class TwitterPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_twitter);
            setHasOptionsMenu(true);

            // Podpinamy EditTextPreference pod nasz listener
            bindPreferenceSummaryToValue(findPreference("twitter_login"));
            bindPreferenceSummaryToValue(findPreference("twitter_pass"));
            bindPreferenceSummaryToValue(findPreference("twitter_profile"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
