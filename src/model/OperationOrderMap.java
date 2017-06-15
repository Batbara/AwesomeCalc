package model;

import java.util.*;

public class OperationOrderMap {
    private List< List<String>> orderMap;
    public OperationOrderMap(){
        orderMap = new ArrayList<>();
        fillMap();
    }
    private void fillMap(){


        String[] firstPriority = {"(", ")"};
        List<String> highestPriority = new ArrayList<>(Arrays.asList(firstPriority));
        orderMap.add(highestPriority);

        String[] secondPriority = {"sqrt", "inv"};
        List<String> listSecond = new ArrayList<>(Arrays.asList(secondPriority));
        orderMap.add(listSecond);

        String[] thirdPriority = { "/",  "%"};
        List<String> middlePriority = new ArrayList<>(Arrays.asList(thirdPriority));
        orderMap.add(middlePriority);

        String[] fourthPriority = { "*"};
        List<String> listThird = new ArrayList<>(Arrays.asList(fourthPriority));
        orderMap.add(listThird);

        String[] fifthPriority = {"+"};
        List<String> fifthList = new ArrayList<>(Arrays.asList(fifthPriority));
        orderMap.add(fifthList);

        String[] sixthPriority = {"-"};
        List<String> lowestPriority = new ArrayList<>(Arrays.asList(sixthPriority));
        orderMap.add(lowestPriority);
    }
    public Integer getPriority(String operationToCheck){
       for(int priority = 0; priority<orderMap.size(); priority++){
           for (String operation : orderMap.get(priority)){
               if(operation.equals(operationToCheck))
                   return priority;
           }
       }
       return -1;
    }
}
