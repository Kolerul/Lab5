/**
 *
 *  @author Владимир Черных
 *  Класс Main, в котором создается экземпляр класса View,
 *  запускает свой метод создания коллекции и основной метод работы с консолью
 *  */


public class Main{
    public static void main(String[] arg){

            View client = new View(new WorkingWithMainStack());

            client.createCollection();

            client.inputCircle();
    }
}