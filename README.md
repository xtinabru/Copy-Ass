[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/cOYaqe20)

# Assignment 5: Java Streams and Exceptions

In this repository, you will find the exercises on records, exceptions and streams. You will learn how to create your own records and exception classes and how to use the I/O streams and the streams API.

A this point in the course, these exercises require that you study the documentation of some Java classes by yourself. All the utilized classes have not been presented during lectures.

## Workflow (the same as usual)

1. Clone this repository to your local machine.

2. Open the project in VS Code.

3. Run the project as it is, just to make sure it works. The program should output something like `Main does nothing`.

4. You can also run the tests now. Obviously, if you haven't finished exercises, tests will fail now and you will see a lot of errors.

5. Follow the instructions below for each exercise to complete the exercise.

6. You can any time run the tests to see if your answer code is correct.

7. Finally when you are satisfied with your answers, commit and push the repository back to the classroom.

## VideoFile Metadata (1p)

In this exercise, you will add more features into the `VideoFile` class that you wrote in the previous assignment. The idea is to parse the given file name (in constructor) to a record, which is a private member of the class.

You need to have record `Metadata`, which declaration should be the following and declared in its own Java file.

```Java
record Metadata(String author, String name, String fileType)
```

Have the following member variables in the `VideoFile` class:

```Java
private Metadata videoFileData
private String fileName;
```

Add the following constructor to parse the given parameter:

```Java
public VideoFile(String videoFileName) {

}
```

The parameter contains a string as `Ludwig van Beethoven - Ode an die Freude.mp4`, which you should parse so that you have "Ludwig van Beethoven" as `author`, "Ode an die Freude" as the `name` and "mp4" as the `fileType` filled in the member record. 

To parse the parameter string, look up the method `split()` in the `String` class. Here also the JDK class `java.util.regex.Pattern` could also be very useful.

Finally, add getter for the video file metadata:

```Java
public Metadata getVideoFileData() {

}
```

Note that you should not change the previous implementation of the `Playable` interface in this class.

## Read a CSV file (3p)

In this exercise, we practise working with Java exceptions. You write a class to read and parse a [CSV](https://en.wikipedia.org/wiki/Comma-separated_values) file. And, you also check that each read line in the file is in the correct format.

The CSV file has the following format, with fields being: `name`, `price`, `category` and `bestBefore` date. 

```text
shirt,12.99,clothes,2024-11-05T19:46:00.000
shirt,12.99,clothes,2024-11-05T19:47:00.000
shirt,12.99,clothes,2024-11-05T19:48:0.000
```

The record `Product` should have the same fields. Create this class in its own file, since you will soon extend it. To our delight, the CSV file format maps directly to these fields.

```Java
public record Product(String name, Double price, String category, LocalDateTime bestBefore)
```

For this exercise, create class `ReadProductFile` which has the following members:

```Java
private File dataFile
private List<Product> products = new LinkedList<Product>()
```

Add new exception class `CSVMissingParameterException` as a member to this class, having only a constructor with `String message` parameter, that passes the message to the superclass `Exception`.

```Java
public class CSVMissingParameterException
```

Next, add class constructor with the following signature, with the file name to be opened as parameter. Constructor should check whether the given file exists by using the method `exists()` of the `File` class. If not, then the exception `IllegalArgumentException` should be thrown. Do not catch this exception in the method itself (as we want to test it..).

```Java
public ReadProductFile(String fileName)
```

Add public getter `getProducts()` for the list of products.

Last but not least, add the method `public void readCSV()` to actually read the contents of the file. This method parses each line in the file and add the parsed data as record into `productList`. The method signature should be the following:

```Java
public void readCSV() {

}
```

This function should first, before adding anything to the product list, check the validity of each CSV line. For this, use the just created exception `CSVMissingParameterException` to report errors. You need to check for two conditions:

1. Each CSV line has exactly four fields.

2. None of the fields is empty.

If either condition fails, the `CSVMissingParameterException` should be thrown with message (an example) `Error in products.csv, skipped line 2: missing parameter`, including the csv file name and the line number where the error occurred, and the method should also print out the stack trace.

If an exception is thrown in any line, *do not add the data into the product list*.

The assignment repository contains test file `products.csv` for implementation testing. For testing, there are faulty sample files under the directory `src/test`.

## Stock management (3p)

Now, you write the class `Stock` which has all kinds of neat properties!

### Extend record Product

First, we extend the record class `Product` from the previous exercise with the implementation of the interface `OutputFormatter`.

The interface `OutputFormatter` should have only one method:

```Java
public String formatProduct(DateTimeFormatter dt) {
}
```

This method uses the object data type [DateTimeFormatter](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html) for the parameter.

The method returns a String of the following format `[%s,%s,%.2f,%s]`, where the first parameter is the `name` of the product, second the `category`, third the `price` with two decimals and finally `bestBefore` in the format `yyyy.MM.dd`.

Caveat: Here you MUST set the locale to US, otherwise the automatic tests at classroom side will not work. To do this, add locale parameter to the method format as `String.format(Locale.US,"[%s,%s,%.2f,%s]", ..)`.

Then, to format the `bestBefore` field of the record to `yyyy.MM.dd`, we need the help of the `DateTimeFormatter` class. Here is an example of how to use it.

```Java
DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");
LocalDateTime currentTime = LocalDateTime.of(2024, 9, 27, 13, 11, 43);  // y, m, d, h, m, s
LocalDateTime parsedcurrentTime = LocalDateTime.parse(currentTime, myFormat);
```

As as example, consider the product `[name=shirt, price=12.99, category=clothes, bestBefore=2024-11-05T19:46]`. Using the `formatProduct` method, the returned string should be in the form `[shirt,clothes,12.99,2024.11.05]`.

### Stock class

Next, let's work on the class `Stock` to implement the main logic for this exercise. Add the following members:

```Java
private List<Product> products = new LinkedList<Product>()
private LocalDateTime expireTime // needed to implement the filter (?)
private DateTimeFormatter stockFormat // initialized to yyyy.MM.dd
```

Then, add getter for the list of products.

Also, add method `addProduct(Product p)` to add products into the list.

Next, override the method `public String toString()` (from the Object class) to return the following output as a string. Note the exact format of the output, there are title lines, then each product in the list through the `formatProduct` method, and lastly, the total number of products in the list and their total price.

```text
List of Products
----------------
[fish,food,14,99,2024.09.01]
[fish,food,14,99,2024.09.01]
[fish,food,14,99,2024.09.02]
-> Total products: 3
-> Total price: 44.97
```

Here you might find the JDK class [StringBuilder](https://docs.oracle.com/javase/tutorial/java/data/buffers.html) useful. This class implements the Stream API and is also in fact a *design pattern*..

All right. Let's also practise the Java stream API with this class. Implement the method below to report the products whose `bestBefore` date is before the given parameter. Meaning that such products have expired.

```Java
public List<Product> reportExpired(LocalDateTime dt) {

}
```

Here, you should implement the expiration filter in the stream based on the interface `Predicate` as shown below. To make the work much easier, it pays to check what methods the `LocalDateTime` class has already built in..

Here is the signature for the filter:

```Java
private Predicate<Product> expireFilter = new Predicate<Product>() {

}
```

There are obviously other ways to implement this expiration functionality, but let's practise stream API here.

This exercise may feel a bit annoying with all the conversions and locale considerations, however, it is similar to the common task of programmers of processing data read from all kinds of APIs and databases.
