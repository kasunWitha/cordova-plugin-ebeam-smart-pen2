package cordova.plugin.ebeam.smart.pen;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.luidia.ebeam.pen.sdk.EBeamSPController;
import com.luidia.ebeam.pen.sdk.constants.PenEvent;
import com.luidia.ebeam.pen.sdk.constants.PenMessage;
import com.luidia.ebeam.pen.sdk.listener.PenEventListener;
import com.luidia.ebeam.pen.sdk.listener.PenMessageListener;

/**
 * This class echoes a string called from JavaScript.
 */
public class EbeamSmartpen extends CordovaPlugin {

    private EBeamSPController penController;
    private CordovaWebView web;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("create")) {
            //String message = args.getString(0);
            this.create(callbackContext);
            return true;
        }else if (action.equals("isPenMode")) {
            this.isPenMode(callbackContext);
            return true;
        }else if (action.equals("connect")) {
            String deviceName = args.getJSONObject(0).getString("deviceName");
            String deviceAddress = args.getJSONObject(0).getString("deviceAddress");
            this.connect(deviceName, deviceAddress, callbackContext);
            return true;
        }else if (action.equals("start")) {
            String message = args.getString(0);
            callbackContext.success(message);
            System.out.println("start method called");
            return true;
        }
        return false;
    }

    private void create(CallbackContext callbackContext) {
       
        
        callbackContext.success("true");
    }

    private void isPenMode(CallbackContext callbackContext) {
        boolean status = penController.isPenMode();

        if(status){
            callbackContext.success("true");
        }else{
            callbackContext.success("false");
        }
    }

    private void connect(String deviceName, String deviceAddress, CallbackContext callbackContext){
        penController.connect(deviceName, deviceAddress);
        callbackContext.success();
    }



    @Override
    public void onPenEvent(int i, int i1, int i2, Object o) {

        final String message = "["+i+","+i1+","+i2+"]";

        sendEventCallback(message);

    }

    void sendEventCallback(String message){
        final String text = message;
        cordova.getActivity().runOnUiThread(new Runnable(){
            public void run(){
                web.loadUrl("javascript:window.plugins.EbeamSmartpen.onEventReceived("+text+")");
            }
        });
    }


    @Override
    public void onPenMessage(int i, int i1, int i2, Object o) {
        final String message = "["+i+"]";

        sendMessageCallback(message);
    }

    void sendMessageCallback(String message){
        final String text = message;
        cordova.getActivity().runOnUiThread(new Runnable(){
            public void run(){
                web.loadUrl("javascript:window.plugins.EbeamSmartpen.onMessageReceived("+text+")");
            }
        });
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView){
        super.initialize(cordova, webView);
        this.web = webView;

        Context context = cordova.getActivity().getApplicationContext();
        EBeamSPController.create(context);
        penController =EBeamSPController.getInstance();

        penController.setPenMessageListener(context);
        penController.setPenEventListener(context);
    }


    
}
