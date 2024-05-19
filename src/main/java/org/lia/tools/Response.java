package org.lia.tools;

import org.lia.models.Product;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {

    private static final long serialVersionUID = 178546477190753L;

    private Product product;
    private ArrayList<String> answer = new ArrayList<>();

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public void addAnswer(String line) {
        this.answer.add(line);
    }

}
