package compiler;

/**
 * Created by Administrator on 2017/5/28.
 */

import java.util.ArrayList;
import java.util.List;
public class JArray implements Json,Value{
    private List<Json> list = new ArrayList<>();

    public JArray(List<Json> list) {
        this.list = list;
    }
    public int length() {
        return list.size();
    }

    public void add(Json element) {
        list.add(element);
    }

    public Json get(int i) {
        return list.get(i);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ \r\n");
        for (int i =0; i < list.size(); i++) {
            sb.append(list.get(i).toString());
            if (i != list.size() - 1) {
                sb.append(", \r\n");
            }
        }
        sb.append(" \r\n]");
        return sb.toString();
    }

    @Override
    public Object value() {
        return this;
    }
}
