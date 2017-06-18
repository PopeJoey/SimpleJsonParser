package compiler;

/**
 * Created by Administrator on 2017/5/28.
 */
public class OtherValue implements Json,Value {//class represent for null,false,true,string in json
    private String value;

    public OtherValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
    @Override
    public Object value() {
        return value;
    }
}
