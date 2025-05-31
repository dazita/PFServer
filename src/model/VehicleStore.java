package model;
import java.util.List;

import structures.*;

public class VehicleStore {

    private BinaryTree<User> users;
    private BinaryTree<Brand> brands;
    private ArbolAvl<Listing> listings;
    private static int idCounter = 0;

    public VehicleStore() {
        this.users = new BinaryTree<User>(new UserComparator());
        this.brands = new VehiclesFetcher().fetchVehicles();
        this.listings = new ArbolAvl<>(new ListingComparatorId());
    }

    public ArbolAvl<Listing> getListings() {
        return listings;
    }

    public boolean createUser(User user) {
        return users.add(user);
    }

    public void deleteUser(String email) {
        User toDelete = users.find(new User(email));
        for (Integer i : toDelete.getListingIds().inOrder()) {
            deleteListing(i, email);
        }
        users.remove(new User(email));
    }

    public User validateUser(String email, String password){
        User user = users.find(new User(email));
        if (user!=null) {
            if (password.equals(user.getPassword())) {
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void createListing(String brandString, String lineString, int modelYear, String vehicleType,  int price, String base64Image, String description, String location, int kms, String userEmail) {
        try {
            Brand existingBrand = brands.find(new Brand(brandString));
            Line existingLine = existingBrand.getLines().find(new Line(lineString));
            Integer existingModel = existingLine.getModels().find(modelYear);
            Listing listing = new Listing(new Vehicle(existingBrand, existingLine, existingModel, vehicleType), price, base64Image, description, location, generateId(), kms);
            listings.insertar(listing);
            User user = users.find(new User(userEmail));
            user.getListingIds().add(listing.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer generateId() {
        return ++idCounter;
    }

    public void deleteListing(int listingId, String email) {
        try {
            listings.eliminar(new Listing(null, 0, null, null, null, listingId, 0));
            User owner = users.find(new User(email));
            owner.getListingIds().remove(listingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BinaryTree<Brand> getBrands() {
        return brands;
    }

    public SimpleList<Listing> searchByReference(String search) {
        List<Listing> listingsList = listings.showInOrder();
        SimpleList<Listing> foundListings = new SimpleList<>();
        for (Listing listing : listingsList) {
            if ((listing.getVehicle().getBrand().getName() + " " + listing.getVehicle().getLine().getName()).equals(search)) {
                foundListings.add(listing);
            }
        }
        return foundListings;
    }

    public SimpleList<Listing> filterByModelRange(int firstYear, int lastYear){
        List<Listing> listingsArray = listings.showInOrder();
        SimpleList<Listing> filteredListings = new SimpleList<>();
        for (Listing listing : listingsArray) {
            int year = listing.getVehicle().getModel();
            if (year > lastYear) {
                break;
            }
            if (year >= firstYear && year <= lastYear) {
                filteredListings.add(listing);
            }
        }
        return filteredListings;
    }

    public SimpleList<Listing> filterByPriceRange(int firstPrice, int lastPrice){
        List<Listing> listingsArray = listings.showInOrder();
        SimpleList<Listing> filteredListings = new SimpleList<>();
        for (Listing listing : listingsArray) {
            int price = listing.getPrice();
            if (price > lastPrice) {
                break;
            }
            if (price >= firstPrice && price <= lastPrice) {
                filteredListings.add(listing);
            }
        }
        return filteredListings;
    }

    public SimpleList<Listing> getUserListings(String email){
        User user = users.find(new User(email));
        SimpleList<Listing> foundListings= new SimpleList<>();
        for (Integer i : user.getListingIds().inOrder()) {
            foundListings.add(listings.encontrar(new Listing(null, 0, null, null, null, i, 0)));
        }
        return foundListings;
    }

    public String userPhoneByListingId(int id){
        for (User u : users.inOrder()) {
            for (Integer i : u.getListingIds().inOrder()) {
                if (i.equals(id)) {
                    return u.getPhoneNumber();
                }
            }
        }
        return null;
    }
}
