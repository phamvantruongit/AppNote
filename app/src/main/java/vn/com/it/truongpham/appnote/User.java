package vn.com.it.truongpham.appnote;

public class User {
    public String token;
    public String device_id;

    public User(String token, String device_id) {
        this.token = token;
        this.device_id = device_id;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "{" +
                ", device_id='" + device_id + '\'' +
                '}';
    }
}
