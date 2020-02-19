package gson;

import com.google.gson.Gson;

public class GsonJavaTest1 {
    public static void main(String[] args) {

        Gson gson = new Gson();

        PersonJava p1 = new PersonJava("Alice", 18, "F", "alice@alice.com");

        System.out.println(gson.toJson(p1));
        // {"name":"Alice","age":18,"gender":"F","email":"alice@alice.com"}

        PersonJava p2 = gson.fromJson("{\"name\":\"Bob\",\"age\":23,\"gender\":\"M\",\"email\":\"bob@bob.com\"}", PersonJava.class);

        System.out.println(p2.toString());
        // PersonJava{name='Bob', age=23, gender='M', email='bob@bob.com'}

    }
}
