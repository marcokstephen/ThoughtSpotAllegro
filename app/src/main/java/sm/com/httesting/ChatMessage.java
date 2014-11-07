package sm.com.httesting;

public class ChatMessage {

    private String author;
    private String message;

    //needed so Firebase can automatically construct object
    private ChatMessage(){}

    public ChatMessage(String op, String message){
        this.author = op;
        this.message = message;
    }

    public String getAuthor(){
        return this.author;
    }

    public String getMessage(){
        return this.message;
    }
}
