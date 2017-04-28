package io.realm;


public interface UserEntityRealmProxyInterface {
    public int realmGet$id();
    public void realmSet$id(int value);
    public String realmGet$token();
    public void realmSet$token(String value);
    public String realmGet$refreshToken();
    public void realmSet$refreshToken(String value);
    public String realmGet$tokenExp();
    public void realmSet$tokenExp(String value);
    public String realmGet$phoneNumber();
    public void realmSet$phoneNumber(String value);
    public String realmGet$email();
    public void realmSet$email(String value);
    public String realmGet$fullName();
    public void realmSet$fullName(String value);
    public String realmGet$password();
    public void realmSet$password(String value);
    public String realmGet$tokenDevice();
    public void realmSet$tokenDevice(String value);
    public String realmGet$profileImage();
    public void realmSet$profileImage(String value);
    public String realmGet$birthday();
    public void realmSet$birthday(String value);
    public int realmGet$gender();
    public void realmSet$gender(int value);
    public String realmGet$job();
    public void realmSet$job(String value);
    public String realmGet$description();
    public void realmSet$description(String value);
    public String realmGet$loginAt();
    public void realmSet$loginAt(String value);
}
