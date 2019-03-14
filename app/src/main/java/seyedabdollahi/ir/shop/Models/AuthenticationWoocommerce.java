package seyedabdollahi.ir.shop.Models;

public class AuthenticationWoocommerce {

    private String key;
    private String signatureMethod;
    private String nonce;
    private String timestamp;
    private String version;
    private String signature;

    public AuthenticationWoocommerce(String key, String nonce, String timestamp, String signature) {
        this.key = key;
        this.nonce = nonce;
        this.timestamp = timestamp;
        this.signature = signature;
        signatureMethod = "HMAC-SHA1";
        version = "1.0";
    }

    public String getKey() {
        return key;
    }

    public String getSignatureMethod() {
        return signatureMethod;
    }

    public String getNonce() {
        return nonce;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getVersion() {
        return version;
    }

    public String getSignature() {
        return signature;
    }
}
