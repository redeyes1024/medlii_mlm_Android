package com.hiddenbrains.MLM.screen;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class Pdf extends Activity
{
	private WebView web_view;
	private Bundle bundle;
	private String Url;
//	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pdf);
		try
		{
			bundle = getIntent().getExtras();
			Url = bundle.getString("url");
			web_view =  (WebView) findViewById(R.id.webkitWebView1);
			web_view.getSettings().setJavaScriptEnabled(true);
			web_view.loadUrl("http://docs.google.com/viewer?url="+Url);
			
//			web_view.setWebViewClient(new HelloWebViewClient());
			
		}catch (Exception e) {
			e.getMessage();
		}
	}
//	class HelloWebViewClient extends WebViewClient 
//	{
//	    @Override
//	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//	        view.loadUrl(url);
//	        return true;
//	    }
//	}
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		finish();
	}
	
}

