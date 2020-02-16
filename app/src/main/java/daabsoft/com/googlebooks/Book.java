package daabsoft.com.googlebooks;

public class Book {
    public String id;
    public String title;
    public String subTitle;
    public String[] authors;
    public String publisher;
    public String publishedDate;
    public String description;

    public Book(String id, String title, String subTitle, String[] authors,
                String publisher, String publishedDate, String description) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
    }
}
