package compiler;

/**
 * Created by Administrator on 2017/5/28.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class JObject implements Json,Value {
  public Map<String,Value> map=new HashMap<String,Value>();

    public JObject(Map<String, Value> map) {
        this.map = map;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\r\n");
        int size = map.size();
        for (String key : map.keySet()) {
            sb.append(key + " : " + map.get(key));
            if (--size != 0) {
                sb.append(", \r\n");
            }
        }
        sb.append("\r\n}");
        return sb.toString();
    }

    @Override
    public Object value() {
        return this;
    }
}
