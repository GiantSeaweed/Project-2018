import java.io.*;
import java.util.*;

public class Turing {
    private static Set<String> stateSet = new HashSet<String>();
    public static Set<String> inputSymbols = new HashSet<String>();
    public static Set<String> tapeSymbols  = new HashSet<String>();
    public static String   initState;
    public static String   blankSymbol;
    public static Set<String> finalSet = new HashSet<String>();
    public static ArrayList<Transition> transitions = new ArrayList<Transition>();


    public static void main(String[] args) throws IOException {
        initParser("/Users/fengshiwei/001NJU/2018Fall/TheoryofComputation/Project-2018-master/Turing/palindrome.tm");
        printParser();

        ArrayList<String> input = readInput("/Users/fengshiwei/001NJU/2018Fall/TheoryofComputation/Project-2018-master/Turing/input.txt");
//        for(String str : input){
//            System.out.println(str);
//        }
        FileWriter writer = null;
        try {
            writer = new FileWriter("result.txt", true);
        } catch(IOException e){
            e.printStackTrace();
        }
        for(String str : input){
            if(str.length() > 0) {
                Tape tape = new Tape();
                System.out.println("Str: " + str);
                String result = tape.execute(str);
                writer.write(result);
            }
        }
        writer.close();
    }

    public static void initParser(String filename) {
        File file = new File(filename);
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(file));
            String tempStr = null;

            while((tempStr = reader.readLine()) != null){
                tempStr = tempStr.split(";")[0]; //remove the comments after ';'
//                System.out.println(tempStr);
                if(tempStr.length() == 0)
                    continue;

                char ch0 = tempStr.charAt(0);
                char ch1 = tempStr.charAt(1);
                if (ch0 != '#') {//transition function
//                    assert(false);
                    String[] temp = tempStr.split(" ");
                    Transition trans = new Transition();
                    trans.curState = temp[0];
                    trans.curSymbol = temp[1];
                    trans.nextSymbol = temp[2];
                    trans.nextState = temp[4];
                    switch (temp[3]){
                        case "l" : trans.direction = Transition.Direction.LEFT; break;
                        case "r" : trans.direction = Transition.Direction.RIGHT; break;
                        case "*" : trans.direction = Transition.Direction.STAY; break;
                        default: assert(false);
                    }
                    transitions.add(trans);

                } else {
                    if(ch1 == 'Q'){
                        String subStr = tempStr.substring(tempStr.indexOf("{")+1,
                                                          tempStr.length()-1 );
//                        System.out.println(subStr);
                        String[] temp = subStr.split(",");
                        for(int i=0; i<temp.length ;i++){
//                            System.out.println(i+" "+temp[i]);
                            stateSet.add(temp[i]);
                        }
                    }
                    else if(ch1 == 'S'){ // input symbol
                        String subStr = tempStr.substring(tempStr.indexOf("{")+1,
                                                          tempStr.length()-1 );
                        String[] temp = subStr.split(",");
                        for(String str : temp){
                            inputSymbols.add(str);
                        }
                    }
                    else if(ch1 == 'T'){ //tape symbol
                        String subStr = tempStr.substring(tempStr.indexOf("{")+1,
                                                          tempStr.length()-1 );
                        String[] temp = subStr.split(",");
                        for(String str : temp){
                            tapeSymbols.add(str);
                        }
                    }
                    else if(ch1 == 'q'){ //init symbol
                        initState= tempStr.substring(tempStr.indexOf("= ")+2);
                    }
                    else if(ch1 == 'B'){ //blank
                        blankSymbol = tempStr.substring(tempStr.indexOf("= ")+2);
                    }
                    else if(ch1 == 'F'){ //final states
                        String subStr = tempStr.substring(tempStr.indexOf("{")+1,
                                                          tempStr.length()-1 );
                        String[] temp = subStr.split(",");
                        for(String str : temp){
                            finalSet.add(str);
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void printParser(){
        System.out.println("StateSet: ");
        for(String tmp : stateSet){
            System.out.print(tmp+" ");
        }
        System.out.println("\n");

        System.out.println("InputSymbol: ");
        for(String tmp : inputSymbols){
            System.out.print(tmp+" ");
        }
        System.out.println("\n");

        System.out.println("TapeSymbol: ");
        for(String tmp : tapeSymbols){
            System.out.print(tmp+" ");
        }
        System.out.println("\n");

        System.out.println("InitState: " + initState);
        System.out.println("");

        System.out.print("FinalState: ");
        for(String tmp : finalSet){
            System.out.print(tmp+" ");
        }
        System.out.println("\n");

        System.out.println("Transition: ");
//        System.out.println(transitions.get(2).nextState);
        for(int i=0;i<transitions.size();i++){
            Transition trans = transitions.get(i);
            System.out.print(trans.curState + " " );
            System.out.print(trans.curSymbol+ " ");
            System.out.print(trans.nextSymbol+ " ");
            System.out.print(trans.direction+ " ");
            System.out.println(trans.nextState+ " ");
        }


    }

    public static ArrayList<String> readInput(String filename){
        File file = new File(filename);
        BufferedReader reader = null;
        ArrayList<String> input = new ArrayList<String>();
        try{
            reader = new BufferedReader(new FileReader(file));
            String tempStr = null;
            while((tempStr = reader.readLine()) != null){
                String tmp = tempStr;
                input.add(tmp);
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return input;
    }

}
