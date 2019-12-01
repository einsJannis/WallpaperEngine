package dev.ludium.wallpaperengine;

import dev.einsjannis.jsonparser.JSONArray;
import dev.einsjannis.jsonparser.JSONObject;
import dev.einsjannis.jsonparser.JSONSyntaxError;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Main {

    public static void main(String[] args) throws IOException, JSONSyntaxError {
        File workingDirectory = new File(".");
        File configFile = new File("config.json");
        if (!configFile.exists()) {
            PrintWriter configWriter = new PrintWriter(configFile);
            configWriter.println("" +
                    "{\n" +
                    "    \"delay\":10000,\n" +
                    "    \"pictures\": [\n" +
                    "        \"sample-background-0.jpg\",\n" +
                    "        \"sample-background-1.jpg\",\n" +
                    "        \"sample-background-2.jpg\"\n" +
                    "    ]\n" +
                    "}");
            configWriter.close();
        }
        for (int i = 0; i < 3; i++) {
            File sampleBackground = new File("sample-background-" + i + ".jpg");
            if (!sampleBackground.exists()) {
                Files.copy(Main.class.getResourceAsStream("/sample-background-" + i + ".jpg"), sampleBackground.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
        FileReader configreader = new FileReader(configFile);
        BufferedReader bufferedconfigreader = new BufferedReader(configreader);
        String configstring = "";
        while(true) {
            String s = bufferedconfigreader.readLine();
            if (s==null) {
                break;
            }
            configstring += s;
        }
        JSONObject configjson = JSONObject.fromString(configstring);
        JSONArray picturesarray = (JSONArray) configjson.get("pictures");
        while (true) {
            picturesarray.forEach(picture -> {
                User32.INSTANCE.SystemParametersInfo(0x0014, 0, new File((String) picture).getAbsolutePath(), 1);
                System.out.println(new File((String) picture).getAbsolutePath());
                try {
                    Thread.sleep(Math.round((Double) configjson.get("delay")));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
