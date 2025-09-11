package sn.malcolm.demo.view;

public class View {
    public interface Public {}
    public interface ReadOnly {}
    public interface ReadOnlyDetail extends ReadOnly {}
    public interface PublicDetail extends Public {}

}
