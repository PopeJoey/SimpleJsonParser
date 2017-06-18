package compiler;
/**
 * Created by Administrator on 2017/5/25.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
public class Source implements AutoCloseable{
    public static final char EOF=(char) 0;
    public static final char EOL='\n';
    private BufferedReader source ;
    private String line;
    private int lineNum;
    private int currentPosition;
    public Source(String path){
        try{
            this.source=new BufferedReader(new FileReader(path));
            line=readNextLine();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

    }
    public int getLineNum(){
        return lineNum;
    }
    public int getCurrentPosition(){
        return currentPosition;
    }
    public char nextChar(){
        currentPosition++;
        if (line ==null)
            return Source.EOF;
        if (line.charAt(currentPosition)==Source.EOL||line.length()==0){
            line=readNextLine();
            return nextChar();
        }
        return line.charAt(currentPosition);
    }
    private String readNextLine(){
        String s=null;
        try{
            s=source.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }
        currentPosition=-1;
        if(s!=null){
            lineNum=getLineNum()+1;
            s=s+Source.EOL;
        }
        return  s;
    }

    @Override
    public void close() throws IOException {
        if (source!=null){
            source.close();
        }
    }
    public char peekChar(int offset){
        if (line==null||"".equals(line)){
            return  nextChar();
        }
        int index=currentPosition+offset;
        if (index>=line.length()||index<=-1){
            return Source.EOL;
        }
//        if (line.charAt(index)==EOL){
//           return this.peekChar();
//        }
        return line.charAt(index);
    }
    public char peekChar(){
        return this.peekChar(1);
    }
}
