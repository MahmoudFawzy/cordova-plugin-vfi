package com.m3printer;

import com.nbbse.printapi.*;
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

public class M3Printer extends CordovaPlugin {
	public static Printer print;
	public Context context;
	CordovaInterface mycordova;
	CordovaWebView mywebView;

	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		print = Printer.getInstance();
		context = this.cordova.getActivity().getApplicationContext();
		mycordova = cordova;
		mywebView = webView;
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("printTest")) {

			String txt = args.getString(0);

			InputStream is = context.getResources().openRawResource(getAppResource("logo", "raw"));
			print.printBitmap(is);

			boolean showFees = true;
			JSONObject json = new JSONObject(txt);
			JSONArray jReciept = json.getJSONArray("Fields");

			print.printText(String.valueOf(prepLabel("الخدمة") + json.getString("ServiceName")), 1, true);

			print.printText("--------------------------------");
			for (int i = 0; i < jReciept.length(); i++) {
				JSONObject jO = jReciept.getJSONObject(i);

				if (Arrays.asList("1100,1094,1106,691".split(",")).indexOf(jO.getString("SFId")) > -1) {
					print.printText(prepLabel(jO.getString("FieldName")), 2, true);
					print.printText(jO.getString("Value"), 2, false);

					showFees = false;
				} else {
					print.printText(String.valueOf(prepLabel(jO.getString("FieldName")) + jO.getString("Value")), 1,
							true);
				}

				if (Arrays.asList("181,182,183,238,239,240,256,257,258".split(","))
						.indexOf(jO.getString("SFId")) > -1) {

					showFees = false;
				}

			}
			print.printText(String.valueOf(prepLabel("تكلفة الخدمة") + json.getString("Totalprice")), 1, true);
					

			if (showFees) {
				print.printText(String.valueOf(prepLabel("رسوم التحصيل") + json.getString("Fees")), 1, true);
			}

			double tot = json.getDouble("Totalprice") + json.getDouble("Fees");

			print.printText(String.valueOf(prepLabel("الإجمالي") + String.valueOf(tot)), 2, true);

			print.printText("--------------------------------");

			String desc = json.getString("InvoiceDescription");
			if (!desc.trim().equals("null")) {
				print.printText(desc, 1, true);
				print.printText("--------------------------------");
			}

			String sDate = json.getString("AddedTime");

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			Date convertedDate = new Date();
			try {
				convertedDate = dateFormat.parse(sDate);
			} catch (ParseException ex) {
				// Do something
			}
			SimpleDateFormat dateFormat_date = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.ENGLISH);
			SimpleDateFormat dateFormat_time = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);

			print.printText(
					String.valueOf(prepLabel("تاريخ التحصيل") + dateFormat_date.format(convertedDate)), 1,
					true);

			// print.printText(String.valueOf(prepLabel("وقت التحصيل") + dateForma
			// _time.format(convertedDate)), 1, true);

			//////////////////////////////////////////////////
			// print.printFormattedTextPrepare();
			// print.addString(prepLabel("تاريخ التحصيل"), 1, true);
			// print.addString(dateFormat_date.format(convertedDate), 2, true);
			// print.printFormattedText();
			//////////////////////////////////////////////////

			print.printText(String.valueOf(prepLabel("رقم الفرع") + json.getString("AgentCode")), 1, true);
			print.printText(String.valueOf(prepLabel("رقم الفاتورة") + json.getString("InvoiceId")), 1, true);
					

			double s = json.getDouble("Status");
			String s_str = "غير محدد";
			if (s == 0 && false) {
				s_str = "عملية ناجحة";
			} else if (s == 2) {
				s_str = "مسترجع";
			} else if (s == 0 || s == 1 || s == 3 || s == 4) {
				s_str = "عملية ناجحة";
			}
			print.printText("     " + s_str, 2, true);

			print.printText("--------------------------------");
			print.printText(" تسعدنا خدمتكم -  0224561600", 1, true);
			print.printText("--------------------------------");
			print.printText(json.getString("Footer"));

			print.printEndLine();
			callbackContext.success("1");
			return true;

		} else if (action.equals("printText")) {

			String txt = args.getString(0);
			String AppId = args.getString(1);
			String Logo = "logo";
			// if(AppId == "30"){
			// 	Logo = "logo30";

			// }

			InputStream is = context.getResources().openRawResource(getAppResource(Logo, "raw"));
			print.printBitmap(is);
 
			JSONObject json = new JSONObject(txt);
			JSONArray jReciept = json.getJSONArray("Fields");

			print.printText(String.valueOf(prepLabel("الخدمة") + json.getString("ServiceName")), 1, true);

			print.printText("--------------------------------");
			for (int i = 0; i < jReciept.length(); i++) {
				JSONObject jO = jReciept.getJSONObject(i);

				if (Arrays.asList("1100,1094,1106,691,1364,1724".split(",")).indexOf(jO.getString("SFId")) > -1) {
					print.printText(prepLabel(jO.getString("FieldName")), 2, true);
					print.printText(jO.getString("Value"), 2, false);
 
				} else {
					print.printText(String.valueOf(prepLabel(jO.getString("FieldName")) + jO.getString("Value")), 1,
							true);
				}  
			}
			print.printText(String.valueOf(prepLabel("تكلفة الخدمة") + json.getString("Totalprice")), 1, true);
					 
			if ( json.getDouble("Fees") > 0) {
				print.printText(String.valueOf(prepLabel("رسوم التحصيل") + json.getString("Fees")), 1, true);
			}

			double tot = json.getDouble("Totalprice") + json.getDouble("Fees");

			print.printText(String.valueOf(prepLabel("الإجمالي") + String.valueOf(tot)), 2, true);

			print.printText("--------------------------------");

			String desc = json.getString("InvoiceDescription");
			if (!desc.trim().equals("null")) {
				print.printText(desc, 1, true);
				print.printText("--------------------------------");
			}

			String sDate = json.getString("AddedTime");

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			Date convertedDate = new Date();
			try {
				convertedDate = dateFormat.parse(sDate);
			} catch (ParseException ex) {
				// Do something
			}
			SimpleDateFormat dateFormat_date = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.ENGLISH);
			SimpleDateFormat dateFormat_time = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);

			print.printText(
					String.valueOf(prepLabel("تاريخ التحصيل") + dateFormat_date.format(convertedDate)), 1,
					true);

			// print.printText(String.valueOf(prepLabel("وقت التحصيل") + dateForma
			// _time.format(convertedDate)), 1, true);

			//////////////////////////////////////////////////
			// print.printFormattedTextPrepare();
			// print.addString(prepLabel("تاريخ التحصيل"), 1, true);
			// print.addString(dateFormat_date.format(convertedDate), 2, true);
			// print.printFormattedText();
			//////////////////////////////////////////////////

			print.printText(String.valueOf(prepLabel("رقم الفرع") + json.getString("AgentCode")), 1, true);
			print.printText(String.valueOf(prepLabel("رقم الفاتورة") + json.getString("InvoiceId")), 1, true);
					

			double s = json.getDouble("Status");
			String s_str = "غير محدد";
			if (s == 0) {
				s_str = "عملية ناجحة";
			} else if (s == 2) {
				s_str = "مسترجع";
			} else if (s == 1 || s == 3 || s == 4) {
				s_str = "عملية ناجحة";
			}
			print.printText("     " + s_str, 2, true);

			print.printText("--------------------------------");
			print.printText(" تسعدنا خدمتكم -  0224561600", 1, true);
			print.printText("--------------------------------");
			print.printText(json.getString("Footer"));

			print.printEndLine();
			callbackContext.success("1");
			return true;
		} else if (action.equals("printJson")) {

			String txt = args.getString(0);
			String AppId = args.getString(1);
			String Logo = "logo";
			// if(AppId == "30"){
			// 	Logo = "logo30"; 
			// }

			InputStream is = context.getResources().openRawResource(getAppResource(Logo, "raw"));
			print.printBitmap(is);


			JSONObject json = new JSONObject(txt);
			JSONArray jReciept = json.getJSONArray("data");

			print.printText(json.getString("header"), 1, true);
			print.printText("--------------------------------");
			for (int i = 0; i < jReciept.length(); i++) {
				JSONObject jO = jReciept.getJSONObject(i);
				String label = jO.getString("label");
				String value = jO.getString("value");
				String pr = prepLabel(label) + value;
				print.printText(pr, 1, true);
				// print.printText(jO.getString("label"), 1, true);
				// print.printText(jO.getString("value"), 1, false);
			}

			print.printEndLine();
			callbackContext.success("1");
			return true;
		} else if (action.equals("printBase64")) {
			// String txt = args.getString(0);

			// final byte[] decodedBytes = Base64.decode(txt, Base64.DEFAULT);

			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			// Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0,
			// decodedBytes.length, options);

			// int mWidth = bitmap.getWidth();
			// int mHeight = bitmap.getHeight();

			// bitmap = resizeImage(bitmap, 48 * 8, mHeight);

			// // ByteArrayOutputStream stream = new ByteArrayOutputStream();
			// // bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			// // byte[] byteArray = stream.toByteArray();
			// // bitmap.recycle();

			// // byte[] bt = decodeBitmap(bitmap);
			// // convertARGBToGrayscale(bt, mWidth, mHeight);
			// print.printBitmap(bitmap);
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