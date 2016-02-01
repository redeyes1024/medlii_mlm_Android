package com.hiddenbrains.MLM.lib;

import android.content.Context;
import android.test.IsolatedContext;
import android.util.Log;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.hiddenbrains.MLM.screen.R;

public class GoogleTracker {
	public static String vGoogleAPIvalue;
	public static String vGoogleAPI;

	private static void trackShered(Context _context, String _action,
			String _label, String _company, String _group, String _user,
			String _GoogleAPI, String _GoogleAPIValue) {
		GoogleAnalyticsTracker tracker;

		tracker = GoogleAnalyticsTracker.getInstance();
		try {
			tracker.startNewSession(_GoogleAPI, _context);
			tracker.trackPageView(_GoogleAPIValue);
			tracker.setCustomVar(0, "company", _company);
			if (_group != null && _group != "") {
				tracker.setCustomVar(1, "group", _group);
			}

			tracker.trackEvent(_user, _action, _label, 3);
			Log.i("GoogleAPI", _action+" : "+_label);
		} catch (Exception e) {
			e.getMessage();

		}
		tracker.dispatch();
	}

	public static void track(Context context, String action, String label) {
		String company = GlobalVar.vCompany;
		String group = GlobalVar.vGroup;
		String user = GlobalVar.vUser;
		trackShered(context, action, label, company, group, user,
				context.getString(R.string.GoogleAPIid),
				context.getString(R.string.GoogleAPIvalue));
		if (vGoogleAPI != null && vGoogleAPI != "" && vGoogleAPIvalue != null
				&& vGoogleAPIvalue != "") {
			trackShered(context, action, label, company, group, user,
					vGoogleAPI, vGoogleAPIvalue);
		} else {
			Log.w("GoogleAPI", "No company googleAPI information.");
		}
	}
}
