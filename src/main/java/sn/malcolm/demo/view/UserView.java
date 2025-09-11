package sn.malcolm.demo.view;

public class UserView {
    public interface UserRead extends View.Public, View.ReadOnly {}
    public interface UserReadDetail extends UserRead {}
    public interface UserWrite {}
    public interface UserUpdate {}
    public interface UserChangePassword {}
    public interface UserActivate {}
}
