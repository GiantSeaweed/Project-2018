
import java.io.File;
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

    public String execute(String str, String pathName) throws IOException {
        FileWriter writer = null;
        try{
            writer = new FileWriter(pathName+ File.separator+"console.txt",true);
        }catch(IOException e){
            e.printStackTrace();
        }
        if(!isLegal(str)) {
            writer.write("Input: " + str + "\n");
            writer.write("==================== ERR ====================\n");
            writer.write("The input \"" + str + "\" is illegal\n");
            writer.write("==================== END ====================\n");
            writer.close();
            return "Error\n";
        }

        writer.write("Input: "+ str+"\n");
        writer.write("==================== RUN ====================\n");
        /** Initialize the tape */
        ArrayList<TapeUnit> tapeList = new ArrayList<TapeUnit>();
        int index=0;

        if(str.length() == 0){
            TapeUnit tapeUnit = new TapeUnit();
            tapeUnit.index = 0;
            tapeUnit.data = "_";
            tapeUnit.head = "^";
            tapeList.add(tapeUnit);
        }
        while(index < str.length()){
            TapeUnit tapeUnit = new TapeUnit();
            tapeUnit.index = index;
            tapeUnit.data = str.substring(index, index + 1);
//            if(str.length() == 0){
//                tapeUnit.data = "_";
//            }else {
//
//            }
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

       // System.out.println("Size : "+tapeList.size());
//        for(Transition trans: Turing.transitions){
//            trans.printTransition();
//        }

        int step = 0;
        String curState = Main.initState;
        String curSymbol = tapeList.get(0).data;
        headIndex = 0;          // related to the tape
        int headLocation = 0;   // mark the position; detect the border

        /** print the initial state*/
        StringBuffer indexString = new StringBuffer("Index : ");
        StringBuffer tapeString  = new StringBuffer("Tape  : ");
        StringBuffer headString  = new StringBuffer("Head  : ");
//        /** should not print unnecessary Blank symbols */
//        int begin = 0, end = 0;
//        for(int i=0;i<tapeList.size();i++){
//            if( !(tapeList.get(i).data.equals("_") && tapeList.get(i).head.equals(" ")) ) {
//                begin = i;
//                break;
//            }
//        }
//        for(int i=0;i<tapeList.size();i++){
//            if( !(tapeList.get(i).data.equals("_") && tapeList.get(i).head.equals(" ")) ) {
//                end = i;
//            }
//        }
        for(int i=0;i<tapeList.size();i++) {
            indexString.append(tapeList.get(i).index + " ");
            tapeString.append(tapeList.get(i).data + " ");
            headString.append(tapeList.get(i).head + " ");

            int temp = Math.abs(tapeList.get(i).index);
            while (temp >= 10) {
                tapeString.append(" ");
                headString.append(" ");
                temp = temp / 10;
            }
        }
        writer.write("Step  : "+step + "\n");
        writer.write(indexString.toString().trim()+ "\n");
        writer.write(tapeString.toString().trim()+ "\n");
        writer.write(headString.toString().trim()+ "\n");
        writer.write("State : "+ curState.trim()+"\n");
        writer.write("---------------------------------------------\n");
        step++;
        /** end of printing the initial step */

        while(true){
            for(int i=0;i<tapeList.size();i++){
                if(headIndex == tapeList.get(i).index){
                    curSymbol = tapeList.get(i).data;
                    headLocation = i;
                    break;
                }
            }

//            System.out.println("cur: " +curState +" curSymbol:"+curSymbol);
            Transition transRule = getTransRule(curState, curSymbol);
//            System.out.println(transRule == null);
            if(transRule == null){
                break;
            }

//            transRule.printTransition();
            /** write the symbol and change the state */
            tapeList.get(headLocation).data = new String(transRule.nextSymbol);
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

            /** encounter the border */
            if(headLocation == tapeList.size()){
                TapeUnit unit = new TapeUnit();
                unit.index = tapeList.get(tapeList.size()-1).index + 1;
                unit.data = "_";
                unit.head = "^";
                tapeList.add(unit);
            }else if(headLocation < 0){
                TapeUnit unit = new TapeUnit();
                unit.index = tapeList.get(0).index - 1;
                unit.data = "_";
                unit.head = "^";
                tapeList.add(0,unit);
                headLocation = 0;
//                tapeList.get(headLocation).head = "^";
            }
//            for(TapeUnit unit: tapeList)
//                System.out.print(unit.data);
//            System.out.println("\nhL:" + headLocation+"\n");
            tapeList.get(headLocation).head = "^";

            indexString = new StringBuffer("Index :");
            tapeString  = new StringBuffer("Tape  :");
            headString  = new StringBuffer("Head  :");

            /** should not print unnecessary Blank symbols */
            int begin = 0, end = 0;
            for(int i=0;i<tapeList.size();i++){
                if( !(tapeList.get(i).data.equals("_") && tapeList.get(i).head.equals(" ")) ) {
                    begin = i;
                    break;
                }
            }
            for(int i=0;i<tapeList.size();i++){
                if( !(tapeList.get(i).data.equals("_") && tapeList.get(i).head.equals(" ")) ) {
                    end = i;
                }
            }

            for(int i=begin;i<=end;i++) {
                indexString.append( " " + Math.abs(tapeList.get(i).index) );
                tapeString.append(  " " + tapeList.get(i).data );
                headString.append(  " " + tapeList.get(i).head );

                int temp = Math.abs(tapeList.get(i).index);
                while (temp >= 10) {
                    tapeString.append(" ");
                    headString.append(" ");
                    temp = temp / 10;
                }
            }

            writer.write("Step  : "+step + "\n");
            writer.write(indexString.toString().trim()+ "\n");
            writer.write(tapeString.toString().trim()+ "\n");
            writer.write(headString.toString().trim()+ "\n");
            writer.write("State : "+ curState.trim()+"\n");
            writer.write("---------------------------------------------\n");

            step++;
            if(Main.finalSet.contains(curState)) {
                break;
            }
//            if(curState.equals("halt_accept")){
//                writer.write("Result: True\n");
//                writer.write("==================== END ====================\n");
//                writer.close();
//                return "True\n";
//            } else if(curState.equals("halt_reject")){
//                writer.write("Result: False\n");
//                writer.write("==================== END ====================\n");
//                writer.close();
//                return "False\n";

        }

        int begin = 0, end = 0;
        for(int i=0;i<tapeList.size();i++){
            if( !(tapeList.get(i).data.equals("_")) ){
                begin = i;
                break;
            }
        }
        for(int i=0;i<tapeList.size();i++){
            if( !(tapeList.get(i).data.equals("_")) ) {
                end = i;
            }
        }
        StringBuffer buffer = new StringBuffer();
        for(int i =begin;i<=end;i++){
            buffer.append(tapeList.get(i).data);
        }
        writer.write("Result: "+buffer.toString().trim()+"\n");
        writer.write("==================== END ====================\n");
        writer.close();

        return buffer.toString()+"\n";
    }

    public static boolean isLegal(String str){
        for(int i=0;i<str.length();i++){
            if(!Main.inputSymbols.contains(str.substring(i,i+1)))
                return false;
        }
        return true;
    }

    public static Transition getTransRule(String curState, String curSymbol){
        Transition result = null;
        for(Transition trans : Main.transitions){
            if(trans.curState.equals(curState) && trans.curSymbol.equals(curSymbol)){
                result = new Transition();
                result.copy(trans);
            }
        }

        if(result == null){
            for(Transition trans : Main.transitions){
                if(trans.curState.equals(curState) && trans.curSymbol.equals("*")){
                    result = new Transition();
                    result.copy(trans);
                    if(trans.nextSymbol.equals("*")) {
                        result.nextSymbol = curSymbol;
                    }else{
                        result.nextSymbol = trans.nextSymbol;
                        break;
                    }
                }
            }
        }

        assert(result != null);

        return result;
    }
}
