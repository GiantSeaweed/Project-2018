import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Tape {
    private static int headIndex;
    public static void headLeft(){
        headIndex--;
    }

    public static void headRight(){
        headIndex++;
    }

    public String execute(String str) throws IOException {
        FileWriter writer = null;
        try{
            writer = new FileWriter("console.txt",false);
        }catch(IOException e){
            e.printStackTrace();
        }
        if(!isLegal(str)) {
            writer.write("Input: " + str + "\n");
            writer.write("==================== ERR ====================\n");
            writer.write("The input " + str + " is illegal\n");
            writer.write("==================== END ====================\n");
            writer.close();
            return "Error\n";
        }

        writer.write("Input: "+ str+"\n");
        writer.write("==================== RUN ====================\n");
        /** Initialize the tape */
        ArrayList<TapeUnit> tapeList = new ArrayList<>();
        int index=0;
        while(index < str.length()){
            TapeUnit tapeUnit = new TapeUnit();
            tapeUnit.index = index;
            tapeUnit.data  = str.substring(index, index+1);
            if(index == 0) {
                tapeUnit.head = "^";
            }else {
                tapeUnit.head = " ";
            }
            tapeList.add(tapeUnit);
//            int temp = Math.abs(index);
//            while(temp >= 10){
//                tapeUnit.data = tapeUnit.data.concat(" ");
//                tapeUnit.head = tapeUnit.head.concat(" ");
//                temp = temp/10;
//            }
            index++;
        }

        System.out.println("Size : "+tapeList.size());

        int step = 0;
        String curState = Turing.initState;
        String curSymbol = null;
        headIndex = 0;          // related to the tape
        int headLocation = 0;   // mark the position; detect the border
        while(true){
            for(int i=0;i<tapeList.size();i++){
                if(headIndex == tapeList.get(i).index){
                    curSymbol = tapeList.get(i).data;
                    headLocation = i;
                    break;
                }
            }


            Transition transRule = getTransRule(curState, curSymbol);
            System.out.println(transRule == null);
            transRule.printTransition();
            /** write the symbol and change the state */
            tapeList.get(headLocation).data = transRule.nextSymbol;
            curState = transRule.nextState;

            /** change the headIndex */
            tapeList.get(headLocation).head = " ";
            if(transRule.direction == Transition.Direction.LEFT){
                headLeft();
                headLocation--;
            }else if(transRule.direction == Transition.Direction.RIGHT){
                headRight();
                headLocation++;
            }
            System.out.println("hL:" + headLocation);
            tapeList.get(headLocation).head = "^";

            StringBuffer indexString = new StringBuffer("Index : ");
            StringBuffer tapeString  = new StringBuffer("Tape  : ");
            StringBuffer headString  = new StringBuffer("Head  : ");

            for(int i=0;i<tapeList.size();i++){
                indexString.append(tapeList.get(i).index+ " ");
                tapeString.append( tapeList.get(i).data + " ");
                headString.append( tapeList.get(i).head + " ");

                int temp = Math.abs(tapeList.get(i).index);
                while(temp >= 10){
                    tapeString.append(" ");
                    headString.append(" ");
                    temp = temp/10;
                }
            }

            writer.write("Step  : "+step + "\n");
            writer.write(indexString.toString()+ "\n");
            writer.write(tapeString.toString()+ "\n");
            writer.write(headString.toString()+ "\n");
            writer.write("State : "+ curState+"\n");
            writer.write("---------------------------------------------\n");

            step++;
            if(curState.equals("halt_accept")){
                break;
            }
        }




        writer.close();
        return "1234\n";
    }

    public static boolean isLegal(String str){
        for(int i=0;i<str.length();i++){
            if(!Turing.tapeSymbols.contains(str.substring(i,i+1)))
                return false;
        }
        return true;
    }

    public static Transition getTransRule(String curState, String curSymbol){
        Transition result = null;
        for(Transition trans : Turing.transitions){
            if(trans.curState.equals(curState) && trans.curSymbol.equals(curSymbol)){
                result = trans;
            }
        }

        if(result == null){
            for(Transition trans : Turing.transitions){
                if(trans.curState.equals(curState) && trans.curSymbol.equals("*")){
                    result = trans;
                }
            }
        }

        assert(result != null);

        return result;
    }
}
