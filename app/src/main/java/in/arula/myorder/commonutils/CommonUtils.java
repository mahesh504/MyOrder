package in.arula.myorder.commonutils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Class for using different android utils
 * @author jonnalagaddasiva
 *
 */
public class CommonUtils {

	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;


	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		}
		return TYPE_NOT_CONNECTED;
	}

	public static String getConnectivityStatusString(Context context) {
		int conn = CommonUtils.getConnectivityStatus(context);
		String status = null;
		if (conn == CommonUtils.TYPE_WIFI) {
			status = "Wifi enabled";
		} else if (conn == CommonUtils.TYPE_MOBILE) {
			status = "Mobile data enabled";
		} else if (conn == CommonUtils.TYPE_NOT_CONNECTED) {
			status = "Not connected to Internet";
		}
		return status;
	}


	 public static void displayAlert(final Context context)
	    {
	        AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        builder.setMessage("Please check your internet connection")
	        .setTitle("No network connection")
	        .setCancelable(
	            false).setPositiveButton("Settings",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	                    final Intent intent = new Intent(Intent.ACTION_MAIN, null);
	                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
	                    final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
	                    intent.setComponent(cn);
	                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
	                    context.startActivity(intent);
	                }
	            }).setNegativeButton("Cancel",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                	 dialog.cancel();
//	                   System.exit(0);
	                }
	            });
	        AlertDialog alert = builder.create();
	        alert.show();
	    }

	/**
	 * validating email pattern
	 */
	public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
	          "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
	          "\\@" +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
	          "(" +
	          "\\." +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
	          ")+"
	      );

	/**
	 * Checking the given emailId is valid or not
	 * @param email --> user emailId
	 * @return ---> true if valid else false
	 */
	 public static boolean isValidEmail(String email) {
	        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	    }

	/**
	 * Checking the Internet connection is available or not
	 * @param context
	 * @return  true if available else false
	 */
	 public static boolean isNetworkAvailable(final Context context) {
	        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        if (null != connectivityManager) {
	            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	        }

	        return false;
	    }

	 /**
	  * Deleting a particular directory from sdcard
	  * @param path ---> file path
	  * @return  ---> true is successfully deleted else false
	  */
	 public static boolean deleteDirectory(File path) {
	        if (path.exists()) {
	            File[] files = path.listFiles();
	            for(int i=0; i<files.length; i++) {
	                if(files[i].isDirectory()) deleteDirectory(files[i]);
	                else files[i].delete();
	            }
	        }
	        return true;
	    }

	 /**
	  * Class for common alert dialog
	  * @author jonnalagaddasiva
	  *
	  */
	 public class AlertDialogMsg extends AlertDialog.Builder{

			Context context;
			public AlertDialogMsg(Context context, String title, String msg) {
				super(context);
				this.setMessage(msg);
				this.setTitle(title);
				this.setCancelable(false);
		   }

		}


	 /**
	  * Checking SD card is available or not in mobile
	  * @param context
	  * @return --> true if available else false
	  */
	 public static boolean isSDcardAvailable(Context context){
			boolean isAvailable=false;
		    boolean available =  Environment.getExternalStorageState().toString().equalsIgnoreCase(Environment.MEDIA_MOUNTED)||Environment.getExternalStorageState().toString().equalsIgnoreCase(Environment.MEDIA_CHECKING)||
	 							 Environment.getExternalStorageState().toString().equalsIgnoreCase(Environment.MEDIA_MOUNTED_READ_ONLY);
		    if(!available){
				isAvailable=false;
			}
			else{
				isAvailable=true;
			}
			return isAvailable;
		}


	 /**
	  * Checking a string contains an integer or not
	  * @param s  --> Input string
	  * @return true is string contains number else false.
	  */
	 public static boolean isInteger(final String s) {
	        return Pattern.matches("^\\d*$", s);
	    }


	 /**
	  * Getting the device id
	  * @param context  --> Current context
	  * @return  --> Device IMEI number as string
	  */
	 public static String getDeviceId(Context context){
		 TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		 String strDeviceIMEI=telephonyManager.getDeviceId();
		 if (strDeviceIMEI != null) {
			return strDeviceIMEI;
		}else{
			return "";
		}
	 }

	 /**
	  * Getting the device information(android version, Device model,Device manufacturer)
	  * @return  ---> Device information in a string formate
	  */
	 public static String getDeviceExtraInfo(){
		 return ""+Build.VERSION.RELEASE+"/n"+Build.MANUFACTURER + " - " + Build.MODEL+"/n";
	 }

	 /**
	  * Getting the current date and time with comma separated.
	  * @param dateFormate  ---> Input date format
	  * @param timeFormate  ---> Input time format
	  * @return  if both are required returns date and time with comma separated else if only time required returns time else only date else default date and time.
	  */
	 public static String getDateTime(String dateFormate, String timeFormate) {
			SimpleDateFormat formatterDate = null;
			SimpleDateFormat	formatterTime = null;
			SimpleDateFormat _currentFormate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
			GregorianCalendar _currentCalendar = new GregorianCalendar();
			Date _currentDate = _currentCalendar.getTime();

			try{
				if ((dateFormate != null && dateFormate.length() > 0)  && (timeFormate != null && timeFormate.length() > 0)) {
					formatterDate = new SimpleDateFormat(dateFormate);
					formatterTime = new SimpleDateFormat(timeFormate);
					return ""+formatterDate.format(_currentDate)+","+formatterTime.format(_currentDate);
				}else if((dateFormate != null && dateFormate.length() > 0) && (timeFormate == null || timeFormate.equalsIgnoreCase(""))){
					formatterDate = new SimpleDateFormat(dateFormate);
					return ""+formatterDate.format(_currentDate);
				}else if((dateFormate == null || dateFormate.equalsIgnoreCase("")) && (timeFormate != null && !timeFormate.equalsIgnoreCase(""))){
					formatterTime = new SimpleDateFormat(timeFormate);
					return formatterTime.format(_currentDate);
				}else{
					formatterDate = new SimpleDateFormat("yyyy-MM-dd");
					formatterTime = new SimpleDateFormat("HH:mm:ss");
					return ""+formatterDate.format(_currentDate)+","+formatterTime.format(_currentDate);
				}
			}catch(Exception ex){
				ex.printStackTrace();
				return ""+_currentDate;
			}
	}


	 /**
	  * Converting given string into date format
	  * @param dateStr  ---> Given date
	  * @param _dateFormatteStr   ----> Given date format
	  * @return  ----> converted date   (if any exception occurred returns null)
	  */
	 public Date convertStringToDate(String dateStr, String _dateFormatteStr){
		 Date _convertedDate = null;
		 SimpleDateFormat _dateFormate = new SimpleDateFormat(""+_dateFormatteStr);
		 try {
			_convertedDate = _dateFormate.parse(_dateFormatteStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return _convertedDate;
	 }

	 /**
	  * Converting given date into required format
	  * @param _date --> Date to convert
	  * @param requiredFormat   ----> Format required
	  * @return  final date string
	  */
	 public String convertDateToString(Date _date, String requiredFormat){
		 String _finalDateStr = "";
		 SimpleDateFormat _dateFormatter = new SimpleDateFormat(requiredFormat);
		 try{
			 _finalDateStr = _dateFormatter.format(_date);
		 }catch(Exception ex){
			 ex.printStackTrace();
		 }
		 return _finalDateStr;
	 }


    public static String getCopyright(final Context context) {
        String version;
        Calendar c = Calendar.getInstance();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            StringBuilder sb = new StringBuilder();
            sb.append("Version ").append(packageInfo.versionName)
                    // .append("\n(Build ").append(Config.BUILD_ID).append(")\n")
                    .append(String.format("\nCopyright " + Html.fromHtml("&copy;") + "2010-" + c.get(Calendar.YEAR) + ",\n Pristine Embed.\nAll Rights Reserved."));
            version = sb.toString();
        } catch (PackageManager.NameNotFoundException e) {
            version = Html.fromHtml("&copy;") + "2010" + c.get(Calendar.YEAR) + "\nPristine Embed.\nAll Rights Reserved.";
        }
        return version;
    }

    public static String[] getGenericArrayValues(String type, int size){
        String[] items = new String[size];
        for (int i = 0; i <= size -1; i++){
            if(type.length() == 0){
                if(i == size -1)
                    items[i]=""+i+"+";
                else
                    items[i]=""+i;
            }else{
                items[i]=""+(i+1980);
            }
        }
        return items;
    }

    public static int getImageOrientation(String imagePath){
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static int getIndex(Set<? extends String> set, String value) {
    	   int result = 0;
    	   for (String entry:set) {
    	     if (entry.equalsIgnoreCase(value)) return result;
    	     result++;
    	   }
    	   return -1;
    }




    public static String getProperInt(String inputStr){
    	String delimiter = "\\.";
    	if(null != inputStr && !inputStr.equalsIgnoreCase("null")){
    	if (inputStr.contains(".")) {
    		String[] outputStr = null;
        	if (inputStr.length() > 0) {
        		outputStr = inputStr.split(delimiter);
    		}
        	return (null != outputStr && outputStr.length > 0) ? outputStr[0] : "0";
		}else{
			return inputStr;
		}
    	}else{
			return "0";
		}
    }

    public static void showKeyboard(Context context, TextView textView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            // only will trigger it if no physical keyboard is open
            imm.showSoftInput(textView, 0);
        }
    }

    public static void hideKeyboard(Context context, View textView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            // only will trigger it if no physical keyboard is open
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
        }
    }

    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return startsWith(str, prefix, true);
    }

    private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return (str == null && prefix == null);
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
    }


    public static int parseIntValue(String inputValue){
    	if (!TextUtils.isEmpty(inputValue) && TextUtils.isDigitsOnly(inputValue)) {
    		try {
    			return Integer.parseInt(inputValue);
    		} catch (Exception e) {
    			// TODO: handle exception
    		}
		}
    	return 0;
    }

    public static int getKeysCount(Iterator<?> itKeys){
    	if (itKeys instanceof Collection<?>) {
			  return ((Collection<?>)itKeys).size();
		}

    	return 0;
    }


    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }



    public static String jsonReturn(String selectedSku, String itemName, String itemDescription, String capacity, String onHand, String parValue, String critical, String parentId, boolean isActive){

    	String message;
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject item = new JSONObject();
		try {
			item.put("\'"+"PropertyID"+"\'", "\'"+"1"+"\'");
			item.put("\'"+"value"+"\'", "\'"+selectedSku+"\'");
			array.put(item);
			JSONObject item1 = new JSONObject();
			item1.put("\'"+"PropertyID"+"\'", "\'"+"2"+"\'");
			item1.put("\'"+"value"+"\'", "\'"+itemName+"\'");
			array.put(item1);
			JSONObject item2 = new JSONObject();
			item2.put("\'"+"PropertyID"+"\'", "\'"+"3"+"\'");
			item2.put("\'"+"value"+"\'", "\'"+itemDescription+"\'");
			array.put(item2);

			if (capacity.trim().length() > 0) {
				JSONObject item3 = new JSONObject();
				item3.put("\'"+"PropertyID"+"\'", "\'"+"14"+"\'");
				item3.put("\'"+"value"+"\'", "\'"+capacity+"\'");
				array.put(item3);
			}

			if (onHand.trim().length() > 0) {
				JSONObject item4 = new JSONObject();
				item4.put("\'"+"PropertyID"+"\'", "\'"+"15"+"\'");
				item4.put("\'"+"value"+"\'", "\'"+onHand+"\'");
				array.put(item4);
			}

			if(isActive){
				if (parValue.trim().length() > 0) {
					JSONObject item5 = new JSONObject();
					item5.put("\'"+"PropertyID"+"\'", "\'"+"27"+"\'");
					item5.put("\'"+"value"+"\'", "\'"+parValue+"\'");
					array.put(item5);
				}

				if (critical.trim().length() > 0) {
					JSONObject item6 = new JSONObject();
					item6.put("\'"+"PropertyID"+"\'", "\'"+"31"+"\'");
					item6.put("\'"+"value"+"\'", "\'"+critical+"\'");
					array.put(item6);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			json1.put(""+"\'"+"Table1"+"\'", array);
			json.put("myObj", json1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			json.put("ParentID", parentId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		message = json.toString();
		message = message.replace("\"'", "'");
		message = message.replace("'"+"\"", "'");
		message = message.replace(":{",":"+ "\"{");
		message = message.replace("}]}","}]}"+"\"");
    	return message;
    }


    public static String jsonReturnCycle(String value,String date, String parentId){

    	String message;
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject item = new JSONObject();
		try {
			item.put("\'"+"PropertyID"+"\'", "\'"+"24"+"\'");
			item.put("\'"+"value"+"\'", "\'"+value+"\'");
			array.put(item);
			JSONObject item1 = new JSONObject();
			item1.put("\'"+"PropertyID"+"\'", "\'"+"25"+"\'");
			item1.put("\'"+"value"+"\'", "\'"+date+"\'");
			array.put(item1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			json1.put(""+"\'"+"Table1"+"\'", array);
			json.put("myObj", json1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			json.put("ParentID", parentId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		message = json.toString();
		message = message.replace("\"'", "'");
		message = message.replace("'"+"\"", "'");
		message = message.replace(":{",":"+ "\"{");
		message = message.replace("}]}","}]}"+"\"");
    	return message;
    }


    public static ArrayList<String> getPrimaryEmail(Context context){
    	ArrayList<String> mailIds = new ArrayList<String>();
    	Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
    	Account[] accounts = AccountManager.get(context).getAccounts();
    	for (Account account : accounts) {
    	    if (emailPattern.matcher(account.name).matches()) {
    	    	if(!mailIds.contains(account.name)){
    	    		mailIds.add(account.name);
    	    	}
    	    }
    	}
    	return mailIds;
    }

    public static String stringoperation(String str) {
        if (str.length() > 0) {
          str = str.substring(0, 10);
          str=str+"..";
        }
        return str;
    }

//    public static String Encrypt(byte[] password)
//    {
//        MD5 md5 = MD5.Create();
//        byte[] b = md5.ComputeHash(password);
//        String hash = "";
//        return hash;
//    }
}
