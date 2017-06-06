package model;

import java.util.*;

public class OperationOrderMap {
    private List< List<String>> orderMap;
    public OperationOrderMap(){
        orderMap = new ArrayList<>();
        fillMap();
    }
    private void fillMap(){

        List<String> highestPriority = new ArrayList<>();
        highestPriority.add("sqrt");
        orderMap.add(highestPriority);

        String[] secondPriority = { "/", "*", "%", "x-1"};
        List<String> middlePriority = new ArrayList<>(Arrays.asList(secondPriority));
        orderMap.add(middlePriority);

        String[] thirdPriority = {"+","-"};
        List<String> lowestPriority = new ArrayList<>(Arrays.asList(thirdPriority));
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
