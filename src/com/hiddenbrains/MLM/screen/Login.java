package com.hiddenbrains.MLM.screen;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.hiddenbrains.MLM.lib.GlobalVar;
import com.hiddenbrains.MLM.lib.GoogleTracker;

public class Login extends Activity implements OnClickListener {
	private SharedPreferences savesetting;
	private ImageButton login, sign_up;
	private TextView forgot_pwd;
	private EditText username, password;
	private String user_id, pwd;
	public static String id;

	GoogleAnalyticsTracker tracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		try {
			login = (ImageButton) findViewById(R.id.btn_login);
			sign_up = (ImageButton) findViewById(R.id.btn_sign_up);
			forgot_pwd = (TextView) findViewById(R.id.forgot_pwd);
			username = (EditText) findViewById(R.id.username);
			password = (EditText) findViewById(R.id.password);

			login.setOnClickListener(this);
			sign_up.setOnClickListener(this);
			forgot_pwd.setOnClickListener(this);

			SharedPreferences app_preferences1 = PreferenceManager
					.getDefaultSharedPreferences(this);

			if (app_preferences1.getBoolean("auto_login", false)) {
				String un1 = app_preferences1.getString("username", "");
				String ps1 = app_preferences1.getString("password", "");

				username.setText(un1);
				password.setText(ps1);
			}
		} catch (Exception e) {
			e.getMessage();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			loginTest();
			break;
		case R.id.btn_sign_up:
			startActivity(new Intent(Login.this, Registration.class));
			break;
		case R.id.forgot_pwd:
			DisplayDialog dis = new DisplayDialog(Login.this);
			dis.show();
			break;
		}
	}

	private void loginTest() {

		user_id = username.getText().toString();
		pwd = password.getText().toString();

		// if(user_id.equals(""))
		// {
		// alert("Please Enter Email Address");
		// }
		if (checkEmail(user_id)) {
			if (pwd.equals("")) {
				alert("Please Enter Password");
			} else {
				// String Login_Url =
				// "http://184.164.156.55/webservice/MLM/User.asmx/Login?username="+user_id+"&password="+pwd;
				// String Login_Url =
				// "http://184.164.156.55/webservice/MLM/User.asmx/Login?email="+user_id+"&password="+pwd;
				String Login_Url = getString(R.string.url)
						+ "User.asmx/Login?email=" + user_id + "&password="
						+ pwd;

				try {
					URL url = new URL(Login_Url);
					URLConnection urlc = url.openConnection();
					HttpURLConnection huc = (HttpURLConnection) urlc;
					huc.setRequestMethod("GET");
					huc.connect();
					int response = huc.getResponseCode();
					if (response == HttpURLConnection.HTTP_OK) {
						InputStream is = huc.getInputStream();
						String result = convertStreamToString(is);

						JSONTokener jt = new JSONTokener(result);
						JSONArray ja = new JSONArray(jt);
						JSONObject OBJECT = (JSONObject) ja.get(0);

						String error_message = OBJECT.getString("success");
						String message = OBJECT.getString("message");
						if (error_message.equalsIgnoreCase("0")) {
							alert(message);
						} else {
							String vGroupName = OBJECT.getString("group_name");
							String vCompanyName = OBJECT
									.getString("company_name");
							String c_id = OBJECT.getString("company_id");
							;
							GlobalVar.vUserID = OBJECT.getString("user_id");
							GlobalVar.vGroupID = OBJECT.getString("group_id");
							savesetting = PreferenceManager
									.getDefaultSharedPreferences(this);
							SharedPreferences.Editor editor = savesetting
									.edit();
							editor.putString("username", user_id);
							editor.putString("password", pwd);
							editor.commit();

							// GlobalVar.vInfo =
							// getDeviceID()+":"+c_id+"----"+vCompanyName+"----"+GlobalVar.vGroupID+"----"+vGroupName+"----";
							GlobalVar.vCompany = vCompanyName;
							GlobalVar.vGroup = vGroupName;
							GlobalVar.vUser = user_id;
							GoogleTracker.vGoogleAPI = OBJECT
									.getString("vGoogleAPI");
							GoogleTracker.vGoogleAPIvalue = OBJECT
									.getString("vGoogleAPIvalue");
							// info = vCompanyName+"[ "+vGroupName+" ]";
							try {
								// user = info;

								// user = user_id;
								// tracker =
								// GoogleAnalyticsTracker.getInstance();
								// tracker.startNewSession("UA-25672706-3",Login.this);
								// tracker.trackPageView("http://sli.hospitalu.com");
								// tracker.trackEvent(user,"Login button clicked",info+user_id,1);
								// tracker.dispatch();

							} catch (Exception e) {
								e.getMessage();
							}
							// GoogleAnalyticsTracker Code Ends Here

							startActivity(new Intent(Login.this, Home.class));
							finish();
						}
					} else {
						alert("Connection error. Try again");
					}
				} catch (Exception e) {
					e.printStackTrace();
					alert("No Connections Available!");
				}
			}
		} else {
			alert("Please enter a valid email address.");
		}
	}

	/*
	 * public String getDeviceID(){ TelephonyManager telephonyManager =
	 * (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); return
	 * telephonyManager.getDeviceId(); }
	 */

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private void alert(String string) {
		String message = string;
		if (message.equals("No Connections Available!")) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Error");
			dialog.setIcon(R.drawable.mlm);
			dialog.setMessage(message);
			dialog.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			dialog.show();
		} else {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Message");
			dialog.setIcon(R.drawable.mlm);
			dialog.setMessage(message);
			dialog.setPositiveButton("Ok", null);
			dialog.show();
		}
	}

	private boolean checkEmail(String inputMail) {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");
		return pattern.matcher(inputMail).matches();
	}

	private boolean emailValidator(String s) {
		String cs = s;
		String message = "";
		cs.trim();
		boolean valid = true;
		if (cs.equals("")) {
			message = "Please Enter Email Address.";
			alert(message);
			valid = false;
		} else {
			try {
				char c = cs.charAt(0);
				if (Character.isDigit(c) || c == '.' || c == '_') // For First
																	// Letter
																	// Non-Digit
																	// and not a
																	// dot,underscore
																	// operator
				{
					message = "Email Address should start with alphabet.";
					alert(message);
					valid = false;
				} else if (cs.indexOf('@') == -1) {
					message = "Email Address should contain '@' symbol";
					alert(message);
					valid = false;
				} else if (cs.indexOf('.') == -1) {
					message = "Email Address should contain '.' symbol";
					alert(message);
					valid = false;
				} else { // For Last Letter not a dot,underscore operator
					int length = cs.indexOf('@');
					char last = cs.charAt(length - 1);
					if (last == '_' || last == '.') {
						message = "Email Address should not end with  a dot or underscore.";
						alert(message);
						valid = false;
					} else {

						// Regular Expression like .com
						String str = "^[a-z_.A-Z0-9]{2,40}+@[a-z]+?\\.[a-z]{2,6}$";

						// Regular Expression like .co.in
						String str1 = "^[a-z_.A-Z0-9]{2,40}+@[a-z]+?\\.[a-z]{2,6}\\.[a-z]{2}$";

						Pattern p = Pattern.compile(str);
						Matcher m = p.matcher(cs);
						Pattern p1 = Pattern.compile(str1);
						Matcher m2 = p1.matcher(cs);

						if (!(m.find() || m2.find())) {
							message = "Enter a valid domain name.";
							alert(message);
							valid = false;
						} else {
							valid = true;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return valid;

	}
}
