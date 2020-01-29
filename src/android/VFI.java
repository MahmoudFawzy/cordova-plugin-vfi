package com.vfi;

import org.apache.cordova.*;

import java.io.*;
import java.util.*;
import java.text.*;
import java.util.Hashtable;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Bitmap.CompressFormat;
import android.util.Xml.Encoding;
import jdk.nashorn.internal.parser.*;
import android.util.Base64;
import java.util.ArrayList;
import java.util.List;
import android.content.*;
import android.R;

public class VFI extends CordovaPlugin {
	public Context context;
	CordovaInterface mycordova;
	CordovaWebView mywebView;

	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);

		context = this.cordova.getActivity().getApplicationContext();
		mycordova = cordova;
		mywebView = webView;
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("printTest")) {

			String txt = args.getString(0);

			callbackContext.success("1");
			return true;

		} else if (action.equals("printText")) {

			String txt = args.getString(0);
			String AppId = args.getString(1);
			String Logo = "logo";
			// if(AppId == "30"){
			// Logo = "logo30";

			// }

			callbackContext.success("1");
			return true;
		} else if (action.equals("printJson")) {

			String txt = args.getString(0);
			String AppId = args.getString(1);
			String Logo = "logo";
			// if(AppId == "30"){
			// Logo = "logo30";
			// }

			InputStream is = context.getResources().openRawResource(getAppResource(Logo, "raw"));
			print.printBitmap(is);

			callbackContext.success("1");
			return true;
		} else if (action.equals("printBase64")) {

			print.printEndLine();
			callbackContext.success("1");
			return true;
		} else {
			return false;
		}
	}

	private int getAppResource(String name, String type) {
		return mycordova.getActivity().getResources().getIdentifier(name, type,
				mycordova.getActivity().getPackageName());
	}

	private String prepLabel(String label) {
		label += " : ";
		// for (int f = 0; label.length() < 15; f++) {
		// label = " " + label;
		// }

		return label;
	}

}