import java.io.*;
import java.util.*;

/**
 * Класс, отвечающий за работу с коллекцией
 */
public class WorkingWithMainStack{
    /**
     * Строковое описние результатов действия методов
     */
    String result = "";
    /**
     * Объект класса с данными
     */
    Model dataBase;
    public WorkingWithMainStack(){
        dataBase = new Model();
    }

    /**
     * Метод, создающий коллекцию по данным из изначльного файла
     * @param line - название переменной окружения, в которой содержится ссылка на интересующий нас файл
     * @return возвращает строковое описние результатов действия метода
     * @throws FileNotFoundException
     * @throws NullPointerException
     */
    public String createNewMainStack(String line) throws FileNotFoundException, NullPointerException{
        //C:\Users\europ\OneDrive\Рабочий стол\csv.txt, Lab5, System.getenv("csv")
        result = "";
        dataBase.startingFile = new File(System.getenv(line));
        Scanner scanner = new Scanner(dataBase.startingFile);
        dataBase.dateOfCreation = new Date();
        while (scanner.hasNextLine()) {

            String[] parts = StringManipulation.parseToParts(scanner.nextLine());

            if (parts.length != 10 || StringManipulation.emptyCheck(parts) != 1){
                continue;
            }

            int capital = StringManipulation.capital(parts[7]);

            Government government = StringManipulation.government(parts[8]);

            dataBase.cities.push(new City(setID(), parts[0], Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Float.parseFloat(parts[5]), Integer.parseInt(parts[6]), capital, government, parts[9]));
            dataBase.numberOfObjectsInStack++;
        }
        scanner.close();
        result = "Коллекция создана, можно приступать к работе";
        return result;
    }

    /**
     * Метод, который добавляет новый элемент в коллекцию (с гененрацией нового ID)
     * @return возвращает строковое описние результатов действия метода
     */
    public String addElement(){
        dataBase.cities.push(new City(setID(), AddMethods.addName(), AddMethods.addCoordinateX(), AddMethods.addCoordinateY(),
                AddMethods.addArea(), AddMethods.addPopulation(), AddMethods.addMeters(), AddMethods.addTimeZone(),
                AddMethods.addCapital(), AddMethods.addGovernment(), AddMethods.addGovernerName()));
        dataBase.numberOfObjectsInStack++;
        addToHistory("add { element }");
        return "Элемент добавлен";
    }
    /**
     * Метод, который добавляет новый элемент в коллекцию (без гененрации нового ID)
     * @return возвращает строковое описние результатов действия метода
     */
    public String addElement(long id){
        dataBase.cities.push(new City(id, AddMethods.addName(), AddMethods.addCoordinateX(), AddMethods.addCoordinateY(),
                AddMethods.addArea(), AddMethods.addPopulation(), AddMethods.addMeters(), AddMethods.addTimeZone(),
                AddMethods.addCapital(), AddMethods.addGovernment(), AddMethods.addGovernerName()));
        addToHistory("add { element }");
        return "Элемент обновлен";
    }

    /**
     * Метод, добавляющий команду в историю команд
     * @param arg команда от пользователя
     */
    public void addToHistory(String arg){
        for (int i = 0; i < dataBase.history.length; i++){
            if (dataBase.history[i] == null) {
                dataBase.history[i] = arg;
                dataBase.historySpaceCounter = 0;
                break;
            }else if (dataBase.historySpaceCounter == dataBase.history.length - 1) {
                offsetOfHistory();
                dataBase.history[dataBase.history.length - 1] = arg;
                break;
            }else {
                dataBase.historySpaceCounter++;
            }
        }
    }

    /**
     * Метод, показывающий массив с историей команд пользователю
     * @return возвращает строковое описние результатов действия метода
     */
    public String showHistory(){
        result = "";
        for (int i = 0; i < dataBase.history.length; i++){
            if (dataBase.history[i] != null) {
                result = result + "[ " + dataBase.history[i] + " ]";
                if (i != dataBase.history.length - 1){
                    result = result + "\n";
                }
            }
        }
        addToHistory("history");
        return result;
    }

    /**
     * Метод, сдвигающий элементы массива истории команд, когда тот заполнился
     */
    private void offsetOfHistory(){
        for (int i = 0; i < (dataBase.history.length - 1); i++){
            dataBase.history[i] = dataBase.history[i+1];
        }
        dataBase.history[dataBase.history.length - 1] = null;
    }

    /**
     * Метод, который ищет и обновляет элемент коллекции по его ID
     * @return возвращает строковое описние результатов действия метода
     */
    public String updateElementById(){
        result = "";
        long id = AddMethods.putId();
        //System.out.println(id);
        Stack<City> citiesSpare = new Stack<>();
        int replacementCounter = 0;
        int removeCheck = 0;
        for (int i = 0; i < dataBase.numberOfObjectsInStack; i++){
            if (dataBase.cities.peek().getId() == id){
                dataBase.cities.pop();
                result = addElement(id);
                removeCheck++;
                break;
            }else{
                citiesSpare.push(dataBase.cities.pop());
                replacementCounter++;
            }
        }
        if (removeCheck == 0){
            result = "Элемент с данным id не удалось найти";
        }
        for (int i = 0; i < replacementCounter; i++){
            dataBase.cities.push(citiesSpare.pop());
        }
        addToHistory("update id { element }");
        return result;
    }

    /*public void iter1(){
        Iterator<City> iterator = dataBase.cities.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next().toString());
        }
    }*/

    /**
     * Метод, который ищет и удаляет элемент из коллекции по его ID
     * @param id - ID элемента, который мы собираемся удалить
     * @return возвращает строковое описние результатов действия метода
     */
    public String removeElementById(long id){
        result = "";
        Stack<City> citiesSpare = new Stack<>();
        int replacementCounter = 0;
        int removeCheck = 0;
        for (int i = 0; i < dataBase.numberOfObjectsInStack; i++){
            if (dataBase.cities.peek().getId() == id){
                dataBase.cities.pop();
                result = "Элемент удален";
                dataBase.numberOfObjectsInStack--;
                removeCheck++;
                break;
            }else{
                citiesSpare.push(dataBase.cities.pop());
                replacementCounter++;
            }
        }
        if (removeCheck == 0){
            result = "Элемент с данным id не удалось найти";
        }
        for (int i = 0; i < replacementCounter; i++){
            dataBase.cities.push(citiesSpare.pop());
        }
        addToHistory("remove_by_id id");
        return result;
    }

    /**
     * Метод, который удаляет первый элемент из коллекции
     * @return возвращает строковое описние результатов действия метода
     */
    public String removeFirst(){
        result = "";
        addToHistory("remove_first");
        if (dataBase.numberOfObjectsInStack <= 0){
            result = "Извините, удалять нечего, так как коллекция пуста";
            return result;
        }
        Stack<City> citiesSpare = new Stack<>();
        int replacementCounter = 0;
        for (int i = 0; i < dataBase.numberOfObjectsInStack - 1; i++){
            citiesSpare.push(dataBase.cities.pop());
            replacementCounter++;
        }
        dataBase.cities = new Stack<>();
        for (int i = 0; i < replacementCounter; i++){
            dataBase.cities.push(citiesSpare.pop());
        }
        result = "Первый элемент удален";
        dataBase.numberOfObjectsInStack--;
        return result;
    }

    /**
     * Метод, удаляющий все элементы из коллекции
     * @return возвращает строковое описние результатов действия метода
     */
    public String clearStack(){
        result = "";
        while (!dataBase.cities.empty()){
            dataBase.cities.pop();
        }
        dataBase.numberOfObjectsInStack = 0;
        result = "Коллекция очищена.";
        addToHistory("clear");
        return result;
    }

    /**
     * Метод, который устанавливает ID. Суть его работы - увеличивать соответствующую переменную на 1 каждый раз,
     * когда добавляется новый элемент
     * @return возвращает увеличенный параметр ID
     */
    private long setID(){
        dataBase.id++;
        return dataBase.id;
    }

    /**
     * Метод, который ищет элементы коллекции по заданной подстроке в именах элементов
     * @param line - искомая подстрока
     * @return возвращает строковое описние результатов действия метода
     */
    public String searchByName(String line){
        result = "";
        //Iterator<City> iterator = dataBase.cities.iterator();
        Stack<City> citiesClone = (Stack<City>) dataBase.cities.clone();
        Stack<City> resultStack = new Stack<>();
        int resultNumber = 0;
        /*while (iterator.hasNext()){
            City city = iterator.next();
            if (city.getName().contains(line)){
                resultStack.push(city);
                resultNumber++;
            }
        }*/
        for (int i = 0; i < dataBase.numberOfObjectsInStack; i++) {
            if (citiesClone.peek().getName().contains(line)) {
                resultStack.push(citiesClone.pop());
                resultNumber++;
            } else {
                citiesClone.pop();
            }
        }
        if (resultNumber > 0){
            /*Iterator<City> resultIterator = resultStack.iterator();
            int counter = 0;
            while (iterator.hasNext()){
                result = result + iterator.next();
                if (counter != resultNumber){
                    result = result + "\n";
                }
                counter++;
            }*/
            for (int j = 0; j < resultNumber; j++) {
                result = result + resultStack.pop().toString();;
                if (j != resultNumber - 1){
                    result = result + "\n";
                }
            }
        }else{
            result = "Ничего не удалось найти";
        }
        addToHistory("filter_contains_name name");
        return result;
    }

    /**
     * Метод, демонстрирующий коллекцию, отсортированную по значению поля capital
     * @param stack отсортированная коллекция
     * @return возвращает строковое описние результатов действия метода
     */
    public String showCapitalStack(Stack<City> stack){
        result = "";
        //stack = (Stack<City>) dataBase.cities.clone();
        addToHistory("prient_field_ascending_capital");
        if (dataBase.numberOfObjectsInStack <= 0){
            result = "Извините, но коллекция пуста";
            return result;
        }
        for (int j = 0; j < dataBase.numberOfObjectsInStack; j++) {
            result = result + "ID: " + stack.peek().getId() +  ", Является столицей: " + stack.pop().getCapital();
            if (j != dataBase.numberOfObjectsInStack - 1){
                result = result + "\n";
            }
        }
        return result;
    }

    /**
     * Метод, который демонстрирует коллекцию
     * @return возвращает строковое описние результатов действия метода
     */
    public String showMainStack(){
        result = "";
        sort();
        Iterator<City> iterator = dataBase.cities.iterator();
        //Stack<City> citiesClone = (Stack<City>) dataBase.cities.clone();
        if (dataBase.numberOfObjectsInStack > 0){
        /*    for (int j = 0; j < dataBase.numberOfObjectsInStack; j++) {
                result = result + citiesClone.pop().toString();
                if (j != dataBase.numberOfObjectsInStack - 1){
                    result = result + "\n";
                }
            }*/
            int i = 0;
            while (iterator.hasNext()){
                result = result + iterator.next().toString();
                if (i != dataBase.numberOfObjectsInStack - 1){
                    result = result + "\n";
                }
                i++;
            }
        }else {
            return "Извините, но коллекция пуста";
        }
        addToHistory("show");
        return result;
    }

    /**
     * Метод, сортирующий коллекцию по значению поля capital
     * @return возвращает строковое описние результатов действия метода
     */
    public Stack<City> filterByCapital(){
        Stack<City> citiesClone = (Stack<City>) dataBase.cities.clone();
        Stack<City> citiesSpare1 = new Stack<>();
        Stack<City> citiesSpare2 = new Stack<>();
        Stack<City> citiesSpare3 = new Stack<>();
        int capitalCounter = 0;
        int unCapitalCounter = 0;
        int nullCapitalCounter = 0;
        for (int i = 0; i < dataBase.numberOfObjectsInStack; i++){
            try{
                if (citiesClone.peek().getCapital()){
                    citiesSpare1.push(citiesClone.pop());
                    capitalCounter++;
                }else{
                    citiesSpare2.push(citiesClone.pop());
                    unCapitalCounter++;
                }
            }catch (NullPointerException e){
                citiesSpare3.push(citiesClone.pop());
                nullCapitalCounter++;
            }
        }
        for (int i = 0; i < capitalCounter; i++){
            citiesClone.push(citiesSpare1.pop());
        }
        for (int i = 0; i < unCapitalCounter; i++){
            citiesClone.push(citiesSpare2.pop());
        }
        for (int i = 0; i < nullCapitalCounter; i++){
            citiesClone.push(citiesSpare3.pop());
        }
        return citiesClone;
    }

    /**
     * Метод, ищущий максимальный элемент среди элементов коллекции по 2 из 3 параметров:
     * площадь, население и плотность
     * @return возвращает строковое описние результатов действия метода
     */
    public City searchMax(){
        Comparator<City> comparator1 = Comparator.comparing(City::getArea);
        City maxArea = dataBase.cities.stream().max(comparator1).get();
        Comparator<City> comparator2 = Comparator.comparing(City::getPopulation);
        City maxPopulation = dataBase.cities.stream().max(comparator2).get();
        City max = null;
        if (maxArea.getPopulation() >= maxPopulation.getPopulation()){
            max = maxArea;
        }else if (maxPopulation.getArea() > maxArea.getArea()){
            max = maxPopulation;
        }else if (maxArea.getPopulation() / maxArea.getArea() >= maxPopulation.getPopulation() / maxPopulation.getArea()){
            max = maxArea;
        }else{
            max = maxPopulation;
        }
        return max;
    }

    /**
     * Метод, сортирующий коллекцию по возврастанию ID
     */
    public void sort(){
        Comparator<City> comparator = Comparator.comparing(City::getId);
        dataBase.cities.sort(comparator);
    }

    /**
     * Метод, сортирующий коллекцию по значению поля capital
     * @deprecated да, такой метод уже был и, казалось бы, тут все проще,
     * но мне не понравился порядок здешней сортировки
     */
    public void capitalSort(){
        Comparator<City> comparator = Comparator.comparing(City::getCapital).reversed();
        dataBase.cities.sort(comparator);
    }

    /**
     * Метод, сортирующий элементы коллекции по ID группам
     * @return возвращает строковое описние результатов действия метода
     */
    public String idGrouper(){
        result = "";
        int mem1 = 1337, mem2 = 1488, mem3 = 228, mem4 = 282, mem5 = 80, mem6 = 47, mem7 = 69;
        Stack<City> citiesClone = (Stack<City>) dataBase.cities.clone();
        int evenId = 0, oddId = 0, memId = 0, randomMax = (int) (Math.random() * 10000), smallerMaxId = 0;

        for (int i = 0; i < dataBase.numberOfObjectsInStack; i++){
            if (citiesClone.peek().getId() % 2 == 0){
                evenId++;
            }else{
                oddId++;
            }
            if (citiesClone.peek().getId() == mem1 || citiesClone.peek().getId() == mem2 || citiesClone.peek().getId() == mem3 ||
                    citiesClone.peek().getId() == mem4 || citiesClone.peek().getId() == mem5 || citiesClone.peek().getId() == mem6 ||
                    citiesClone.peek().getId() == mem7){
                memId++;
            }
            if (citiesClone.pop().getId() <= randomMax){
                smallerMaxId++;
            }
        }
        result = "Четных id в коллекции: " + evenId + "\n" +
                "Нечетных id в коллекции: " + oddId + "\n" +
                "Мемных id в коллекции: " + memId + "\n" +
                "id меньше рандомного числа (" + randomMax + "): " + smallerMaxId;
        addToHistory("group_counting_by_id");
        return result;
    }

    /**
     * Метод, проверяющий наличие элемента в коллекции с данным ID
     * @param id -указанный ID
     * @return возвращает строковое описние результатов действия метода
     */
    public boolean checkId(long id){
        Stack<City> citiesClone = (Stack<City>) dataBase.cities.clone();
        for (int i = 0; i < dataBase.numberOfObjectsInStack; i++){
            if (citiesClone.pop().getId() == id){
                return true;
            }
        }
        return false;
    }

    /**
     * Метод, демонстрирующий шпаргалку с коммандами для пользователя
     * @return возвращает строковое описние результатов действия метода
     */
    public String showHelp(){
        result = dataBase.cheatSheet;
        addToHistory("help");
        return result;
    }

    /**
     * Метод, демонстрирующий информацию о коллекции
     * @return возвращает строковое описние результатов действия метода
     */
    public String showInfo(){
        result = "Тип коллекции: " + dataBase.cities.getClass() + "\n" + "Время создания коллекции: " + dataBase.dateOfCreation + "\n" +
                "Количество элементов коллекции: " + dataBase.numberOfObjectsInStack;
        addToHistory("info");
        return result;
    }

    /**
     * Метод, добавляющий элемент в коллекцию, если он больше наибольшего
     * @return возвращает строковое описние результатов действия метода
     */
    public String addIfMax(){
        result = "";
        City applicant = new City(setID(), AddMethods.addName(), AddMethods.addCoordinateX(), AddMethods.addCoordinateY(),
                AddMethods.addArea(), AddMethods.addPopulation(), AddMethods.addMeters(), AddMethods.addTimeZone(),
                AddMethods.addCapital(), AddMethods.addGovernment(), AddMethods.addGovernerName());
        if (dataBase.numberOfObjectsInStack >= 1){
            City max = searchMax();
            int checkCounter = 0;
            if (applicant.getArea() >= max.getArea()){
                checkCounter++;
            }
            if (applicant.getPopulation() >= max.getPopulation()){
                checkCounter++;
            }
            if ((applicant.getPopulation() / applicant.getArea() >= max.getPopulation() / max.getArea())){
                checkCounter++;
            }
            if (checkCounter >=2 ){
                dataBase.cities.push(applicant);
                dataBase.numberOfObjectsInStack++;
                result = "Элемент добавлен";
            }else{
                result = "Элемент не оказался больше наибольшего. Элемент не был добавлен";
                dataBase.id--;
            }
        }else{
            dataBase.cities.push(applicant);
            dataBase.numberOfObjectsInStack++;
            result = "Элемент добавлен";
        }
        addToHistory("add_if_max { element }");
        return result;
    }

    /**
     * Метод, сохраняющий коллекцию в указанный файл
     * @param scanner почему-то непереведенный мною в String название файла, для сохранения
     * @return возвращает строковое описние результатов действия метода
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String save(Scanner scanner) throws FileNotFoundException, IOException{
        result = "";
        sort();
        //Iterator<City> iterator = dataBase.cities.iterator();
        Stack<City> citiesClone = (Stack<City>) dataBase.cities.clone();
        String text;
            FileOutputStream file = new FileOutputStream(scanner.nextLine());
            for (int i = 0; i < dataBase.numberOfObjectsInStack; i++){
                text = citiesClone.peek().getName() + "," + citiesClone.peek().getCoordinates().getX()
                        + "," + citiesClone.peek().getCoordinates().getY() + "," + citiesClone.peek().getArea() + "," + citiesClone.peek().getPopulation()
                        + "," + citiesClone.peek().getMetersAboveSeaLevel() + "," + citiesClone.peek().getTimezone() + "," + citiesClone.peek().getCapital()
                        + "," + citiesClone.peek().getGoverment() + "," + citiesClone.pop().getGoverner().getName() + "\n";
                byte[] buffer = text.getBytes();
                BufferedOutputStream bufOut = new BufferedOutputStream(file, buffer.length);
                bufOut.write(buffer, 0, buffer.length);
            }
            file.close();
        result = "Коллекция сохранена";
        addToHistory("save");
    return result;
    }

    /**
     * Метод, исполняющий скрипт из указанного файла
     * @param path название файла со скриптом
     * @param tull сессия или коллекция, с которой мы работаем
     * @return возвращает строковое описние результатов действия метода
     * @throws IOException
     */
    public String executeScript(String path, WorkingWithMainStack tull) throws IOException {
        result = "";
        File script = new File(path);
        Scanner scriptScanner = new Scanner(script);
        View scriptView = new View(tull);

        addToHistory("execute_script file_name");

        while (scriptScanner.hasNextLine()){
            String line = scriptScanner.nextLine();
            result = result + line + "\n" +
                    scriptView.argCheck(line) + "\n";
        if (line.equals("exit")){
            System.exit(0);
        }
    }

        scriptScanner.close();
        return result;
    }
}