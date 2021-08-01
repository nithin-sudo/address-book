package com.bridgelabz.address;
import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
public class Service
{
    private List<Person> addressBookData;
    private PreparedStatement addressBookPreparedStatement;
    static Scanner scanner = new Scanner(System.in);
    ArrayList<Person> personList = new ArrayList<>();
    HashMap<String, ArrayList<Person>> addressBooks = new HashMap<>();
   // public enum IOService {CONSOLE_IO, FILE_IO, DB_IO, REST_IO}
    public static final String FILE_PATH="C:\\Users\\nithinkrishna\\Desktop";
    public static String addressBookFile = "AddressBookFile.txt";
    public static final String CSV_FILE="/addressBook.csv";
    public static final String JSON_FILE="/addressBook.json";
    /**
     * Asking user enter the details of the person and adding multiple persons from console."
     */
    public void addNewContact() {
        Person person = new Person();
        System.out.println("Enter First name:");
        person.setFirstName(scanner.next());

        System.out.println("Enter Last Name:");
        person.setLastName(scanner.next());

        System.out.println("Enter Address:");
        person.setAddress(scanner.next());

        System.out.println("Enter City:");
        person.setCity(scanner.next());

        System.out.println("Enter State:");
        person.setState(scanner.next());

        System.out.println("Enter Zip:");
        person.setZip(scanner.nextInt());

        System.out.println("Enter Email:");
        person.setEmail(scanner.next());

        System.out.println("Enter Phone:");
        person.setPhoneNumber(scanner.next());

        System.out.println("Enter Book name to which you have to add contact");
        String bookName = scanner.next();
        if (addressBooks.containsKey(bookName)) {
            personList.stream().filter(value -> value.getFirstName().equals(person.getFirstName())).forEach(value ->
            {
                System.out.println("duplicate contact");
                addNewContact();
            });
            personList.add(person);
            addressBooks.put(bookName, personList);
            System.out.println("New Contact Added Successfully");
        } else {
            personList.add(person);
            addressBooks.put(bookName, personList);
            System.out.println("New book created and added Contact Added Successfully");
        }
    }

    /**
     * writing data into a file.
     */

    public void writeToFile()
    {
        StringBuffer addressBuffer = new StringBuffer();
        personList.forEach(address -> { String addressDataString = address.toString().concat("\n");addressBuffer.append(addressDataString);});
        try
        {
            Files.write(Paths.get(addressBookFile),addressBuffer.toString().getBytes(StandardCharsets.UTF_8));
            System.out.println("Data successfully written to file.");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * reading data from file and printing to console.
     */
    public void readDataFromFile()
    {
        try
        {
            System.out.println("Reading Data From File :");
            Files.lines(new File(addressBookFile).toPath()).map(String::trim).forEach(line -> System.out.println(line));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * editing the existing of a person using their first name.
     */
    public void editContact(String bookName) {
        if (addressBooks.containsKey(bookName)) {
            String enteredFirstName;
            System.out.println("Enter First name of contact to edit it ");
            enteredFirstName = scanner.next();
            personList.stream().filter(person -> person.getFirstName().equals(enteredFirstName)).forEach(person -> {
                System.out.println("Enter the field to edit:\n1.First Name\n2.Last Name\n3.Address\n4.city\n5.State\n6.Zip\n7.Phone\n8.Email");
                int userInput = scanner.nextInt();
                switch (userInput) {
                    case 1 -> {
                        System.out.println("Enter new first name");
                        person.setFirstName(scanner.next());
                    }
                    case 2 -> {
                        System.out.println("Enter new last name");
                        person.setLastName(scanner.next());
                    }
                    case 3 -> {
                        System.out.println("Enter new Address");
                        person.setAddress(scanner.next());
                    }
                    case 4 -> {
                        System.out.println("Enter new city");
                        person.setCity(scanner.next());
                    }
                    case 5 -> {
                        System.out.println("Enter new state");
                        person.setState(scanner.next());
                    }
                    case 6 -> {
                        System.out.println("Enter new zip");
                        person.setZip(scanner.nextInt());
                    }
                    case 7 -> {
                        System.out.println("Enter new phone number");
                        person.setPhoneNumber(scanner.next());
                    }
                    case 8 -> {
                        System.out.println("Enter new email");
                        person.setEmail(scanner.next());
                    }
                    default -> System.out.println("Invalid Entry");
                }
            });
        }
    }

    /**
     * deleting a person using person's name
     *
     * @param name
     */
    public void deleteContact(String name, String bookName) {
        if (addressBooks.containsKey(bookName)) {
            for (int i = 0; i < personList.size(); i++) {
                if (personList.get(i).getFirstName().equals(name)) {
                    Person person = personList.get(i);
                    personList.remove(person);
                }
            }
        }
    }

    /**
     * displaying the list
     */
    public void displayList() {
        for (Person person : personList) {
            System.out.print(person + "\n");
        }
    }

    /**
     * searching a person by city.
     *
     * @param city
     */
    public void searchPersonInACity(String city) {
        System.out.println("following are the persons who belongs to :" + city);
        for (String bookName : addressBooks.keySet()) {
            addressBooks.get(bookName);
            personList.stream().filter(value -> value.getCity().equals(city)).map(Person::getFirstName).forEach(System.out::println);
        }
    }

    /**
     * viewing a person according to the city.
     *
     * @param city
     */
    public void viewPersonInACity(String city) {
        for (String bookName : addressBooks.keySet()) {
            int countPerson = 0;
            addressBooks.get(bookName);
            personList.stream().filter(value -> value.getCity().equals(city)).map(Person::getFirstName).forEach(System.out::println);
            countPerson++;
            System.out.println("total persons:" + countPerson);
        }
    }

    /**
     * person list will be sorted in a alphabetical order.
     */
    public void sortByName() {
        addressBooks.keySet().forEach((String name) -> {
            addressBooks.get(name).stream()
                    .sorted(Comparator.comparing(Person::getFirstName))
                    .collect(Collectors.toList())
                    .forEach(person -> System.out.println(person.toString()));
        });
    }

    /**
     * person list will be sorted and displayed with city name of an alphabetical order.
     */

    public void sortByCity() {
        addressBooks.keySet().forEach((String key) -> {
            addressBooks.get(key).stream()
                    .sorted(Comparator.comparing(Person::getCity))
                    .collect(Collectors.toList())
                    .forEach(person -> System.out.println(person.toString()));
        });
    }
    public void writeToCsv()
    {
        try
        {
            Writer writer = Files.newBufferedWriter(Paths.get(FILE_PATH+CSV_FILE));
            StatefulBeanToCsv<Person> beanToCsv = new StatefulBeanToCsvBuilder<Person>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
            List<Person> ContactList = new ArrayList<>();
            addressBooks.entrySet().stream()
                    .map(Map.Entry::getKey)
                    .map(bookNames->{
                        return addressBooks.get(bookNames);
                    }).forEach(contacts ->{
                ContactList.addAll(contacts);
            });
            beanToCsv.write(ContactList);
            writer.close();
        }
        catch (CsvDataTypeMismatchException e)
        {
            e.printStackTrace();
        }
        catch (CsvRequiredFieldEmptyException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void readFromCsvFile()
    {
        Reader reader;
        try {
            reader = Files.newBufferedReader(Paths.get(FILE_PATH+CSV_FILE));
            CsvToBean<Person> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(Person.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<Person> contacts = csvToBean.parse();

            for(Person contact: contacts) {
                System.out.println("Name : " + contact.getFirstName()+" "+contact.getLastName());
                System.out.println("Email : " + contact.getEmail());
                System.out.println("PhoneNo : " + contact.getPhoneNumber());
                System.out.println("Address : " + contact.getAddress());
                System.out.println("State : " + contact.getState());
                System.out.println("City : " + contact.getCity());
                System.out.println("Zip : " + contact.getZip());
                System.out.println("==========================");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void writeToJson()
    {
        List<Person> contacts = getContentOfCsv();
        Gson gson = new Gson();
        String json = gson.toJson(contacts);
        try
        {
            FileWriter writer = new FileWriter(FILE_PATH+JSON_FILE);
            writer.write(json);
            writer.close();
            System.out.println("Written sucessfully");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void readFromJson()
    {
        try
        {
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new FileReader(FILE_PATH+JSON_FILE));
            Person[] contacts = gson.fromJson(br,Person[].class);
            List<Person> contactsList = Arrays.asList(contacts);
            for(Person contact: contactsList) {
                System.out.println("Name : " + contact.getFirstName()+" "+contact.getLastName());
                System.out.println("Email : " + contact.getEmail());
                System.out.println("PhoneNo : " + contact.getPhoneNumber());
                System.out.println("Address : " + contact.getAddress());
                System.out.println("State : " + contact.getState());
                System.out.println("City : " + contact.getCity());
                System.out.println("Zip : " + contact.getZip());
                System.out.println("==========================");
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    private List<Person> getContentOfCsv()
    {
        try
        {
            Reader reader = Files.newBufferedReader(Paths.get(FILE_PATH+CSV_FILE));  
            CsvToBean<Person> csvToBean = new CsvToBeanBuilder<Person>(reader)
                    .withType(Person.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static Service addressBookDBService;

    Service() {
    }

    public static Service getInstance() {
        if (addressBookDBService == null)
            addressBookDBService = new Service();
        return addressBookDBService;
    }

    public List<Person> readData() throws AddressBookException {
        String sql = "SELECT * FROM address_book; ";
        return this.getAddressBookDataUsingDB(sql);
    }

    private List<Person> getAddressBookDataUsingDB(String sql) throws AddressBookException {
        List<Person> addressBookList = new ArrayList<>();
        try (Connection connection = AddressBookConnection.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            addressBookList = this.getAddressBookDetails(resultSet);
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
        }
        return addressBookList;
    }

    private List<Person> getAddressBookDetails(ResultSet resultSet) throws AddressBookException {
        List<Person> addressBookList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String address = resultSet.getString("Address");
                String city = resultSet.getString("City");
                String state = resultSet.getString("State");
                int zip = resultSet.getInt("Zip");
                String phoneNo = resultSet.getString("Phone");
                String email = resultSet.getString("Email");
                addressBookList.add(new Person(firstName, lastName, address, city, state, zip , phoneNo, email));
            }
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
        }
        return addressBookList;
    }
    AddressBookConnection addressBookConnection = new AddressBookConnection();
    private void prepareAddressBookStatement() throws AddressBookException {
        try {
            Connection connection = addressBookConnection.getConnection();
            String query = "select * from address_book where FirstName = ?";
            addressBookPreparedStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
        }
    }
    public int updateAddressBookData(String firstname, String address) throws AddressBookException {
        String query = String.format("update address_book set Address = '%s' where FirstName = '%s';", address,
                firstname);
        try (Connection connection = addressBookConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeUpdate(query);
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.CONNECTION_FAILED);
        }
    }

    public List<Person> getAddressBookData(String firstname) throws AddressBookException {
        if (this.addressBookPreparedStatement == null)
            this.prepareAddressBookStatement();
        try {
            addressBookPreparedStatement.setString(1, firstname);
            ResultSet resultSet = addressBookPreparedStatement.executeQuery();
            addressBookData = this.getAddressBookDetails(resultSet);
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.CONNECTION_FAILED);
        }
        System.out.println(addressBookData);
        return addressBookData;
    }
    public List<Person> readData(LocalDate start, LocalDate end) throws AddressBookException {
        String query = null;
        if (start != null)
            query = String.format("select * from address_book where Date between '%s' and '%s';", start, end);
        if (start == null)
            query = "select * from address_book";
        List<Person> addressBookList = new ArrayList<>();
        try (Connection con = addressBookConnection.getConnection()) {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            addressBookList = this.getAddressBookDetails(rs);
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
        }
        return addressBookList;
    }
}
