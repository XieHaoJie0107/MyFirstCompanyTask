package xhj.zime.com.mymaptest.Model;

import org.litepal.crud.LitePalSupport;

public class Express extends LitePalSupport{
    private int user_id;
    private byte[] user_img;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public byte[] getUser_img() {
        return user_img;
    }

    public void setUser_img(byte[] user_img) {
        this.user_img = user_img;
    }
}
