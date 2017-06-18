import compiler.*;
import sun.security.jca.GetInstance;
import java.io.*;
import java.util.HashMap;
import java.util.Objects;


/**
 * Created by Administrator on 2017/5/28.
 */
public class Test {
    public static void main(String args[]){

        if (args.length==1){
            usage1(args[0]);
        }else if (args.length==2){
            if (args[0].equals("-pretty")){
                usage2(args[1]);
            }
            else {
                String result= usage3(args[0],args[1]);
                System.out.println(result);
            }
        }else {
            usage();
        }
  //        usage1("afalse.json");

 //      String result= usage3("country.json","/RECORDS/35/countryname");
//        System.out.println(result);
    }
    public static void usage(){
        System.out.println("Usage1: java -jar json.jar jsonFilePath");
        System.out.println("Usage2: java -jar json.jar -pretty  jsonFilePath");
        System.out.println("Usage3: java -jar json.jar jsonFilePath queryPath" );
    }
    public static void usage1(String path){
        Source s=new Source(path);
        Lexer l=new Lexer(s);
        Parser p=new Parser(l);
        try {
            p.parse();
            System.out.println("valid");
            s.close();
        }
        catch (LexException|SyntaxException e){
            System.err.println(e.getMessage());

        }catch (Exception e){
            System.err.println(e.getMessage());
        }

    }
    public static void usage2(String path){
        Source s=new Source(path);
        Lexer l=new Lexer(s);
        Parser p=new Parser(l);
        try {
            Json j= p.parse();
            System.out.println("valid");
                try {
                    File newFile= new File(newPath(path));
                    if(newFile.exists()){
                        newFile.delete();
                        newFile= new File(newPath(path));
                    }else {
                        newFile.createNewFile();
                    }
                    FileOutputStream fos=new FileOutputStream(newFile);
                    OutputStreamWriter osw=new OutputStreamWriter(fos);
                    BufferedWriter br=new BufferedWriter(osw);
                    br.write(prettyJson(path));
                    br.close();
                    osw.close();
                    fos.close();
                    System.out.println("The pretty json file has been created successfully !");
                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                s.close();
        }
        catch (LexException|SyntaxException e){

            System.err.println(e.getMessage());
        }catch (Exception e){

        }

    }
    public static String  usage3(String path,String queryPath){
        Source s=new Source(path);
        Lexer l=new Lexer(s);
        Parser p=new Parser(l);
        try {
//            Json j=p.parse();
//            System.out.println("valid");
//            if(j.getClass()==JObject.class){//JObject
//               p.object() ;
//            }else{ //JArrray
//
//            }
            String[] querys=queryPath.split("/");
            int i=1;
            Object o=p.parse();
            while (i<querys.length){
                o=recursiveFind(o,querys[i]) ;
               if(o.getClass()==String.class){
                   s.close();
                   return o.toString();
               }
                i++;
            }
            if (o.getClass()==JArray.class){
                return ((JArray)o).toString();
            }
            if (o.getClass()==JObject.class){
                return ((JObject)o).toString();
            }
        }
        catch (LexException|SyntaxException e){
            System.err.println(e.getMessage());
        }catch (Exception e){
            System.err.println(e.getMessage());
        }

        return null;
    }
    public static Object recursiveFind(Object o,String querycontent){
        if (o.getClass()==JObject.class){
            return  ((JObject)o).map.get(querycontent).value();
        } else if (o.getClass()==JArray.class) {
            return   ((JArray)o).get(Integer.parseInt(querycontent)-1);
        }else {
            return ((OtherValue)o).value().toString();
        }

    }
    public static String newPath(String path){
        String newPath;
        int pos=path.lastIndexOf('.');
        newPath=path.substring(0,pos+1);
        newPath=newPath+"pretty.json";
        return newPath;
    }
    public static String prettyJson(String path){ //convert the content in json file to format txt
        StringBuilder sb=new StringBuilder();
        String line;
        try {
            BufferedReader br=new BufferedReader(new FileReader(path));
            while ((line=br.readLine())!=null){
                sb.append(line);
            }
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        String content=sb.toString();
        if (content==null||"".equals(content)){
            return "";
        }

        sb=new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i=0;i<content.length();i++){
            last=current;
            current=content.charAt(i);
            switch (current){
                case '{':
                case '[':
                    sb.append(current);
                    sb.append("\r\n");
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append("\r\n");
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append("\r\n");
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);


            }

        }
        return sb.toString();
    }
    public static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
}
