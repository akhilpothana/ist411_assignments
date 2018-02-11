
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class NewsDeck extends JPanel {

    private JButton articleButtons[] = new JButton[7];
    
    private final String API_Key = "74e0caef3f69426e9228da0337cfbb18";
    private Desktop desktop = Desktop.getDesktop();

    public NewsDeck() {
        super();
        setBackground(Color.white);
        setLayout(new GridLayout(7,1));        
        
        for(int i=0; i<= 6; i++)
        {
            articleButtons[i] = new JButton("");
            add(articleButtons[i]);
        }
        getNews();  
    }

    private void getNews() {
      
        JSONParser parser = new JSONParser();
        for(int i=0; i<= 6; i++)
        {
            try {  
                JSONObject json = (JSONObject) parser.parse(getNewsData());
                JSONArray articles = (JSONArray) json.get("articles");
                JSONObject article = (JSONObject) articles.get(i);

                articleButtons[i].setText((String)article.get("title"));
                articleButtons[i].addActionListener((ActionEvent e) -> {
                    
                   try {
                        desktop.browse(new URL((String)article.get("url")).toURI());
                    }
                    catch(Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        } 
        
    }
    
    public String getNewsData(){
        
        String response = "";
        try {
            URL url = new URL("https://newsapi.org/v2/top-headlines?country=us&apiKey=" + API_Key);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // read the response and obtain string value of JSON data
            InputStream in = new BufferedInputStream(connection.getInputStream());
            response = convertJSONtoString(in);
        }
        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return response;
    }
    
    private String convertJSONtoString(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }      
        return sb.toString();
    }
}
