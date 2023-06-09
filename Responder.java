import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

/**
 * The responder class represents a response generator object.
 * It is used to generate an automatic response, based on specified input.
 * Input is presented to the responder as a set of words, and based on those
 * words the responder will generate a String that represents the response.
 *
 * Internally, the reponder uses a HashMap to associate words with response
 * strings and a list of default responses. If any of the input words is found
 * in the HashMap, the corresponding response is returned. If none of the
 * input words is recognized, one of the default responses is randomly chosen.
 * 
 * @author Salvatore Anzalone
 * @version 4/17/2023
 */
public class Responder
{
    // Used to map key words to responses.
    private HashMap<String, String> responseMap;
    // Default responses to use if we don't recognise a word.
    private ArrayList<String> defaultResponses;
    // The name of the file containing the default responses.
    private static final String FILE_OF_DEFAULT_RESPONSES = "default.txt";
    private Random randomGenerator;

    /**
     * Construct a Responder
     */
    public Responder()
    {
        responseMap = new HashMap<>();
        defaultResponses = new ArrayList<>();
        fillResponseMap();
        fillDefaultResponses();
        randomGenerator = new Random();
    }

    /**
     * Generate a response from a given set of input words.
     * 
     * @param words  A set of words entered by the user
     * @return       A string that should be displayed as the response
     */
    public String generateResponse(HashSet<String> words)
    {
        Iterator<String> it = words.iterator();
        
        while(it.hasNext())
        {
            String word = it.next();
            String response = responseMap.get(word);
            
            if(response != null)
            {
                return response;
            }
        }
        // If we get here, none of the words from the input line was
        // recognized.
        // In this case we pick one of our default responses (what we say
        // when we cannot think of anything else to say...)
        
        return pickDefaultResponse();
    }

    /**
     * Enter all the known keywords and their associated responses
     * into our response map.
     * This data is copied over to a text file, which puts the
     * information into a single new file, containing the format that
     * was originally here.  However, some alterations were made, but only
     * for keywords that share the same response.
     */
    private void fillResponseMap()
    {
        responseMap.put("crash",
                        "Well, it never crashes on our system.\n" +
                        "It must have something to do with your system.\n" +
                        "Tell me more about your configuration.\n\n");
        responseMap.put("crashes",
                        "Well, it never crashes on our system.\n" +
                        "It must have something to do with your system.\n" +
                        "Tell me more about your configuration.\n\n");
        responseMap.put("slow", 
                        "I think this has to do with your hardware.\n" +
                        "Upgrading your processor should solve all\n" +
                        "performance problems. Have you got a problem\n" +
                        "with our software?\n\n");
        responseMap.put("performance", 
                        "Performance was quite adequate in all our\n" +
                        "tests. Are you running any other processes\n" +
                        "in the background?\n\n");
        responseMap.put("bug",
                        "Well, you know, all software has some bugs.\n" +
                        "But our software engineers are working very\n" +
                        "hard to fix them. Can you describe the problem\n" +
                        "a bit further?\n\n");
        responseMap.put("buggy",
                        "Well, you know, all software has some bugs.\n" +
                        "But our software engineers are working very\n" +
                        "hard to fix them. Can you describe the problem\n" +
                        "a bit further?\n\n");
        responseMap.put("windows", 
                        "This is a known bug to do with the Windows\n" +
                        "operating system. Please report it to\n" +
                        "Microsoft. There is nothing we can do about\n" +
                        "this.\n\n");
        responseMap.put("macintosh", 
                        "This is a known bug to do with the Mac\n" +
                        "operating system. Please report it to Apple.\n" +
                        "There is nothing we can do about this.\n\n");
        responseMap.put("expensive", 
                        "The cost of our product is quite\n" +
                        "competitive. Have you looked around and really\n" +
                        "compared our features?\n\n");
        responseMap.put("installation", 
                        "The installation is really quite straight\n" +
                        "forward. We have tons of wizards that do\n" +
                        "all the work for you. Have you read the\n" +
                        "installation instructions?\n\n");
        responseMap.put("memory", 
                        "If you read the system requirements\n" +
                        "carefully, you will see that the specified\n" +
                        "memory requirements are 1.5 giga byte. You\n" +
                        "really should upgrade your memory. Anything\n" +
                        "else you want to know?\n\n");
        responseMap.put("linux", 
                        "We take Linux support very seriously. But\n" +
                        "there are some problems. Most have to do with\n" +
                        "incompatible glibc versions. Can you be a bit\n" +
                        "more precise?\n\n");
        responseMap.put("bluej", 
                        "Ahhh, BlueJ, yes. We tried to buy out those\n" +
                        "guys long ago, but they simply won't sell...\n" +
                        "Stubborn people they are. Nothing we can\n" +
                        "do about it, I'm afraid.\n\n");
        responseMap.put("other", 
                        "It's good to hear that you are not\n" +
                        "experiencing problems with the any of the\n" +
                        "operating systems we are aware of. But, if\n" +
                        "your situation has to do with how Java works,\n" +
                        "please refer to the Java documentation of\n" +
                        "specific methods, clauses, etc.\n\n");
        responseMap.put("mobile",
                        "We are not capable of interacting with you\n" +
                        "on a mobile device. This is something we\n" + 
                        "should consider doing.\n\n");
        responseMap.put("documentation",
                        "Feel free to view the documentation found\n" +
                        "within this system. This system has public\n" +
                        "access to all users!\n\n");
        
        //These responses and keywords can also be viewed in the text file
        //named default.txt.
        responseMap.get(FILE_OF_DEFAULT_RESPONSES);
    }

    /**
     * Build up a list of default responses from which we can pick
     * if we don't know what else to say.
     */
    private void fillDefaultResponses()
    {
        Charset charset = Charset.forName("US-ASCII");
        Path path = Paths.get(FILE_OF_DEFAULT_RESPONSES);
        
        try (BufferedReader reader = Files.newBufferedReader(path, charset))
        {
            String response = reader.readLine() + "\n";
            
            while(response != null)
            {
                defaultResponses.add(response);
                response = reader.readLine();
            }
        }
        
        catch(FileNotFoundException e)
        {
            System.err.println("Unable to open " +
                                FILE_OF_DEFAULT_RESPONSES + "\n\n");
        }
        
        catch(IOException e)
        {
            System.err.println("A problem was encountered reading " +
                               FILE_OF_DEFAULT_RESPONSES + "\n\n");
        }
        
        // Make sure we have at least one response.
        
        if(defaultResponses.size() == 0)
        {
            defaultResponses.add("Could you elaborate on that?\n\n");
        }
    }

    /**
     * Randomly select and return one of the default responses.
     * @return     A random default response
     */
    private String pickDefaultResponse()
    {
        // Pick a random number for the index in the default response list.
        // The number will be between 0 (inclusive) and the size of the list
        // (exclusive).
        int index = randomGenerator.nextInt(defaultResponses.size());
        
        return defaultResponses.get(index);
    }
}