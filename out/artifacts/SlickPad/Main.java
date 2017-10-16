import java.util.*;
import java.nio.file.*;
import java.io.*;
class Main{
	public static void main(String args[]) throws FileNotFoundException, IOException{
		Scanner s = new Scanner(new File("file.txt"));
        ArrayList<String> in = new ArrayList<>();
        while(s.hasNextLine()){
			String t = s.nextLine();
         	t = t.replaceAll("\\[\\d*?\\]", "");
         	t = t + "\n";
         	in.add(t);
        }
		Path file = Paths.get("output.txt");
		Files.write(file, in);
    }
}

