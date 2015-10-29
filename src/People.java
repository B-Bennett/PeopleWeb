import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by zach on 10/19/15.
 */
public class People {
    static final int SHOW_COUNT = 20;
    public static void main(String[] args) {
        ArrayList<Person> people = new ArrayList();

        String fileContent = readFile("people.csv");
        String[] lines = fileContent.split("\n");

        for (String line : lines) {
            if (line == lines[0])
                continue;

            String[] columns = line.split(",");
            Person person = new Person(Integer.valueOf(columns[0]), columns[1], columns[2], columns[3], columns[4], columns[5]);
            people.add(person);
        }

        Spark.get(
                "/",
                ((request, response) -> {

                    String offset = request.queryParams("offset");
                    int offsetNum;
                    if (offset == null) {
                        offsetNum = 0;
                    }
                    else {
                        offsetNum = Integer.valueOf(offset);
                    }
                    if (offsetNum >= people.size() || offsetNum < 0) {
                        Spark.halt(403);
                    }


                    ArrayList<Person> temp = new ArrayList(people.subList(
                            Math.max(0, Math.min(people.size(), offsetNum)),
                            Math.max(0, Math.min(people.size(), offsetNum + SHOW_COUNT))
                    ));
                    HashMap m = new HashMap();
                    m.put("people", temp); // pass into template
                    m.put("oldOffset", offsetNum - SHOW_COUNT);
                    m.put("newOffset", offsetNum + SHOW_COUNT);

                    boolean showPervious = offsetNum > 0;
                    m.put("showPrevious", showPervious);

                    boolean showNext = offsetNum + SHOW_COUNT < people.size();
                    m.put("showNext", showNext);

                    return new ModelAndView(m, "people.html");
                }),
                new MustacheTemplateEngine()
        );

        Spark.get(
                "/person",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    String id = request.queryParams("id");

                    try {
                        int idNum = Integer.valueOf(id);
                        Person p = people.get(idNum - 1);
                        m.put("person", p);
                    }
                    catch (Exception e) {

                    }
                     return new ModelAndView(m, "person.html");
                }),

                new MustacheTemplateEngine()
        );
    }



    static String readFile(String fileName) {
        File f = new File(fileName);
        try {
            FileReader fr = new FileReader(f);
            int fileSize = (int) f.length();
            char[] fileContent = new char[fileSize];
            fr.read(fileContent);
            return new String(fileContent);
        } catch (Exception e) {
            return null;
        }
    }
}
