package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.*;

import structures.BinaryTree;

public class VehiclesFetcher {
    
    public BinaryTree<Brand> fetchVehicles() {
        try {
            BinaryTree<Brand> brands = new BinaryTree<>(new BrandComparator());
            String jsonContent = new String(Files.readAllBytes(Paths.get("resources/Vehicles.json")));
            JsonArray brandsArray = JsonParser.parseString(jsonContent).getAsJsonArray();
            for (JsonElement brandElement : brandsArray) {
                Brand newBrand = fetchBrand(brandElement);
                brands.add(newBrand);
            }
            return brands;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Brand fetchBrand(JsonElement brandElement){
        JsonObject obj = brandElement.getAsJsonObject();
        String name = obj.get("name").getAsString();
        JsonArray linesArray = obj.getAsJsonArray("lines");
        Brand newBrand = new Brand(name);
        for (JsonElement lineElement : linesArray) {
            Line newLine = fetchLine(lineElement);
            newBrand.getLines().add(newLine);
        }
        return newBrand;
    }

    private Line fetchLine(JsonElement lineElement){
        JsonObject obj = lineElement.getAsJsonObject();
        String name = obj.get("name").getAsString();
        JsonArray modelsArray = obj.getAsJsonArray("models");
        Line newLine = new Line(name);
        for (JsonElement jsonElement : modelsArray) {
            newLine.getModels().add(jsonElement.getAsInt());
        }
        return newLine;
    }
    
}
