package saved_images;

import java.util.HashMap;
import java.util.Map;

public class SavedImages {
    private Map<String, String> imageMap;

    public SavedImages() {
        imageMap = new HashMap<>();
    }

    public void saveImage(String username, String imagePath) {
        imageMap.put(username, imagePath);
    }

    public String getImagePath(String username) {
        return imageMap.get(username);
    }


    //TILL EXEMPEL

    /*SavedImages storage = new ImageStorage();

     Spara bilder med användarnamn som nyckel
        storage.saveImage("user1", "path/to/image1.jpg");*/
}