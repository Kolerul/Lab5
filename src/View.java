import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Класс, отвечающий за взаимодействие с клиентом
 */
public class View {
    /**
     * объект класса WorkingWithMainStack, который показывает, что каждому объекту View будет соответствовать
     * свои данные в коллекции
     */
    WorkingWithMainStack presenter;
    public View(WorkingWithMainStack presenter){
        this.presenter  = presenter;
    }

    /**
     * Метод, создающий коллекцию из данных в изначальном файле
     */
    public void createCollection(){
        System.out.println("Укажите переменную окружения (или введите 'cancel' если хотите пропустить этот шаг)");
        Scanner scanner = new Scanner(System.in);
        try{
            String line = scanner.nextLine().trim();
            if (line.equals("cancel")){
                System.out.println("Шаг пропущен. Коллекция не была создана");
            }else {
                System.out.println(System.getenv(line));
                System.out.println(presenter.createNewMainStack(line));
            }
        }catch (FileNotFoundException e){
            System.out.println("Указанный файл не найден");
            createCollection();
        }catch (NullPointerException e){
            System.out.println("Указанный файл не найден или данные в нем указаны в неправильном формате");
            createCollection();
        }
    }

    /**
     * Метод, который принимает команды от пользователя и выдает ему результат их действия
     */
    public void inputCircle(){

        Scanner scanner = new Scanner(System.in);
        String lineArg = null;
        do{
            try {
                lineArg = scanner.nextLine();

                System.out.println(argCheck(lineArg));

            }catch (NumberFormatException e){
                System.out.println("Команда не распознана, так как нее было необходимо указать число. Пожалуйста, заново ввидите команду");
            }catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Ошибка в распознании команды, повторите запрос");
            }catch (FileNotFoundException e) {
                System.out.println("Указанный файл не найден. Пожалуйста, повторите попытку");
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
        }while (!lineArg.trim().equals("exit"));
        scanner.close();
    }

    /**
     * Метод, который обрабатывает команды от пользователя и затем предпринимает необходимые действия
     * @param source - строка, которую ввел пользователь
     * @return возвращает строковое описание результатов действия команды от пользователя
     * @throws NumberFormatException
     * @throws ArrayIndexOutOfBoundsException
     * @throws IOException
     */
    public String argCheck(String source) throws NumberFormatException, ArrayIndexOutOfBoundsException, IOException {
        String report = "";
        source = source.trim();
        if(source.equals("help")) {
            report = presenter.showHelp();
        }else if (source.equals("history")) {
            report = presenter.showHistory();
        }else if (source.equals("info")) {
            report = presenter.showInfo();
            //presenter.iter1();
        }else if (source.equals("show")) {
            report = presenter.showMainStack();
        }else if (source.replaceAll(" ", "").equals("add{element}")) {
            report = presenter.addElement();
        }else if (source.replaceAll(" ", "").equals("updateid{element}")) {
            report = presenter.updateElementById();
        }else if (source.equals("remove_first")){
            report = presenter.removeFirst();
        }else if(StringManipulation.commandWithArgumentsCheck(source, "remove_by_id")) {
            report = presenter.removeElementById(Long.parseLong(StringManipulation.returnLastWords(source)));
        }else if (source.equals("clear")) {
            report = presenter.clearStack();
        }else if (StringManipulation.commandWithArgumentsCheck(source, "filter_contains_name")) {
            report = presenter.searchByName(StringManipulation.returnLastWords(source));
        }else if (source.equals("print_field_ascending_capital")) {
            report = presenter.showCapitalStack(presenter.filterByCapital());
        }else if (source.replaceAll(" ", "").equals("add_if_max{element}")) {
            report = presenter.addIfMax();
        }else if (source.equals("save")) {
            System.out.println("Укажите название файла, для сохранения (осторожнее, если файл уже существует, то он перезапишется)");
            Scanner scanner = new Scanner(System.in);
            report = presenter.save(scanner);
        }else if (StringManipulation.commandWithArgumentsCheck(source, "execute_script")) {
            report = presenter.executeScript(StringManipulation.returnLastWords(source), presenter);
        }else if (source.equals("group_counting_by_id")){
            report = presenter.idGrouper();
        }else if (source.equals("exit")){
            report = "Работа завершается...";
        }else{
            return "Команда не распознана, повторите запрос";
        }
        return report;
    }
}