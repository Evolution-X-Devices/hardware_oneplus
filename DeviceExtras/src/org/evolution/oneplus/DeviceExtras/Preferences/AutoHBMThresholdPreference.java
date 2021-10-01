/*
* Copyright (C) 2016 The OmniROM Project
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
package org.evolution.oneplus.DeviceExtras.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import androidx.preference.PreferenceManager;

import org.evolution.oneplus.DeviceExtras.DeviceExtras;
import org.evolution.oneplus.DeviceExtras.FileUtils;
import org.evolution.oneplus.DeviceExtras.R;

public class AutoHBMThresholdPreference extends CustomSeekBarPreference {

    public AutoHBMThresholdPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mInterval = context.getResources().getInteger(R.integer.auto_hbm_threshold_preference_interval);
        mShowSign = false;
        mUnits = "";
        mContinuousUpdates = false;
        int[] mAllValues = context.getResources().getIntArray(R.array.auto_hbm_threshold_preference_array);
        mMinValue = mAllValues[1];
        mMaxValue = mAllValues[2];
        mDefaultValueExists = true;
        mDefaultValue = mAllValues[0];
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mValue = Integer.parseInt(sharedPrefs.getString(DeviceExtras.KEY_AUTO_HBM_THRESHOLD, context.getString(R.string.auto_hbm_threshold_preference_default)));

        setPersistent(false);
    }

    @Override
    protected void changeValue(int newValue) {
        SharedPreferences.Editor prefChange = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        prefChange.putString(DeviceExtras.KEY_AUTO_HBM_THRESHOLD, String.valueOf(newValue)).commit();
    }
}
