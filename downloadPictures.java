
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class doanloadPictures {
    private static final String IN_FILE_TXT = "C:\\Users\\polue\\IdeaProjects\\Projects\\src\\downloadpictures\\inPictures.txt";
    private static final String OUT_FILE_TXT = "C:\\Users\\polue\\IdeaProjects\\Projects\\src\\downloadpictures\\outPictures.txt";
    private static final String PATH_TO_MUSIC = "C:\\Users\\polue\\IdeaProjects\\Projects\\src\\downloadpictures\\picture";

    public static void main(String[] args) {
        String Url;
        try (BufferedReader inFile = new BufferedReader(new FileReader(IN_FILE_TXT));
             BufferedWriter outFile = new BufferedWriter(new FileWriter(OUT_FILE_TXT))) {
            while ((Url = inFile.readLine()) != null) {
                URL url = new URL(Url);

                String result;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    result = bufferedReader.lines().collect(Collectors.joining("\n"));
                }
                Pattern email_pattern = Pattern.compile("(?<=data-src ?= ?\")https?://\\S+(?:jpg|jpeg|png)"); //("\\s*(?<=data-src\\s?=\\s?\")\\.(jpg|png|jpeg|gif)");
                Matcher matcher = email_pattern.matcher(result);
                int i = 0;
                while (matcher.find() && i < 2) {
                    outFile.write(matcher.group() + "\r\n");
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader pictureFile = new BufferedReader(new FileReader(OUT_FILE_TXT))) {
            String picture;
            int count = 0;
            try {
                while ((picture = pictureFile.readLine()) != null) {
                    downloadUsingNIO(picture, PATH_TO_MUSIC + String.valueOf(count) + ".png");
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadUsingNIO(String strUrl, String file) throws IOException {
        URL url = new URL(strUrl);
        ReadableByteChannel byteChannel = Channels.newChannel(url.openStream());
        FileOutputStream stream = new FileOutputStream(file);
        stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
        stream.close();
        byteChannel.close();
    }
}
