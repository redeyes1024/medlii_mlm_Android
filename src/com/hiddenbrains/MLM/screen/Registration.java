package com.hiddenbrains.MLM.screen;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

public class Registration extends Activity implements OnClickListener {
	public static ArrayList<String> spinner_list = new ArrayList<String>();
	public static ArrayList<String> spinner_list2 = new ArrayList<String>();
	private ArrayList<String> company_id = new ArrayList<String>();
	private ArrayList<String> group_id = new ArrayList<String>();

	public static ArrayAdapter<String> spinner_adapter;
	public static ArrayAdapter<String> spinner_adapter2;
	private boolean exception = false;

	private ImageButton back, submit;
	// private EditText user_name;
	private EditText pwd, con_pwd, email_add, emp_id, groupid;
	boolean valid;

	// public static TextView company,group;
	public static int g_id_index, c_id_index;
	private String comp_id, gup_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		back = (ImageButton) findViewById(R.id.btn_back);
		submit = (ImageButton) findViewById(R.id.btn_sign_up);
		back.setOnClickListener(this);
		submit.setOnClickListener(this);

		// user_name = (EditText) findViewById(R.id.username);
		pwd = (EditText) findViewById(R.id.password);
		con_pwd = (EditText) findViewById(R.id.re_password);
		email_add = (EditText) findViewById(R.id.email);
		emp_id = (EditText) findViewById(R.id.emp_id);
		groupid = (EditText) findViewById(R.id.groupid);

		// company = (TextView) findViewById(R.id.company_spinner);
		// company.setOnClickListener(this);
		// group = (TextView) findViewById(R.id.group_spinner);
		// group.setOnClickListener(this);
		// listData();

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_back) {
			finish();
		}
		if (id == R.id.btn_sign_up) {
			sendData();
		}
		// if(id==R.id.company_spinner)
		// {
		// SelectClassDialog dis = new
		// SelectClassDialog(Registration.this,"company");
		// dis.show();
		// }
		// if(id==R.id.group_spinner)
		// {
		// String comp = company.getText().toString();
		// if(comp.equals("Select Company "))
		// {
		// alert("Select Company First");
		// }
		// else
		// {
		// comp_id = company_id.get(c_id_index).toString();
		// listData2();
		// SelectClassDialog dis = new
		// SelectClassDialog(Registration.this,"group");
		// dis.show();
		// }
		// }
	}

	private void sendData() {
		String username, email, employee_id, password, confirm_password;// cmp,gup;

		// username = user_name.getText().toString();
		employee_id = emp_id.getText().toString().trim();
		password = pwd.getText().toString().trim();
		confirm_password = con_pwd.getText().toString().trim();
		email = email_add.getText().toString().trim();
		// cmp = company.getText().toString();
		// gup = group.getText().toString();
		gup_id = groupid.getText().toString();
		if (emailValidator(email)) {
			if (password.equals("")) {
				alert("Please Enter Password.");
			} else if (password.length() < 5) {
				String message = "Password Must Contain Min. 5 Characters.";
				alert(message);
			} else if (confirm_password.equals("")) {
				alert("Please Enter Re-enter Password.");
			} else if (!confirm_password.equals(password)) {
				alert("Password and Re-enter Password must be same.");
			} else if (gup_id.equals("")) {
				alert("Please Enter GroupID.");
			} else {
				// gup_id = group_id.get(g_id_index).toString();
				// String Login_Url =
				// "http://sli.hospitalu.com/iphone/User.asmx/Registration?username="+username+"&email="+email+"&employee_id="+employee_id+"&password="+password+"&confirm_password="+confirm_password;
				String Login_Url = "http://184.164.156.55/webservice/MLM/User.asmx/Registration?username="
						+ email
						+ "&email="
						+ email
						+ "&employee_id="
						+ employee_id
						+ "&password="
						+ password
						+ "&confirm_password="
						+ confirm_password
						+ "&group_id=" + gup_id;
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

						String success = OBJECT.getString("success");
						String message = OBJECT.getString("message");
						if (success.equalsIgnoreCase("0")) {
							alert(message);
						} else {
							// String user_id = OBJECT.getString("user_id");
							// alert(message);
							// startActivity(new
							// Intent(Registration.this,Login.class));
							// finish();

							AlertDialog.Builder dialog = new AlertDialog.Builder(
									this);
							dialog.setIcon(R.drawable.mlm);
							dialog.setTitle("Message");
							dialog.setMessage(message);
							dialog.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											finish();
										}
									});
							dialog.show();

						}
					} else {
						alert("No Connections Available!");
					}
				}

				catch (Exception e) {
					e.printStackTrace();
					alert("No Connections Available!");
				}
			}
		}

	}

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
			dialog.setIcon(R.drawable.mlm);
			dialog.setTitle("Error");
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
			dialog.setIcon(R.drawable.mlm);
			dialog.setTitle("Message");
			dialog.setMessage(message);
			dialog.setPositiveButton("Ok", null);
			dialog.show();
		}
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

	private void listData() {
		try {
			String Url = getString(R.string.url)+"Others.asmx/CompanyDropDown";
			URL url = new URL(Url);
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
				String success = OBJECT.getString("success");
				String message = OBJECT.getString("message");

				if (success.equalsIgnoreCase("0")) {
					alert(message);
					exception = true;
				} else {
					if (!spinner_list.isEmpty()) {
						spinner_list.clear();
					}
					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = (JSONObject) ja.get(i);
						spinner_list.add(jo.getString("name"));
						company_id.add(jo.getString("id"));
					}
				}
			} else {
				alert("No Connections Available!");

				exception = true;
			}

			if (!exception) {
				spinner_adapter = new ArrayAdapter<String>(this,
						R.layout.spinner_text, spinner_list);

			}
		} catch (Exception e) {
			e.printStackTrace();
			alert("No Connections Available!");
			exception = true;
		}
	}

	private void listData2() {
		try {

			String Url = getString(R.string.url)+"Others.asmx/GroupDropDown?id="
					+ comp_id;
			URL url = new URL(Url);
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
				String success = OBJECT.getString("success");
				String message = OBJECT.getString("message");

				if (success.equalsIgnoreCase("0")) {
					alert(message);
					exception = true;
				} else {
					if (!spinner_list2.isEmpty()) {
						spinner_list2.clear();
					}
					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = (JSONObject) ja.get(i);
						spinner_list2.add(jo.getString("name"));
						group_id.add(jo.getString("id"));
					}
				}
			} else {
				alert("No Connections Available!");

				exception = true;
			}

			if (!exception) {
				spinner_adapter2 = new ArrayAdapter<String>(this,
						R.layout.spinner_text, spinner_list2);

			}
		} catch (Exception e) {
			e.printStackTrace();
			alert("No Connections Available!");
			exception = true;
		}

	}
}
