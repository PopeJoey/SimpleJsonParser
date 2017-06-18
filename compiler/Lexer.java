package compiler;


import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/25.
 */
public class Lexer {
    private Source source;
    public StringBuilder sb;
    private static final char[] HEX="abcdefABCDEF".toCharArray();
    private ArrayList<Token> tokens=new ArrayList<Token>();
    public void tokenize() throws Exception {
        Token token;
        do {
            token = start();
            tokens.add(token);
        } while (token.getType() != TokenType.END_DOC);
    }
    public ArrayList<Token> getTokens() {

        return tokens;
    }
    public Token peek(int i) {
        return tokens.get(i);
    }
    public Token next() {
        return tokens.remove(0);
    }
    public boolean hasNext() {
        return tokens.get(0).getType() != TokenType.END_DOC;
    }
    public Lexer(Source source) {
        this.source = source;
    }
    public Token start() throws Exception{
        char ch= 0;

        ch = source.nextChar();

        while (Character.isWhitespace(ch)){
            ch=source.nextChar();
        }
        if (ch==','){
            return new Token(TokenType.COMMA,",",source.getLineNum(),source.getCurrentPosition());
        }else if (ch==':'){
            return new Token(TokenType.COLON,":",source.getLineNum(),source.getCurrentPosition());
        }else  if (ch=='{'){
            return new Token(TokenType.START_OBJ,"{",source.getLineNum(),source.getCurrentPosition());
        }else if(ch=='}'){
            return new Token(TokenType.END_OBJ,"}",source.getLineNum(),source.getCurrentPosition());
        }else if(ch=='['){
            return new Token(TokenType.START_ARRAY,"[",source.getLineNum(),source.getCurrentPosition());
        }else if(ch==']'){
            return new Token(TokenType.END_ARRAY,"]",source.getLineNum(),source.getCurrentPosition());
        }else if (isTrue(ch)){
            return new Token(TokenType.BOOLEAN ,"true",source.getLineNum(),source.getCurrentPosition());
        }else if (isFalse(ch)){
            return new Token(TokenType.BOOLEAN,"false",source.getLineNum(),source.getCurrentPosition());
        }else if(isNull(ch)){
            return new Token(TokenType.NULL,"null",source.getLineNum(),source.getCurrentPosition());
        } else if (ch=='"') {
            return readString();
        } else if (isNum(ch)) {
             StringBuilder number=new StringBuilder();
             number.append(ch);
             if(ch=='-'){
                 ch=source.nextChar();
                 if(ch=='0') {
                     if(source.peekChar()!='.'){
                         return new Token(TokenType.NUMBER,"0",source.getLineNum(),source.getCurrentPosition());
                     }else if(source.peekChar()=='.'){
                         ch=source.nextChar();
                         number.append(ch);
                         while (Character.isDigit(source.peekChar())||isScientific(source.peekChar())){
                             ch=source.nextChar();
                             number.append(ch);
                         }
                     }
                 }else if (Character.isDigit(ch)){
                     while(Character.isDigit(source.peekChar())){
                         ch=source.nextChar();
                         number.append(ch);
                     }
                     if(source.peekChar()=='.'){
                         ch=source.nextChar();
                         number.append(ch);
                         while (Character.isDigit(source.peekChar())||isScientific(source.peekChar())){
                             ch=source.nextChar();
                             number.append(ch);
                         }
                     }

                 }else {
                     throw new LexException("invalid ,line "+String.valueOf(source.getLineNum())+",position "+String.valueOf(source.getCurrentPosition() )+" invalid number");
                 }

             } else if(ch=='0') {
                  if(source.peekChar()!='.'){
                      return new Token(TokenType.NUMBER,"0",source.getLineNum(),source.getCurrentPosition());
                  }else if(source.peekChar()=='.'){
                      ch=source.nextChar();
                      number.append(ch);
                      while (Character.isDigit(source.peekChar())||isScientific(source.peekChar())){
                          ch=source.nextChar();
                          number.append(ch);
                      }
                  }
             }else {//1-9
                  while(Character.isDigit(source.peekChar())){
                      ch=source.nextChar();
                      number.append(ch);
                  }
                  if(source.peekChar()=='.'){
                      ch=source.nextChar();
                      number.append(ch);
                      while (Character.isDigit(source.peekChar()) ||isScientific(source.peekChar()) ){
                          ch=source.nextChar();
                          number.append(ch);
                      }
                  }
             }
              return new Token(TokenType.NUMBER,number.toString(),source.getLineNum(),source.getCurrentPosition());

        } else if (ch==Source.EOF) {
            return new Token(TokenType.END_DOC,"EOF",source.getLineNum(),source.getCurrentPosition());
        }else {
            throw new LexException("invalid,line "+String.valueOf(source.getLineNum())+",position "+String.valueOf(source.getCurrentPosition() )+" invalid identifier");
        }

    }
    private Boolean isNull(char c) throws Exception{
         if (c=='n'){
             c=source.nextChar();
             if(c=='u'){
                 c=source.nextChar();
                 if (c=='l'){
                     c=source.nextChar();
                     if (c=='l'){
                         return true;
                     }else {
                         throw new LexException("invalid,line "+String.valueOf(source.getLineNum())+",position "+String.valueOf(source.getCurrentPosition() )+" expected <null>");
                     }
                 }else{
                     throw new LexException("invalid,line "+String.valueOf(source.getLineNum())+",position "+String.valueOf(source.getCurrentPosition() )+" expected <null>");
                 }
             }else{
                 throw new LexException("invalid,line "+String.valueOf(source.getLineNum())+",position "+String.valueOf(source.getCurrentPosition() )+" expected <null>");
             }
         }else {
             return false;
         }

    }
    private Boolean isTrue(char c)throws Exception{
        if (c=='t'){
            c=source.nextChar();
            if(c=='r'){
                c=source.nextChar();
                if (c=='u'){
                    c=source.nextChar();
                    if (c=='e'){
                        return true;
                    }else {
                        throw new LexException("invalid,line "+String.valueOf(source.getLineNum())+",position "+String.valueOf(source.getCurrentPosition() )+" expected <true>");
                    }
                }else{
                    throw new LexException("invalid,line "+String.valueOf(source.getLineNum())+",position "+String.valueOf(source.getCurrentPosition() )+" expected <true>");
                }
            }else{
                throw new LexException("invalid,line "+String.valueOf(source.getLineNum())+",position "+String.valueOf(source.getCurrentPosition() )+" expected <true>");
            }
        }else {
           return false;
        }

    }
    private Boolean isFalse(char c)throws Exception {
        if (c=='f'){
            c=source.nextChar();
            if(c=='a'){
                c=source.nextChar();
                if (c=='l'){
                    c=source.nextChar();
                    if (c=='s'){
                        c=source.nextChar();
                        if (c=='e'){
                            return true;
                        }
                        else {
                            throw new LexException("invalid,line "+String.valueOf(source.getLineNum())+",position "+String.valueOf(source.getCurrentPosition() )+"expected <false>");
                        }
                    }else {
                        throw new LexException("invalid,line "+String.valueOf(source.getLineNum())+",position "+String.valueOf(source.getCurrentPosition() )+" expected <false>");
                    }
                }else{
                    throw new LexException("invalid,line "+String.valueOf(source.getLineNum())+",position "+String.valueOf(source.getCurrentPosition() )+" expected <false>");
                }
            }else{
                throw new LexException("invalid,line "+String.valueOf(source.getLineNum())+",position "+String.valueOf(source.getCurrentPosition() )+" expected <false>");
            }
        }else {
            return false;
        }

    }
    private Token readString() throws Exception{
       sb=new StringBuilder();
        char c;
        while (true){
           c=source.nextChar();
            if(c=='"'){
               return new Token(TokenType.STRING,sb.toString(),source.getLineNum(),source.getCurrentPosition());
            } else if (isEscape(c)) {
                if (c=='u'){
                    sb.append('\\' + (char) c);
                    for (int i = 0; i < 4; i++) {
                        c=source.nextChar();
                        if(isHex(c)){
                            sb.append((char) c);
                        }
                        else {
                            throw new LexException("invalid,line "+String.valueOf(source.getLineNum())+",position "+String.valueOf(source.getCurrentPosition() )+" expected hex number");
                        }
                    }
                }else {
                    sb.append("\\" + (char) c);
                }

            } else if (c == '\r' || c == '\n'||c=='{'||c=='}'||c=='['||c==']') {
                throw new LexException("invalid,line "+String.valueOf(source.getLineNum())+",position "+String.valueOf(source.getCurrentPosition() )+" expected \" or unknown character");
            }else {

                sb.append((char)c);
            }

        }

    }
    private boolean isEscape(char c)   throws Exception{
        if (c == '\\') {
            c = source.nextChar();
            if (c == '"' || c == '\\' || c == '/' || c == 'b' ||
                    c == 'f' || c == 'n' || c == 't' || c == 'r' || c == 'u') {
                return true;
            } else {
                throw new LexException("invalid "+String.valueOf(source.getLineNum())+String.valueOf(source.getCurrentPosition() )+" expected json defined character");
            }
        } else {
            return false;
        }
    }
    private boolean isNum(char ch){
        if(Character.isDigit(ch)||ch=='-'){
            return true;
        }else {
            return false;
        }
    }
    private boolean isScientific(char ch){
        return  ch=='e'||ch=='E';
    }
    private static final boolean isHex(final char ch) {
        for (int i=0;i<HEX.length;i++) {
            if (HEX[i]==ch) {
                return true;
            }
        }
        return Character.isDigit(ch);
    }
//    public boolean hasNext(){
//        return source.peekChar()!=Source.EOF;
//    }

}
