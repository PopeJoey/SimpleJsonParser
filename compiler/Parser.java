package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by Administrator on 2017/5/28.
 */
public class Parser {
   public Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        try {
            this.lexer.tokenize();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public JObject object() throws Exception{
        lexer.next();
        Map<String, Value> map = new HashMap<>();
        if (isToken(TokenType.END_OBJ)) {
            lexer.next(); //consume '}'
            return new JObject(map);
        } else if (isToken(TokenType.STRING)) {
            map = key(map);
        }
        return new JObject(map);
    }
    private Map<String, Value> key(Map<String, Value> map) throws  Exception{
        String key = lexer.next().getValue();
        if (!isToken(TokenType.COLON)) {
            throw new SyntaxException("Invalid,line "+lexer.peek(0).getLine()+"position:"+lexer.peek(0).getPosition()+"expected <:>");
        } else {
            lexer.next(); //consume ':'
            if (isOtherValue()) {
                Value primary = new OtherValue(lexer.next().getValue());
                map.put(key, primary);
            } else if (isToken(TokenType.START_ARRAY)) {
                Value array = array();
                map.put(key, array);
            } else if (isToken(TokenType.START_OBJ)) {
                JObject object =object();
                map.put(key,object);
            }
            if (isToken(TokenType.COMMA)) {
                lexer.next(); //consume ','
                if (isToken(TokenType.STRING)) {
                    map = key(map);
                }
            } else if (isToken(TokenType.END_OBJ)) {
                lexer.next(); //consume '}'
                return map;
            } else {
                throw new SyntaxException("Invalid,line "+lexer.peek(0).getLine()+"position:"+lexer.peek(0).getPosition()+"expected<,> or <}>");
            }
        }
        return map;
    }
    public JArray array() throws Exception{
        lexer.next(); //consume '['
        List<Json> list = new ArrayList<>();
        JArray array = null;
        if (isToken(TokenType.START_ARRAY)) {
            array = array();
            list.add(array);
            if (isToken(TokenType.COMMA)) {
                lexer.next(); //consume ','
                list = element(list);
            }
        } else if (isOtherValue()) {
            list = element(list);
        } else if (isToken(TokenType.START_OBJ)) {
            list.add(object());
            while (isToken(TokenType.COMMA)) {
                lexer.next(); //consume ','
                list.add(object());
            }

        } else if (isToken(TokenType.END_ARRAY)) {
            lexer.next(); //consume ']'
            array =  new JArray(list);
            return array;
        }
        lexer.next(); //consume ']'
        array = new JArray(list);
        return array;
    }
    private List<Json> element(List<Json> list) throws Exception{
        list.add(new OtherValue(lexer.next().getValue()));
        if (isToken(TokenType.COMMA)) {
            lexer.next(); //consume ','
            if (isOtherValue()) {
                list = element(list);
            } else if (isToken(TokenType.START_OBJ)) {
                list.add(object());
            } else if (isToken(TokenType.START_ARRAY)) {
                list.add(array());
            } else {
                throw new SyntaxException("Invalid,line "+lexer.peek(0).getLine()+"position:"+lexer.peek(0).getPosition()+"expected <{> or <[> or some value");
            }
        } else if (isToken(TokenType.END_ARRAY)) {
            return list;
        } else {
            throw new SyntaxException("Invalid,line "+lexer.peek(0).getLine()+"position:"+lexer.peek(0).getPosition()+"expected <]>");
        }
        return list;
    }
    private Json json() throws Exception{
        TokenType type = lexer.peek(0).getType();
        if (type == TokenType.START_ARRAY) {
            return array();
        } else if (type == TokenType.START_OBJ) {
            return object();
        } else {
            throw new SyntaxException("Invalid,line "+lexer.peek(0).getLine()+"position:"+lexer.peek(0).getPosition()+"expected <{> or <[>");
        }
    }
    private boolean isToken(TokenType tokenType) {
        Token t = lexer.peek(0);
        return t.getType() == tokenType;
    }
    private boolean isToken(String name) {
        Token t = lexer.peek(0);
        return t.getValue().equals(name);
    }
    private boolean isOtherValue() {
        TokenType type = lexer.peek(0).getType();
        return type == TokenType.BOOLEAN || type == TokenType.NULL  ||
                type == TokenType.NUMBER || type == TokenType.STRING;
    }
    public Json parse() throws Exception {
        Json result = json();

        return result;
    }


}
