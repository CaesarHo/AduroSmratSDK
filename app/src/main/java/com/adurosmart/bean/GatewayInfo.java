package com.adurosmart.bean;

import com.adurosmart.adapters.SimpleAdapter;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by best on 2016/7/8.
 */
public class GatewayInfo implements Serializable{

    public static final String PROPERTY_ACCESSIBLE_BY_LAN = "accessibleByLAN";
    public static final String PROPERTY_ACCESSIBLE_BY_CLOUD = "accessibleByCloud";

    public static final byte[] BROADCAST_ADDRESS = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    private final Object COMMUNICATION_LOCK = new Object();
    public Object getCommunicationLock() {
        return COMMUNICATION_LOCK;
    }

    private byte[] aesKey;
    private InetAddress inetAddress;
    private int port;
    private String uniqueid;
    private static GatewayInfo gatewayInfo;

    private String firmwareVersion = "v0.00";
    private boolean accessibleByLAN, accessibleByCloud;
//    private ControlLeds.State leds = ControlLeds.State.ON;

    private static Map<String, GatewayInfo> pool = new HashMap<String, GatewayInfo>();

    public synchronized static GatewayInfo getInstance() {
        if (null == gatewayInfo) {
            synchronized (GatewayInfo.class) {
                if (null == gatewayInfo) {
                    gatewayInfo = new GatewayInfo();
                }
            }
        }
        return gatewayInfo;
    }

    // FIXME: private
    public GatewayInfo() {
    }

    public GatewayInfo(String key, String version) {
        this.aesKey = key.getBytes(com.adurosmart.utils.StandardCharsets.UTF_8);
        this.firmwareVersion = version;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String version) {
        this.firmwareVersion = version;
    }

    public void setGatewayUniqueId(String uniqueid){
        this.uniqueid = uniqueid;
    }

    public String getGatewayUniqueId(){
        return uniqueid;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public byte[] getAesKey() {
        return aesKey;
    }

    public void setAesKey(byte[] aesKey) {
        this.aesKey = aesKey;
    }

    /**
     * @return whether this gateway is accessible via LAN
     */
    @Deprecated // Use getInetAddress == null
    public boolean isAccessibleByLAN() {
        return accessibleByLAN;
    }

    @Deprecated // Use getInetAddress == null
    public boolean setAccessibleByLAN(boolean value) {
        if (accessibleByLAN != value) {
            this.accessibleByLAN = value;
            return true;
        }
        return false;
    }

    /**
     * @return whether the gateway has connectivity to the cloud
     */
    public boolean isAccessibleByCloud() {
        return accessibleByCloud;
    }

    public void setAccessibleByCloud(boolean value) {
        if (accessibleByCloud != value) {
            accessibleByCloud = value;
        }
    }

//    public ControlLeds.State getLeds(){
//        return leds;
//    }
//
//    public void setLeds(ControlLeds.State state){
//        this.leds = state;
//    }

    @Override
    public String toString() {
        return String.format("%s (%s, In LAN: %b, Cloud: %b, %s)",
                getFirmwareVersion(),
                isAccessibleByLAN(),
                isAccessibleByCloud(),
                super.toString());
    }

    public static class Adapter extends SimpleAdapter implements JsonDeserializer<GatewayInfo> {
        @Override
        public GatewayInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject object = json.getAsJsonObject();
//                String macAddressHexString = readString(object, Keys.JSON_PROPERTY_MAC_ADDRESS);
//                if (!TextUtils.isEmpty(macAddressHexString) && macAddressHexString.matches(Bytes.MAC_ADDRESS_REGEX)) {
//                    Character[] chars = Collections.toObjectArray(macAddressHexString.toCharArray());
//                    String macAddress = String.format("%C%C:%C%C:%C%C:%C%C:%C%C:%C%C", chars);
//
//                    String aesHexKey = readString(object, Keys.JSON_PROPERTY_AES_KEY);
//                    if (!TextUtils.isEmpty(aesHexKey) && aesHexKey.length() == 32) {
//                        byte[] aesKey = Bytes.hexStringToByteArray(aesHexKey);
//                        Gateway gateway = Gateway.getInstance(macAddress);
//                        gateway.setAesKey(aesKey);
//                        return gateway;
//                    }
//                }
            }
            return null;
        }
    }
}

