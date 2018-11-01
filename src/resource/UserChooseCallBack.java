package resource;

import java.util.List;

public abstract interface UserChooseCallBack {
    public abstract boolean action();
    public abstract void setUserList(List<User> user);
}
