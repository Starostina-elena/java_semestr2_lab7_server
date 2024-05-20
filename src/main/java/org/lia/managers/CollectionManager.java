package org.lia.managers;

import org.lia.models.Organization;
import org.lia.models.Product;

import java.util.ArrayDeque;

/**CollectionManager class. Stores collection*/
public class CollectionManager {

    private volatile ArrayDeque<Product> productCollection;
    private java.time.LocalDate creationDate;

    public CollectionManager() {
        creationDate = java.time.LocalDate.now();
        productCollection = new ArrayDeque<>();
    }

    public ArrayDeque<Product> getProductCollection() {
        return productCollection;
    }

    public long getNumberOfElements() {
        return productCollection.size();
    }

    public void setProductCollection(ArrayDeque<Product> productCollection) {
        this.productCollection = productCollection;
    }

    public void addToCollection(Product product) {
        productCollection.add(product);
    }

    public void removeFromCollection(Product product){
        productCollection.remove(product);
    }

    /**Moves id for Product and Organization after adding objects from a file*/
    public void changeIdIndex() {
        long maxProductId = 0;
        long maxOrgId = 0;
        for (Product c : productCollection) {
            if (maxProductId < c.getId()) {
                maxProductId = c.getId();
            }
            if (maxOrgId < c.getManufacturer().getId()) {
                maxOrgId = c.getManufacturer().getId();
            }
        }
        //Product.updateId(maxProductId);
        Organization.updateId(maxOrgId);
    }

    public Product getById(long id) {
        for (Product c : productCollection) {
            if (id == c.getId()) {
                return c;
            }
        }
        throw new IllegalArgumentException("Product with this id doesn't exist");
    }

    public String shortInfo() {
        return "Collection of " + Integer.toString(productCollection.size()) + " elements. Created at " + creationDate;
    }

    public void show() {
        System.out.println(this.shortInfo());
        for (Product c : productCollection) {
            System.out.println(c);
        }
    }

    @Override
    public String toString() {
        return "CollectionManager{" +
                "productCollection=" + productCollection +
                '}';
    }
}
