import java.util.ArrayList;

public class RecursionExample {

    ArrayList<Integer> ints = new ArrayList();

    public static void main(String[] args){
        new RecursionExample();
    }

    RecursionExample() {
        containsNumber(1);
        containsNumber(5);
        containsNumber(6);
        containsNumber(8);
        containsNumber(1);
        containsNumber(8);
        containsNumber(6);
        containsNumber(88);
        containsNumber(9);
        containsNumber(0);
        containsNumber(-9);
    }

    public void containsNumber(int number)
    {
        if (ints.size() > 0) {
            contatinsNumber(number, ints.size() -1);
        } else {
            System.out.println("ArrayList is empty. Number added");
           ints.add(number);
        }
    }

    protected void contatinsNumber(int number, int index) {
        if (index < 0) {
            System.out.println("ArrayList doesnt contain number. Number added");
            ints.add(number);
            System.out.println(ints.toString());
        } else if (ints.get(index) != number) {
            contatinsNumber(number, index - 1);
        } else {

            System.out.println("ArrayList contains number");
            System.out.println(ints.toString());
        }

    }
}
