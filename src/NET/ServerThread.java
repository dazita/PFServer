package NET;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.*;
import structures.SimpleList;

public class ServerThread extends Thread {

    private Connection connection;
    private VehicleStore store;
    private boolean running;

    public ServerThread(Socket socket, VehicleStore store) {
        running = true;
        connection = new Connection(socket);
        this.store = store;
    }

    @Override
    public void run() {
        try {
            while (running) {
                String message = connection.receive();
                if (message == null) {
                    break;
                }
                processPetition(message.split(":", 2));
            }
        } catch (Exception e) {
            System.out.println("Error en la conexión: " + e.getMessage());
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }


    private void processPetition(String[] message) {
        switch (message[0]) {
            case "REGISTER":
                register(message[1]);
                break;

            case "LOGIN":
                login(message[1]);
                break;

            case "DELETE_ACC":
                deleteAccount(message[1]);
                break;

            case "SEARCH":
                search(message[1]);
                break;

            case "ADD_LISTING":
                addListing(message[1]);
                break;

            case "GET_BRANDS":
                sendBrands();
                break;

            case "GET_LINES":
                sendLines(message[1]);
                break;

            case "GET_MODELS":
                sendModels(message[1]);
                break;

            case "GET_MY_LISTINGS":
                sendMyListings(message[1]);
                break;

            case "DELETE_LISTING":
                deleteListing(message[1]);
                break;

            case "FILTER_MODEL":
                sendFilteredByModel(message[1]);
                break;

            case "FILTER_PRICE":
                sendFilteredByPrice(message[1]);
                break;

            case "GET_LISTING_FULL_INFO":
                sendListingFullInfo(message[1]);
                break;

            case "EXIT":
                running = false;
                connection.close();
                break;
                

            default:
                break;
        }
    }

    private void sendListingFullInfo(String JsonId){
        JsonObject obj = JsonParser.parseString(JsonId).getAsJsonObject();
        int id = Integer.valueOf(obj.get("id").getAsString());
        Listing listing = store.getListings().encontrar(new Listing(null, 0, null, null, null, id, 0));
        String phoneNumber = store.userPhoneByListingId(id);
        connection.send(buildFullInfoJson(listing, phoneNumber));
    }

    private String buildFullInfoJson(Listing listing, String phoneNumber){
        JsonObject obj = new JsonObject();
        obj.addProperty("reference", listing.getVehicle().getBrand().getName() + " " + listing.getVehicle().getLine().getName());
        obj.addProperty("vehicleType", listing.getVehicle().getVehicleType());
        obj.addProperty("model", String.valueOf(listing.getVehicle().getModel()));
        obj.addProperty("mileage", listing.getKms());
        obj.addProperty("price", Double.valueOf(listing.getPrice()));
        obj.addProperty("carImage", listing.getBase64Image());
        obj.addProperty("description", listing.getDescription());
        obj.addProperty("location", listing.getLocation());
        obj.addProperty("id", listing.getId());
        obj.addProperty("ownerPhoneNumber", Integer.valueOf(phoneNumber));
        return obj.toString();
    }

    private void sendFilteredByModel(String filterJson){
        JsonObject obj = JsonParser.parseString(filterJson).getAsJsonObject();
        int minModel = obj.get("minModel").getAsInt();
        int maxModel = obj.get("maxModel").getAsInt();
        SimpleList<Listing> filteredListings = store.filterByModelRange(minModel, maxModel);
        String filteredListingJson = buildListingJson(filteredListings);
        connection.send(filteredListingJson);
    }

    private void sendFilteredByPrice(String filterJson){
        JsonObject obj = JsonParser.parseString(filterJson).getAsJsonObject();
        int minPrice = obj.get("minPrice").getAsInt();
        int maxPrice = obj.get("maxPrice").getAsInt();
        SimpleList<Listing> filteredListings = store.filterByPriceRange(minPrice, maxPrice);
        String filteredListingJson = buildListingJson(filteredListings);
        connection.send(filteredListingJson);
    }

    private void deleteListing(String deleteJson){
        JsonObject obj = JsonParser.parseString(deleteJson).getAsJsonObject();
        String email = obj.get("email").getAsString();
        String id = obj.get("id").getAsString();
        store.deleteListing(Integer.valueOf(id), email);
    }

    private void sendMyListings(String jsonEmail){
        JsonObject obj = JsonParser.parseString(jsonEmail).getAsJsonObject();
        String email = obj.get("email").getAsString();
        SimpleList<Listing> myListingsList = store.getUserListings(email);
        String myListingsJson = buildListingJson(myListingsList);
        connection.send(myListingsJson);
    }

    private void sendBrands() {
        List<Brand> brands = store.getBrands().inOrder();
        List<String> brandNames = new ArrayList<>();
        for (Brand brand : brands) {
            brandNames.add(brand.getName());
        }
        JsonObject response = new JsonObject();
        JsonArray brandArray = new JsonArray();
        for (String name : brandNames) {
            brandArray.add(name);
        }
        response.add("brands", brandArray);
        connection.send(response.toString());
    }

    private void sendLines(String brandNameJson) {
        String brandName = JsonParser.parseString(brandNameJson).getAsJsonObject().get("brand").getAsString();
        Brand brand = store.getBrands().find(new Brand(brandName));
        List<Line> lines = brand.getLines().inOrder();
        List<String> linesNames = new ArrayList<>();
        for (Line line : lines) {
            linesNames.add(line.getName());
        }
        JsonObject response = new JsonObject();
        JsonArray lineArray = new JsonArray();
        for (String name : linesNames) {
            lineArray.add(name);
        }
        response.add("lines", lineArray);
        connection.send(response.toString());
    }

    private void sendModels(String json) {
        String brandName = JsonParser.parseString(json).getAsJsonObject().get("brand").getAsString();
        String lineName = JsonParser.parseString(json).getAsJsonObject().get("line").getAsString();
        Brand brand = store.getBrands().find(new Brand(brandName));
        Line line = brand.getLines().find(new Line(lineName));
        List<Integer> models = line.getModels().inOrder();
        JsonObject response = new JsonObject();
        JsonArray modelArray = new JsonArray();
        for (Integer model : models) {
            modelArray.add(model);
        }
        response.add("models", modelArray);
        connection.send(response.toString());
    }

    private void addListing(String listingJson) {
        JsonObject obj = JsonParser.parseString(listingJson).getAsJsonObject();
        String brand = obj.get("brand").getAsString();
        String line = obj.get("line").getAsString();
        int model = obj.get("model").getAsInt();
        int mileage = obj.get("mileage").getAsInt();
        String description = obj.get("description").getAsString();
        int price = obj.get("price").getAsInt();
        String location = obj.get("location").getAsString();
        String vehicleType = obj.get("vehicleType").getAsString();
        String imageBase64 = obj.get("image").getAsString();
        String email = obj.get("email").getAsString();
        store.createListing(brand, line, model, vehicleType, price, imageBase64, description, location, mileage, email);
    }

    private void search(String searchJson) {
        JsonObject obj = JsonParser.parseString(searchJson).getAsJsonObject();
        String search = obj.get("searchText").getAsString();
        SimpleList<Listing> foundListings = store.searchByReference(search);
        String foundListingsJson = buildListingJson(foundListings);
        connection.send(foundListingsJson);
    }

    private String buildListingJson(SimpleList<Listing> listings) {
        JsonArray listingsArray = new JsonArray();
        

        for (Listing listing : listings) {
            JsonObject obj = new JsonObject();
            obj.addProperty("reference", (listing.getVehicle().getBrand().getName() + " " + listing.getVehicle().getLine().getName()));
            obj.addProperty("price", listing.getPrice());
            obj.addProperty("year", listing.getVehicle().getModel());
            obj.addProperty("km", listing.getKms());
            obj.addProperty("location", listing.getLocation());
            obj.addProperty("id", listing.getId());
            obj.addProperty("image", listing.getBase64Image());
    
            listingsArray.add(obj);
        }

        return listingsArray.toString();
    }

    private void register(String userJson) {
        User user = User.fromJson(userJson);
        boolean confirmation = store.createUser(user);
        if (!confirmation) {
            connection.send("ERROR:Correo electronico ya registrado");
        } else {
            connection.send("VALID:Usuario registrado");
        }
    }

    private void login(String loginJson) {
        JsonObject obj = JsonParser.parseString(loginJson).getAsJsonObject();
        String email = obj.get("email").getAsString();
        String password = obj.get("password").getAsString();
        User confirmation = store.validateUser(email, password);
        if (confirmation == null) {
            connection.send("ERROR:Usuario o correo incorrectos");
        } else {
            connection.send("VALID:" + confirmation.getUsername());
        }
    }

    private void deleteAccount(String email) {
        System.out.println(email);
        store.deleteUser(email);
    }
}
