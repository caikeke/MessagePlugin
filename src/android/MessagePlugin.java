package com.message.yhck;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagePlugin extends CordovaPlugin{

  private CallbackContext mCallbackContext;
  private List<MessageInfo> messageInfoList;
  private ContentResolver cr;
  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    this.mCallbackContext = callbackContext;
    if (!"".equals(action) || action != null) {
      getMessageInfo();
      return true;
    }
    mCallbackContext.error("error");
    return false;
  }

  private void getMessageInfo(){
    //得到访问者ContentResolver
    cr = cordova.getActivity().getContentResolver();
    messageInfoList = new ArrayList<MessageInfo>();
    Uri uri = Uri.parse("content://sms/");
    Cursor cursor = cr.query(uri, null, null, null, null);
    while (cursor.moveToNext()) {
      String body = cursor.getString(cursor.getColumnIndex("body"));//内容
      long data = cursor.getLong(cursor.getColumnIndex("date"));//时间
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date mDate = new Date(data);
      String msgDate = simpleDateFormat.format(mDate);
      String address = cursor.getString(cursor.getColumnIndex("address"));//电话
      Date currentDate = new Date();
      Calendar calendar = Calendar.getInstance(); //得到日历
      calendar.setTime(currentDate);//把当前时间赋给日历
      calendar.add(calendar.MONTH, -3);  //设置为前3月
      currentDate = calendar.getTime();//获取3个月前的时间
      if (data >= currentDate.getTime()) {
        MessageInfo item = new MessageInfo();
        item.setBody(body);
        item.setDate(msgDate);
        item.setAddress(address);
        messageInfoList.add(item);
      }
    }
    cursor.close();
    if(messageInfoList.size()>0){
      packagJSONData(messageInfoList);
    }else {
      mCallbackContext.success("");
    }
  }
  private void packagJSONData(List<MessageInfo> messageInfoList ){
    JSONArray mJSonArray = new JSONArray();
    for (MessageInfo item :messageInfoList){
      JSONObject jsonObject = new JSONObject();
      try {
        jsonObject.put("body",item.getBody());
        jsonObject.put("date",item.getDate());
        jsonObject.put("phone",item.getAddress());
        mJSonArray.put(jsonObject);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    mCallbackContext.success(mJSonArray);
  }
  private class MessageInfo{
    String body;//内容
    String date;//时间
    String address;//电话

    public String getBody() {
      return body;
    }

    public void setBody(String body) {
      this.body = body;
    }

    public String getDate() {
      return date;
    }

    public void setDate(String date) {
      this.date = date;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }
  }
}
