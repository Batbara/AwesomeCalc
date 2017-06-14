package model;

import java.util.*;

public class OperationOrderMap {
    private List< List<String>> orderMap;
    public OperationOrderMap(){
        orderMap = new ArrayList<>();
        fillMap();
    }
    private void fillMap(){


        String[] firstPriority = {"sqrt", "(", ")"};
        List<String> highestPriority = new ArrayList<>(Arrays.asList(firstPriority));
        orderMap.add(highestPriority);

        String[] secondPriority = { "/",  "%", "x-1"};
        List<String> middlePriority = new ArrayList<>(Arrays.asList(secondPriority));
        orderMap.add(middlePriority);

        String[] thirdPriority = {  "*"};
        List<String> listThird = new ArrayList<>(Arrays.asList(thirdPriority));
        orderMap.add(listThird);

        String[] fourthPriority = {"+","-"};
        List<String> lowestPriority = new ArrayList<>(Arrays.asList(fourthPriority));
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
