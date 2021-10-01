/*
* Copyright (C) 2018 The OmniROM Project
* Copyright (C) 2022 The Evolution X Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package org.evolution.oneplus.DeviceExtras.panelsettings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.TwoStatePreference;
import androidx.viewpager.widget.ViewPager;

import org.evolution.oneplus.DeviceExtras.DeviceExtras;
import org.evolution.oneplus.DeviceExtras.FileUtils;
import org.evolution.oneplus.DeviceExtras.modeswitch.P3ModeSwitch;
import org.evolution.oneplus.DeviceExtras.modeswitch.CustomerP3ModeSwitch;
import org.evolution.oneplus.DeviceExtras.modeswitch.SRGBModeSwitch;
import org.evolution.oneplus.DeviceExtras.modeswitch.CustomerSRGBModeSwitch;
import org.evolution.oneplus.DeviceExtras.modeswitch.WideModeSwitch;
import org.evolution.oneplus.DeviceExtras.modeswitch.LoadingEffectModeSwitch;
import org.evolution.oneplus.DeviceExtras.R;
import org.evolution.oneplus.DeviceExtras.ViewPagerAdapter;

public class PanelSettings extends PreferenceFragment implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup mRadioGroup;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        int checkedButtonId = R.id.standard_mode;

        if (SRGBModeSwitch.isCurrentlyEnabled(getContext())) {
            checkedButtonId = R.id.natural_mode;

        } else if (WideModeSwitch.isCurrentlyEnabled(getContext())) {
            checkedButtonId = R.id.wide_mode;

        } else if (CustomerSRGBModeSwitch.isCurrentlyEnabled(getContext())) {
            checkedButtonId = R.id.srgb_mode;

        } else if (CustomerP3ModeSwitch.isCurrentlyEnabled(getContext())) {
            checkedButtonId = R.id.p3_mode;
        }
        mRadioGroup.check(checkedButtonId);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
	final View rootView = inflater.inflate(R.layout.panel_modes, container, false);

	viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) rootView.findViewById(R.id.SliderDots);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity());
        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.inactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.inactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
	return rootView;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor edit = sharedPrefs.edit();

        if (checkedId == R.id.standard_mode) {
            FileUtils.writeValue(P3ModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_P3_SWITCH, false);

            FileUtils.writeValue(WideModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_WIDE_SWITCH, false);

            FileUtils.writeValue(SRGBModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_SRGB_SWITCH, false);

            FileUtils.writeValue(CustomerSRGBModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_CUSTOMER_SRGB_SWITCH, false);

            FileUtils.writeValue(CustomerP3ModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_CUSTOMER_P3_SWITCH, false);

            FileUtils.writeValue(P3ModeSwitch.getFile(getContext()), "1");
            edit.putBoolean(DeviceExtras.KEY_P3_SWITCH, true);

            FileUtils.writeValue(LoadingEffectModeSwitch.getFile(getContext()), "1");
            edit.putBoolean(DeviceExtras.KEY_LOADING_EFFECT_SWITCH, true);

        } else if (checkedId == R.id.natural_mode) {
            FileUtils.writeValue(P3ModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_P3_SWITCH, false);

            FileUtils.writeValue(WideModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_WIDE_SWITCH, false);

            FileUtils.writeValue(SRGBModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_SRGB_SWITCH, false);

            FileUtils.writeValue(CustomerSRGBModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_CUSTOMER_SRGB_SWITCH, false);

            FileUtils.writeValue(CustomerP3ModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_CUSTOMER_P3_SWITCH, false);

            FileUtils.writeValue(SRGBModeSwitch.getFile(getContext()), "1");
            edit.putBoolean(DeviceExtras.KEY_SRGB_SWITCH, true);

            FileUtils.writeValue(LoadingEffectModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_LOADING_EFFECT_SWITCH, false);

        } else if (checkedId == R.id.wide_mode) {
            FileUtils.writeValue(P3ModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_P3_SWITCH, false);

            FileUtils.writeValue(WideModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_WIDE_SWITCH, false);

            FileUtils.writeValue(SRGBModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_SRGB_SWITCH, false);

            FileUtils.writeValue(CustomerSRGBModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_CUSTOMER_SRGB_SWITCH, false);

            FileUtils.writeValue(CustomerP3ModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_CUSTOMER_P3_SWITCH, false);

            FileUtils.writeValue(WideModeSwitch.getFile(getContext()), "1");
            edit.putBoolean(DeviceExtras.KEY_WIDE_SWITCH, true);

        } else if (checkedId == R.id.srgb_mode) {
            FileUtils.writeValue(P3ModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_P3_SWITCH, false);

            FileUtils.writeValue(WideModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_WIDE_SWITCH, false);

            FileUtils.writeValue(SRGBModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_SRGB_SWITCH, false);

            FileUtils.writeValue(CustomerSRGBModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_CUSTOMER_SRGB_SWITCH, false);

            FileUtils.writeValue(CustomerP3ModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_CUSTOMER_P3_SWITCH, false);

            FileUtils.writeValue(CustomerSRGBModeSwitch.getFile(getContext()), "1");
            edit.putBoolean(DeviceExtras.KEY_CUSTOMER_SRGB_SWITCH, true);

        } else if (checkedId == R.id.p3_mode) {
            FileUtils.writeValue(P3ModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_P3_SWITCH, false);

            FileUtils.writeValue(WideModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_WIDE_SWITCH, false);

            FileUtils.writeValue(SRGBModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_SRGB_SWITCH, false);

            FileUtils.writeValue(CustomerSRGBModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_CUSTOMER_SRGB_SWITCH, false);

            FileUtils.writeValue(CustomerP3ModeSwitch.getFile(getContext()), "0");
            edit.putBoolean(DeviceExtras.KEY_CUSTOMER_P3_SWITCH, false);

            FileUtils.writeValue(CustomerP3ModeSwitch.getFile(getContext()), "1");
            edit.putBoolean(DeviceExtras.KEY_CUSTOMER_P3_SWITCH, true);
        }
        edit.commit();
    }
}
